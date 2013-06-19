// Copyright(c) 2011 easou.com
package com.easou.novel.crawl.service;

import java.net.URI;

public class TestUri {

	/**
	 * 
	 */
	public TestUri() {
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		URI uri = URI.create("http://www.chinanews.com/sportchannel/");
		System.out.println(uri.getHost());
		System.out.println(uri.getScheme());
		System.out.println(uri.getUserInfo());		

	}

}
