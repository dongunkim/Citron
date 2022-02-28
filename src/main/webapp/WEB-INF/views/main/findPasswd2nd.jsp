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
			if(encryptMgr.get()) getKey(next);
			else next('frm');
		}
	});
	// 엔터키
	$('form input').keydown(function(e) {
        if (e.keyCode == 13) {
        	$("#btn-next").trigger("click");        	
        }
    });
    
 	// jQuery Validation 설정
	var validatorObj = $("#frm").validate({			
		rules: {
			adminName: {
				required: true	
			},
			email: {
				required: true,
				email: true
			}
		},
		showErrors:false,
		highlight: function(element) {
			$(element).focus();
		}
	}); 	
});

/*
 * 비밀번호 찾기를 위한 본인 확인(이름 + 이메일 주소)
 */
var next = function(form) {
	var formData = $("#"+form).serialize();
  	
	serverCall(formData, "/main/ajaxFindPasswd.json", function(data) {
		onSuccess("next", data);
	});	
}

/*
 * 암호화 처리를 위해 Public Key 조회
 */
function getKey(method) {	
	getPublicKey(function(data) {
		
		var crypt = new JSEncrypt();
		crypt.setPublicKey(data.publicKey);
		
		$("#encAdminId").val(crypt.encrypt($("#adminId").val()));		
		$("#encAdminName").val($("#adminName").val());
		$("#encEmail").val($("#email").val());
		method('encform');
	});	
}
 
/*
 * 본인 확인 결과가 성공(OK)인 경우의 처리
 * status=OK 라도 forward 값이 이어야 정상
 *                forward 값이 없을 경우 에러 처리
 */
function onSuccess(from, data) {
	if (data.status == '<%=Constants.Error.RESULT_OK%>') {		
		if (from == "next") {			
			Confirm("<spring:message code='ui.main.findPassword.navi.step3'/>", 
					"<spring:message code='ui.main.findPassword.alert.action.send' arguments='" + $("#email").val() + "'/>", 
					true, function() {				
				if(encryptMgr.get()) getKey(sendPassMail);
				else sendPassMail('frm');
			});				
		}
		else if (from == "sendPassMail") {
			if (data.forward) goStep3(data.forward);			
			else showError("<spring:message code='ui.common.ajax.error'/>");					// 처리중 오류가 발생했습니다.\n잠시 후 다시 시도하십시오.			
		}
	}			
	else {
		if (data.code == '<%=Constants.Error.VALUE_NOT_EXIST%>') showError(data.message);		// 등록된 정보와 일치하지 않습니다.					
		else showError("<spring:message code='ui.common.ajax.error'/>");						// 처리중 오류가 발생했습니다.\n잠시 후 다시 시도하십시오.		
	}
}

/*
 * 임시비밀번호 발송
 */
var sendPassMail = function(form) {
	var formData = $("#"+form).serialize();
  	
	serverCall(formData, "/main/ajaxSendPassMail.json", function(data) {
		onSuccess("sendPassMail", data);
	});	
}

/*
 * 비밀번호찾기 3단계화면 이동
 */
function goStep3(url) {
	var comSubmit = new commonSubmit();
	comSubmit.setUrl(url);
	comSubmit.addParam("nextStep", "3");
	comSubmit.submit();
}

/*
 * 이름 + 이메일 확인 시 발생한 서버 오류 Display
 */
function showError(error) {
	$("#error").html(error);	
	$("#error").show();
}
</script>
<div class="container">
	<form class="form-findPasswd" id="frm" name="frm">
		<input type="hidden" id="adminId" name="adminId" value="${adminId}" />
		<input type="hidden" id="step" name="step" value="2" />
		<h2 class="form-findPasswd-heading text-center form-item-group"><spring:message code='ui.main.label.findPassword'/></h2> <!-- 비밀번호 찾기 -->
		<br/><br/>
		<p><spring:message code='ui.main.findPassword.desc.step2'/></p>		
		<hr>
		<p align="right">1. <spring:message code='ui.main.findPassword.navi.step1'/> &nbsp;&gt;&nbsp; <font color="red">2. <spring:message code='ui.main.findPassword.navi.step2'/> </font>&nbsp;&gt;&nbsp; 3. <spring:message code='ui.main.findPassword.navi.step3'/></p>
		<br/><br/>
		<div class="form-item-group">
			<input class="form-control" id="adminName" name="adminName" placeholder="<spring:message code='ui.common.placeholder.name'/>" data-label="<spring:message code='ui.common.label.name'/>" required autofocus>
		</div>
		<div class="form-item-group">			
			<input class="form-control" id="email" name="email" placeholder="<spring:message code='ui.main.findPassword.placeholder.email'/>" data-label="<spring:message code='ui.main.findPassword.label.email'/>" required>
			<a href="#this" class="btn btn-lg btn-primary btn-next" id="btn-next"><spring:message code="ui.main.findPassword.button.next"/></a> <!-- 다음 -->
			<label class="error" id="error" style="display: none;"></label>
		</div>
		<br/><br/>
	</form>
</div>
<form id="encform" name="encform">
	<input type="hidden" id="useEnc" name="useEnc" value="Y" />
	<input type="hidden" id="step" name="step" value="2" />
	<input type="hidden" id="encAdminId" name="adminId" />
	<input type="hidden" id="encAdminName" name="adminName" />
	<input type="hidden" id="encEmail" name="email" />
</form>
