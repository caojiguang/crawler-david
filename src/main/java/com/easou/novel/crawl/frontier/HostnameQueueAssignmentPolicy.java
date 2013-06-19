package com.easou.novel.crawl.frontier;

import java.net.URI;

import com.easou.novel.crawl.model.CrawlUrl;

public class HostnameQueueAssignmentPolicy {
	
	public String getClassKey(CrawlUrl curi) {
		URI uri = curi.getUri();
		String host = uri.getHost();
		if (host == null || host.equals("")) {
			host = "default";
		}
		return host;
	}
	
}
