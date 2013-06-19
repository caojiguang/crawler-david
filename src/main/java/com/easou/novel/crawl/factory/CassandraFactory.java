package com.easou.novel.crawl.factory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.easou.hector.cas07.Cas07ClientFactory;
import com.easou.hector.cas07.ICas07ClientMgr;

@Component("cassandraFactory")
public class CassandraFactory {
	private static Logger logger = LoggerFactory.getLogger(CassandraFactory.class);
	private String CASS_IMAGE_FILE = "/cassandra_image.properties";
	private String CASS_IMAGE_NAME = "image_cas";
	
	@PostConstruct
	public void init() {
		try {
			Properties casPro = getProperties(CASS_IMAGE_FILE);
			Cas07ClientFactory.INSTANCE.builder(CASS_IMAGE_NAME, casPro);
		} catch (Exception e) {
			logger.error("init cassandra md5 error.", e);
		}
	}
	
	private Properties getProperties(String configFile) throws IOException {
		InputStream is = this.getClass().getResourceAsStream(configFile);
		Properties pro = new Properties();
		pro.load(is);
		return pro;
	}
	
	/**
	 * 获取存取MD5值得Cassandra对象
	 */
	public ICas07ClientMgr getImageClient() {
		ICas07ClientMgr client = null;
		try {
			client = Cas07ClientFactory.INSTANCE.getClient(CASS_IMAGE_NAME);
		} catch (Exception e) {
			logger.error("Get the cassandra md5 client error.", e);
		}
		return client;
	}

	
	@PreDestroy
	public void destory() {
		ICas07ClientMgr client = getImageClient();
		if(client != null)
			client.shutdown(CASS_IMAGE_NAME);
	}
}
