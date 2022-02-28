<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/include-taglib.jspf"%>
<%@ page import="com.dinens.citron.p.admin.common.constants.Constants" %>

<style>
#footer {
    position:absolute;
    bottom:0;
    width:100%;
    text-align: center;    
}
</style>

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

$(function(){	
	$("#btn-next").on("click", function(e) {
		e.preventDefault();
		
		$("#error").hide();
		if (validatorObj.customValidate()) {			
			if(encryptMgr.get()) getKey();
			else next('frm');
		}
	});
	// 엔터키
	$('form input').keydown(function(e) {		
        if (e.keyCode == 13) {
        	$("#btn-next").trigger("click");
        	e.preventDefault();
        }
    });
    
 	// jQuery Validation 설정
	var validatorObj = $("#frm").validate({			
		rules: {
			adminId: "required"		    
		},
		showErrors:false,
		highlight: function(element) {
			$(element).focus();
		}
	}); 	
});

/*
 * 비밀번호 찾기를 위한 아이디 확인
 */
function next(form) {
	var formData = $("#"+form).serialize();
  	
	serverCall(formData, "/main/ajaxFindPasswd.json", function(data) {
		onSuccess(data);
	});
}

/*
 * 암호화 처리를 위해 Public Key 조회
 */
function getKey() {
	getPublicKey(function(data) {
		
		var crypt = new JSEncrypt();
		crypt.setPublicKey(data.publicKey);
		
		$("#encAdminId").val(crypt.encrypt($("#adminId").val()));		
		
		next('encform');
	});	
}
 
/*
 * 아이디 확인 결과가 성공(OK)인 경우의 처리
 * status=OK 라도 forward 값이 이어야 정상
 *                forward 값이 없을 경우 에러 처리
 */
function onSuccess(data) {
	if (data.status == '<%=Constants.Error.RESULT_OK%>') {		
		if (data.forward) goStep2(data.forward);
		else showError("<spring:message code='ui.common.ajax.error'/>");						// 처리중 오류가 발생했습니다.\n잠시 후 다시 시도하십시오.		
	}			
	else {
		if (data.code == '<%=Constants.Error.ID_NOT_EXIST%>') showError(data.message);			// 존재하지 않는 아이디 입니다.					
		else showError("<spring:message code='ui.common.ajax.error'/>");						// 처리중 오류가 발생했습니다.\n잠시 후 다시 시도하십시오.		
	}
}

/*
 * 비밀번호찾기 2단계화면 이동
 */
function goStep2(url) {
	var comSubmit = new commonSubmit();
	comSubmit.setUrl(url);
	comSubmit.addParam("nextStep", "2");
	comSubmit.addParam("adminId", $("#adminId").val());
	comSubmit.submit();
}

/*
 * 아이디 확인 시 발생한 서버 오류 Display
 */
function showError(error) {
	$("#error").html(error);	
	$("#error").show();
}
</script>
<div class="container">
	<form class="form-findPasswd" id="frm" name="frm">
		<input type="hidden" id="step" name="step" value="1" />
		<h2 class="form-findPasswd-heading text-center form-item-group"><spring:message code='ui.main.label.findPassword'/></h2> <!-- 비밀번호 찾기 -->
		<br/>		
		<br/>
		<p><spring:message code='ui.main.findPassword.desc.step1'/></p>		
		<hr>
		<p align="right"><font color="red">1. <spring:message code='ui.main.findPassword.navi.step1'/></font> &nbsp;&gt;&nbsp; 2. <spring:message code='ui.main.findPassword.navi.step2'/> &nbsp;&gt;&nbsp; 3. <spring:message code='ui.main.findPassword.navi.step3'/></p>
		<br/><br/>
		<div class="form-item-group">
			<input class="form-control" id="adminId" name="adminId" placeholder="<spring:message code='ui.common.placeholder.id'/>" data-label="<spring:message code='ui.common.label.id'/>" required autofocus>
			<a href="#this" class="btn btn-lg btn-primary btn-next" id="btn-next"><spring:message code="ui.main.findPassword.button.next"/></a> <!-- 다음 -->
			<label class="error" id="error" style="display: none;"></label>
		</div>
		<br/><br/>
	</form>
</div>
<form id="encform" name="encform">
	<input type="hidden" id="useEnc" name="useEnc" value="Y" />
	<input type="hidden" id="step" name="step" value="1" />
	<input type="hidden" id="encAdminId" name="adminId" />	
</form>
