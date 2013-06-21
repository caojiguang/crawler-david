package com.easou.novel.crawl.frontier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.easou.novel.crawl.model.CrawlUrl;
import com.easou.novel.crawl.task.CrawlTask;

public class WorkQueue implements Comparable<WorkQueue>{
    protected final String classKey;
    private long wakeTime = 0;
	private StringRedisTemplate redisTemplate;
    private boolean isHeld = false;
    private static Logger logger = LoggerFactory.getLogger(WorkQueue.class);
    
    public WorkQueue(final String pClassKey) {
        this.classKey = pClassKey;
    }
	public CrawlUrl pop(){
		try {
			String url = redisTemplate.opsForList().rightPop("wq:"+classKey);
			if(url != null){
				CrawlUrl curl = CrawlUrl.fromJson(url);
				curl.setHolder(this);
				return curl;
			}
		} catch (Exception e) {
			logger.error("pop error!");
		}
		
		return null;
	}
	public boolean push(CrawlUrl curl){
	    String url = CrawlUrl.toJson(curl);
	    try {
			redisTemplate.opsForList().leftPush("wq:"+classKey, url);
			return true;
		} catch (Exception e) {
			logger.error("push error " + "wq:"+classKey +" : " + url);
			return false;
		}
	    
	}
	
	@Override
	public int compareTo(WorkQueue obj) {
		if (this == obj) {
			return 0; // for exact identity only
		}
		if (getWakeTime() > obj.getWakeTime()) {
			return 1;
		}
		if (getWakeTime() < obj.getWakeTime()) {
			return -1;
		}
		// at this point, the ordering is arbitrary, but still
		// must be consistent/stable over time
		return this.classKey.compareTo(obj.getClassKey());
	}
	public long getWakeTime() {
		return wakeTime;
	}
	public void setWakeTime(long wakeTime) {
		this.wakeTime = wakeTime;
	}
	public StringRedisTemplate getRedisTemplate() {
		return redisTemplate;
	}
	public void setRedisTemplate(StringRedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
	public String getClassKey() {
		return classKey;
	}
	public boolean isHeld() {
		return isHeld;
	}
	public void setHeld(boolean isHeld) {
		this.isHeld = isHeld;
	}
}
