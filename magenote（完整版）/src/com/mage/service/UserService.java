package com.mage.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import com.mage.dao.UserDao;
import com.mage.po.User;
import com.mage.po.vo.ResultInfo;
import com.mage.util.MD5Util;
import com.mage.util.StringUtil;

public class UserService {
	
	private UserDao userDao = new UserDao();
	
	/**
	 * 业务逻辑：参数的非空判断、判断用户对象是否为空、判断密码是否正确
		1、判断非空参数
			如果为空，code=0，msg=XXX不能为空，返回resultInfo对象
		2、调用Dao层，通过用户名查询用户对象，返回user对象
		3、判断用户对象是否为空
			如果为空，code=0，msg=用户不存在，设置user的回显对象，返回resultInfo对象
		4、判断前台传递的密码是否和数据库中查询到的一致
			如果为空，code=0，msg=用户密码不正确，设置user回显对象，返回resultInfo对象
		5、code=1，msg=登录成功，返回resultInfo对象
	 * @param uname
	 * @param upwd
	 * @return
	 */
	public ResultInfo<User> login(String uname, String upwd){
		ResultInfo<User> resultInfo = new ResultInfo<>();
		// 1、判断非空参数
		if(StringUtil.isEmpty(uname) || StringUtil.isEmpty(upwd)){
			// 如果为空，code=0，msg=XXX不能为空，返回resultInfo对象
			resultInfo.setCode(0);
			resultInfo.setMsg("用户名称或密码不能为空！");
			return resultInfo;
		}
		
		// 回显对象（前台登录界面）
		User u = new User();
		u.setUname(uname);
		u.setUpwd(upwd);
		
		// 2、调用Dao层，通过用户名查询用户对象，返回user对象
		User user = userDao.findUserByUname(uname);
		// 3、判断用户对象是否为空
		if(user == null){
			// 如果为空，code=0，msg=用户不存在，设置user的回显对象，返回resultInfo对象
			resultInfo.setCode(0);
			resultInfo.setMsg("用户不存在");
			resultInfo.setResult(u);
			return resultInfo;
		}
		
		// 4、判断前台传递的密码是否和数据库中查询到的一致
		String pwd = MD5Util.encode(upwd);
		if(!pwd.equals(user.getUpwd())){
			// 如果为空，code=0，msg=用户密码不正确，设置user回显对象，返回resultInfo对象
			resultInfo.setCode(0);
			resultInfo.setMsg("用户密码不正确！");
			resultInfo.setResult(u);
			return resultInfo;
		}
		
		// 5、code=1，msg=登录成功，返回resultInfo对象
		resultInfo.setCode(1);
		resultInfo.setMsg("登录成功！");
		resultInfo.setResult(user);
		
		return resultInfo;
	}

	/**
	 * 	1、判断昵称是否为空
			code=0，msg="用户昵称不能为空"
		2、调用Dao层，通过昵称和用户ID查询用户对象是否存在，返回查询到的总数量
		3、判断总数量是否大于0
			大于0，昵称不可用
				code=0,msg="用户昵称已存在，请重试！"
			不大于0，昵称可用
				code=1,msg="昵称可用"
		4、返回resultInfo对像
			不大于0，昵称可用
	 * @param nick
	 * @return
	 */
	public ResultInfo<User> checkNick(String nick,Integer userId) {
		ResultInfo<User> resultInfo = new ResultInfo<>();
		// 1、判断昵称是否为空
		if(StringUtil.isEmpty(nick)){
			// code=0，msg="用户昵称不能为空"
			resultInfo.setCode(0);
			resultInfo.setMsg("用户昵称不能为空");
			return resultInfo;
		}
		
		// 2、调用Dao层，通过昵称和用户ID查询用户对象是否存在，返回查询到的总数量
		long count = userDao.checkNick(nick,userId);
		
		// 3、判断总数量是否大于0
		if(count > 0){
			// 大于0，昵称不可用
			resultInfo.setCode(0);
			resultInfo.setMsg("用户昵称已存在，请重试！");
		} else{
			// 不大于0，昵称可用
			resultInfo.setCode(1);
			resultInfo.setMsg("昵称可用！");
		}
		
		return resultInfo;
	}

	/**
	 * 修改用户信息
	 * 	1、接受参数（昵称、心情）
		2、判断参数是否为空（昵称）
		3、获取session作用域中的user对象，并得到userId
		4、通过Servlet3.0的文件上传功能，得到上传的文件名
			1、通过getPart(name)方法，得到part对象 name：表单中的file文件域的name属性值
			2、得到文件存放的路径
			3、得到上传的文件名
			4、如果文件名不为空，表示上传了文件，上传文件到指定路径；如果文件名为空，则设置默认的值为session域对象中的user的head属性的值
		5、调用Dao层，修改用户信息，返回受影响的行数
		6、判断受影响的行数的值，设置不同的响应值
			如果修改成功，更新session域对象中的user对象
		7、返回resultInfo的值
	 * @param request
	 * @return
	 */
	public ResultInfo<User> updateInfo(HttpServletRequest request) {
		ResultInfo<User> resultInfo = new ResultInfo<>();
		
		// 1、接受参数（昵称、心情）
		String nick = request.getParameter("nick");
		String mood = request.getParameter("mood");
		
		// 2、判断参数是否为空（昵称）
		if(StringUtil.isEmpty(nick)){
			resultInfo.setCode(0);
			resultInfo.setMsg("用户昵称不能为空！");
			return resultInfo;
		}
		
		// 3、获取session作用域中的user对象，并得到userId
		User user = (User) request.getSession().getAttribute("user");
		
		// 默认的头像
		String head = user.getHead();
		
		// 4、通过Servlet3.0的文件上传功能，得到上传的文件名
		// ---------------文件上传-----------------
		try {
			// 1、通过getPart(name)方法，得到part对象 name：表单中的file文件域的name属性值
			Part part = request.getPart("img");
			// 2、得到文件存放的路径
			String path = request.getServletContext().getRealPath("/WEB-INF/upload/");
			// 3、得到上传的文件名
			String fileName = part.getSubmittedFileName();
			// 4、如果文件名不为空，表示上传了文件，上传文件到指定路径；如果文件名为空，则设置默认的值为session域对象中的user的head属性的值
			if(StringUtil.isNotEmpty(fileName)){
				// 上传文件到指定路径
				part.write(path + fileName);
				
				// 重新设置头像的值
				head = fileName;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// ---------------修改用户-----------------
		// 5、调用Dao层，修改用户信息，返回受影响的行数
		int row = userDao.updateInfo(nick,head,mood,user.getUserId());
		// 6、判断受影响的行数的值，设置不同的响应值
		if(row > 0){
			resultInfo.setCode(1);
			resultInfo.setMsg("更新成功");
			// 更新session域对象中的user对象
			user.setNick(nick);
			user.setHead(head);
			user.setMood(mood);
			request.getSession().setAttribute("user", user);
		} else {
			resultInfo.setCode(0);
			resultInfo.setMsg("更新失败");
		}
		return resultInfo;
	}

}
