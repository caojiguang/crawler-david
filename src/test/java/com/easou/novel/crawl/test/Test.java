package com.easou.novel.crawl.test;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

public class Test {
	public static void main(String[] args) throws IOException {
		String str = FileUtils.readFileToString(new File("f:\\1.html"));
//		String str = "dskajdks endText moxiaoming endText hello div";
//		System.out.println(str);
		String regex = "<div id=\"endText\">((?![\\s\\S]*?<div id=\"endText\">)[\\s\\S]*?)<!-- 分页 -->";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
//		System.out.println(matcher.find());
		if(matcher.find()){
			System.out.println(matcher.group(1));
		}
	}
}
