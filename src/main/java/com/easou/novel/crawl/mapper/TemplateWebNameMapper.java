package com.easou.novel.crawl.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("templateWebNameMapper")
public interface TemplateWebNameMapper {
	List<String> selectRegexByFid(int fid);
}