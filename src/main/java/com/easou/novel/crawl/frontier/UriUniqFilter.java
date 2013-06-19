package com.easou.novel.crawl.frontier;

import javax.annotation.Resource;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.easou.novel.crawl.model.CrawlUrl;
import com.easou.novel.crawl.util.Md5Util;

@Component("uriUniqFilter")
public class UriUniqFilter {
    @Resource(name = "uniRedisTemplate")
    private StringRedisTemplate uniRedisTemplate;
    public boolean addUrl(CrawlUrl curl) {
        String md5 = Md5Util.generateMd5ByUrl(curl.getUri().toString());
        boolean r = uniRedisTemplate.opsForValue().setIfAbsent(md5, "");
        return r;
    }

    public boolean urlExist(String url) {
        String md5 = Md5Util.generateMd5ByUrl(url);
        return uniRedisTemplate.opsForValue().get(md5) == null ? false : true;
    }
}
