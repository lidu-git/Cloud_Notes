package com.mage.service;

import java.util.List;

import com.mage.dao.TypeDao;
import com.mage.po.NoteType;
import com.mage.po.vo.ResultInfo;
import com.mage.util.StringUtil;

public class TypeService {

	private TypeDao typeDao = new TypeDao();

	/**
	 * 查询类型列表
	    1、调用Dao层的查询方法，通过用户ID查询当前登录用户下的所有类型列表
		2、返回集合
	 * @param userId
	 * @return
	 */
	public List<NoteType> findTypeList(Integer userId) {
		List<NoteType> typeList = typeDao.findTypeListByUserId(userId);
		return typeList;
	}

	/**
	 * 添加或修改类型
	 * 	1、判断参数是否为空
		2、调用Dao层的更新方法，返回受影响的行数
		3、如果受影响的行数大于0，调用Dao层，通过类型名和用户ID获取类型对象
		4、返回resultInfo对象
	 * @param typeName
	 * @param userId
	 * @param typeId 
	 * @return
	 */
	public ResultInfo<NoteType> addOrUpdate(String typeName, Integer userId, String typeId) {
		ResultInfo<NoteType> resultInfo = new ResultInfo<>();
		
		// 1、判断参数是否为空
		if(StringUtil.isEmpty(typeName)){
			resultInfo.setCode(0);
			resultInfo.setMsg("更新失败！");
			return resultInfo;
		}
		
		// 验证当前用户的类型名的唯一性
		Integer code = checkType(userId, typeName, typeId);
		if(code == 0){
			resultInfo.setCode(0);
			resultInfo.setMsg("类型名称不可用");
			return resultInfo;
		}
		
		// 2、调用Dao层的更新方法，返回受影响的行数
		int row = typeDao.addOrUpdate(typeName,userId,typeId);
		
		// 3、如果受影响的行数大于0，调用Dao层，通过类型名和用户ID获取类型对象
		if(row > 0){
			// 调用Dao层，通过类型名和用户ID获取类型对象
			NoteType type = typeDao.findTypeByNameAndUserId(typeName,userId);
			resultInfo.setCode(1);
			resultInfo.setMsg("更新成功！");
			resultInfo.setResult(type);
		} else {
			resultInfo.setCode(0);
			resultInfo.setMsg("更新失败！");
		}
		return resultInfo;
	}

	/**
	 * 验证当前用户的类型名唯一性
	 * 	1、判断参数是否为空（类型名称）
		2、调用Dao层，通过用户ID、类型名称、（类型ID）查询用户对象
		3、判断用户对象是否为空
			如果为空，返回1；不为空则返回0
	 * @param userId
	 * @param typeName
	 * @param typeId
	 * @return
	 */
	public Integer checkType(Integer userId, String typeName, String typeId) {
		// 1、判断参数是否为空（类型名称）
		if(StringUtil.isEmpty(typeName)){
			return 0;
		}
		// 2、调用Dao层，通过用户ID、类型名称、（类型ID）查询用户对象
		NoteType type = typeDao.checkType(userId, typeName, typeId);
		// 3、判断用户对象是否为空
		if(type != null){
			return 0;
		}
		return 1;
	}

	/**
	 * 删除类型
	 *  1、判断参数是否为空
		2、调用Dao层，通过typeId查询对应的云记数量
		3、如果云记数量大于0，不可删除
		4、调用Dao层，删除类型
		5、返回resultInfo对象
	 * @param typeId
	 * @return
	 */
	public ResultInfo<NoteType> deleteType(String typeId) {
		ResultInfo<NoteType> resultInfo = new ResultInfo<>();
		// 1、判断参数是否为空
		if(StringUtil.isEmpty(typeId)){
			resultInfo.setCode(0);
			resultInfo.setMsg("删除失败！");
			return resultInfo;
		}
		// 2、调用Dao层，通过typeId查询对应的云记数量
		Integer count = typeDao.findNoteCountByTypeId(typeId);

		// 3、如果云记数量大于0，不可删除
		if(count > 0){
			resultInfo.setCode(0);
			resultInfo.setMsg("该类型存在云记记录，不可删除！");
			return resultInfo;
		}
		
		// 4、调用Dao层，删除类型
		int row = typeDao.deleteType(typeId);
		
		if(row<1){
			resultInfo.setCode(0);
			resultInfo.setMsg("删除失败！");
		} else {
			resultInfo.setCode(1);
			resultInfo.setMsg("删除成功！");
		}
		return resultInfo;
	}
	
}
