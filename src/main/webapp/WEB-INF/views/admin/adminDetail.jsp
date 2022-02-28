<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/include-taglib.jspf" %>

<script type="text/javascript">
$(function() {
	$("#btn-list").on("click", function(e) {
		e.preventDefault();
		goAdminList();
	});

	$("#btn-modify").on("click", function(e) {
		e.preventDefault();
		goAdminModify();
	});

	$("#btn-delete").on("click", function(e) {
		e.preventDefault();
		Confirm("<spring:message code='ui.common.label.delete'/>",
				"${info.adminId}</br></br>" + "<spring:message code='ui.common.alert.action.delete'/>", // 삭제 하시겠습니까?
				true,deleteAdmin);
	});

});

function goAdminList() {
	var comSubmit = new commonSubmit();
	comSubmit.setUrl("<c:url value='/admin/adminList'/>");
	comSubmit.submit();
}

function goAdminModify() {
	var adminId = '${info.adminId}';
	var comSubmit = new commonSubmit();
	comSubmit.setUrl("<c:url value='/admin/adminModify'/>");
	comSubmit.addParam("adminId", adminId);
	comSubmit.submit();
}

function deleteAdmin() {
	var form = {
		adminId: "${info.adminId}"
	}
	$.ajax({
		url: "<c:url value='/admin/ajaxAdminDelete.json'/>",
		type: "POST",
		data: form,
		dataType: "json",
		success: function(data) {
			goAdminList();
		}
	});
}
</script>

<!-- 컨텐츠 영역 -->
<div class="row">
	<table class="table table-bordered table-info">
		<colgroup>
			<col class="col-xs-2" />
			<col class="col-xs-4" />
			<col class="col-xs-2" />
			<col class="col-xs-4" />
		</colgroup>
		<tbody>
			<tr>
				<th><spring:message code='ui.common.label.id'/></th><!-- ID -->
				<td colspan="3">${info.adminId}</td>
			</tr>
			<tr>
				<th><spring:message code='ui.common.label.name'/></th><!-- 이름 -->
				<td>${info.adminName}</td>
				<th><spring:message code='ui.admingroup.label.adminGroup'/></th><!-- 관리자 그룹 -->
				<td>${info.adminGroupName}</td>
			</tr>
			<tr>
				<th><spring:message code='ui.admin.label.phoneNumber'/></th><!-- 전화 번호 -->
				<td>${info.phoneNumber}</td>
				<th><spring:message code='ui.admin.label.email'/></th><!-- 이메일 -->
				<td>${info.email}</td>
			</tr>
			<tr>
				<th><spring:message code='ui.common.label.useYn'/></th><!-- 사용 여부 -->
				<td colspan="3">${info.useYn}</td>
			</tr>
			<tr>
				<th><spring:message code='ui.common.label.createId'/></th><!-- 등록자 ID -->
				<td>${info.createId}</td>
				<th><spring:message code='ui.common.label.createTime'/></th><!-- 등록 시간 -->
				<td>${info.createTime}</td>
			</tr>
			<tr>
				<th><spring:message code='ui.common.label.updateId'/></th><!-- 수정자 ID -->
				<td>${info.updateId}</td>
				<th><spring:message code='ui.common.label.updateTime'/></th><!-- 수정 시간 -->
				<td>${info.updateTime}</td>
			</tr>
		</tbody>
	</table>
</div>

<!-- 버튼 영역 -->
<div class="row btn-group-area">
	<div class="col-xs-6 text-left">
		<a href="#" class="btn btn-default" role="button" id="btn-list"><spring:message code='ui.common.button.list'/></a>
	</div>
	<div class="col-xs-6 text-right">
		<a href="#" class="btn btn-secondary" role="button" id="btn-delete"><spring:message code='ui.common.button.delete'/></a>
		<a href="#" class="btn btn-primary" role="button" id="btn-modify"><spring:message code='ui.common.button.modify'/></a>
	</div>
</div>
