package com.easou.novel.crawl.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.easou.novel.crawl.model.CrawlBasicInfo;

@Repository("templateBasicInfoMapper")
public interface TemplateBasicInfoMapper {
	List<CrawlBasicInfo> selectAllByFid(int fid);
}