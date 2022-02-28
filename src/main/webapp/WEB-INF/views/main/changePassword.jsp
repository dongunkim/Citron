<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/include-taglib.jspf" %>
<%@ page import="com.dinens.citron.p.admin.common.constants.Constants" %>

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
	$("#btn-save").on("click", function(e) {
		e.preventDefault();
		
		$("#error").hide();		
		if (!validatorObj.customValidate()) return;
		
		if(encryptMgr.get()) getKey();
		else goChangePasswd('frm');		
	});	
	// 엔터키
	$('form input').keydown(function(e) {
        if (e.keyCode == 13) {
        	$("#btn-save").trigger("click");        	
        }
    });	
	// 비밀번호보기
	$('form i').on('mousedown',function(){
    	$(this).attr('class',"glyphicon glyphicon-eye-open")
        .next('input').attr('type',"text");
	});
	$('form i').on('mouseup',function(){
		$(this).attr('class',"glyphicon glyphicon-eye-close")
		.next('input').attr('type','password');
	});
	
	// jQuery Validation 설정
	var validatorObj = $("#frm").validate({			
		rules: {
			password: {
				required: true
			},
			newPassword: {
				required: true,
				minlength: 8
			},
			newPasswordConfirm: {
				required: true,
				minlength: 8,
				equalTo: newPassword
			}
		},
		customHandler: function(form) {
			if (!passwdCheck()) return false;
			return true;			
		},
		showErrors:false,
		highlight: function(element) {
			$(element).focus();
		}
	});
});


/*
 * 비밀번호 유효성 체크
 */
 function passwdCheck() {
	var str = $("#newPassword").val();
	
	// 기존비밀번호 동일여부 체크
	if(str == $("#password").val()) {
		showError("<spring:message code='ui.admin.validation.password.notEqualTo'/>"); // 같은 비밀번호는 사용할 수 없습니다.
		return false;
	}
	
	// 공백 체크
	if(str.search(/\s/) != -1) {		
		showError("<spring:message code='ui.admin.validation.password.noSpace'/>"); // 비밀번호는 공백을 포함할 수 없습니다.
		return false;
	}
	
	// 숫자, 문자, 특수문자 패턴 체크
	var numbers = /[0-9]/; 								// 숫자 
	var alphabets = /[a-zA-Z]/;							// 문자
	var specials = /[<{~!@#$%^&*()_+\?|}>]/;			// 특수문자
	                
	var score = 0;
	
	if (numbers.test(str)) score++;	
	if (alphabets.test(str)) score++;	
	if (specials.test(str)) score++;	
	
	if(score < 2) {		
		showError("<spring:message code='ui.admin.validation.password.pattern'/>");	// 비밀번호는 문자, 숫자, 특수문자 중 2가지 이상으로 구성하여야 합니다.
		return false; 
	}
		
    return true;
}
 
 
/*
 * 암호화 처리를 위해 Public Key 조회
 */
function getKey() {
	getPublicKey(function(data) {
		
		var crypt = new JSEncrypt();
		crypt.setPublicKey(data.publicKey);		
		
		$("#encPassword").val(crypt.encrypt($("#password").val()));
		$("#encNewPassword").val(crypt.encrypt($("#newPassword").val()));
		
		goChangePasswd('encform');
	});
}


/*
 * 비밀번호 변경
 */
function goChangePasswd(form) {	
	var formData = $("#"+form).serialize();
	
	serverCall(formData, "/main/ajaxChangePasswd.json", function(data) {
		onSuccess(data);
	});	
}

/*
 * 비밀번호 변경 결과가 성공(OK)인 경우의 처리
 * status=OK 라도 forward 값이 이어야 정상
 *                forward 값이 없을 경우 에러 처리
 */
function onSuccess(data) {
	if (data.status == "<%=Constants.Error.RESULT_OK%>") {		
		if (data.forward) document.location.href = data.forward;		
		else showError("<spring:message code='ui.common.ajax.error'/>");										// 처리중 오류가 발생했습니다.\n잠시 후 다시 시도하십시오.		
	}
	else {
		if (data.code == "<%=Constants.Error.PASSWORD_NOT_MATCH%>") showError(data.message);					// 비밀번호 불일치					
		else showError("<spring:message code='ui.common.ajax.error'/>");										// 처리중 오류가 발생했습니다.\n잠시 후 다시 시도하십시오.		
	}
}

/*
 * 비밀번호 변경 시 발생한 서버 오류 Display
 */
function showError(error) {
	$("#error").html(error);	
	$("#error").show();
}
</script>

<div class="container-fluid">
	<!-- 페이지 타이틀 -->
	<div class="row">
		<h2 class="page-header text-center"><spring:message code="ui.admin.button.changePassword"/></h2> <!-- 비밀번호 변경 -->
	</div>

	<!-- 사이트 이동경로 -->
	<div class="row">
		<ol class="breadcrumb">
			<li>Home</li>
			<li class="active"><spring:message code="ui.admin.button.changePassword"/></li> <!-- 비밀번호 변경 -->
		</ol>
	</div>

	<!-- 컨텐츠 영역 -->
	<div class="row">
		<form class="form-horizontal" id="frm" name="frm">
			<div class="form-group change-password">
				<label for="currentPassword" class="col-xs-4 control-label">
					<spring:message code="ui.admin.label.currentPassword"/><em class="fc-red">*</em>
				</label>
				<div class="col-xs-8">
					<input type="password" class="form-control" id="password" name="password" placeholder="<spring:message code='ui.admin.label.currentPassword'/>" autofocus> <!-- 현재 비밀번호 -->
				</div>
			</div>
			<div class="form-group change-password">
				<label for="newPassword" class="col-xs-4 control-label">
					<spring:message code="ui.admin.label.newPassword"/><em class="fc-red">*</em>
				</label>
				<div class="col-xs-8">
					<i class="glyphicon glyphicon-eye-close"></i>					
					<input type="password" class="form-control" id="newPassword" name="newPassword" placeholder="<spring:message code='ui.admin.label.newPassword'/>"> <!-- 새 비밀번호 -->				
				</div>
			</div>
			<div class="form-group change-password">
				<label for="newPassword" class="col-xs-4 control-label">
					<spring:message code="ui.admin.label.newPassword.confirm"/><em class="fc-red">*</em>
				</label>				
				<div class="col-xs-8">
					<input type="password" class="form-control" id="newPasswordConfirm" name="newPasswordConfirm" placeholder="<spring:message code='ui.admin.label.newPassword.confirm'/>"> <!-- 새 비밀번호 확인 -->
				</div>
			</div>
			
				<div class="change-password col-xs-8" style="left:33.3%;">
					<label class="server-error" id="error" style="display: none;"></label>
				</div>
			
			<div class="form-group text-right">
				<div class="col-xs-12">
					<a href="#" class="btn btn-primary" role="button" id="btn-save"><spring:message code="ui.common.button.confirm"/></a> <!-- 확인 -->
				</div>
			</div>
		</form>
	</div>
</div>
<form id="encform" name="encform">
	<input type="hidden" id="useEnc" name="useEnc" value="Y" />
	<input type="hidden" id="encPassword" name="password" />
	<input type="hidden" id="encNewPassword" name="newPassword" />	
</form>
