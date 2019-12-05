package com.mage.service;

import java.util.List;

import com.mage.dao.NoteDao;
import com.mage.po.Note;
import com.mage.po.vo.NoteVo;
import com.mage.po.vo.ResultInfo;
import com.mage.util.Page;
import com.mage.util.StringUtil;

public class NoteService {

	private NoteDao noteDao = new NoteDao();
	
	/**
	 * 添加或修改云记
	 * 	1、非空判断
		2、调用Dao层的更新方法，返回受影响的行数
		3、判断是否更新成功
		4、返回resultInfo对象
	 * @param typeId
	 * @param title
	 * @param content
	 * @param noteId 
	 * @return
	 */
	public ResultInfo<Note> addOrUpdate(String typeId, String title, String content, String noteId) {
		ResultInfo<Note> resultInfo = new ResultInfo<>();
		// 1、非空判断
		if(StringUtil.isEmpty(typeId) || StringUtil.isEmpty(title)){
			resultInfo.setCode(0);
			resultInfo.setMsg("更新失败！");
			return resultInfo;
		}
		
		// 2、调用Dao层的更新方法，返回受影响的行数
		int row = noteDao.addOrUpdate(typeId,title,content,noteId);
		// 判断是否更新成功
		if(row > 0){
			resultInfo.setCode(1);
			resultInfo.setMsg("更新成功！");
			return resultInfo;
		} else {
			resultInfo.setCode(0);
			resultInfo.setMsg("更新失败！");
			Note note = new Note();
			note.setContent(content);
			note.setTitle(title);
			note.setTypeId(Integer.parseInt(typeId));
			resultInfo.setResult(note);
		}
		return resultInfo;
	}

	/**
	 * 分页查询云记列表
	 *  1、判断分页参数，如果为空设置默认值（pageNum当前页、pageSize每页显示的数量）
		2、调用Dao层，通过当前登录用户的ID查询云记总数量（totalCount）
		3、得到Page对象（带参构造pageNum、pageSize、totalCount）
		4、调用Dao层，通过当前登录用户的ID查询当前页指定数量的云记集合（关键字：limit  limit需要两个参数，参数一：开始查询的下标数（当前页-1）*每页显示的数量；参数二：每页显示的数量）
		5、将查询到的云记集合设置到page对象中
		6、将page对象设置到resultInfo对象中
		7、返回resultInfo对象
	 * @param userId
	 * @param typeId 
	 * @param date 
	 * @param title 
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public ResultInfo<Page<Note>> findNoteListByPage(Integer userId, String pageNumStr, String pageSizeStr, String title, String date, String typeId) {
		ResultInfo<Page<Note>> resultInfo = new ResultInfo<>();
		
		// 1、判断分页参数，如果为空设置默认值（pageNum当前页、pageSize每页显示的数量）
		Integer pageNum = 1;
		Integer pageSize = 2;
		if(StringUtil.isNotEmpty(pageNumStr)){
			pageNum = Integer.parseInt(pageNumStr);
		}
		if(StringUtil.isNotEmpty(pageSizeStr)){
			pageSize = Integer.parseInt(pageSizeStr);
		}
		
		// 2、调用Dao层，通过当前登录用户的ID查询云记总数量（totalCount）
		Integer totalCount = noteDao.findNoteCount(userId,title,date,typeId);
		
		if(totalCount == 0){
			resultInfo.setCode(0);
			resultInfo.setMsg("暂未查询到云记记录！");
			return resultInfo;
		}
		
		// 3、得到Page对象（带参构造pageNum、pageSize、totalCount）
		Page<Note> page = new Page<>(pageNum, pageSize, totalCount);
		
		// 4、调用Dao层，通过当前登录用户的ID查询当前页指定数量的云记集合（关键字：limit  limit需要两个参数，参数一：开始查询的下标数（当前页-1）*每页显示的数量；参数二：每页显示的数量）
		Integer index = (pageNum-1)*pageSize;
		List<Note> noteList = noteDao.findNoteListByPage(userId,index,pageSize,title,date,typeId);
		
		// 5、将查询到的云记集合设置到page对象中
		page.setDatas(noteList);
		
		// 6、将page对象设置到resultInfo对象中
		resultInfo.setResult(page);
		resultInfo.setCode(1);
		return resultInfo;
	}

	/**
	 * 查看云记详情
	 * 	1、判断参数是否为空
		2、调用Dao层，通过云记ID查询云记对象
		3、返回云记对象
	 * @param noteId
	 * @return
	 */
	public Note findNoteById(String noteId) {
		// 1、判断参数是否为空
		if(StringUtil.isEmpty(noteId)){
			return null;
		}
		// 2、调用Dao层，通过云记ID查询云记对象
		Note note = noteDao.findNoteById(noteId);
		// 3、返回云记对象
		return note;
	}

	/**
	 * 删除云记
	 *  1、判断参数
		2、调用Dao层，删除记录，返回受影响的行数
		3、判断受影响的行数
		4、返回code
	 * @param noteId
	 * @return
	 */
	public Integer deleteNote(String noteId) {
		// 1、判断参数
		if(StringUtil.isEmpty(noteId)){
			return null;
		}
		// 2、调用Dao层，删除记录，返回受影响的行数
		int row = noteDao.deleteNote(noteId);
		// 3、判断受影响的行数
		if(row>0){
			return 1;
		}
		// 4、返回code
		return 0;
	}

	/**
	 * 通过日期分组查询当前登录用户对应的云记数量
	 * @param userId
	 * @return
	 */
	public List<NoteVo> findNoteGroupByDate(Integer userId) {
		List<NoteVo> list = noteDao.findNoteGroupByDate(userId);
		return list;
	}

	/**
	 * 通过类型分组查询当前登录用户对应的云记数量
	 * @param userId
	 * @return
	 */
	public List<NoteVo> findNoteGroupByType(Integer userId) {
		List<NoteVo> list = noteDao.findNoteGroupByType(userId);
		return list;
	}
}
