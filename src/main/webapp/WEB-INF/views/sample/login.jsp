<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/include-header.jspf" %>

<script type="text/javascript" src="/js/jsencrypt-master/jsencrypt.min.js"></script>
<script>
      
   function openBoardList() {
	  var comSubmit = new commonSubmit();
	  comSubmit.setUrl("<c:url value='/sample/openBoardList'/>");
	  comSubmit.submit();
   }
 
   function getPublicKey() {
	   $.ajax({
			url: "/sample/getPubKey.json",
			type: "POST",
			dataType: "json",
			success: function(data) {	
				if (data.publicKey) {
					console.log("data \t:\n" + JSON.stringify(data));
										
					var crypt = new JSEncrypt();
					crypt.setPublicKey(data.publicKey);
							
					$("#encPassword").val(crypt.encrypt($("#password").val()));					
					loginEncrypt();
				}
									
				else alert('처리중 오류가 발생했습니다.\n잠시 후 다시 시도하십시오.');
			},
			error: function(e) {
				// 공통으로 에러 처리 (Alert)
			}
		});
   }
   
   function loginEncrypt() {
	   var data = {
				"adminId": $("#adminId").val(),
				"password": $("#encPassword").val()
	   		};
	   	   
		$.ajax({
			url: "<c:url value='/sample/loginEncrypt.json'/>",
			type: "POST",
			data: data,
			dataType: "json",
			success: function(data) {
				console.log("data \t:\n" + JSON.stringify(data));
								
				if (data.forward) 
					document.location.href = data.forward;
				else {
					if (data.code == "102") 	// 로그인정보 불일치
						alert(data.message);
					else 
						alert('처리중 오류가 발생했습니다.\n잠시 후 다시 시도하십시오.');
				}
			},
			error: function(e) {
				// 공통으로 에러 처리 (Alert)
			}
		});
   }
   
   function login() {
	   var data = $("#frm").serialize();
	   	   
		$.ajax({
			url: "<c:url value='/sample/login.json'/>",
			type: "POST",
			data: data,
			dataType: "json",
			success: function(data) {
				console.log("data \t:\n" + JSON.stringify(data));
								
				if (data.forward)
					document.location.href = data.forward;
				else {
					if (data.code == "102") 	// 로그인정보 불일치
						alert(data.message);
					else 
						alert('처리중 오류가 발생했습니다.\n잠시 후 다시 시도하십시오.');
				}
			},
			error: function(e) {
				// 공통으로 에러 처리 (Alert)
			}
		});
   }
   
   function logout() {
	   $.ajax({
			url: "<c:url value='/sample/logout.json'/>",
			type: "POST",			
			dataType: "json",
			success: function(data) {
				console.log("data \t:\n" + JSON.stringify(data));
				
				if (data.forward) document.location.href = data.forward;
				else alert('처리중 오류가 발생했습니다.\n잠시 후 다시 시도하십시오.');
			},
			error: function(e) {
				// 공통으로 에러 처리 (Alert)
			}
		});
   }
   
</script>

	<form id="frm" name="frm">
		<table class="login_view">
			<colgroup>
				<col width="30%">
				<col width="*">
			</colgroup>
			<caption>로그인</caption>
			<tbody>
				<tr>
					<th scope="row">아이디</th>
					<td>
						<input type="text" id="adminId" name="adminId" class="wdp_90" value="jinilamp"/>						
					</td>					
				</tr>
				<tr>
					<th scope="row">비밀번호</th>
					<td>
						<input type="password" id="password" name="password" class="wdp_90" value="!wlslzja!" />
						<input type="hidden" id="encPassword" name="encPassword" />
					</td>					
				</tr>
			</tbody>
		</table>		
		<br/><br/>
		
		<a href="#this" class="btn" id="login">로그인</a>
		<a href="#this" class="btn" id="loginEnc">암호화 로그인</a>
		<a href="#this" class="btn" id="logout">로그아웃</a>
		<a href="#this" class="btn" id="list">목록으로</a>
	</form>
	
	<script type="text/javascript">
		$(document).ready(function(){ 
			$("#login").on("click", function(e) {
				e.preventDefault();
				login();
			});
			
			$("#loginEnc").on("click", function(e) {
				e.preventDefault();
				getPublicKey();
			});
			
			$("#logout").on("click", function(e) {
				e.preventDefault();
				logout();
			});
			
			$("#list").on("click", function(e) {
				e.preventDefault();
				openBoardList();
			});
			
		});
	</script>
