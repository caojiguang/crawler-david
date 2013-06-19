package com.easou.novel.crawl.util;

import java.net.MalformedURLException;
import java.net.URL;

public class UrlUtil {
	/**
	 * 拼接url
	 * @param baseUri
	 * @param append
	 * @return
	 */
	public static String joinUrl(String baseUri, String append) {
		try {
			URL base = new URL(baseUri);
			if (append.startsWith("?"))
				append = base.getPath() + append;
			URL abs = new URL(base, append);
			return abs.toExternalForm();
		} catch (MalformedURLException e) {
			try {
				URL abs = new URL(append);
				return abs.toExternalForm();
			} catch (MalformedURLException e1) {}
		}
		
		return null;
	}
	
	public static void main(String[] args) {
		String baseUri = "http://mil.21cn.com/gn/2012/10/29/13432053.shtml";
		String append = "http://ww.easou.com/13432053_1.shtml";
		System.out.println(joinUrl(baseUri, append));
	}
}
