package com.easou.novel.crawl.service;

import java.util.List;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.easou.novel.crawl.model.Template;
import com.easou.novel.crawl.service.ITemplateService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext.xml"})
public class TemplateServiceTest {
	@Autowired
	private ITemplateService templateService;
	
	@org.junit.Test
	public void selectAll(){
		List<Template> templates = templateService.getAll();
		System.out.println(templates.size());
	}
}
