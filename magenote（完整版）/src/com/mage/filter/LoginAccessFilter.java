package com.mage.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mage.po.User;

/**
 * 非法访问拦截
 * 	拦截什么：未登录情况下，访问必须登录之后才能访问的页面
 * 	方向什么：
 * 		1、静态资源 放行（statics目录下资源）
 * 		2、指定页面 放行（不需要登录即可访问的页面）
 * 		3、指定行为 放行（登录操作、注册操作等）
 * 		4、登录状态 放行
 * 	自动登录
 * 		1、未登录情况
 * 		2、选择了记住密码
 */
@WebFilter("/*")
public class LoginAccessFilter implements Filter {

    public LoginAccessFilter() {
    
    }

	public void destroy() {
	
	}

	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain chain) throws IOException, ServletException {
		// 基于HTTP
		HttpServletRequest request = (HttpServletRequest) arg0;
		HttpServletResponse response = (HttpServletResponse) arg1;
		
		// 获取请求路径
		String url = request.getRequestURI(); // /站点名/资源路径
		
		// 静态资源 放行（statics目录下资源）
		if(url.contains("/statics")){
			chain.doFilter(request, response);
			return;
		}
		
		// 指定页面 放行（不需要登录即可访问的页面）
		if(url.contains("/login.jsp")){
			chain.doFilter(request, response);
			return;
		}
		
		// 指定行为 放行（登录操作、注册操作等）
		String actionName = request.getParameter("actionName");
		
		// 判断是否是用户模块
		if(url.contains("/user")){
			// 登录功能
			if("login".equals(actionName)){
				chain.doFilter(request, response);
				return;
			}
		}
		
		// 得到session的用户对象
		User user = (User) request.getSession().getAttribute("user");
		// 如果用户对象不为空，说明未登录状态
		if(user != null){
			chain.doFilter(request, response);
			return;
		}
		
		// 未登录情况
		// 判断cookie中是否记住密码
		// 获取cookie
		Cookie[] cookies = request.getCookies();
		// 判断cookie是否为空
		if(cookies != null && cookies.length > 0){
			// 遍历cookie数组
			for (Cookie cookie : cookies) {
				// 判断是否是记住密码的cookie对象
				if("user".equals(cookie.getName())){
					// 得到cookie的值
					String value = cookie.getValue();
					// 分割cookie的值，得到登录的用户名和密码
					String uname = value.split("-")[0];
					String upwd = value.split("-")[1];
					// 请求转发跳转 调用登录功能
					request.getRequestDispatcher("user?actionName=login&uname="+uname+"&upwd="+upwd).forward(request, response);
					return;
				}
			}
		}
		
		// 拦截到登录界面
		response.sendRedirect("login.jsp");
	}

	public void init(FilterConfig fConfig) throws ServletException {
	
	}

}
