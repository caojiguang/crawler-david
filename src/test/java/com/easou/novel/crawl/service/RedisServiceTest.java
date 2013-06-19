package com.easou.novel.crawl.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.easou.novel.crawl.constant.CommandContant;
import com.easou.novel.crawl.model.CrawlBasicInfo;
import com.easou.novel.crawl.service.IRedisService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext.xml"})
public class RedisServiceTest {
	@Autowired
	private IRedisService redisService;
	
	@Test
	public void popNewsInfoTest(){
		CrawlBasicInfo basicInfo = redisService.popNewsInfo(CommandContant.NEW_BASICINFO_LIST);
		System.out.println(basicInfo);
	}
}
