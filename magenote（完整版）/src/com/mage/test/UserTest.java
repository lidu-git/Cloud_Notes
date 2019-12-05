package com.mage.test;

import org.junit.Test;

import com.mage.dao.UserDao;
import com.mage.po.User;

/**
 * 单元测试类
 * @Test标注的类
 * 1、不能有父类
 * 2、不能是静态方法
 * 3、不能有参数
 * 4、返回类型为void
 * @author Cushier
 *
 */
public class UserTest {

	@Test
	public void test() {
		UserDao userDao = new UserDao();
		User user = userDao.findUserByUname("admin");
		System.out.println(user.getNick());
	}

}
