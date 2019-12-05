package com.mage.util;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;

/**
 * // 设置JSON格式的响应类型，将resultInfo对象转换成JSON格式的字符串，响应给ajax的回调函数
 * @author Cushier
 *
 */
public class JsonUtil {

	public static void toJson(Object object,HttpServletResponse response){
		try {
			// 设置响应的MIME 类型
			response.setContentType("application/json;charset=UTF-8");
			// 将对象转化成JSON字符串
			String json = JSON.toJSONString(object);
			// 得到输出流
			PrintWriter out = response.getWriter();
			// 响应JSON字符串
			out.write(json);
			// 关闭流
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
