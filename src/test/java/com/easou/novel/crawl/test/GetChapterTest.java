package com.easou.novel.crawl.test;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.easou.novel.crawl.model.CrawlBasicInfo;
import com.easou.novel.crawl.service.IRedisService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext.xml"})

public class GetChapterTest {

    @Autowired
    private IRedisService redisService;

    @Test
    public void getChapter(){
        String queueName = String.valueOf(1);
        List<CrawlBasicInfo> chapterInfos = redisService.popChapterInfo(queueName, 1, Integer.MAX_VALUE);
        if(null == chapterInfos || chapterInfos.size() < 1)
            return;
        System.out.println("---------------------" );
        System.out.println(chapterInfos.get(0).getEntryWay());
        for(CrawlBasicInfo chapter : chapterInfos) {
//            System.out.println(chapter.getRankOrder() + " :" + chapter.getCatalogTitle() + " :"  + chapter.getContent().substring(0, 20) );
            System.out.println(chapter.getRankOrder() + " :" + chapter.getCatalogTitle() + " :"  + chapter.getPageUrl());
        }
    }
    
}
