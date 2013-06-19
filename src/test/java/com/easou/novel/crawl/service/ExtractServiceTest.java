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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.easou.novel.crawl.constant.CommandContant;
import com.easou.novel.crawl.factory.TemplateFactory;
import com.easou.novel.crawl.model.CrawlBasicInfo;
import com.easou.novel.crawl.model.CrawlUrl;
import com.easou.novel.crawl.model.Template;
import com.easou.novel.crawl.service.IExtractService;
import com.easou.novel.crawl.service.IHttpClientService;
import com.easou.novel.crawl.util.UrlUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext.xml"})
public class ExtractServiceTest {
	
	private static final Pattern REDIRECT_PATTERN = Pattern.compile("CONTENT=\"0;[ ]*URL=([\\s\\S]*?)\"", Pattern.CASE_INSENSITIVE);
	
	private static final Pattern FILTER_PATTERN_BR = Pattern.compile("(([\\s]*<br />)|([\\s]*<br/>)){2,}", Pattern.CASE_INSENSITIVE);

	
	@Autowired
	private IExtractService extractService;
	@Autowired
	private IHttpClientService httpClientService;
	
	@Resource(name="templateFactory")
	private TemplateFactory templateFactory;

	@Test
	public void extractCatalogTest(){
		try {
//			templateFactory.loadTemplateById(477);
			String entryWay = "http://www.23hh.com/";
			String charset = null;
			CrawlUrl crawlUrl = new CrawlUrl();
			crawlUrl.setEntryWay(entryWay);
			crawlUrl.setUri(new URI("http://www.23hh.com/book/0/960/"));
			crawlUrl.setCatalog_level(0);
			String body = httpClientService.getResponseBodyByGet(crawlUrl.getUri().toString(), charset);
			crawlUrl.setBody(body);
			
			System.out.println(body);
			List<CrawlUrl> crawlUrls = extractService.extractHtmlCatalog(crawlUrl);
			for(CrawlUrl u : crawlUrls){
				System.out.println(u.getRankOrder() + ", " +u.getUri());
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
//	@Test
	public void extractHtmlContentTest(){
		String entryWay = "http://roll.ent.sina.com.cn/s/channel.php?ch=04#col=63&spec=&type=&ch=04&k=&offset_page=0&offset_num=0&num=60&asc=&page=1";
		String url = "http://ent.sina.com.cn/m/c/2012-12-24/00303818549.shtml";
//		templateFactory.loadTemplateById(49);
		String charset = null;
		CrawlUrl contentUrl = new CrawlUrl();
		contentUrl.setUri(URI.create(url));
		contentUrl.setEntryWay(entryWay);
		contentUrl.setType(1);
		String body = httpClientService.getResponseBodyByGet(contentUrl.getUri().toString(), charset);
		contentUrl.setBody(body);
		System.out.println("-----------------------------------------");
		int len = body.length();
		System.out.println(body.substring(1000, len/2));
		System.out.println("-------------------------------------------");
		CrawlBasicInfo crawlBasicInfo = extractService.extractHtmlContent(contentUrl);
	//	CrawlBasicInfo crawlBasicInfo = extractService.transformHtmlContent(contentUrl);
		Matcher matcher = FILTER_PATTERN_BR.matcher(crawlBasicInfo.getContent());
		crawlBasicInfo.setContent(matcher.replaceAll("<br />"));
		System.out.println(crawlBasicInfo);
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
