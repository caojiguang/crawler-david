package com.easou.novel.crawl.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.easou.novel.crawl.model.CrawlBasicInfo;
import com.easou.novel.crawl.service.IRedisService;
import com.easou.novel.crawl.util.JsonUtil;

@Service
public class RedisServiceImpl implements IRedisService {
    private static Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);

    @Resource(name = "storageRedisTemplate")
    
    @Autowired
    private StringRedisTemplate storageRedisTemplate;

    @Override
    public boolean pushNewsInfo(String queueName, CrawlBasicInfo basicInfo) {
        try {
            String json = JsonUtil.json(basicInfo);
            BoundListOperations<String, String> operations = storageRedisTemplate
                    .boundListOps(queueName);
            return operations.leftPush(json) > 0;
        } catch (Exception e) {
            logger.error("", e);
        }

        return false;
    }

    @Override
    public CrawlBasicInfo popNewsInfo(String queueName) {
        try {
            BoundListOperations<String, String> operations = storageRedisTemplate
                    .boundListOps(queueName);
            String jsonStr = operations.rightPop();
            if (jsonStr != null)
                return (CrawlBasicInfo) JsonUtil.toObject(jsonStr);
        } catch (Exception e) {
            logger.error("", e);
        }

        return null;
    }

    @Override
    public boolean pushRankInfo(String queueName, CrawlBasicInfo basicInfo) {
        try {
            String json = JsonUtil.json(basicInfo);
            BoundSetOperations<String, String> operations = storageRedisTemplate
                    .boundSetOps(queueName);
            return operations.add(json);
        } catch (Exception e) {
            logger.error("", e);
        }

        return false;
    }

    @Override
    public CrawlBasicInfo popRankInfo(String queueName) {
        try {
            BoundSetOperations<String, String> operations = storageRedisTemplate
                    .boundSetOps(queueName);
            String jsonStr = operations.pop();
            if (jsonStr != null)
                return (CrawlBasicInfo) JsonUtil.toObject(jsonStr);
        } catch (Exception e) {
            logger.error("", e);
        }

        return null;
    }
    
    /**
     * 写入章节列表信息;
     */
    public boolean pushChapterInfo(String queueName, CrawlBasicInfo basicInfo) {
        try {
            System.out.println("json: ");
            String json = JsonUtil.json(basicInfo);
            System.out.println(json);
            BoundZSetOperations<String, String> operations = storageRedisTemplate
                    .boundZSetOps(queueName);
            System.out.println(basicInfo.getRankOrder());
            return operations.add(json, basicInfo.getRankOrder());
            
        } catch (Exception e) {
            logger.error("", e);
            return false;
        }
        
    }
    
    /**
     * 读取章节列表信息;
     */
    public List<CrawlBasicInfo> popChapterInfo(String queueName, int sortBegin, int sortEnd) {
        List<CrawlBasicInfo> chapterInfos = new ArrayList<CrawlBasicInfo>();
        try {
            BoundZSetOperations<String, String> operations = storageRedisTemplate
                    .boundZSetOps(queueName);
            Set<String> jsons = operations.rangeByScore(sortBegin, sortEnd);
            if(null == jsons || 0 == jsons.size())
                return null;
            for(String jsonStr : jsons) {
                chapterInfos.add((CrawlBasicInfo)JsonUtil.toObject(jsonStr));
            }
        } catch (Exception e) {
            logger.error("popChapterInfo Error", e);
            return null;
        } finally {
            
        }
        return chapterInfos;
        
    }
}
