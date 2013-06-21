package com.easou.novel.crawl.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.easou.novel.crawl.util.Md5Util;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext.xml"})

public class md5Test {

    @Test
    public void getChapter(){
        String md5 = Md5Util.generateMd5ByUrl("http://www.23hh.com/book/3/3045/11658043.html");
        System.out.println(md5);
    }
    
}
