<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/include-taglib.jspf"%>
<%@ page import="com.dinens.citron.p.admin.common.constants.Constants" %>

<script type="text/javascript" src="/lib/jsencrypt-master/jsencrypt.min.js"></script>

<script type="text/javascript">
// 암호화 여부 설정
// 전역변수를 클로저로 대체
var encryptMgr = (function() {
	var _useEncrypt = true;
	return {
		get: function() {
			return _useEncrypt;
		}
	}
}());

$(function(){	
	$("#btn-login").on("click", function(e) {
		e.preventDefault();
		
		$("#error").hide();
		if (validatorObj.customValidate()) {
			if(encryptMgr.get()) getKey();
			else login('frm');
		}
	});
	// 엔터키
    $('form input').keydown(function(e) {
        if (e.keyCode == 13) {
        	$("#btn-login").trigger("click");        	
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
	// 비밀번호찾기
	$("#btn-findPasswd").on('click', function(e) {
		e.preventDefault();
		goFindPasswd();
	});
          
 	// jQuery Validation 설정
	var validatorObj = $("#frm").validate({			
		rules: {
			adminId: "required",
		    password: "required"
		},
		showErrors:false,
		highlight: function(element) {
			$(element).focus();
		}
	});
 	
 	// RememberMe 체크
	if (!isNull($.cookie('adminId'))) {
		$("#adminId").val($.cookie('adminId'));
		$("#password").focus();
		$("#rememberMe").prop('checked', true);
	}
});

/*
 * 로그인 처리
 */
function login(form) {
	var formData = $("#"+form).serialize();
  	
	serverCall(formData, "/main/login.json", function(data) {
		onSuccess(data);
	});
}

/*
 * 암호화 로그인을 위해 Public Key 조회
 */
function getKey() {
	getPublicKey(function(data) {
		
		var crypt = new JSEncrypt();
		crypt.setPublicKey(data.publicKey);
		
		$("#encAdminId").val(crypt.encrypt($("#adminId").val()));
		$("#encPassword").val(crypt.encrypt($("#password").val()));
		
		login('encform');
	});	
}
 
/*
 * 로그인 결과가 성공(OK)인 경우의 처리
 * status=OK 라도 forward 값이 이어야 정상
 *                forward 값이 없을 경우 에러 처리
 */
function onSuccess(data) {
	if (data.status == "<%=Constants.Error.RESULT_OK%>") {
		
		if (data.forward) {
			// 아이디 기억하기 체크
			if ($("#rememberMe").is(":checked")) {
				$.cookie('adminId', $("#adminId").val(), { expires: 365 });
			} else {
				$.removeCookie('adminId');
			}									
			document.location.href = data.forward;
		}
		else {
			showError("<spring:message code='ui.common.ajax.error'/>");									// 처리중 오류가 발생했습니다.\n잠시 후 다시 시도하십시오.					
			$("#password").val('');
		}
	}			
	else {
		if (data.code == "<%=Constants.Error.LOGIN_NOT_MATCH%>") showError(data.message);				// 로그인정보 불일치
		else if (data.code == "<%=Constants.Error.EXPIRED_TEMP_PASSWORD%>") showError(data.message);	// 임시비빌번호 만료
		else showError("<spring:message code='ui.common.ajax.error'/>");								// 처리중 오류가 발생했습니다.\n잠시 후 다시 시도하십시오.				
		$("#password").val('');
	}
}

/*
 * 로그인 처리 시 발생한 서버 오류 Display
 */
function showError(error) {
	$("#error").html(error);	
	$("#error").show();
}

/*
 * 비밀번호 찾기화면 이동
 */
function goFindPasswd() {
	var comSubmit = new commonSubmit();
	comSubmit.setUrl("<c:url value='/main/findPasswd'/>");
	comSubmit.submit();
}
</script>

<div class="container">
	<form class="form-signin" id="frm" name="frm">
		<h2 class="form-signin-heading text-center form-item-group"><spring:message code='ui.admin.label.admin'/></h2> <!-- 관리자 -->
		<div class="form-item-group">
			<input class="form-control" id="adminId" name="adminId" placeholder="ID" data-label="<spring:message code='ui.common.label.id'/>" required autofocus>
		</div>		
		<div class="form-item-group">
			<i class="glyphicon glyphicon-eye-close"></i>
			<input type="password" class="form-control" id="password" name="password" placeholder="<spring:message code='ui.common.label.password'/>" data-label="<spring:message code='ui.common.label.password'/>" maxlength="20" required>		
		</div>
		<div class="checkbox form-item-group">
			<label>
				<input type="checkbox" id="rememberMe" name="rememberMe">
				<spring:message code='ui.admin.label.idRemember'/> <!-- 아이디 기억하기 -->
			</label>
			<label><a href="#" class="find-passwd" id="btn-findPasswd"><spring:message code='ui.main.label.findPassword'/></a></label> <!-- 비밀번호 찾기 -->
		</div>
		<div>
		<a href="#this" type="submit" class="btn btn-lg btn-primary btn-block" id="btn-login"><spring:message code='ui.admin.button.login'/></a> <!-- 로그인 -->	
		<label class="error" id="error" style="display: none;"></label>
		</div>		
	</form>
</div>
<form id="encform" name="encform">
	<input type="hidden" id="useEnc" name="useEnc" value="Y" />
	<input type="hidden" id="encAdminId" name="adminId" />
	<input type="hidden" id="encPassword" name="password" />		
</form>
