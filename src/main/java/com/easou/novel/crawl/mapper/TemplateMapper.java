package com.easou.novel.crawl.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.easou.novel.crawl.model.Template;
import com.easou.novel.crawl.model.TemplateNovelSeed;

@Repository("templateMapper")
public interface TemplateMapper {
	List<Template> selectAll();
	
	Template findById(int id);
	
	List<Template> selectAllSeed();
	
	List<Template> selectDownSeed();
	
}