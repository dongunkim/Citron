<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/include-taglib.jspf" %>

<script type="text/javascript">
$(function() {
	// 영문 입력 방지 (한글은 안됨)
	$("#phoneNumber2").on('keypress', function(event){		
		if((event.keyCode<48)||(event.keyCode>57)) event.preventDefault();		
	});
	
	// 영문 입력 방지 (한글은 안됨)
	$("#phoneNumber3").on('keypress', function(event){
		if((event.keyCode<48)||(event.keyCode>57)) event.preventDefault();
	});	

	$("#btn-list").on("click", function(e) {
		e.preventDefault();
		goAdminList();
	});

	$("#btn-cancel").on("click", function(e) {
		e.preventDefault();
		goAdminDetail();
	});

	$("#btn-modify").on("click", function(e) {
		e.preventDefault();
		if (validatorObj.customValidate()) goAdminModify();
	});

	// Validation Rule 설정
	var validatorObj = $("#frm").validate({
		rules: {
			adminName: {required: true,minlength: 2},
			adminGroupId: {required: true},
			useYn: {required: true},
			phoneNumber2: {minlength: 3,digits: true},
			phoneNumber3: {minlength: 4,digits: true},
		    email: {email: true}
		}
	});

});

function goAdminList() {
	var comSubmit = new commonSubmit();
	comSubmit.setUrl("<c:url value='/admin/adminList'/>");
	comSubmit.submit();
}

function goAdminDetail() {
	var comSubmit = new commonSubmit();
	comSubmit.setUrl("<c:url value='/admin/adminDetail'/>");
	comSubmit.addParam("adminId", "${info.adminId}");
	comSubmit.submit();
}

function goAdminModify() {
	Confirm("<spring:message code='ui.common.label.modify'/>", // 수정
			"${info.adminId}</br></br>" + "<spring:message code='ui.common.alert.action.modify'/>", // 수정 하시겠습니까?
			true,adminModify);
}

function adminModify() {
	$("#phoneNumber").val($("#phoneNumber1").val()+"-"+$("#phoneNumber2").val()+"-"+$("#phoneNumber3").val());
	var form = $("#frm").serialize();
	$.ajax({
		url: "<c:url value='/admin/ajaxAdminModify.json'/>",
		type: "POST",
		data: form,
		dataType: "json",
		success: function(data) {
			if (data.status == "OK") {
				goAdminDetail();
			}
			else {
				Alert("<spring:message code='ui.common.ajax.error'/>");			// 처리중 오류가 발생했습니다.\n잠시 후 다시 시도하십시오.			
			}
		}
	});
}

</script>

<!-- 컨텐츠 영역 -->
<div class="row">
	<form id="frm" name="frm">
	<input type="hidden" id="adminId" name="adminId" value="${info.adminId}">
	<table class="table table-bordered table-info">
		<colgroup>
			<col class="col-xs-2" />
			<col class="col-xs-4" />
			<col class="col-xs-2" />
			<col class="col-xs-4" />
		</colgroup>
		<tbody>
			<tr>
				<th><spring:message code="ui.common.label.id"/></th>
				<td colspan="3">${info.adminId}</td>
			</tr>
			<tr>
				<th><spring:message code="ui.common.label.name"/><em class="fc-red">*</em></th>
				<td><input type="text" class="form-control" id="adminName" name="adminName" data-label="<spring:message code='ui.common.label.name'/>" value="${info.adminName}" maxlength="50" autofocus></td>
				<th><spring:message code="ui.admingroup.label.adminGroup"/><em class="fc-red">*</em></th>
				<td>
					<div class="form-inline">
						<select class="form-control" id="adminGroupId" name="adminGroupId" data-label="<spring:message code='ui.admingroup.label.adminGroup'/>" >
							<option value=""><spring:message code="ui.common.label.select"/></option> <!-- 선택 -->
						  <c:forEach items="${adminGroupList}" var="row">
							<option value="${row.adminGroupId}" <c:if test="${info.adminGroupId eq row.adminGroupId}">selected</c:if> >${row.adminGroupName}</option>
						  </c:forEach>
						</select>
					</div>
				</td>
			</tr>
			<tr>
				<th><spring:message code="ui.admin.label.phoneNumber"/></th>
				<td>
					<div class="form-inline">
						<input type="hidden" id="phoneNumber" name="phoneNumber" data-label="<spring:message code='ui.admin.label.phoneNumber'/>">
						<c:set var="tel1" value="${fn:substringBefore(info.phoneNumber,'-')}" />
						<c:set var="tempTel" value="${fn:substringAfter(info.phoneNumber,'-')}" />
						<c:set var="tel2" value="${fn:substringBefore(tempTel,'-')}" />
						<c:set var="tel3" value="${fn:substringAfter(tempTel,'-')}" />
						<select class="form-control" id="phoneNumber1" >
							<option value="" <c:if test="${tel1 eq ''}">selected</c:if> ><spring:message code="ui.common.label.select"/></option><!-- 선택 -->
							<option value="010" <c:if test="${tel1 eq '010'}">selected</c:if> >010</option>
							<option value="011" <c:if test="${tel1 eq '011'}">selected</c:if> >011</option>
							<option value="016" <c:if test="${tel1 eq '016'}">selected</c:if> >016</option>
							<option value="017" <c:if test="${tel1 eq '017'}">selected</c:if> >017</option>
							<option value="018" <c:if test="${tel1 eq '018'}">selected</c:if> >018</option>
							<option value="019" <c:if test="${tel1 eq '019'}">selected</c:if> >019</option>
						</select>
						 - 
						<input type="text" class="form-control w-70" id="phoneNumber2" name="phoneNumber2" data-label="<spring:message code='ui.admin.label.phoneNumber'/>" maxlength="4" value="${tel2}" />
						 - 
						<input type="text" class="form-control w-70" id="phoneNumber3" name="phoneNumber3" data-label="<spring:message code='ui.admin.label.phoneNumber'/>" maxlength="4" value="${tel3}" />
					</div>
				</td>
				<th><spring:message code="ui.admin.label.email"/></th>
				<td>
					<input type="email" class="form-control" id="email" name="email" data-label="<spring:message code='ui.admin.label.email'/>" placeholder="<spring:message code="ui.admin.label.email"/>" value="${info.email}" maxlength="100">
				</td>
			</tr>
			<tr>
				<th><spring:message code="ui.common.label.useYn"/><em class="fc-red">*</em></th>
				<td colspan="3">
					<div class="form-inline">
						<select class="form-control" id="useYn" name="useYn" data-label="<spring:message code='ui.common.label.useYn'/>" value="${info.useYn}">
							<option value=""><spring:message code="ui.common.label.select"/></option> <!-- 선택 -->
							<option value="Y" <c:if test="${info.useYn eq 'Y'}">selected</c:if> ><spring:message code="ui.common.label.use"/></option>
							<option value="N" <c:if test="${info.useYn eq 'N'}">selected</c:if> ><spring:message code="ui.common.label.useNot"/></option>
						</select>
					</div>
				</td>
			</tr>
		</tbody>
	</table>
	</form>
</div>

<!-- 버튼 영역 -->
<div class="row btn-group-area">
	<div class="col-xs-6 text-left">
		<a href="#" class="btn btn-default" role="button" id="btn-list"><spring:message code="ui.common.button.list"/></a>
	</div>
	<div class="col-xs-6 text-right">
		<a href="#" class="btn btn-outline-primary" role="button" id="btn-cancel"><spring:message code="ui.common.button.cancel"/></a>
		<a href="#" class="btn btn-primary" role="button" id="btn-modify"><spring:message code="ui.common.button.save"/></a>
	</div>
</div>
