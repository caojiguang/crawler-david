package com.easou.novel.crawl.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.easou.novel.crawl.model.TemplateNovelSeed;

@Repository("novelSeedMapper")
public interface TemplateNovelSeedMapper {
    List<TemplateNovelSeed> selectAllNovelSeed();
    
    TemplateNovelSeed  selectAllNovelSeedByContentUrl(String contents_url);
}