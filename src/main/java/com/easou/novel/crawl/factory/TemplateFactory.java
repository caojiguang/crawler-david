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
        List<TemplateNovelSeed> templates = templateMapper.selectAllNovelSeed();
        for ( TemplateNovelSeed template : templates ) {        
            String[] webNames = template.getContents_url().split(",");
            for (String webName : webNames) {
                if (StringUtils.isBlank(webName)) {
                    continue;
                }
                map.put(webName, template);
            }
        }
        logger.info("init load, template size : " + map.size());
        
//        TemplateNovelSeed template = templateMapper.selectAllNovelSeedByContentUrl("http://www.23hh.com/book/3/3045/");
//        map.put(template.getContents_url(), template);
//        String[] webNames = template.getContents_url().split(",");
//        for (String webName : webNames) {
//            if (StringUtils.isBlank(webName)) {
//                continue;
//            }
//            logger.info("template");
//        }      
        
    }

    public void reload() {
        logger.info("begin to reload template!");
        Map<String, TemplateNovelSeed> tmpMap = new HashMap<String, TemplateNovelSeed>();

        List<TemplateNovelSeed> templates = templateMapper.selectAllNovelSeed();
        for ( TemplateNovelSeed template : templates ) {        
            String[] webNames = template.getContents_url().split(",");
            for (String webName : webNames) {
                if (StringUtils.isBlank(webName)) {
                    continue;
                }
                map.put(webName, template);
            }
        }
        
        logger.info("reload template size : " + map.size());
        
        if (tmpMap.size() != 0) {
        	map = tmpMap;
        }
    }

    public TemplateNovelSeed getTemplate(String webname) {
        return map.get(webname);
    }

    public void putTemplate(String webName, TemplateNovelSeed template) {
        map.put(webName, template);
    }

}
