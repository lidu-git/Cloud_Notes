package com.mage.po.vo;

/**
 * 响应的结果封装对象
 * 	code：响应状态码 1=成功，0=失败
 * 	msg：提示信息
 * 	result：返回的对象（user对象、type对象、note对象、甚至集合）
 * @author Cushier
 * @param <T>
 *
 */
public class ResultInfo<T> {
	
	private Integer code; // 响应状态吗 1=成功，0=失败
	private String msg; // 提示信息
	private T result; // 返回的对象（user对象、type对象、note对象、甚至集合）
	
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public T getResult() {
		return result;
	}
	public void setResult(T result) {
		this.result = result;
	}
	
}
