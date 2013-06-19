package com.easou.novel.crawl.model;

import java.util.List;

public class TemplateNovelSeed {
   
    private int id;
    private int sort;
    private int gid;
    private String authorname;
    private String name;
    private String description;
    private String fromweb;
    private int ori_novelid;
    private String tag;
    private String cover_url;
    private String contents_url;
    private String content_url_reg;
    private String content_reg;
    private String charset;
    private String source;
    private String classify;
    private List<TemplateBasicInfo> templateBasicInfos;
    
    public List<TemplateBasicInfo> getTemplateBasicInfos() {
        return templateBasicInfos;
    }
    public void setTemplateBasicInfos(List<TemplateBasicInfo> templateBasicInfos) {
        this.templateBasicInfos = templateBasicInfos;
    }
    public String getSource() {
        return source;
    }
    public void setSource(String source) {
        this.source = source;
    }
    public String getClassify() {
        return classify;
    }
    public void setClassify(String classify) {
        this.classify = classify;
    }
    public String getCharset() {
        return charset;
    }
    public void setCharset(String charset) {
        this.charset = charset;
    }
    public String getContent_url_reg() {
        return content_url_reg;
    }
    public void setContent_url_reg(String content_url_reg) {
        this.content_url_reg = content_url_reg;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getSort() {
        return sort;
    }
    public void setSort(int sort) {
        this.sort = sort;
    }
    public int getGid() {
        return gid;
    }
    public void setGid(int gid) {
        this.gid = gid;
    }
    public String getAuthorname() {
        return authorname;
    }
    public void setAuthorname(String authorname) {
        this.authorname = authorname;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getFromweb() {
        return fromweb;
    }
    public void setFromweb(String fromweb) {
        this.fromweb = fromweb;
    }
    public int getOri_novelid() {
        return ori_novelid;
    }
    public void setOri_novelid(int ori_novelid) {
        this.ori_novelid = ori_novelid;
    }
    public String getTag() {
        return tag;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }
    public String getCover_url() {
        return cover_url;
    }
    public void setCover_url(String cover_url) {
        this.cover_url = cover_url;
    }
    public String getContents_url() {
        return contents_url;
    }
    public void setContents_url(String contents_url) {
        this.contents_url = contents_url;
    }
    public String getContent_reg() {
        return content_reg;
    }
    public void setContent_reg(String content_reg) {
        this.content_reg = content_reg;
    }
}
