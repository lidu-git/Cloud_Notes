package com.mage.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mage.po.NoteType;
import com.mage.util.DBUtil;
import com.mage.util.StringUtil;

public class TypeDao {

	/**
	 * 通过用户ID查询当前登录用户的所有类型列表
	 * @param userId
	 * @return
	 */
	public List<NoteType> findTypeListByUserId(Integer userId) {
		List<NoteType> typeList = new ArrayList<>();
		Connection conn = null;
		PreparedStatement sta = null;
		ResultSet res = null;
		try {
			// 得到数据连接
			conn = DBUtil.getConnection();
			// 编写sql
			String sql = "select * from tb_note_type where userId = ?";
			// 预编译
			sta = conn.prepareStatement(sql);
			// 设置参数
			sta.setInt(1, userId);
			
			res = sta.executeQuery();
			// 判断是否查询到 分析结果集
			while (res.next()) {
				NoteType noteType = new NoteType();
				noteType.setTypeId(res.getInt("typeId"));
				noteType.setTypeName(res.getString("typeName"));
				noteType.setUserId(userId);
				typeList.add(noteType);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 关闭资源
			DBUtil.close(res, sta, conn);
		}
		return typeList;
	}

	/**
	 * 更新操作
	 * @param typeName
	 * @param userId
	 * @param typeId 
	 * @return
	 */
	public int addOrUpdate(String typeName, Integer userId, String typeId) {
		int row = 0;
		Connection conn = null;
		PreparedStatement sta = null;
		try {
			// 建立连接
			conn = DBUtil.getConnection();
			// 判断typeId是否为空（为空表示添加操作；不为空表示修改操作）
			if(StringUtil.isEmpty(typeId)){ // 添加操作
				// 编写sql
				String sql = "insert into tb_note_type (typeName, userId) values (?,?)";
				// 预编译
				sta = conn.prepareStatement(sql);
				// 设置参数
				sta.setString(1, typeName);
				sta.setInt(2, userId);
			} else { // 修改操作
				// 编写sql
				String sql = "update tb_note_type set typeName = ? where typeId = ?";
				// 预编译
				sta = conn.prepareStatement(sql);
				// 设置参数
				sta.setString(1, typeName);
				sta.setInt(2, Integer.parseInt(typeId));
			}
			// 执行更新
			row = sta.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(null, sta, conn);
		}
		return row;
	}

	/**
	 * 通过类型名和用户ID获取类型对象
	 * @param typeName
	 * @param userId
	 * @return
	 */
	public NoteType findTypeByNameAndUserId(String typeName, Integer userId) {
		NoteType type = null;
		Connection conn = null;
		PreparedStatement sta = null;
		ResultSet res = null;
		try {
			// 建立连接
			conn = DBUtil.getConnection();
			// 编辑sql
			String sql = "select * from tb_note_type where typeName = ? and userId = ?";
			// 预编译
			sta = conn.prepareStatement(sql);
			// 设置参数
			sta.setString(1, typeName);
			sta.setInt(2, userId);
			// 执行查询
			res = sta.executeQuery();
			// 判断是否查询到 分析结果集
			while(res.next()){
				type = new NoteType();
				type.setTypeId(res.getInt("typeId"));
				type.setTypeName(res.getString("typeName"));
				type.setUserId(res.getInt("userId"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(res, sta, conn);
		}
		return type;
	}

	/**
	 * 验证当前用户的类型名唯一性
	 * @param userId
	 * @param typeName
	 * @param typeId
	 * @return
	 */
	public NoteType checkType(Integer userId, String typeName, String typeId) {
		NoteType type = null;
		Connection conn = null;
		PreparedStatement sta = null;
		ResultSet res = null;
		try {
			// 建立连接
			conn = DBUtil.getConnection();
			// 编辑sql
			String sql = "select * from tb_note_type where typeName = ? and userId = ?";
			if(StringUtil.isNotEmpty(typeId)){
				sql += " and typeId != ?";
			}
			// 预编译
			sta = conn.prepareStatement(sql);
			// 设置参数
			sta.setString(1, typeName);
			sta.setInt(2, userId);
			if(StringUtil.isNotEmpty(typeId)){
				sta.setInt(3, Integer.parseInt(typeId));
			}
			// 执行查询
			res = sta.executeQuery();
			// 判断是否查询到 分析结果集
			while(res.next()){
				type = new NoteType();
				type.setTypeId(res.getInt("typeId"));
				type.setTypeName(res.getString("typeName"));
				type.setUserId(res.getInt("userId"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(res, sta, conn);
		}
		return type;
	}

	/**
	 * 通过类型ID查询云记数量
	 * @param typeId
	 * @return
	 */
	public Integer findNoteCountByTypeId(String typeId) {
		Integer count = 0;
		Connection conn = null;
		PreparedStatement sta = null;
		ResultSet res = null;
		try {
			// 得到数据库连接
			conn = DBUtil.getConnection();
			// 编写sql
			String sql = "select count(1) from tb_note where typeId = ?";
			// 预编译
			sta = conn.prepareStatement(sql);
			// 设置参数
			sta.setInt(1, Integer.parseInt(typeId));
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
	 * 通过typeId删除类型
	 * @param typeId
	 * @return
	 */
	public int deleteType(String typeId) {
		int row = 0;
		Connection conn = null;
		PreparedStatement sta = null;
		try {
			// 建立连接
			conn = DBUtil.getConnection();
			// 编写sql
			String sql = "delete from tb_note_type where typeId = ?";
			// 预编译
			sta = conn.prepareStatement(sql);
			// 设置参数
			sta.setInt(1, Integer.parseInt(typeId));
			// 执行更新
			row = sta.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(null, sta, conn);
		}
		return row;
	}
	
}
