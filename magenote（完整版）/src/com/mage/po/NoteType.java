package com.mage.po;

/**
 * 云记类型表
 * @author Cushier
 *
 */
public class NoteType {

	private Integer typeId; // 类型ID
	private String typeName; // 类型名称
	private Integer userId; // 外键，关联tb_user表的主键
	
	public Integer getTypeId() {
		return typeId;
	}
	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
}
