package com.mage.web;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;

import com.mage.po.User;
import com.mage.po.vo.ResultInfo;
import com.mage.service.UserService;
import com.mage.util.JsonUtil;

/**
 * 用户模块
 * 	1、用户登录 actionName=login
 * 	2、退出登录 actionName=logout
 * 	3、进入个人中心 actionName=userCenter
 * 	4、加载用户头像 actionName=userHead
 * 	5、验证昵称的唯一性 actionName=checkNick
 * 	6、修改用户信息（上传文件） actionName=updateInfo
 * @author Cushier
 *
 */
@WebServlet("/user")
@MultipartConfig
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserService userService = new UserService();

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 接受用户行为（actionName）
		String actionName = request.getParameter("actionName");
		
		if("login".equals(actionName)){
			
			// 用户登录
			userLogin(request, response);
			
		} else if("logout".equals(actionName)){
			
			// 退出登录
			userLogOut(request, response);
			
		} else if("userCenter".equals(actionName)){
			
			// 进入个人中心
			userCenter(request, response);
			
		} else if("userHead".equals(actionName)){
			
			// 加载用户头像
			userHead(request, response);
			
		} else if("checkNick".equals(actionName)){
			
			// 验证昵称的唯一性
			checkNick(request, response);
			
		} else if("updateInfo".equals(actionName)){
			
			// 修改用户信息
			updateInfo(request, response);
			
		} else {
			
			// 跳转到登录页面
			response.sendRedirect("login.jsp");
			
		}
	}
	
	/**
	 * 修改用户信息
	 * 	1、调用Service层的修改方法，返回resultInfo对象（参数为：request对象）
		2、将resultInfo对象存到request作用域中
		3、请求转发跳转到user?actionName=userCenter
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void updateInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1、调用Service层的修改方法，返回resultInfo对象（参数为：request对象）
		ResultInfo<User> resultInfo = userService.updateInfo(request);
		// 2、将resultInfo对象存到request作用域中
		request.setAttribute("resultInfo", resultInfo);
		// 3、请求转发跳转到user?actionName=userCenter
		request.getRequestDispatcher("user?actionName=userCenter").forward(request, response);
		
	}

	/**
	 * 验证昵称的唯一性
	 *  1、接受参数（昵称）
		2、获取session作用域中的user对象，并得到userId
		3、调用Service层的验证方法，返回resultInfo对象
		4、设置JSON格式的响应类型，将resultInfo对象转换成JSON格式的字符串，响应给ajax的回调函数
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	private void checkNick(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 1、接受参数（昵称）
		String nick = request.getParameter("nickName");
		// 2、获取session作用域中的user对象，并得到userId
		User user = (User)request.getSession().getAttribute("user");
		Integer userId = user.getUserId();
		// 3、调用Service层的验证方法，返回resultInfo对象
		ResultInfo<User> resultInfo = userService.checkNick(nick,userId);
		// 4、将resultInfo对象转换成JSON格式的字符串，响应给ajax的回调函数
		JsonUtil.toJson(resultInfo, response);
	}

	/**
	 * 加载用户头像
		1、获取参数（图片名称）
		2、获取图片存放的真实路径
		3、通过存放路径得到file对象
		4、判断file对象是否为空并且是一个标准文件
		5、判断图片类型，设置指定的MIME响应类型
		6、利用commons-io的工具类FileUtils.coptFile()拷贝文件
	 * @param request
	 * @param response
	 * @throws IOException 4
	 */
	private void userHead(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 1、获取参数（图片名称）
		String imageName = request.getParameter("imageName");
		// 2、获取图片存放的真实路径
		String filePath = request.getServletContext().getRealPath("/WEB-INF/upload/" + imageName);
		// 3、通过存放路径得到file对象
		File file = new File(filePath);
		// 4、判断file对象是否为空并且是一个标准文件
		if(file.exists() && file.isFile()){
			// 5、判断图片类型，设置指定的MIME响应类型
			String pic = imageName.substring(imageName.indexOf(".")+1);
			if("png".equalsIgnoreCase(pic)){
				response.setContentType("image/png");
			} else if("jpg".equalsIgnoreCase(pic) || "jpeg".equalsIgnoreCase(pic)){
				response.setContentType("image/jpeg");
			} else if("gif".equalsIgnoreCase(pic)){
				response.setContentType("image/gif");
			}
			// 6、利用commons-io的工具类FileUtils.coptFile()拷贝文件
			FileUtils.copyFile(file, response.getOutputStream());
		}
	}

	/**
	 * 进入个人中心
	 * 	1、设置首页动态包含的值，存到request作用域中
	 * 	2、请求转发跳转到index.jsp
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void userCenter(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1、设置首页动态包含的值，存到request作用域中
		request.setAttribute("changePage", "user/info.jsp");
		
		// 2、请求转发跳转到index.jsp
		request.getRequestDispatcher("index.jsp").forward(request, response);
	}

	/**
	 * 退出登录
	 * 	1、销毁session对象
	 * 	2、删除cookie对象
	 * 	3、重定向到登录页面
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	private void userLogOut(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 1、销毁session对象
		request.getSession().invalidate();
		
		// 清空session中的user对象
		// request.getSession().setAttribute("user", null);
		
		// 删除session中的user对象
		// request.getSession().removeAttribute("user");
		
		// 2、删除cookie对象
		Cookie cookie = new Cookie("user",null);
		cookie.setMaxAge(0);
		response.addCookie(cookie);
		
		// 3、重定向到登录页面
		response.sendRedirect("login.jsp");
	}

	/**
	 * 	1、接受参数（用户名、密码、记住密码）
		2、调用Service层的登录方法，返回resultInfo对象
		3、判断resultInfo的code的值
			code=0，登录失败，将resultInfo对象存到request作用域中，请求转发跳转到登录页面
			code=1，登录成功
				将resultInfo对象的用户对象（result）存到session作用域中（验证当前用户是否是登录状态）
				判断用户是否记住密码，如果记住密码，存cookie对象（1、创建cookie对象 2、设置cookie的失效时间 3、响应cookie对象）
				重定向到首页
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void userLogin(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		// 1、接受参数（用户名、密码、记住密码）
		String uname = request.getParameter("uname");
		String upwd = request.getParameter("upwd");
		
		// 2、调用Service层的登录方法，返回resultInfo对象
		ResultInfo<User> resultInfo = userService.login(uname, upwd);
		
		// 3、判断resultInfo的code的值
		if(resultInfo.getCode() == 1){ // code=1，登录成功
			// 将resultInfo对象的用户对象（result）存到session作用域中（验证当前用户是否是登录状态）
			request.getSession().setAttribute("user", resultInfo.getResult());
			// 判断用户是否记住密码
			String rem = request.getParameter("rem");
			// 如果记住密码，存cookie对象 2、设置cookie的失效时间 3、响应cookie对象）
			if("1".equals(rem)){
				// 1、创建cookie对象
				Cookie cookie = new Cookie("user",uname + "-" + upwd);
				// 2、设置cookie的失效时间
				cookie.setMaxAge(3 * 24 * 60 * 60); // 3天有效
				// 3、响应cookie对象
				response.addCookie(cookie);
			}
			// 重定向到首页跳转到indexServlet
			response.sendRedirect("index");
			
		} else { // code=0，登录失败
			// 将resultInfo对象存到request作用域中
			request.setAttribute("resultInfo", resultInfo);
			// 请求转发跳转到登录界面
			request.getRequestDispatcher("login.jsp").forward(request, response);
		}
	}

}
