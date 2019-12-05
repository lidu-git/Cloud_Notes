package com.mage.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.mage.po.Note;
import com.mage.po.vo.NoteVo;
import com.mage.util.DBUtil;
import com.mage.util.StringUtil;

public class NoteDao {

	/**
	 * 添加或修改云记
	 * @param typeId
	 * @param title
	 * @param content
	 * @param noteId 
	 * @return
	 */
	public int addOrUpdate(String typeId, String title, String content, String noteId) {
		int row = 0;
		Connection conn = null;
		PreparedStatement sta = null;
		try {
			// 获取数据库连接
			conn = DBUtil.getConnection();
			// 编写sql
			String sql = "";
			if(StringUtil.isEmpty(noteId)){ // 添加操作
				sql = "insert into tb_note (title,content,typeId,pubTime) values (?,?,?,now())";
			} else {
				sql = "update tb_note set title = ?,content = ?,typeId = ?,pubTime = now() where noteId = ?";
			}
			// 预编译
			sta = conn.prepareStatement(sql);
			// 设置参数
			sta.setString(1, title);
			sta.setString(2, content);
			sta.setInt(3, Integer.parseInt(typeId));
			if(StringUtil.isNotEmpty(noteId)){
				sta.setInt(4, Integer.parseInt(noteId));
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
	 * 查询云记总数量
	 * @param userId
	 * @param typeId 
	 * @param date 
	 * @param title 
	 * @return
	 */
	public Integer findNoteCount(Integer userId, String title, String date, String typeId) {
		Integer count = 0;
		Connection conn = null;
		PreparedStatement sta = null;
		ResultSet res = null;
		try {
			// 获取数据库连接
			conn = DBUtil.getConnection();
			// 编写sql
			String sql = "select count(1) from tb_note n left join tb_note_type t on n.typeId = t.typeId where userId = ? ";
			if(StringUtil.isNotEmpty(title)){
				sql += "and title like ? ";
			}
			if(StringUtil.isNotEmpty(date)){
				sql += "and date_format(pubTime,'%Y年%m月') = ? ";
			}
			if(StringUtil.isNotEmpty(typeId)){
				sql += "and n.typeId = ? ";
			}
			// 预编译
			sta = conn.prepareStatement(sql);
			// 设置参数
			sta.setInt(1, userId);
			if(StringUtil.isNotEmpty(title)){
				sta.setString(2, "%"+title+"%");
			}
			if(StringUtil.isNotEmpty(date)){
				sta.setString(2, date);
			}
			if(StringUtil.isNotEmpty(typeId)){
				sta.setInt(2, Integer.parseInt(typeId));
			}
			// 执行查询
			res = sta.executeQuery();
			// 分析结果集
			while(res.next()){
				count = res.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(res, sta, conn);
		}
		return count;
	}

	/**
	 * 查询云记集合
	 * @param userId
	 * @param index
	 * @param pageSize 
	 * @param typeId 
	 * @param date 
	 * @param title 
	 * @return
	 */
	public List<Note> findNoteListByPage(Integer userId, Integer index, Integer pageSize, String title, String date, String typeId) {
		List<Note> list = new ArrayList<>();
		Connection conn = null;
		PreparedStatement sta = null;
		ResultSet res = null;
		try {
			// 获取数据库连接
			conn = DBUtil.getConnection();
			// 编写sql
			String sql = "select noteId, title, content, pubTime, n.typeId "
					+ "from tb_note n left join tb_note_type t "
					+ "on n.typeid = t.typeId "
					+ "where userId = ? ";
			if(StringUtil.isNotEmpty(title)){
				sql += "and title like ? ";
			}
			if(StringUtil.isNotEmpty(date)){
				sql += "and date_format(pubTime,'%Y年%m月') = ? ";
			}
			if(StringUtil.isNotEmpty(typeId)){
				sql += "and n.typeId = ? ";
			}
			sql += "order by pubTime desc limit ?,?";
			// 预编译
			sta = conn.prepareStatement(sql);
			// 设置参数
			sta.setInt(1, userId);
			if(StringUtil.isNotEmpty(title)){
				sta.setString(2, "%"+title+"%");
				sta.setInt(3, index);
				sta.setInt(4, pageSize);
			} else if(StringUtil.isNotEmpty(date)){
				sta.setString(2, date);
				sta.setInt(3, index);
				sta.setInt(4, pageSize);
			} else if(StringUtil.isNotEmpty(typeId)){
				sta.setInt(2, Integer.parseInt(typeId));
				sta.setInt(3, index);
				sta.setInt(4, pageSize);
			} else {
				sta.setInt(2, index);
				sta.setInt(3, pageSize);
			}
			// 执行查询
			res = sta.executeQuery();
			// 分析结果集
			while(res.next()){
				Note note = new Note();
				note.setContent(res.getString("content"));
				note.setNoteId(res.getInt("noteId"));
				note.setPubTime(res.getTimestamp("pubTime"));
				note.setTitle(res.getString("title"));
				note.setTypeId(res.getInt("typeId"));
				list.add(note);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(res, sta, conn);
		}
		return list;
	}

	/**
	 * 查询云记详情
	 * @param noteId
	 * @return
	 */
	public Note findNoteById(String noteId) {
		Note note = new Note();
		Connection conn = null;
		PreparedStatement sta = null;
		ResultSet res = null;
		try {
			// 获取数据库连接
			conn = DBUtil.getConnection();
			// 编写sql
			String sql = "select title, content, pubTime, noteId, n.typeId, typeName from tb_note n left join tb_note_type t on n.typeId = t.typeId where n.noteId = ?;";
			// 预编译
			sta = conn.prepareStatement(sql);
			// 设置参数
			sta.setInt(1, Integer.parseInt(noteId));
			// 执行查询
			res = sta.executeQuery();
			// 分析结果集
			while(res.next()){
				note.setContent(res.getString("content"));
				note.setNoteId(res.getInt("noteId"));
				note.setPubTime(res.getTimestamp("pubTime"));
				note.setTitle(res.getString("title"));
				note.setTypeId(res.getInt("typeId"));
				note.setTypeName(res.getString("typeName"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(res, sta, conn);
		}
		return note;
	}

	/**
	 * 删除云记
	 * @param noteId
	 * @return
	 */
	public int deleteNote(String noteId) {
		int row = 0;
		Connection conn = null;
		PreparedStatement sta = null;
		try {
			// 获取数据库连接
			conn = DBUtil.getConnection();
			// 编写sql
			String sql = "delete from tb_note where noteId = ?";
			// 预编译
			sta = conn.prepareStatement(sql);
			// 设置参数
			sta.setInt(1, Integer.parseInt(noteId));
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
	 * 通过日期分组查询当前登录用户对应的云记数量
	 * @param userId
	 * @return
	 */
	public List<NoteVo> findNoteGroupByDate(Integer userId) {
		List<NoteVo> list = new ArrayList<>();
		Connection conn = null;
		PreparedStatement sta = null;
		ResultSet res = null;
		try {
			// 获取数据库连接
			conn = DBUtil.getConnection();
			// 编写sql
			String sql = "select date_format(pubTime,'%Y年%m月') as noteGroupName, count(noteId) as noteCount "
					+ "from tb_note n left join tb_note_type t on n.typeId = t.typeId where userId = ? "
					+ "group by noteGroupName order by noteGroupName desc";
			// 预编译
			sta = conn.prepareStatement(sql);
			// 设置参数
			sta.setInt(1, userId);
			// 执行查询
			res = sta.executeQuery();
			// 分析结果集
			while(res.next()){
				NoteVo noteVo = new NoteVo();
				noteVo.setNoteGroupName(res.getString("noteGroupName"));
				noteVo.setNoteCount(res.getInt("noteCount"));
				list.add(noteVo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(null, sta, conn);
		}
		return list;
	}

	/**
	 * 通过类型分组查询当前登录用户对应的云记数量
	 * @param userId
	 * @return
	 */
	public List<NoteVo> findNoteGroupByType(Integer userId) {
		List<NoteVo> list = new ArrayList<>();
		Connection conn = null;
		PreparedStatement sta = null;
		ResultSet res = null;
		try {
			// 获取数据库连接
			conn = DBUtil.getConnection();
			// 编写sql
			String sql = "select t.typeId, typeName as noteGroupName, count(noteId) as noteCount "
					+ "from tb_note_type t LEFT JOIN tb_note n "
					+ "on t.typeId = n.typeId where userId = ? group by t.typeId order by t.typeId desc";
			// 预编译
			sta = conn.prepareStatement(sql);
			// 设置参数
			sta.setInt(1, userId);
			// 执行查询
			res = sta.executeQuery();
			// 分析结果集
			while(res.next()){
				NoteVo noteVo = new NoteVo();
				noteVo.setNoteGroupName(res.getString("noteGroupName"));
				noteVo.setNoteCount(res.getInt("noteCount"));
				noteVo.setTypeId(res.getInt("typeId"));
				list.add(noteVo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(null, sta, conn);
		}
		return list;
	}

}
