package com.easou.novel.crawl.service.impl;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.easou.novel.crawl.factory.TemplateFactory;
import com.easou.novel.crawl.frontier.UriUniqFilter;
import com.easou.novel.crawl.model.CrawlBasicInfo;
import com.easou.novel.crawl.model.CrawlImage;
import com.easou.novel.crawl.model.CrawlUrl;
import com.easou.novel.crawl.model.Template;
import com.easou.novel.crawl.model.TemplateBasicInfo;
import com.easou.novel.crawl.model.TemplateNovelSeed;
import com.easou.novel.crawl.service.IExtractServiceChapterUrl;
import com.easou.novel.crawl.service.IHttpClientService;
import com.easou.novel.crawl.service.IRedisService;
import com.easou.novel.crawl.service.ITransformService;
import com.easou.novel.crawl.util.DateUtil;
import com.easou.novel.crawl.util.HtmlUtil;
import com.easou.novel.crawl.util.MatcherUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service
public class ExtractChapterUrl implements IExtractServiceChapterUrl {

    private static Logger logger = LoggerFactory.getLogger(ExtractChapterUrl.class);
    
    @Resource(name="templateFactory")
    private TemplateFactory templateFactory;
    @Autowired
    private IHttpClientService httpClientService;
    @Autowired
    private ITransformService transformService;
    @Autowired
    private IRedisService redisService;

    @Resource(name="uriUniqFilter")
    private UriUniqFilter uriUniqFilter ;
    
    @Override
    // 提取章节页的 Url,标题等信息
    public List<CrawlUrl> extractHtmlCatalog(CrawlUrl catalogUrl) {
        if (StringUtil.isBlank(catalogUrl.getBody())) {
            return new ArrayList<CrawlUrl>();
        }
        StopWatch stopWatch = new Log4JStopWatch();
        List<CrawlUrl> crawlUrls = new ArrayList<CrawlUrl>();
        TemplateNovelSeed template= templateFactory.getTemplate(catalogUrl.getUri().toString());
        
        Document document = Jsoup.parse(catalogUrl.getBody(),catalogUrl.getBaseUri());
        Elements elements = null;
        
        elements = document.getElementsByTag("a");
        String url = "";
        int sort = 0;
        for (Element element : elements) {
            try {
                url = element.attr("abs:href");
                // content url not valide ?
                if(StringUtils.isBlank(url) || catalogUrl.getEntryWay().equals(url) || catalogUrl.getUri().toString().equals(url)) {
                    continue;
                }
                
                // content url already exists ?
                if(uriUniqFilter.urlExist(url))
                    continue;
                
                url = url.replaceAll("#[^#?&]+", "");
                url = url.trim();
               // if match content url regex ?
                if(MatcherUtil.isMatch(url,   Arrays.asList(template.getContent_url_reg()))) {
                 // chapter sort auto increment
                    sort ++;
                    
                    CrawlUrl crawlUrl = new CrawlUrl(catalogUrl.getEntryWay(), URI.create(url), 1, 0);
                    crawlUrl.setCatalog_title(element.text());
                    crawlUrl.setRankOrder(sort); //章节序号
                    crawlUrl.setType(1); // 表明是章节内容页面
                    crawlUrl.setNovelid(template.getId()); // 对应数据库序号, 利用其唯一性;
                    crawlUrls.add(crawlUrl);                    
                    logger.debug("sort=" + sort + " " + crawlUrl.getUri().toString());
                }
            } catch (Exception e) {
                logger.error("catalog url:" + url, e);
            }
        }
        
        stopWatch.stop("extract catalog");
        return crawlUrls;
        
    }

    @Override
    /**
     * 提取正文
     */
    public CrawlBasicInfo extractHtmlContent(CrawlUrl contentUrl) {

        if (StringUtil.isBlank(contentUrl.getBody())) {
            return new CrawlBasicInfo();
        }
        StopWatch stopWatch = new Log4JStopWatch();

        TemplateNovelSeed template= templateFactory.getTemplate(contentUrl.getEntryWay());
        Document document = Jsoup.parse(contentUrl.getBody(), contentUrl.getBaseUri());
        CrawlBasicInfo crawlBasicInfo = packageBasicInfo(contentUrl, template, document);
        if(0 == crawlBasicInfo.getNovelid())
            crawlBasicInfo.setNovelid(template.getId());
        
        TemplateBasicInfo templateBasicInfo = new TemplateBasicInfo(); 
        boolean result = parseAndPackage(templateBasicInfo, contentUrl, document, crawlBasicInfo);
        if (!result) {
            return new CrawlBasicInfo();
        }
        stopWatch.stop("content catalog");
        return crawlBasicInfo;
    }

    private CrawlBasicInfo packageBasicInfo(CrawlUrl contentUrl, TemplateNovelSeed template, Document document) {
        CrawlBasicInfo crawlBasicInfo = new CrawlBasicInfo();
        crawlBasicInfo.setEntryWay(contentUrl.getEntryWay());
        crawlBasicInfo.setPageUrl(contentUrl.getUri().toString());
        if (document != null) {
            crawlBasicInfo.setPageTitle(document.title());
        }
        crawlBasicInfo.setCatalogTitle(contentUrl.getCatalog_title());
        crawlBasicInfo.setWeight(contentUrl.getWeight());
        crawlBasicInfo.setSource(template.getSource());
        crawlBasicInfo.setClassify(template.getClassify());
        crawlBasicInfo.setDownloadTime(DateUtil.currentDateMilliseconds());
        crawlBasicInfo.setHost(contentUrl.getUri().getHost());
        crawlBasicInfo.setNovelid(contentUrl.getNovelid());
        crawlBasicInfo.setType(contentUrl.getType());
        crawlBasicInfo.setRankOrder(contentUrl.getRankOrder());
        return crawlBasicInfo;
    }
    
    private boolean parseAndPackage(TemplateBasicInfo templateBasicInfo, CrawlUrl crawlUrl, Document document, CrawlBasicInfo crawlBasicInfo){
        TemplateNovelSeed templateNovelSeed = templateFactory.getTemplate(crawlUrl.getEntryWay());
        if(null == templateNovelSeed)
            return false;
//        String value = fixedHtmlContent("select", "[{'action':'attr','key':'id','value':'contents'}]", crawlUrl.getBody(), document);//根据表达式得到指定的内
        String value = fixedHtmlContent("select", templateNovelSeed.getContent_reg(), crawlUrl.getBody(), document);//根据表达式得到指定的内
        if(value != null){
            value = HtmlUtil.removeScriptTags(value);//去掉script、style等标签
            value = HtmlUtil.replaceSign(value);//替换&nbsp;等标记
            Document valueDoc = Jsoup.parse(value, crawlBasicInfo.getHost());
            Element valueBody = valueDoc.body();

            //保留指定的标签
//            if(StringUtils.isNotBlank(templateBasicInfo.getHoldTags()))
//                value = HtmlUtil.keepFixedHtml(valueBody.html(), templateBasicInfo.getHoldTags());
//            else
//                value = valueBody.text();

            value = HtmlUtil.keepFixedHtml(valueBody.html(), "p|br|strong");
            //去掉空白行
            value = HtmlUtil.removeSpaceLine(value);
            
            //格式化日期， 通过反射来设置属性
//            if(StringUtils.isNotBlank(templateBasicInfo.getDatePattern())){
//                long milltimes = DateUtil.fixedDateMilliseconds(value.trim(), templateBasicInfo.getDatePattern());
//                setField(templateBasicInfo.getProperty(), milltimes, crawlBasicInfo, Long.class); 
//            } else {
//                setField(templateBasicInfo.getProperty(), value.trim(), crawlBasicInfo, String.class);  
//            }
            crawlBasicInfo.setContent(value);
        }
        return true;
    }
    
    @SuppressWarnings("unchecked")
    private void setField(String name, Object value, CrawlBasicInfo c, Class parameterType) {
        try {
            name = name.substring(0,1).toUpperCase()+name.substring(1);
            Method m = c.getClass().getDeclaredMethod("set" + name, parameterType);
            m.invoke(c, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private String fixedHtmlContent(String action, String expression, String htmlContent, Document document) {
        String value = null;
//        if("select".equals(action)){
        if(true){
            JsonArray jsonArray = new JsonParser().parse(expression).getAsJsonArray();
            Element element = document;
            boolean hasExclude = false;
            for(int i=0;i<jsonArray.size();i++){
                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                String jsonAction = jsonObject.get("action").getAsString();
                if("attr".equals(jsonAction))
                    element = element.getElementsByAttributeValue(jsonObject.get("key").getAsString(), jsonObject.get("value").getAsString()).first();  
                else if("tag".equals(jsonAction))
                    element = element.getElementsByTag(jsonObject.get("name").getAsString()).first();
                else if("ex-attr".equals(jsonAction) || "ex-tag".equals(jsonAction) || "ex-reg".equals(jsonAction))
                    hasExclude = true;
                if(element == null)
                    break;
            }
            if(element != null) {
                if (!hasExclude) {
                    value = element.html();
                } else {
                    if (hasExclude) {
                        Element valueDoc = element.clone();
                        Element exElement = null;
                        for(int i=0;i<jsonArray.size();i++) {
                            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                            String jsonAction = jsonObject.get("action").getAsString();
                            if("ex-attr".equals(jsonAction)) {
                                exElement = valueDoc.getElementsByAttributeValue(jsonObject.get("key").getAsString(), jsonObject.get("value").getAsString()).first();   
                                if (exElement != null) {
                                    exElement.remove();
                                }
                            } else if("ex-tag".equals(jsonAction)) {
                                Elements exElments = valueDoc.getElementsByTag(jsonObject.get("name").getAsString());
                                int idx = 0;
                                if (jsonObject.get("idx") != null) {
                                    idx = jsonObject.get("idx").getAsInt();
                                }
                                if (exElments != null && exElments.size() > 0) {
                                    if (idx == -1) {
                                        exElments.remove();
                                    } else {
                                        exElement = exElments.get(idx);
                                        if (exElement != null) {
                                            exElement.remove();
                                        }
                                    }
                                }
                                
                            } else {
                                exElement = null;
                            }                       
                            if(valueDoc == null)
                                break;  
                            
                        }
                        value = valueDoc.html();
                        for(int i=0;i<jsonArray.size();i++) {
                            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                            String jsonAction = jsonObject.get("action").getAsString();
                            if("ex-reg".equals(jsonAction)) {
                                Pattern exPattern = Pattern.compile(jsonObject.get("value").getAsString());
                                Matcher matcher = exPattern.matcher(value);
                                if(matcher.find()) {
                                    if (matcher.groupCount() > 0) {
                                        String mat = matcher.group(1);
                                        value = value.replaceAll(mat, "");
                                        
                                    } else {
                                        value = matcher.replaceAll("");
                                    }
                                }
                            }               
                        }
                    }
                }
                
            }
        }
        
        return value;
    }

    
    @Override
    public CrawlBasicInfo transformHtmlContent(CrawlUrl contentUrl) {
        // TODO Auto-generated method stub
        return null;
    }

}
