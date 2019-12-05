package com.mage.po.vo;

public class NoteVo {

	private String noteGroupName; // 分组名称
	private Integer noteCount; // 分组数量
	
	private Integer typeId; // 类型ID
	
	public String getNoteGroupName() {
		return noteGroupName;
	}
	public void setNoteGroupName(String noteGroupName) {
		this.noteGroupName = noteGroupName;
	}
	public Integer getNoteCount() {
		return noteCount;
	}
	public void setNoteCount(Integer noteCount) {
		this.noteCount = noteCount;
	}
	public Integer getTypeId() {
		return typeId;
	}
	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}
}