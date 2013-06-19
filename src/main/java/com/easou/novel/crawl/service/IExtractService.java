package com.easou.novel.crawl.service;

import java.util.List;

import com.easou.novel.crawl.model.CrawlBasicInfo;
import com.easou.novel.crawl.model.CrawlUrl;

public interface IExtractService {
	
	/**
	 * 抓取html目录页url
	 * @param catalogUrl
	 * @return
	 */
	List<CrawlUrl> extractHtmlCatalog(CrawlUrl catalogUrl);
	
	/**
	 * 抓取html内容页的basic信息
	 * @param contentUrl
	 * @return
	 */
	CrawlBasicInfo extractHtmlContent(CrawlUrl contentUrl);
	
	/**
	 * 转换内容页的basic信息
	 * @param contentUrl
	 * @return
	 */
	CrawlBasicInfo transformHtmlContent(CrawlUrl contentUrl);
}
