<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="col-md-9">
	<div class="data_list">
		<div class="data_list_title">
			<span class="glyphicon glyphicon-list"></span>&nbsp;类型列表
			<span class="noteType_add">
				<button class="btn btn-sm btn-success" type="button" id="addBtn">添加类别</button>
			</span>
			
		 </div>
		<div>
			<c:if test="${empty typeList }">
				<h2 id="h2">暂未查询到类型记录！</h2>
			</c:if>
			<c:if test="${!empty typeList }">
				<table id="myTable" class="table table-hover table-striped ">
			 		<tbody>
				 		<tr>
				 			<th>编号</th>
				 			<th>类型</th>
				 			<th>操作</th>
				 		</tr>
				 		<c:forEach items="${typeList }" var="item">
				 			<tr id="tr_${item.typeId }">
				 				<td>${item.typeId }</td>
				 				<td>${item.typeName }</td>
				 				<td>
				 				<button class="btn btn-primary" type="button" onclick="openUpdateDialog(this,${item.typeId },'${item.typeName }');">修改</button>&nbsp;
				 				<button class="btn btn-danger del" type="button" onclick="deleteType(${item.typeId})">删除</button>
				 				</td>
				 			</tr>
				 		</c:forEach>
				 	</tbody>
			 	</table>
			</c:if>
	
		</div>	
	</div>
</div>
<!-- 模态框（Modal） -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel"></h4>
            </div>
            <div class="modal-body">
            	<div class="container">
            		<div class="row">
            			<input type="hidden" id="typeId" value="" />
			            <div class="col-md-1" style="line-height:30px">
						    <label for="typeName">类型名称</label>
						</div>
						<div class="col-md-4">
						      <input type="text" class="form-control" id="typeName" placeholder="请输入类别名称">
						</div>
            		</div>
            	</div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="saveBtn">提交更改</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>
<script type="text/javascript" src="statics/js/noteType.js"></script>