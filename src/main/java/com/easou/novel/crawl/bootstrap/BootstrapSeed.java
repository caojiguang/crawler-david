package com.easou.novel.crawl.bootstrap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BootstrapSeed {
	private static Logger logger = LoggerFactory.getLogger(BootstrapSeed.class);
	private AbstractApplicationContext ac;
	private ServerSocket serverSocket;
	private int port = 10000;

	public void start() {
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			throw new RuntimeException("bind port error.", e);
		}
		ac = new ClassPathXmlApplicationContext("applicationContext-seed.xml");
		ac.registerShutdownHook();
	}

	public void waitForStop() {
		while (true) {
			Socket socket = null;
			try {
				socket = serverSocket.accept();
				socket.setSoTimeout(6000);
				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				if ("shutdown".equalsIgnoreCase(reader.readLine())) {
					break;
				}
			} catch (SocketTimeoutException e) {
				logger.error("",e);
			} catch (IOException e) {
				logger.error("",e);
				break;
			} finally{
				if(socket != null){
					try {
						socket.close();
					} catch (IOException e) {
						logger.error("",e);
					}
				}
			}

		}
	}

	public void stop() {
		ac.close();
		try {
			serverSocket.close();
		} catch (IOException e) {
			throw new RuntimeException("close port error.", e);
		}
	}
	
	public static void main(String[] args) {
		BootstrapSeed bootstrap = new BootstrapSeed();
		bootstrap.start();
		bootstrap.waitForStop();
		bootstrap.stop();
	}
}