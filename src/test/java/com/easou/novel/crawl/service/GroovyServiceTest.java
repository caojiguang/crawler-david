// Copyright(c) 2011 easou.com
package com.easou.novel.crawl.service;

import java.net.URI;
import java.util.regex.Matcher;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.easou.novel.crawl.factory.TemplateFactory;
import com.easou.novel.crawl.model.CrawlBasicInfo;
import com.easou.novel.crawl.model.CrawlUrl;
import com.easou.novel.crawl.service.IGroovyService;
import com.easou.novel.crawl.service.IHttpClientService;

/**
 * @author yunchat
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext.xml"})
public class GroovyServiceTest {
	
	@Resource(name="templateFactory")
	private TemplateFactory templateFactory;
	
	@Autowired
	private IHttpClientService httpClientService;
	
	@Autowired
	private IGroovyService groovyService;


	@Test
	public void extractHtmlContentTest(){
		String entryWay = "http://news.163.com/photo/";
		String url = "http://news.163.com/photoview/00AN0001/30966.html";
//		templateFactory.loadTemplateById(754);
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
		CrawlBasicInfo crawlBasicInfo = groovyService.extractHtmlContent(contentUrl);
	//	CrawlBasicInfo crawlBasicInfo = extractService.transformHtmlContent(contentUrl);
		System.out.println(crawlBasicInfo);
	}

}
