<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/include-header.jspf" %>

<script type="text/javascript" src="/js/jsencrypt-master/jsencrypt.min.js"></script>
<script>      
   function sendPassMail() {
	  var comSubmit = new commonSubmit('frm');
	  comSubmit.setUrl("<c:url value='/sample/sendMail'/>");
	  comSubmit.submit();
   } 
</script>

	<form id="frm" name="frm">
		<table class="login_view">
			<colgroup>
				<col width="30%">
				<col width="*">
			</colgroup>
			<caption>메일 전송</caption>
			<tbody>
				<tr>
					<th scope="row">이름</th>
					<td>
						<input type="text" id="adminName" name="adminName" class="wdp_90" value="박준용"/>						
					</td>					
				</tr>
				<tr>
					<th scope="row">메일주소</th>
					<td>
						<input type="text" id="email" name="email" class="wdp_90" value="jinilamp@nate.com"/>						
					</td>					
				</tr>							
			</tbody>
		</table>		
		<br/><br/>
		
		<a href="#this" class="btn" id="passMail">임시비밀번호</a>		
	</form>
	
	<script type="text/javascript">
		$(document).ready(function(){ 
			$("#passMail").on("click", function(e) {
				e.preventDefault();
				sendPassMail();
			});			
			
		});
	</script>
