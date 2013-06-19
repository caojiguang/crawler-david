package com.easou.novel.crawl.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.easou.novel.crawl.service.IHttpClientService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext.xml"})
public class HttpClientServiceTest {
	@Autowired
	private IHttpClientService httpClientService;
	
	@Test
	public void getResponseBodyByGetTest(){
		String url = "http://www.23hh.com/book/0/960/";
		System.out.println(httpClientService.getResponseBodyByGet(url, null));
	}
}