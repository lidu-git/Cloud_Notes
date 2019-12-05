package com.mage.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mage.po.Note;
import com.mage.po.User;
import com.mage.po.vo.NoteVo;
import com.mage.po.vo.ResultInfo;
import com.mage.service.NoteService;
import com.mage.util.Page;

/**
 * 主页
 */
@WebServlet("/index")
public class IndexServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private NoteService noteService = new NoteService();

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 接收用户行为 actionName
		String actionName = request.getParameter("actionName");
		// 将用户行为存到request作用域中（给云记列表的分页导航栏的查询参数）
		request.setAttribute("actionName", actionName);
		// 标题搜索
		if("searchTitle".equals(actionName)){
			// 得到查询的标题
			String title = request.getParameter("title");
			// 将查询的标题存到request作用域中
			request.setAttribute("title", title);
			// 分页查询云记列表，标题搜索
			noteList(request,response,title,null,null);
		} else if("searchDate".equals(actionName)){
			// 得到查询的日期
			String date = request.getParameter("date");
			// 将查询到的日期存到request作用域中
			request.setAttribute("date", date);
			// 分页查询云记列表，标题搜索
			noteList(request,response,null,date,null);
		} else if("searchType".equals(actionName)){
			// 得到查询的类型Id
			String typeId = request.getParameter("typeId");
			// 将查询到的类型Id存到request作用域中
			request.setAttribute("typeId", typeId);
			// 分页查询云记列表，标题搜索
			noteList(request,response,null,null,typeId);
		} else {
			// 分页查询云记列表
			noteList(request,response,null,null,null);
		}
	}

	/**
	 * 分页查询云记列表
	 *  1、接收分页参数（当前页、每页显示的数量）
		2、从session中获取user对象
		3、调用Service层的查询方法，返回resultInfo对象
		4、将resultInfo存request作用域中
		5、设置首页动态包含的页面值
		6、请求转发跳转到index.jsp
	 * @param request
	 * @param response
	 * @param type 
	 * @param object2 
	 * @param object 
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void noteList(HttpServletRequest request, HttpServletResponse response, String title, String date, String typeId) throws ServletException, IOException {
		// 1、接收分页参数（当前页、每页显示的数量）
		String pageNum = request.getParameter("pageNum");
		String pageSize = request.getParameter("pageSize");
		
		// 2、从session中获取user对象
		User user = (User)request.getSession().getAttribute("user");
		
		// 3、调用Service层的查询方法，返回resultInfo对象
		ResultInfo<Page<Note>> resultInfo = new NoteService().findNoteListByPage(user.getUserId(),pageNum,pageSize,title,date,typeId);
		// 4、将resultInfo存request作用域中
		request.setAttribute("resultInfo", resultInfo);
		// 查询左侧云记日期分组
		List<NoteVo> dateInfo = noteService.findNoteGroupByDate(user.getUserId());
		request.getSession().setAttribute("dateInfo", dateInfo);
		// 查询左侧云记类型分组
		List<NoteVo> typeInfo = noteService.findNoteGroupByType(user.getUserId());
		request.getSession().setAttribute("typeInfo", typeInfo);
		// 设置首页动态包含的页面值
		request.setAttribute("changePage", "note/list.jsp");
		// 请求转发跳转到index.jsp
		request.getRequestDispatcher("index.jsp").forward(request, response);
	}

}
