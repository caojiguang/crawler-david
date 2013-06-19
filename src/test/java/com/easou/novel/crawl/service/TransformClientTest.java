package com.easou.novel.crawl.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.easou.novel.crawl.model.CrawlUrl;
import com.easou.novel.crawl.service.IHttpClientService;
import com.easou.novel.crawl.service.ITransformService;
import com.easou.novel.crawl.service.impl.TransformServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml" })
public class TransformClientTest {
    @Autowired
    private IHttpClientService httpClientService;
    
	@Autowired
	private ITransformService transformService;

    @Test
    public void extractCatalogTest() throws IOException {
        byte[] contentBytes = null;
        String url = "http://www.ailing.cc/bookreader/75023-6316829.html";
        contentBytes = url.getBytes("utf-8");
        System.out.println(url);

        String transformedContent = transformService.transform(contentBytes);

        System.out.println(transformedContent);
        
    }
    
//    @Test
    public void jsoupDocumentTest() {
    	String ss = "<xml><Main id='Main'>dd<img src='sss'/>dddddddd<img src='bbb'/></Main></xml>";
    	Document d = Jsoup.parse(ss);
        System.out.println("1" + d.getElementsByTag("content_tag").text());
        System.out.println("2:" + d.getElementById("Main").attr("title"));
        Elements e = d.getElementsByTag("img");
        for (Element ee : e) {
            System.out.println(ee.attr("src"));
        }
        System.out.println("---end---");
    }
}
