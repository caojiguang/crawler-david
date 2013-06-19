package com.easou.novel.crawl.service;

import java.util.List;

import com.easou.novel.crawl.model.CrawlBasicInfo;

public interface IRedisService {

    /**
     * 保存新闻信息
     * @param crawlImage
     */
    boolean pushNewsInfo(String queueName, CrawlBasicInfo basicInfo);

    /**
     * 弹出新闻信息
     * @param crawlImage
     */
    CrawlBasicInfo popNewsInfo(String queueName);

    /**
     * 保存排行榜信息
     * @param crawlImage
     */
    boolean pushRankInfo(String queueName, CrawlBasicInfo basicInfo);

    /**
     * 弹出排行榜信息
     * @param crawlImage
     */
    CrawlBasicInfo popRankInfo(String queueName);
    
    /**
     * 章节列表信息;
     */
    boolean pushChapterInfo(String queueName, CrawlBasicInfo basicInfo);
    
    
    /**
     * 读取章节列表信息;
     */
    public List<CrawlBasicInfo> popChapterInfo(String queueName, int sortBegin, int sortEnd);

}
