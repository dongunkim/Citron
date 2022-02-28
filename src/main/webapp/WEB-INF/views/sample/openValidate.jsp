<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/include-header.jspf" %>

<script>
	$(document).ready(function () {			
		$("#frm").validate({
		  rules: {
		    id: {
		      required: true
		    },
		    password: {
		    	required: true,
		    	minlength : 3
		    },
		    repassword: {
		    	required: true,
		    	equalTo : password
		    },
		    age: {
		    	required: true,
		    	digits : true
		    },
		    name: {
		    	required: true
		    },
		    email: {
		    	required: true,
		    	email : true
		    },
		    homepage: {
		    	required: true,
		    	url : true
		    }
		  }
		});
	});   
</script>
	<form action="/sample//submitValidate" id="frm"> 
		아이디 : <input type="text" name="id" id="id"/><br/> 
		비밀번호 : <input type="password" name="password" id="password"/><br/> 
		비밀번호확인 : <input type="password" name="repassword" id="repassword"/><br/> 
		나이 : <input type="text" name="age" id="age"/><br/> 
		이름 : <input type="text" name="name" id="name"/><br/> 
		이메일 : <input type="text" name="email" id="email"/><br/> 
		홈페이지 : <input type="text" name="homepage" id="homepage"/><br/> 
		<input type="submit" value="회원가입" /> 
	</form>