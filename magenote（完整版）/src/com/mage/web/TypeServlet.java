package com.mage.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mage.po.NoteType;
import com.mage.po.User;
import com.mage.po.vo.ResultInfo;
import com.mage.service.TypeService;
import com.mage.util.JsonUtil;

/**
 * 类型模块
 * 	类型列表 actionName=list
 * 	添加类型 修改类型 actionName=addOrUpdate
 * 	删除类型 actionName=delete
 * 	验证类型名的唯一性 actionName=checkType
 */
@WebServlet("/type")
public class TypeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TypeService typeService = new TypeService();

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 接收参数
		String actionName = request.getParameter("actionName");
		
		// 判断用户行为
		if("list".equals(actionName)){
			// 查询类型列表
			typeList(request, response);
		} else if("addOrUpdate".equals(actionName)) {
			// 添加或修改类型
			addOrUpdate(request,response);
		} else if("checkType".equals(actionName)){
			// 验证当前用户的类型名唯一性
			checkType(request,response);
		} else if("deleteType".equals(actionName)){
			// 删除类型
			deleteType(request,response);
		}
	}

	/**
	 * 删除类型
	 *  1、接收参数（typeId）
		2、调用Service层，返回resultInfo对象
		3、将resultInfo对象转换成JSON字符串，响应给ajax的回调函数
	 * @param request
	 * @param response
	 */
	private void deleteType(HttpServletRequest request, HttpServletResponse response) {
		// 1、接收参数（typeId）
		String typeId = request.getParameter("typeId");
		
		// 2、调用Service层，返回resultInfo对象
		ResultInfo<NoteType> resultInfo = typeService.deleteType(typeId);
		
		// 3、将resultInfo对象转换成JSON字符串，响应给ajax的回调函数
		JsonUtil.toJson(resultInfo, response);
	}

	/**
	 * 验证当前用户的类型名唯一性
	 * 	1、接收参数（类型ID、类型名称）
		2、从session域对象获取user对象，得到userId
		3、调用Service层的方法，返回code（1=可用，0=不可用）
		4、响应结果
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	private void checkType(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 1、接收参数（类型ID、类型名称）
		String typeName = request.getParameter("typeName");
		String typeId = request.getParameter("typeId");
		// 2、从session域对象获取user对象，得到userId
		User user = (User) request.getSession().getAttribute("user");
		Integer userId = user.getUserId();
		// 3、调用Service层的方法，返回code（1=可用，0=不可用）
		Integer code = typeService.checkType(userId,typeName,typeId);
		// 4、响应结果
		response.getWriter().write(code+"");
		response.getWriter().close();
	}

	/**
	 * 添加或修改类型
	 * 	1、接收参数（类型名称）
		2、从session作用域中获取user对象，得到userId
		3、调用Service层的方法，返回resultInfo对象
		4、设置响应类型
		5、将resultInfo对象转换成JSON格式的字符串，响应给ajax的回调函数
	 * @param request
	 * @param response
	 */
	private void addOrUpdate(HttpServletRequest request, HttpServletResponse response) {
		// 1、接收参数（类型名称）
		String typeName = request.getParameter("typeName");
		String typeId = request.getParameter("typeId");
		// 2、从session作用域中获取user对象，得到userId
		User user = (User) request.getSession().getAttribute("user");
		Integer userId = user.getUserId();
		// 3、调用Service层的方法，返回resultInfo对象
		ResultInfo<NoteType> resultInfo = typeService.addOrUpdate(typeName,userId,typeId);
		// 4、将resultInfo对象转换成JSON格式的字符串，响应给ajax的回调函数
		JsonUtil.toJson(resultInfo, response);
	}

	/**
	 * 查询类型列表
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void typeList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1、从session域对象中得到user对象，并且得到userId
		User user = (User) request.getSession().getAttribute("user");
		Integer userId = user.getUserId();
		// 2、调用Service层的查询方法，通过用户ID查询当前登录用户下的所有类型列表，返回集合
		List<NoteType> typeList = typeService.findTypeList(userId);
		// 3、将集合存到request作用域中
		request.setAttribute("typeList", typeList);
		// 设置首页动态包含的页面值
		request.setAttribute("changePage", "type/list.jsp");
		// 请求转发跳转到index.jsp
		request.getRequestDispatcher("index.jsp").forward(request, response);
	}

}
