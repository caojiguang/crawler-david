package com.easou.novel.crawl.factory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.easou.novel.crawl.mapper.TemplateNovelSeedMapper;
import com.easou.novel.crawl.model.TemplateNovelSeed;

@Component("templateFactory")
public class TemplateFactory {
    private static Logger logger = LoggerFactory.getLogger(TemplateFactory.class);

    @Autowired
    private TemplateNovelSeedMapper templateMapper;
    private Map<String, TemplateNovelSeed> map;

    @PostConstruct
    public void init() {
        logger.info("begin to init template!");
        map = new HashMap<String, TemplateNovelSeed>();
//        List<TemplateNovelSeed> templates = templateMapper.selectAllNovelSeed();
//        for ( TemplateNovelSeed template : templates ) {        
//            String[] webNames = template.getContents_url().split(",");
//            for (String webName : webNames) {
//                if (StringUtils.isBlank(webName)) {
//                    continue;
//                }
//                map.put(webName, template);
//            }
//        
//        }
        
        TemplateNovelSeed template = templateMapper.selectAllNovelSeedByContentUrl("http://www.23hh.com/book/3/3045/");
        map.put("http://www.23hh.com/book/3/3045/", template);
        String[] webNames = template.getContents_url().split(",");
        for (String webName : webNames) {
            if (StringUtils.isBlank(webName)) {
                continue;
            }
            logger.info("template");
        }      

        
//        logger.info("template size : " + map.size());
        
    }

    public void reload() {
        logger.info("begin to reload template!");
        Map<String, TemplateNovelSeed> tmpMap = new HashMap<String, TemplateNovelSeed>();

        TemplateNovelSeed template = templateMapper.selectAllNovelSeedByContentUrl("http://www.23hh.com/book/3/3045/");// .selectAllNovelSeed();
//        for (TemplateNovelSeed template : templates) {
        	String[] webNames = template.getContents_url().split(",");
        	for (String webName : webNames) {
        		if (StringUtils.isBlank(webName)) {
        			continue;
        		}
        		logger.info(template.toString());
            	tmpMap.put(webName, template);
        	}
//        }

        if (tmpMap.size() != 0) {
        	map = tmpMap;
        }
        
        logger.info("template size : " + map.size());
    }

    public TemplateNovelSeed getTemplate(String webname) {
        return map.get(webname);
    }

    public void putTemplate(String webName, TemplateNovelSeed template) {
        map.put(webName, template);
    }

//    public TemplateNovelSeed loadTemplateById(int id) {
//        if (map == null) {
//            map = new HashMap<String, TemplateNovelSeed>();
//        }
//        Template template = this.templateMapper.findById(id);
//        
//    	String[] webNames = template.getWebName().split(",");
//    	for (String webName : webNames) {
//    		if (StringUtils.isBlank(webName)) {
//    			continue;
//    		}
//    		System.out.println("webName:" + webName);
//        	map.put(webName, template);
//    	}
//        return template;
//    }
}
