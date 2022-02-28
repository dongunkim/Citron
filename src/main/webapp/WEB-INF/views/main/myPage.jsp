<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/include-taglib.jspf" %>

<script type="text/javascript" src="/lib/jsencrypt-master/jsencrypt.min.js"></script>

<script type="text/javascript">
//암호화 여부 설정
//전역변수를 클로저로 대체
var encryptMgr = (function() {
	var _useEncrypt = true;
	return {
		get: function() {
			return _useEncrypt;
		}
	}
}());

$(function() {
	// 영문 입력 방지 (한글은 안됨)
	$("#phoneNumber2").on('keypress', function(event){		
		if((event.keyCode<48)||(event.keyCode>57)) event.preventDefault();		
	});	
	// 영문 입력 방지 (한글은 안됨)
	$("#phoneNumber3").on('keypress', function(event){
		if((event.keyCode<48)||(event.keyCode>57)) event.preventDefault();
	});
	
	$("#btn-modify").on("click", function(e) {
		e.preventDefault();
		
		if (!validatorObj.customValidate()) return;
		
		Confirm("<spring:message code='ui.common.label.modify'/>",			// 수정
			    "${board.title}</br></br>" + 
				"<spring:message code='ui.common.alert.action.modify'/>",	// 수정하시겠습니까? 
				true, function() {
					if(encryptMgr.get()) getKey();
					else goMyPageModify('frm');
				});
	});
	
	// jQuery Validation 설정
	var validatorObj = $("#frm").validate({			
		rules: {
			adminName: {
				required: true,
				minlength: 2
			},
			phoneNumber2: {
				minlength: 3,
				digits: true
			},
			phoneNumber3: {
				minlength: 4,
				digits: true
			},
		    email: {
		    	email: true
		    }
		}
	});
});


/*
 * 내정보 업데이트
 */
function goMyPageModify(form) {
	if (form == 'frm') {
		$("#phoneNumber").val($("#phoneNumber1").val()+"-"+$("#phoneNumber2").val()+"-"+$("#phoneNumber3").val());
	}
	var formData = $("#"+form).serialize();
	
	serverCall(formData, "/main/ajaxMyPageModify.json", function(data) {
		if (data.status == "OK") {
			if (data.forward) document.location.href = data.forward;
			else Alert("<spring:message code='ui.common.ajax.error'/>");	// 처리중 오류가 발생했습니다.\n잠시 후 다시 시도하십시오.	
		}
		else {
			Alert("<spring:message code='ui.common.ajax.error'/>");			// 처리중 오류가 발생했습니다.\n잠시 후 다시 시도하십시오.			
		}
	});	
}

/*
 * 암호화 업데이트를 위해 Public Key 조회
 */
function getKey() {	
	getPublicKey(function(data) {
		
		var crypt = new JSEncrypt();
		crypt.setPublicKey(data.publicKey);
		
		$("#phoneNumber").val($("#phoneNumber1").val()+"-"+$("#phoneNumber2").val()+"-"+$("#phoneNumber3").val());
		
		// 암호화할 경우, XSS 적용안됨
		// 따라서, 암호화와 XSS 필요성을 각각 따져보고 암호화 대상 항목을 정해야 함		
		$("#encAdminId").val(crypt.encrypt($("#adminId").val()));				
		$("#encAdminName").val($("#adminName").val());
		$("#encPhoneNumber").val(crypt.encrypt($("#phoneNumber").val()));
		$("#encEmail").val(crypt.encrypt($("#email").val()));
		
		goMyPageModify('encform');
	});	
}
</script>

<div class="container-fluid">
	<!-- 페이지 타이틀 -->
	<div class="row">
		<h2 class="page-header text-center"><spring:message code="ui.admin.button.myPage"/></h2> <!-- 내 정보 -->
	</div>

	<!-- 사이트 이동경로 -->
	<div class="row">
		<ol class="breadcrumb">
			<li>Home</li>
			<li class="active"><spring:message code="ui.admin.button.myPage"/></li> <!-- 내 정보 -->
		</ol>
	</div>

	<!-- 컨텐츠 영역 -->
	<div class="row">
		<form class="form-horizontal" id="frm" name="frm">
			<div class="form-group">
				<label for="adminId" class="col-xs-2 control-label"><spring:message code="ui.common.label.id"/></label> <!-- ID -->
				<div class="col-xs-10">
					<label class="col-xs-2 control-label">${admin.adminId}</label>
					<input type="hidden" id="adminId" name="adminId" value="${admin.adminId}">
				</div>
			</div>
			<div class="form-group">
				<label for="adminName" class="col-xs-2 control-label">
					<spring:message code="ui.common.label.name"/><em class="fc-red">*</em> <!-- 이름 -->
				</label>
				<div class="col-xs-10">
					<input type="text" class="form-control" placeholder="<spring:message code='ui.common.label.name'/>" id="adminName" name="adminName" data-label="<spring:message code='ui.common.label.name'/>" value="${admin.adminName}">
				</div>
			</div>
			<div class="form-group">
				<label for="phoneNumber" class="col-xs-2 control-label"><spring:message code="ui.admin.label.phoneNumber"/></label> <!-- 전화번호 -->
				<div class="col-xs-10 form-inline">
					<c:set var="tel1" value="${fn:substringBefore(admin.phoneNumber,'-')}" />
					<c:set var="tempTel" value="${fn:substringAfter(admin.phoneNumber,'-')}" />
					<c:set var="tel2" value="${fn:substringBefore(tempTel,'-')}" />
					<c:set var="tel3" value="${fn:substringAfter(tempTel,'-')}" />
						<select class="form-control" id="phoneNumber1">
							<option value="" <c:if test="${tel1 eq ''}">selected</c:if> ><spring:message code="ui.common.label.select"/></option><!-- 선택 -->
							<option value="010" <c:if test="${tel1 eq '010'}">selected</c:if> >010</option>
							<option value="011" <c:if test="${tel1 eq '011'}">selected</c:if> >011</option>
							<option value="016" <c:if test="${tel1 eq '016'}">selected</c:if> >016</option>
							<option value="017" <c:if test="${tel1 eq '017'}">selected</c:if> >017</option>
							<option value="018" <c:if test="${tel1 eq '018'}">selected</c:if> >018</option>
							<option value="019" <c:if test="${tel1 eq '019'}">selected</c:if> >019</option>
						</select>
						- 
						<input type="text" class="form-control w-100" maxlength="4" id="phoneNumber2" name="phoneNumber2" data-label="<spring:message code='ui.admin.label.phoneNumber'/>" value="${tel2}" />
						- 
						<input type="tel" class="form-control w-100" maxlength="4" id="phoneNumber3" name="phoneNumber3" data-label="<spring:message code='ui.admin.label.phoneNumber'/>" value="${tel3}" />
					<input type="hidden" id="phoneNumber" name="phoneNumber" />
				</div>
			</div>
			<div class="form-group">
				<label for="email" class="col-xs-2 control-label"><spring:message code="ui.admin.label.email"/></label> <!-- 이메일 -->
				<div class="col-xs-10">
					<input type="email" class="form-control" placeholder="Email" id="email" name="email" data-label="<spring:message code='ui.admin.label.email'/>" value="${admin.email}">
				</div>
			</div>
			<div class="form-group text-right">
				<div class="col-xs-12">
					<a href="#" class="btn btn-primary" role="button" id="btn-modify"><spring:message code="ui.common.button.save"/></a> <!-- 저장 -->
				</div>
			</div>
		</form>
	</div>
</div>
<form id="encform" name="encform">
	<input type="hidden" id="useEnc" name="useEnc" value="Y" />
	<input type="hidden" id="encAdminId" name="adminId" />	
	<input type="hidden" id="encAdminName" name="adminName" />
	<input type="hidden" id="encPhoneNumber" name="phoneNumber" />
	<input type="hidden" id="encEmail" name="email" />
</form>
