package com.easou.novel.crawl.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("templateContentMapper")
public interface TemplateContentMapper {
	List<String> selectRegexByFid(int fid);
}
