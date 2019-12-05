<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div class="col-md-9">
	<div class="data_list">
		<div class="data_list_title"><span class="glyphicon glyphicon-edit"></span>&nbsp;个人中心 </div>
		<div class="container-fluid">
		  <div class="row" style="padding-top: 20px;">
		  	<div class="col-md-8">
		  		<!-- 
		  			文件上传表单
		  				1、提交方式为post
		  				2、设置属性 enctype="multipart/form-data"
		  		 -->
		  		<form id="userForm" class="form-horizontal" method="post" action="user" enctype="multipart/form-data"">
				  <div class="form-group">
				  	<input type="hidden" name="actionName" value="updateInfo">
				    <label for="nickName" class="col-sm-2 control-label">昵称:</label>
				    <div class="col-sm-3">
				      <input class="form-control" name="nick" id="nickName" placeholder="昵称" value="${user.nick }">
				    </div>
				    <label for="img" class="col-sm-2 control-label">头像:</label>
				    <div class="col-sm-5">
				    	<input type="file" id="img" name="img">
				    </div>
				  </div>
				  <div class="form-group">
				    <label for="mood" class="col-sm-2 control-label">心情:</label>
				    <div class="col-sm-10">
				      <textarea class="form-control" name="mood" id="mood" rows="3">${user.mood }</textarea>
				    </div>
				  </div>			 
				  <div class="form-group">
				    <div class="col-sm-offset-2 col-sm-10">
				      <button type="button" id="btn" class="btn btn-success">修改</button>&nbsp;&nbsp;
				      <span style="color:red" id="msg">${resultInfo.msg }</span>
				    </div>
				  </div>
				</form>
		  	</div>
	  		<div class="col-md-4"><img style="width:240px;height:180px" src="user?actionName=userHead&imageName=${user.head }"></div>
		  </div>
		</div>	
	</div>
</div>

<script>
	// 给昵称文本框绑定聚焦失焦事件
	$("#nickName").focus(function(){
		// 清空提示信息
		$("#msg").html("");
		// 按钮可用
		$("#btn").prop("disabled",false);
		
	}).blur(function(){ // 失焦事件
		// 得到文本框的值
		var nickName = $("#nickName").val();
		// 判断是否为空
		if(isEmpty(nickName)){
			$("#msg").html("用户昵称不能为空！");
			// 禁用按钮
			$("#btn").prop("disabled", true);
			return;
		}
		
		// 发送ajax请求
		$.ajax({
			type:"get",
			url:"user",
			data:{
				"actionName":"checkNick",
				"nickName":nickName
			},
			success:function(result){
				// result相当于后台的ResultInfo对象
				// 判断昵称是否可用 code=1可用；code=0不可用
				if(result.code != 1){
					$("#msg").html(result.msg);
					// 禁用按钮
					$("#btn").prop("disabled", true);
				}
			}
		})
	});
	
	// 表单验证
	$("#btn").click(function(){
		// 得到文本框的值
		var nickName = $("#nickName").val();
		// 判断是否为空
		if(isEmpty(nickName)){
			$("#msg").html("用户昵称不能为空！");
			return;
		}
		// 提交表单
		$("#userForm").submit();
	});
</script>