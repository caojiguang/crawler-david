package com.easou.novel.crawl.mapper;

import org.springframework.stereotype.Repository;

import com.easou.novel.crawl.model.TemplateContentPage;

@Repository("templateContentPageMapper")
public interface TemplateContentPageMapper {
	TemplateContentPage selectByFid(int fid);
}