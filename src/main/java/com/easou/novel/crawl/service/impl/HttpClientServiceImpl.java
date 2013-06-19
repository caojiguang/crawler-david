package com.easou.novel.crawl.service.impl;

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easou.novel.crawl.factory.HttpClientFactory;
import com.easou.novel.crawl.service.IHttpClientService;

@Service
public class HttpClientServiceImpl implements IHttpClientService {
	private static Logger logger = LoggerFactory.getLogger(HttpClientServiceImpl.class);
	
	@Autowired
	private HttpClientFactory httpClientFactory;
	
	@Override
	public String getResponseBodyByGet(String url, String configCharset) {
		StopWatch stopWatch = new Log4JStopWatch();
		HttpGet httpGet = new HttpGet(url);
//		httpGet.setHeader("User-Agent", httpClientFactory.getRequestUA());
		httpGet.addHeader("Accept", "text/*");
		httpGet.addHeader("Pragma", "no-cache");
		httpGet.addHeader("Cache-Control", "no-cache");
        httpGet.addHeader("User-Agent", httpClientFactory.getRequestUA());
		httpGet.addHeader("Accept-Language", "zh-cn");
        httpGet.addHeader("Content-Type", "application/x-www-form-urlencoded");
		String body = null;
		try {
			HttpResponse httpResponse = httpClientFactory.getInstance().execute(httpGet);
			StatusLine statusLine = httpResponse.getStatusLine();
			if(statusLine.getStatusCode() == HttpStatus.SC_OK){
				HttpEntity httpEntity = httpResponse.getEntity();
				if(httpEntity != null){
					//编码获得策略
					String defaultCharset = "gbk";
					if(StringUtils.isBlank(configCharset)){
						Charset charset = ContentType.getOrDefault(httpEntity).getCharset();
						if(charset != null)
							defaultCharset = charset.name();
					} else {
						defaultCharset = configCharset;
					}
					
					body = EntityUtils.toString(httpEntity, defaultCharset);
				}
			}
		} catch (ClientProtocolException e) {
			logger.error("get response body error! url[{}]", url, e);
		} catch (IOException e) {
			logger.error("get response body error! url[{}]", url, e);
		} finally {
			httpGet.abort();//终止本次请求
		}
		
		stopWatch.stop("body response");
		return body;
	}

	@Override
	public String getResponseBodyByPost(String url) {
		return null;
	}

	@Override
	public byte[] getImageByGet(String url) {
		StopWatch stopWatch = new Log4JStopWatch();
		HttpGet httpGet = new HttpGet(url);
		byte[] data = null;
		try {
			HttpResponse httpResponse = httpClientFactory.getInstance().execute(httpGet);
			StatusLine statusLine = httpResponse.getStatusLine();
			if(statusLine.getStatusCode() == HttpStatus.SC_OK){
				HttpEntity httpEntity = httpResponse.getEntity();
				if(httpEntity != null){
					data = EntityUtils.toByteArray(httpEntity);	
				}
			}
		} catch (ClientProtocolException e) {
			logger.error("get image error! url[{}]", url, e);
		} catch (IOException e) {
			logger.error("get image error! url[{}]", url, e);
		} finally {
			httpGet.abort();//终止本次请求
		}
		stopWatch.stop("image response");
		return data;
	}
}
