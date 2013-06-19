package com.easou.novel.crawl.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.easou.novel.crawl.mapper.TemplateMapper;
import com.easou.novel.crawl.mapper.TemplateNovelSeedMapper;
import com.easou.novel.crawl.model.Template;
import com.easou.novel.crawl.service.ITemplateService;

@Service
public class TemplateServiceImpl implements ITemplateService {
	@Resource(name="templateMapper")
	private TemplateMapper templateMapper;
	
	@Resource(name="novelSeedMapper")
    private TemplateNovelSeedMapper templateNovelSeedMapper;
	
	
	@Override
	public List<Template> getAll() {
		return templateMapper.selectAll();
	}
}
