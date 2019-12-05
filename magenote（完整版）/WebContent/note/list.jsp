<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<div class="col-md-9">
	<div class="data_list">
		<div class="data_list_title">
			<span class="glyphicon glyphicon glyphicon-th-list"></span>&nbsp;云记列表
		</div>
		<c:if test="${resultInfo.code !=1 }">
			<h2>${resultInfo.msg }</h2>
		</c:if>
		<c:if test="${resultInfo.code ==1 }">
			<div class="note_datas">
				<ul>
					<c:forEach items="${resultInfo.result.datas }" var="item">
						<fmt:formatDate value="${item.pubTime }" pattern="yyyy-MM-dd" var="noteDate"/>
						<li>『 ${noteDate }』&nbsp;&nbsp;<a href="note?actionName=detail&noteId=${item.noteId }">${item.title }</a> </li>
					</c:forEach>
				</ul>
			</div>
				<nav style="text-align: center">
					<ul class="pagination   center">
						<c:if test="${resultInfo.result.pageNum != 1 }">
							<li><a href="index?pageNum=${resultInfo.result.prePage }<c:if test="${!empty actionName }">&actionName=${actionName }</c:if><c:if test="${!empty title }">&title=${title}</c:if><c:if test="${!empty date }">&date=${date}</c:if><c:if test="${!empty typeId }">&typeId=${typeId}</c:if>"><span>«</span></a></li>
						</c:if>
						<c:forEach begin="${resultInfo.result.startNavPage }" end="${resultInfo.result.endNavPage }" var="p">
							<li <c:if test="${resultInfo.result.pageNum == p }">class="active"</c:if>><a href="index?pageNum=${p }<c:if test="${!empty actionName }">&actionName=${actionName }</c:if><c:if test="${!empty title }">&title=${title}</c:if><c:if test="${!empty date }">&date=${date}</c:if><c:if test="${!empty typeId }">&typeId=${typeId}</c:if>">${p }</a></li>
						</c:forEach>
						<c:if test="${resultInfo.result.pageNum != resultInfo.result.totalPages }">
							<li><a href="index?pageNum=${resultInfo.result.nextPage }<c:if test="${!empty actionName }">&actionName=${actionName }</c:if><c:if test="${!empty title }">&title=${title}</c:if><c:if test="${!empty date }">&date=${date}</c:if><c:if test="${!empty typeId }">&typeId=${typeId}</c:if>"><span>»</span></a></li>
						</c:if>
					</ul>
				</nav>
		</c:if>
	</div>
</div>	