package com.mage.po;

import java.util.Date;

/**
 * 云记表
 * @author Cushier
 *
 */
public class Note {

	private Integer noteId; // 云记ID,主键
	private String title; // 云记标题
	private String content; // 云记内容
	private Date pubTime; // 发布时间
	private Integer typeId; // 类型ID 外键，关联tb_note表的主键
	
	private String typeName; // 类型名称
	
	public Integer getNoteId() {
		return noteId;
	}
	public void setNoteId(Integer noteId) {
		this.noteId = noteId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getPubTime() {
		return pubTime;
	}
	public void setPubTime(Date pubTime) {
		this.pubTime = pubTime;
	}
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
	
}
