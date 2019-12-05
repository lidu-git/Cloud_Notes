package com.mage.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mage.po.Note;
import com.mage.po.NoteType;
import com.mage.po.User;
import com.mage.po.vo.ResultInfo;
import com.mage.service.NoteService;
import com.mage.service.TypeService;
import com.mage.util.StringUtil;

/**
 * 云记模块：
 * 1、进入发表云记页面 actionName=view
 * 2、添加或修改云记 actionName=addOrUpdate
 * 3、查询云记详情页面 actionName=detail
 * 4、删除云记 actionName=delete
 * 5、云记列表 actionName=list
 */
@WebServlet("/note")
public class NoteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private NoteService noteService = new NoteService();

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 得到用户行为
		String actionName = request.getParameter("actionName");
		if("view".equals(actionName)){
			// 进入发表云记页面
			viewNote(request,response);
		} else if("addOrUpdate".equals(actionName)){
			// 添加或修改云记
			addOrUpdate(request,response);
		} else if("detail".equals(actionName)){
			// 查看云记详情
			noteDetail(request,response);
		} else if("delete".equals(actionName)){
			// 删除云记
			deleteNote(request,response);
		}
	}

	/**
	 * 删除云记
	 * 	1、接收参数
		2、调用Service层，返回code
		3、响应结果
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	private void deleteNote(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 1、接收参数
		String noteId = request.getParameter("noteId");
		// 2、调用Service层，返回code
		Integer code = noteService.deleteNote(noteId);
		// 3、响应结果
		response.getWriter().write(code+"");
		response.getWriter().close();
	}

	/**
	 * 查看云记详情
	 * 	1、接收参数（云记ID）
		2、调用Service层，查询云记对象
		3、将云记对象存到request作用域中
		4、设置首页动态包含的页面值
		5、请求转发跳转到index.jsp
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void noteDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1、接收参数（云记ID）
		String noteId = request.getParameter("noteId");
		// 2、调用Service层，查询云记对象
		Note note = noteService.findNoteById(noteId);
		// 3、将云记对象存到request作用域中
		request.setAttribute("note", note);
		// 4、设置首页动态包含的页面值
		request.setAttribute("changePage", "note/detail.jsp");
		// 5、请求转发跳转到index.jsp
		request.getRequestDispatcher("index.jsp").forward(request, response);
	}

	/**
	 * 添加或修改云记
	 *  1、接收参数（类型Id、标题、内容）
		2、调用Service层更新方法，返回resultInfo对象
		3、判断是否更新成功
			如果成功，跳转到首页index.jsp
			如果失败，请求转发跳转到note?actionName=view页面
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void addOrUpdate(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		// 1、接收参数（类型Id、标题、内容）
		String typeId = request.getParameter("typeId");
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		// 修改功能还需要noteId
		String noteId = request.getParameter("noteId");
		
		
		// 2、调用Service层更新方法，返回resultInfo对象
		ResultInfo<Note> resultInfo = noteService.addOrUpdate(typeId,title,content,noteId);
		
		// 3、判断是否更新成功
		if(resultInfo.getCode() == 1){
			// 如果成功，跳转到首页index.jsp
			response.sendRedirect("index");
		} else {
			// 将resultInfo对象存到request作用域中
			request.setAttribute("resultInfo", resultInfo);
			String url = "note?actionName=view";
			if(StringUtil.isNotEmpty(noteId)){
				url += "&typeId=" + noteId;
			}
			// 请求转发跳转到note?actionName=view
			request.getRequestDispatcher(url).forward(request, response);
		}
		
	}

	/**
	 * 进入发表云记页面
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void viewNote(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 如果是修改云记，则需要通过noteId查询云记对象
		// 1、接收参数（云记ID）
		String noteId = request.getParameter("noteId");
		// 2、调用Service层，查询云记对象
		Note note = noteService.findNoteById(noteId);
		// 3、将云记对象存到request作用域中
		request.setAttribute("noteInfo", note);
		
		// 从session作用域中获取user对象
		User user = (User)request.getSession().getAttribute("user");
		// 得到当前登录用户的所有类型列表
		List<NoteType> noteList = new TypeService().findTypeList(user.getUserId());
		// 将查询到的类型集合存到request作用域中
		request.setAttribute("noteList", noteList);
		// 设置首页动态包含的页面值
		request.setAttribute("changePage", "note/view.jsp");
		// 请求转发跳转到index.jsp
		request.getRequestDispatcher("index.jsp").forward(request, response);
	}

}
