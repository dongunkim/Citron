<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/include-header.jspf" %>

<script type="text/javascript" src="/js/jsencrypt-master/jsencrypt.min.js"></script>
<script>
   function save() {
	   var comSubmit = new commonSubmit("frm");	  
		  comSubmit.setUrl("<c:url value='/sample/insertAccount'/>");
		  comSubmit.submit();
   }
   
   function encSave() {
	   var comSubmit = new commonSubmit("encform");
		  comSubmit.setUrl("<c:url value='/sample/encInsertAccount'/>");
		  comSubmit.submit();
   }
   
   function openBoardList() {
	  var comSubmit = new commonSubmit();
	  comSubmit.setUrl("<c:url value='/sample/openBoardList'/>");
	  comSubmit.submit();
   }
 
   function getPublicKey() {
	   $("#encAdminGroupId").val($("#adminGroupId").val());
	   $("#encAdminId").val($("#adminId").val());
	   
	   $.ajax({
			url: "<c:url value='/sample/getPubKey.json'/>",
			type: "POST",			
			dataType: "json",
			success: function(data) {				
				if (data.status == "Success") {					
					var crypt = new JSEncrypt();
					crypt.setPublicKey(data.publicKey);
										
					$("#encPassword").val(crypt.encrypt($("#password").val()));
					$("#encAdminName").val(crypt.encrypt($("#adminName").val()));
					$("#encPhoneNumber").val(crypt.encrypt($("#phoneNumber").val()));
					$("#encEmail").val(crypt.encrypt($("#email").val()));
					
					encSave();
				}									
				else alert('처리중 오류가 발생했습니다.\n잠시 후 다시 시도하십시오.');
			},
			error: function(e) {
				alert("처리 중 오류가 발생했습니다.\n에러코드 : " + e.status);
				
				console.log("Error \t: " + JSON.stringify(e));
				console.log("Error Status \t: " + e.status);				
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
			<caption>사용자 등록</caption>
			<tbody>
				<tr>
					<th scope="row">그룹</th>
					<td>
						<select id="adminGroupId" name="adminGroupId" style="width:70%; height:25px">
							<option value="000">선택하세요.</option>
							<option value="001">관리자 A그룹</option>
							<option value="002"selected>관리자 B그룹</option>
							<option value="003">관리자 C그룹</option>
						</select>
												
					</td>					
				</tr>
				<tr>
					<th scope="row">아이디</th>
					<td>
						<input type="text" id="adminId" name="adminId" class="wdp_90" value="citron"/>						
					</td>					
				</tr>
				<tr>
					<th scope="row">비밀번호</th>
					<td>
						<input type="password" id="password" name="password" class="wdp_90" value="!tlxmfhs!" />						
					</td>					
				</tr>
				<tr>
					<th scope="row">이름</th>
					<td>
						<input type="text" id="adminName" name="adminName" class="wdp_90" value="시트론"/>												
					</td>					
				</tr>
				<tr>
					<th scope="row">핸드폰</th>
					<td>						
						<select id="phone1" name="phone1" style="width:30%">
							<option value="000">선택</option>
							<option value="010">010</option>
							<option value="011"selected>011</option>
							<option value="016">016</option>
							<option value="019">019</option>
						</select> -
						<input type="text" id="phone2" name="phone2" style="width:20%" value="3791"/> -
						<input type="text" id="phone3" name="phone3" style="width:20%" value="0635"/>
						<input type="hidden" id="phoneNumber" name="phoneNumber" class="wdp_90" value="010-3791-0635"/>												
					</td>					
				</tr>
				<tr>
					<th scope="row">이메일</th>
					<td>
						<input type="text" id="email" name="email" class="wdp_90" value="citron@nate.com"/>												
					</td>					
				</tr>
			</tbody>
		</table>		
		<br/><br/>
		
		<a href="#this" class="btn" id="save">저장</a>
		<a href="#this" class="btn" id="saveEnc">암호화 저장</a>		
		<a href="#this" class="btn" id="list">목록으로</a>
	</form>
	<form id="encform" name="encform">
		<input type="hidden" id="encAdminGroupId" name="adminGroupId" />
		<input type="hidden" id="encAdminId" name="adminId" />
		<input type="hidden" id="encPassword" name="password" />
		<input type="hidden" id="encAdminName" name="adminName" />
		<input type="hidden" id="encPhoneNumber" name="phoneNumber" />
		<input type="hidden" id="encEmail" name="email" />
	</form>
	
	<script type="text/javascript">
		$(document).ready(function(){ 
			$("#save").on("click", function(e) {
				e.preventDefault();
				save();
			});
			
			$("#saveEnc").on("click", function(e) {
				e.preventDefault();
				getPublicKey();
			});
						
			$("#list").on("click", function(e) {
				e.preventDefault();
				openBoardList();
			});
			
		});
	</script>
