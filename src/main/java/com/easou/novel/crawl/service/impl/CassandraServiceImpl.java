package com.easou.novel.crawl.service.impl;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easou.hector.cas07.ICas07ClientMgr;
import com.easou.novel.crawl.factory.CassandraFactory;
import com.easou.novel.crawl.service.ICassandraService;

@Service
public class CassandraServiceImpl implements ICassandraService {
	private static Logger logger = LoggerFactory.getLogger(CassandraServiceImpl.class);
	
	@Autowired
	private CassandraFactory cassandraFactory;
	
	@Override
	public boolean saveImage(String key, byte[] image) {
		try {
			ICas07ClientMgr cas07ClientMgr = cassandraFactory.getImageClient();
			cas07ClientMgr.insert(key, image, new byte[]{1});
			return true;
		} catch (Exception e) {
			logger.error("save image error! key[{}]", key, e);
		}
		return false;
	}

	@Override
	public byte[] getImage(String key) {
		try {
			ICas07ClientMgr cas07ClientMgr = cassandraFactory.getImageClient();
			ByteBuffer buffer = cas07ClientMgr.get(key, new byte[]{1});
			if(buffer != null)
				return buffer.array();
		} catch (Exception e) {
			logger.error("get image error! key[{}]", key, e);
		}
		
		return null;
	}
}
