<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/include-taglib.jspf"%>

<style>
#footer {
    position:absolute;
    bottom:0;
    width:100%;
    text-align: center;    
}
</style>

<script type="text/javascript">
$(function(){
	$("#btn-login").on("click", function(e) {
		e.preventDefault();		
		goLogin();
	});
});

/*
 * 비밀번호찾기 3단계화면 이동
 */
function goLogin() {
	var comSubmit = new commonSubmit();
	comSubmit.setUrl("<c:url value='/main/login'/>");	
	comSubmit.submit();
}
</script>

<div class="container">
	<form class="form-findPasswd" id="frm" name="frm">
		<h2 class="form-findPasswd-heading text-center form-item-group"><spring:message code='ui.main.label.findPassword'/></h2> <!-- 비밀번호 찾기 -->
		<br/><br/>
		<p><spring:message code='ui.main.findPassword.desc.step3'/></p>		
		<hr>
		<p align="right">1. <spring:message code='ui.main.findPassword.navi.step1'/> &nbsp;&gt;&nbsp; 2. <spring:message code='ui.main.findPassword.navi.step2'/> &nbsp;&gt;&nbsp; <font color="red">3. <spring:message code='ui.main.findPassword.navi.step3'/></font></p>
		<br/><br/>		
		<div class="form-item-group" style="text-align: center;">						
			<a href="#this" class="btn btn-lg btn-primary" id="btn-login"><spring:message code="ui.common.button.confirm"/></a> <!-- 확인 -->			
		</div>
		<br/><br/>
	</form>
</div>

