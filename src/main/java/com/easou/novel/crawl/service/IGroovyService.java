// Copyright(c) 2011 easou.com
package com.easou.novel.crawl.service;

import com.easou.novel.crawl.model.CrawlBasicInfo;
import com.easou.novel.crawl.model.CrawlUrl;

/**
 * @author yunchat
 *
 */
public interface IGroovyService {


	CrawlBasicInfo extractHtmlContent(CrawlUrl crawlUrl);

}
