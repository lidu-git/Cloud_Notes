package com.mage.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.tomcat.util.codec.binary.Base64;

/**
 * MD5加密字符串
 * @author Cushier
 *
 */
public class MD5Util {

	public static String encode(String str){
		String value = "";
		
		try {
			// 得到MD5加密算法应用程序
			MessageDigest messageDigest = MessageDigest.getInstance("md5");
			// 通过MD5加密算法加密字符串后得到 byte数组
			byte[] bytes = messageDigest.digest(str.getBytes());
			// 通过base64编码，将byte数组转换成字符串
			value = Base64.encodeBase64String(bytes);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return value;
	}
	
	public static void main(String[] args) {
		System.out.println(MD5Util.encode("123456"));
	}
}