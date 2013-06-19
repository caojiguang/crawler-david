package com.easou.novel.crawl.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.easou.novel.crawl.factory.TemplateFactory;
import com.easou.novel.crawl.model.CrawlBasicInfo;
import com.easou.novel.crawl.model.CrawlUrl;
import com.easou.novel.crawl.model.Template;
import com.easou.novel.crawl.model.TemplateNovelSeed;
import com.easou.novel.crawl.service.impl.ExtractChapterUrl;
import com.easou.novel.crawl.service.impl.RedisServiceImpl;
import com.easou.novel.crawl.util.UrlUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext.xml"})
public class ExtractServiceChapterUrlTest {
	
	private static final Pattern REDIRECT_PATTERN = Pattern.compile("CONTENT=\"0;[ ]*URL=([\\s\\S]*?)\"", Pattern.CASE_INSENSITIVE);
	
	private static final Pattern FILTER_PATTERN_BR = Pattern.compile("(([\\s]*<br />)|([\\s]*<br/>)){2,}", Pattern.CASE_INSENSITIVE);

	
	@Autowired
	private ExtractChapterUrl extractService;
	@Autowired
	private IHttpClientService httpClientService;
	
	@Resource(name="templateFactory")
	private TemplateFactory templateFactory;
	
	@Autowired
	private IRedisService redisService;

//	@Test
	public void extractCatalogTest(){
		try {
			String entryWay = "http://www.dajiadu.net/";
			String charset = null;
			CrawlUrl crawlUrl = new CrawlUrl();
			crawlUrl.setEntryWay(entryWay);
			crawlUrl.setUri(new URI("http://www.dajiadu.net/files/article/html/15/15523/index.html"));
			System.out.println(crawlUrl.getUri().toString());
			crawlUrl.setCatalog_level(0);
			String body = httpClientService.getResponseBodyByGet(crawlUrl.getUri().toString(), charset);
			crawlUrl.setBody(body);
			System.out.println(body);
			List<CrawlUrl> crawlUrls = extractService.extractHtmlCatalog(crawlUrl);
			for(CrawlUrl u : crawlUrls){
				System.out.println(u.getRankOrder() + " : " + u.getCatalog_title() + " : " + u.getUri());
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void extractHtmlContentTest(){
		String entryWay = "http://www.23hh.com/";
		String url = "http://www.23hh.com/book/3/3045/11653535.html";
		CrawlUrl contentUrl = new CrawlUrl();
		contentUrl.setUri(URI.create(url));
		contentUrl.setEntryWay(entryWay);
		contentUrl.setType(1);
		String body = httpClientService.getResponseBodyByGet(contentUrl.getUri().toString(), "" );
		contentUrl.setBody(body);
		System.out.println("-------------------------------------------");
		CrawlBasicInfo crawlBasicInfo = extractService.extractHtmlContent(contentUrl);
		Matcher matcher = FILTER_PATTERN_BR.matcher(crawlBasicInfo.getContent());
		crawlBasicInfo.setContent(matcher.replaceAll("<br />"));
		System.out.println("-------------------------------------------");
		System.out.println(crawlBasicInfo.toString());
		System.out.println(crawlBasicInfo.toString().length() );
	}
	
//   @Test
    public void writeChapterPageInfoTest(){
        String entryWay = "http://www.23hh.com/";
        String url = "http://www.23hh.com/book/3/3045/11653535.html";
        CrawlUrl contentUrl = new CrawlUrl();
        contentUrl.setUri(URI.create(url));
        contentUrl.setEntryWay(entryWay);
        contentUrl.setType(1);
        String body = httpClientService.getResponseBodyByGet(contentUrl.getUri().toString(), "" );
        contentUrl.setBody(body);
        System.out.println("-------------------------------------------");
        CrawlBasicInfo crawlBasicInfo = extractService.extractHtmlContent(contentUrl);
        Matcher matcher = FILTER_PATTERN_BR.matcher(crawlBasicInfo.getContent());
        crawlBasicInfo.setContent(matcher.replaceAll("<br />"));
        System.out.println("-------------------------------------------");
        System.out.println(crawlBasicInfo.toString());
        System.out.println(crawlBasicInfo.toString().length() );
        
        redisService.pushChapterInfo(String.valueOf(crawlBasicInfo.getNovelid()), crawlBasicInfo);
        System.out.println(redisService.popChapterInfo(String.valueOf(crawlBasicInfo.getNovelid()), 1, 1));
    }

//	  @Test
	    public void extractCatalogTest1(){
	        try {
	            String entryWay = "http://www.23hh.com/";
	            String charset = null;
	            CrawlUrl crawlUrl = new CrawlUrl();
	            crawlUrl.setEntryWay(entryWay);
	            crawlUrl.setUri(new URI("http://www.23hh.com/book/3/3045/"));
	            System.out.println(crawlUrl.getUri().toString());
	            crawlUrl.setCatalog_level(0);
	            String body = httpClientService.getResponseBodyByGet(crawlUrl.getUri().toString(), charset);
	            crawlUrl.setBody(body);
	            
	            List<CrawlUrl> crawlUrls = extractService.extractHtmlCatalog(crawlUrl);
	            for(CrawlUrl u : crawlUrls){
	                System.out.print(u.getRankOrder() + " : " + u.getCatalog_title() + " : " + u.getUri());
	                body = httpClientService.getResponseBodyByGet(u.getUri().toString(), "" );
	                CrawlBasicInfo crawlBasicInfo = extractService.extractHtmlContent(u);
	                Matcher matcher = FILTER_PATTERN_BR.matcher(crawlBasicInfo.getContent());
	                crawlBasicInfo.setContent(matcher.replaceAll("<br />"));
	                System.out.println(" chapter info length: " + crawlBasicInfo.toString().length());
	                redisService.pushChapterInfo(String.valueOf(crawlBasicInfo.getNovelid()), crawlBasicInfo);
	            }
	        } catch (URISyntaxException e) {
	            e.printStackTrace();
	        }
	    }
//	@Test
	public void extractSkipUrl(){
		try {
//			templateFactory.loadTemplateById(406);
			String entryWay = "http://epaper.gmw.cn/gmrb/paperindex.htm";
			String charset = null;
			CrawlUrl crawlUrl = new CrawlUrl();
			crawlUrl.setEntryWay(entryWay);
			crawlUrl.setUri(new URI(entryWay));
			crawlUrl.setCatalog_level(0);
			Template template = null;
//			template = templateFactory.getTemplate(entryWay);
			String body = httpClientService.getResponseBodyByGet(crawlUrl.getUri().toString(), charset);
			crawlUrl.setBody(body);
			if(body != null){
				Matcher matcher = REDIRECT_PATTERN.matcher(body);
				if (matcher.find()) {
					String url =  matcher.group(1);
					if (url != null && url.length() != 0) {
						url = UrlUtil.joinUrl(crawlUrl.getUri().toString(), url);
						crawlUrl.setUri(URI.create(url));
						System.out.println(url);
						crawlUrl.setUri(URI.create(url));
						body = httpClientService.getResponseBodyByGet(url, charset);
						crawlUrl.setBody(body);
					}
				}
			}
			System.out.println("-----"+ body);					
			List<CrawlUrl> crawlUrls = extractService.extractHtmlCatalog(crawlUrl);
			for(CrawlUrl u : crawlUrls){
				System.out.println(u);
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
//	@Test
	public void transformHtmlContentTest(){
		String entryWay = "http://www.smweekly.com/style/";
		String url = "http://www.smweekly.com/style/hotwind/201211/31678.aspx";
		String charset = null;
		CrawlUrl contentUrl = new CrawlUrl();
		contentUrl.setUri(URI.create(url));
		contentUrl.setEntryWay(entryWay);
		contentUrl.setType(1);
		String body = httpClientService.getResponseBodyByGet(contentUrl.getUri().toString(), charset);
		contentUrl.setBody(body);
		System.out.println(body);
		CrawlBasicInfo crawlBasicInfo = extractService.transformHtmlContent(contentUrl);
		System.out.println("-----------------------------------------------------------");
		System.out.println(crawlBasicInfo);
	}
	
}
