<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="col-md-9">
	<div class="data_list">
		<div class="data_list_title">
			<span class="glyphicon glyphicon-cloud-upload"></span>&nbsp;
			<c:choose>
				<c:when test="${empty noteInfo }">发布云记</c:when>
				<c:otherwise>修改云记</c:otherwise>
			</c:choose>
		</div>
		<div class="container-fluid">
			<div class="container-fluid">
			  <div class="row" style="padding-top: 20px;">
			  <c:if test="${empty noteList }">
			  	<h2>请先添加云记类型！</h2>
			  	<h3><a href="type?actionName=list">添加类型</a></h3>
			  </c:if>
			  <c:if test="${!empty noteList }">
			  	<div class="col-md-12">
			  		<form id="noteForm" class="form-horizontal" method="post" action="note">
			  		   <div class="form-group">
					    <label for="typeId" class="col-sm-2 control-label">类别:</label>
					    <div class="col-sm-8">
					    	<c:choose>
					    		<c:when test="${!empty resultInfo.result.typeId }">
						    		<select id="typeId" class="form-control" name="typeId">
										<option value="">请选择云记类别...</option>
										<c:forEach items="${noteList }" var="item">
											<option <c:if test="${resultInfo.result.typeId == item.typeId }">selected="selected"</c:if> value="${item.typeId }">${item.typeName }</option>
										</c:forEach>
									</select>
								</c:when>
								<c:otherwise>
									<select id="typeId" class="form-control" name="typeId">
										<option value="">请选择云记类别...</option>
										<c:forEach items="${noteList }" var="item">
											<option <c:if test="${noteInfo.typeId == item.typeId }">selected="selected"</c:if> value="${item.typeId }">${item.typeName }</option>
										</c:forEach>
									</select>
								</c:otherwise>
					    	</c:choose>
					    </div>
					  </div>
					  <div class="form-group">
					  	<input type="hidden" name="noteId" value="${noteInfo.noteId }">
					  	<input type="hidden" name="actionName" value="addOrUpdate">
					    <label for="title" class="col-sm-2 control-label">标题:</label>
					    <div class="col-sm-8">
					      <c:choose>
					      	<%-- 当添加或修改云记失败的回显数据 --%>
					      	<c:when test="${!empty resultInfo.result.title }">
					      		<input class="form-control" name="title" id="title" placeholder="云记标题" value="${resultInfo.result.title }">
					      	</c:when>
					      	<%-- 当进入修改云记页面时的填充数据 --%>
					      	<c:otherwise>
					      		<input class="form-control" name="title" id="title" placeholder="云记标题" value="${noteInfo.title }">
					      	</c:otherwise>
					      </c:choose>
					    </div>
					   </div>
					  
					  <div class="form-group">
					  	<label for="content" class="col-sm-2 control-label">内容:</label>
					    <div class="col-sm-8">
					    	<c:choose>
					    		<c:when test="${!empty resultInfo.result.content }">
					    			<textarea id="content" name="content">${resultInfo.result.content }</textarea>
					    		</c:when>
					    		<c:otherwise>
					    			<textarea id="content" name="content">${noteInfo.content }</textarea>
					    		</c:otherwise>
					    	</c:choose>
					    	
					    </div>
					  </div>			 
					  <div class="form-group">
					    <div class="col-sm-offset-5 col-sm-4">
					      <input type="button" class="btn btn-primary" onclick="saveNote();" value="保存">
							<font id="error" color="red">${resultInfo.msg }</font>  
					    </div>
					  </div>
					</form>
			  	</div>
			  </c:if>
			  </div>
			</div>	
		</div>		
	</div>
<script>
	var ue;
	$(function(){
		//UE.getEditor('noteEditor');
		//var editor = new UE.ui.Editor({initialFrameHeight:'100%',initialFrameWidth:'100%'});  
	    //editor.render("noteEditor"); 
	   	ue = UE.getEditor("content");
	});
	
	// 表单验证
	function saveNote(){
		// 获取下拉框的值
		var typeId = $("#typeId").val();
		// 获取标题
		var title = $("#title").val();
		// 获取内容（编辑器取值：1、getContentTxt()获取纯文本内容2、getContent获取html内容）
		var content = ue.getContent;
		
		// 非空判断
		if(isEmpty(typeId)){
			$("#error").html("请选择云记类型！");
			return;
		}
		if(isEmpty(title)){
			$("#error").html("云记标题不能为空！");
			return;
		}
		$("#noteForm").submit();
	}
</script>
</div>		