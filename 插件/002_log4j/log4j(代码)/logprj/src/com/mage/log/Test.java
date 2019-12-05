package com.mage.log;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 1、获取记录器
 * 2、读取配置文件
 * 3、插入记录信息
 *
 */
public class Test {
	
	//1、获取slf4j的记录器
	private static Logger logger = LoggerFactory.getLogger(Test.class);
	
	public static void main(String[] args) {
		//2、读取配置文件 -->可以省略
		PropertyConfigurator.configure("bin/log4j.properties");		
		//3、插入记录信息
		String msg ="This is {} message";
		 // 记录debug级别的信息  
        logger.debug(msg,"debug");  
        // 记录info级别的信息  
        logger.info(msg,"info");  
        // 记录error级别的信息  
        logger.error(msg,"error");  
	}
}
