package com.mage.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mage.po.User;
import com.mage.util.DBUtil;

/**
 * 用户表的JDBC操作
 * @author Cushier
 *
 */
public class UserDao {
	
	// 记入日志
	private static Logger logger = LoggerFactory.getLogger(UserDao.class);

	/**
	 * 通过用户名查询用户对象
		1、获取数据库连接
		2、定义sql语句
		3、预编译
		4、设置参数，下表从1开始
		5、执行查询，返回resultSet结果集
		6、判断并分析结果集，得到user对象
		7、关闭资源
		8、返回user对象
	 * @param uname
	 * @return
	 */
	public User findUserByUname(String uname){
		User user = null;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			// 1、获取数据库连接
			connection = DBUtil.getConnection();
			// 2、定义sql语句
			String sql = "select * from tb_user where uname = ?";
			
			// 打印sql语句
			logger.info("查询的sql语句：" + sql);
			logger.debug("查询的sql语句：{}",sql);
			// 3、预编译
			preparedStatement = connection.prepareStatement(sql);
			// 4、设置参数，下表从1开始
			preparedStatement.setString(1, uname);
			// 5、执行查询，返回resultSet结果集
			resultSet = preparedStatement.executeQuery();
			// 6、判断并分析结果集，得到user对象
			while(resultSet.next()){
				user = new User();
				user.setHead(resultSet.getString("head"));
				user.setMood(resultSet.getString("mood"));
				user.setNick(resultSet.getString("nick"));
				user.setUname(uname);
				user.setUpwd(resultSet.getString("upwd"));
				user.setUserId(resultSet.getInt("userId"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 7、关闭资源
			DBUtil.close(resultSet, preparedStatement, connection);
		}
		// 8、返回user对象
		return user;
	}

	/**
	 * 验证昵称的唯一性
	 * @param nick
	 * @param userId
	 * @return
	 */
	public long checkNick(String nick, Integer userId) {
		long count = 0;
		Connection conn = null;
		PreparedStatement sta = null;
		ResultSet res = null;
		try {
			// 得到数据库连接
			conn = DBUtil.getConnection();
			// 编写sql
			String sql = "select count(1) from tb_user where nick = ? and userId != ?";
			// 预编译
			sta = conn.prepareStatement(sql);
			// 设置参数
			sta.setString(1, nick);
			sta.setInt(2, userId);
			// 执行查询
			res = sta.executeQuery();
			// 判断是否查询到 分析结果集
			while(res.next()){
				count = res.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			// 关闭资源
			DBUtil.close(res, sta, conn);
		}
		return count;
	}

	/**
	 * 修改用户信息
	 * @param nick
	 * @param head
	 * @param mood
	 * @param userId
	 * @return
	 */
	public int updateInfo(String nick, String head, String mood, Integer userId) {
		int row = 0;
		Connection conn = null;
		PreparedStatement sta = null;
		try {
			// 获得连接
			conn = DBUtil.getConnection();
			// 编写sql
			String sql = "update tb_user set nick = ?, head = ?, mood = ? where userId = ?";
			// 预编译
			sta = conn.prepareStatement(sql);
			// 设置参数
			sta.setString(1, nick);
			sta.setString(2, head);
			sta.setString(3, mood);
			sta.setInt(4, userId);
			// 执行更新
			row = sta.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭资源
			DBUtil.close(null, sta, conn);
		}
		return row;
	}
	
}
