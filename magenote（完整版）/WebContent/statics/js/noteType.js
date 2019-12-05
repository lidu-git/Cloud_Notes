/**
 * 打开添加类型模态框
 */
$("#addBtn").click(function(){
	// 设置模态框的标题
	$("#myModalLabel").html("<span class='glyphicon glyphicon-plus'></span> 添加类型");
	// 清空隐藏域的值
	$("#typeId").val("");
	// 清空表单数据
	$("#typeName").val("");
	// 打开模态框
	$("#myModal").modal("show");
});

/**
 * 添加操作/修改操作
 */
$("#saveBtn").click(function(){
	// 获取类型名称的值
	var typeName = $("#typeName").val();
	// 非空判断
	if(isEmpty(typeName)){
		swal("类别名称不能为空！","","warning");
		return;
	}
	
	// 获取typeId隐藏域的值（如果typeId不为空，说明是修改操作；如果为空，则为添加操作）
	var typeId = $("#typeId").val();
	
	// 当类型名称不为空时，发送ajax请求后台
	$.ajax({
		type:"post",
		url:"type",
		data:{
			"actionName":"addOrUpdate",
			"typeName":typeName,
			"typeId":typeId
		},
		success:function(resultInfo){
			// resultInfo ——> Servlet中的resultInfo对象（code、msg、result）
			// 判断是否更新成功
			/*resultInfo = {
				code:1,
				msg:"更新成功",
				result:{
					typeId:1,
					typeName:"php"
				}
			}*/
			if(resultInfo.code == 1){ // 成功
				// 关闭模态框
				$("#myModal").modal("hide");
				// 提示用户成功
				swal(resultInfo.msg,"","success")
				// 判断是添加操作还是修改操作（typeId是否为空）
				if(isEmpty(typeId)){
					// 添加类型的DOM操作
					addDom(resultInfo.result);
				} else {
					// 修改类型的DOM操作
					updateDom(resultInfo.result);
				}
			} else{
				// 失败，提示用户
				swal(resultInfo.msg,"","error");
			}
		}
	});
});

/**
 * 添加类型的DOM操作
 * @param type
 */
function addDom(type){
	var tr = "<tr id='tr_"+type.typeId+"'><td>"+type.typeId+"</td><td>"+type.typeName+"</td>" +
			"<td><button class='btn btn-primary' type='button' onclick='openUpdateDialog(this,"+type.typeId +",\""+type.typeName+"\");'>修改</button>&nbsp;" +
			"<button class='btn btn-danger del' type='button' onclick='deleteType("+type.typeId+");'>删除</button></td></tr>";
	// 得到table对象
	var myTable = $("#myTable");
	// 判断table是否存在
	console.log(myTable);
	if(myTable.length > 0){ // table存在
		// 得到table的子元素tbody
		var tbody = myTable.children();
		// 将tr追加到tbody中
		tbody.append(tr);
	} else { // table不存在
		
		// 拼接table的html
		var table = "<table id='myTable' class='table table-hover table-striped '>"+
				"<tbody><tr><th>编号</th><th>类型</th><th>操作</th></tr>"+tr+"</tbody></table>";
		// 通过h2标签得到父元素div
		var div = $("#h2").parent();
		// 移除h2标签
		$("#h2").remove();
		// 将表格追加到div中
		div.append(table);
	}
	
	// 添加左侧云记类型分组的列表项
	// 定义要添加的li
	var li = '<li id="li_'+type.typeId+'"><a href=""><span id="sp_'+type.typeId+'">'+type.typeName+'</span> <span class="badge">0</span></a></li>';
	// 将li前追加到类型的ul列表中	
	$("#typeUl").prepend(li);
}


/**
 * 打开修改类型模态框
 * @param typeId
 * @param typeName
 */
function openUpdateDialog(_this,typeId,typeName){
	// 设置模态框的标题
	$("#myModalLabel").html("<span class='glyphicon glyphicon-pencil'></span> 修改类型");
	// 设置隐藏域的值
	$("#typeId").val(typeId);
	// 设置文本框的值
	// typeName = $(_this).parent().parent().children().eq(1).text();
	typeName = $("#tr_"+typeId).children().eq(1).text();
	$("#typeName").val(typeName);
	// 打开模态框
	$("#myModal").modal("show");
}

/**
 * 修改类型的DOM操作
 * @param type
 */
function updateDom(type){
	// 得到要修改的tr对象
	var tr = $("#tr_" + type.typeId);
	// 得到tr的所有td子元素
	var tds = tr.children();
	// 得到当前tr对象的第二个td子元素
	var td = tds.eq(1);
	// 修改当前td的值
	td.text(type.typeName);
	
	// 修改左侧类型菜单的指定li的值
	$("#sp_" + type.typeId).html(type.typeName);
}

/**
 * 验证类型名的唯一性
 */
$("#typeName").blur(function(){
	// 获取类型名
	var typeName = $("#typeName").val();
	// 判断非空
	if(!isEmpty(typeName)){
		// 不为空，验证类型名是否可用
		$.ajax({
			type:"get",
			url:"type",
			data:{
				"actionName":"checkType",
				"typeName":typeName,
				"typeId":$("#typeId").val()
			},
			success:function(code){
				// 1=可用；0=不可用
				if(code != 1){ // 不可用
					// 禁用按钮
					$("#saveBtn").prop("disabled",true);
					// 提示用户
					swal("类型名称不可用","","warning");
				}
			}
		});
	}
}).focus(function(){
	// 按钮可用
	$("#saveBtn").prop("disabled",false);
});

/**
 * 删除类型
 * @param typeId
 */
function deleteType(typeId){
	// 询问用户是否确认删除
	swal({ 
		  title: "你确定要删除这条记录吗？", 
		  text: "", 
		  type: "warning",
		  showCancelButton: true, 
		  confirmButtonColor: "orange",
		  confirmButtonText: "确定",
		  cancelButtonText: "取消"
		}).then(function(){
			// 发送ajax请求，删除用户
			$.ajax({
				type:"post",
				url:"type",
				data:{
					"actionName":"deleteType",
					"typeId":typeId
				},
				success:function(resultInfo){
					if(resultInfo.code == 1){ // 删除成功
						swal(resultInfo.msg,"","success");
						// 删除的DOM操作
						deleteDom(typeId);
					} else { // 删除失败
						swal(resultInfo.msg,"","error");
					}
				}
			});
	    });
}

/**
 * 删除类型的DOM操作
 * @param typeId
 */
function deleteDom(typeId){
	// 判断表格中是否只有一条数据（tr的数量为2）
	// 得到表格对象
	var table = $("#myTable");
	// 得到tr的数量（table的子元素是tbody）
	var trs = table.children().children();
	
	if(trs.length == 2){ // 只有一条数据，删除整个表格
		// 得到table的父元素div
		var div = table.parent();
		var h2 = '<h2 id="h2">暂未查询到类型记录！</h2>';
		// 将h2追加到div中
		div.append(h2);
		// 移除表格
		table.remove();
	} else {
		// 删除指定tr对象
		$("#tr_" + typeId).remove();
	}
	
	// 删除左侧云记类别分组对应的li元素
	$("#li_" + typeId).remove();
}