package com.mage.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

/**
 * 请求字符乱码处理
 * 			
 * 	POST请求
 * 		Tomcat任意版本处理   request.setCharsetEncoding("UTF-8");
 * 	
 * 	GET请求
 * 		Tomcat8及以上版本 不需要处理
 * 		Tomcat7需要处理
 * 			new String(request.getParameter(参数值),"UTF-8");
 * 	
 */
@WebFilter("/*")
public class EncodeFilter implements Filter {

    public EncodeFilter() {
    	
    }
	public void destroy() {


	}


	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain chain) throws IOException, ServletException {

		
		// 基于HTTP
		HttpServletRequest request = (HttpServletRequest) arg0;
		HttpServletResponse response = (HttpServletResponse) arg1;
		
		// 处理POST请求乱码
		request.setCharacterEncoding("UTF-8");
		
		/**
		 * 处理Tomcat7及以下版本的GET请求
		 */
		// 获取请求方式 GET/PSOT  get/post
		String method = request.getMethod();
		// 判断是否是GET请求
		if ("GET".equals(method.toUpperCase())) {
			// 得到服务器版本信息
			String serverInfo = request.getServletContext().getServerInfo(); // Apache Tomcat/7.0.79
			// 得到服务器的版本号
			String versionStr = serverInfo.substring(serverInfo.indexOf("/")+1,serverInfo.indexOf("/")+2);
			// 判断服务器版本是否是tomcat8以下（不包含8）
			if (versionStr != null && Integer.parseInt(versionStr) < 8) {
				// 获取QueryString(),分割字符串，循环设置
				
				// 服务器版本是7及以下
				// 定义内部类，继承HttpServletRequestWapper类，通过重写getParameter()方法，解决乱码问题
				MyWapper mywapper = new MyWapper(request); // MyWapper类的本质就是request对象
				chain.doFilter(mywapper, response);
				return;
			}
		}

		chain.doFilter(request, response);
	}



	public void init(FilterConfig fConfig) throws ServletException {

	}
	
	/**
	 * 1、定义内部类
	 * 2、继承HttpServletRequestWapper类
	 * 3、重写getParameter()方法，解决乱码问题
	 * @author Cushier
	 *
	 */
	class MyWapper extends HttpServletRequestWrapper {

		private HttpServletRequest request; // 定义request对象，提升构造方法里面的request对象的作用域
		public MyWapper(HttpServletRequest request) {
			super(request);
			this.request = request;
		}

		/**
		 * 重写getParameter()方法，处理系统接收参数的乱码问题
		 */
		@Override
		public String getParameter(String name) {
			String value = null;
			// new String(request.getParameter(name).getBytes("ISO-8859-1"),"UTF-8")
			try {
				value = new String (request.getParameter(name).getBytes("ISO-8859-1"),"UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return value;
		}
		
	}
	

}