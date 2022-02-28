<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/include-taglib.jspf" %>

<script type="text/javascript">
$(function() {
	// 영문 입력 방지 (한글은 안됨)
	$("#phoneNumber2").on('keypress', function(event){		
		if((event.keyCode<48)||(event.keyCode>57)) event.preventDefault();		
	});
	
	// 영문 입력 방지 (한글은 안됨)
	$("#phoneNumber3").on('keypress', function(event){
		if((event.keyCode<48)||(event.keyCode>57)) event.preventDefault();
	});	

	$("#btn-list").on("click", function(e) {
		e.preventDefault();
		goAdminList();
	});

	$("#btn-regist").on("click", function(e) {
		e.preventDefault();
		if (validatorObj.customValidate()) registConfirm();			
	});

	$("#btn-dup-check").on("click", function(e) {
		e.preventDefault();
		if(idCheck($("#adminId").val())){
			isDuplication();
		} else {
			Alert("<spring:message code='ui.admin.alert.input.idRule'/>", function(){$("#adminId").focus()});	// 아이디는 6~20자의 영문 대소문자와 숫자로만 입력 하세요."
		}
	});
	
	// Validation Rule 설정
	var validatorObj = $("#frm").validate({
		rules: {
			adminId: {required: true},
			password: {required: true,minlength: 8},
			passwordConfirm: {required: true,equalTo: password},
			adminName: {required: true,minlength: 2},
			adminGroupId: {required: true},
			useYn: {required: true},
			phoneNumber2: {minlength: 3,digits: true},
			phoneNumber3: {minlength: 4,digits: true},
		    email: {email: true}
		},
		customHandler: function(form) {
			if(!idCheck($("#adminId").val())){
				Alert("<spring:message code='ui.admin.alert.input.idRule'/>", function(){$("#adminId").focus()});	// 아이디는 6~20자의 영문 대소문자와 숫자로만 입력 하세요."
				return false;
			}
			if(!passwdCheck($("#password").val())){
				Alert("<spring:message code='ui.admin.validation.password.pattern'/>", function(){$("#password").focus()});	// 비밀번호는 문자, 숫자, 특수문자 중 2가지 이상으로 구성하여야 합니다.
				return false;
			}
			return true;
		}
	});
});

function idCheck(id){
	var re = /^[a-zA-Z0-9]{6,20}$/;
	if(re.test(id))
		return true;
	return false;
}

function passwdCheck(password) {
	// 공백 체크
	if(password.search(/\s/) != -1) {		
		return false;
	}
	
	// 숫자, 문자, 특수문자 패턴 체크
	var numbers = /[0-9]/; 								// 숫자 
	var alphabets = /[a-zA-Z]/;							// 문자
	var specials = /[<{~!@#$%^&*()_+\?|}>]/;			// 특수문자
	                
	var score = 0;
	
	if (numbers.test(password)) score++;	
	if (alphabets.test(password)) score++;	
	if (specials.test(password)) score++;	
	
	if(score < 2) {		
		return false; 
	}
		
    return true;
}

function goAdminList() {
	var comSubmit = new commonSubmit();
	comSubmit.setUrl("<c:url value='/admin/adminList'/>");
	comSubmit.submit();
}

function registConfirm() {
	Confirm("<spring:message code='ui.common.label.regist'/>",
			$("#adminId").val()+"</br></br>" + "<spring:message code='ui.common.alert.action.regist'/>",
			true,
			adminRegist);
}

function adminRegist() {
	$("#phoneNumber").val($("#phoneNumber1").val()+"-"+$("#phoneNumber2").val()+"-"+$("#phoneNumber3").val());
	var form = $("#frm").serialize();
	$.ajax({
		url: "<c:url value='/admin/ajaxAdminRegist.json'/>",
		type: "POST",
		data: form,
		dataType: "json",
		success: function(data) {
			//console.log("data : " + JSON.stringify(data));
			if(data.status == "OK") {
				goAdminList();
			} else {
				Alert(data.message);
			}
		}
	});
}

function isDuplication() {
	var form = {
			adminId: $("#adminId").val()
		}
	$.ajax({
		url: "<c:url value='/admin/ajaxDuplicateIdCheck.json'/>",
		type: "POST",
		data: form,
		dataType: "json",
		success: function(data) {
			if(data)
				Alert("<spring:message code='ui.common.alert.duplicteId'/>")
			else
				Alert("<spring:message code='ui.common.alert.availableId'/>")
		}
	});
}

</script>

<!-- 컨텐츠 영역 -->
<div class="row">
	<form id="frm">
	<table class="table table-bordered table-info">
		<colgroup>
			<col class="col-xs-2" />
			<col class="col-xs-4" />
			<col class="col-xs-2" />
			<col class="col-xs-4" />
		</colgroup>
		<tbody>
			<tr>
				<th><spring:message code="ui.common.label.id"/><em class="fc-red">*</em></th><!-- ID -->
				<td colspan="3">
					<div class="col-xs-9">
						<input type="text" class="form-control" id="adminId" name="adminId" data-label="<spring:message code='ui.common.label.id'/>" maxlength="20" autofocus>
					</div>
					<div class="col-xs-3">
						<a href="#" class="btn btn-default" id="btn-dup-check"><spring:message code='ui.common.button.dupCheck'/></a><!-- 중복 확인 -->
					</div>
				</td>
			</tr>
			<tr>
				<th><spring:message code='ui.common.label.password'/><em class="fc-red">*</em></th>
				<td>
					<input type="password" class="form-control" id="password" name="password" data-label="<spring:message code='ui.common.label.password'/>" maxlength="20" >
				</td>
				<th><spring:message code='ui.admin.label.password.confirm' /><em class="fc-red">*</em></th>
				<td><input type="password" class="form-control" id="passwordConfirm" name="passwordConfirm" data-label="<spring:message code='ui.admin.label.password.confirm'/>" maxlength="20"></td>
			</tr>
			<tr>
				<th><spring:message code='ui.common.label.name'/><em class="fc-red">*</em></th>
				<td><input type="text" class="form-control" id="adminName" name="adminName" data-label="<spring:message code='ui.common.label.name'/>" maxlength="50"></td>
				<th><spring:message code='ui.admingroup.label.adminGroup'/><em class="fc-red">*</em></th>
				<td>
					<div class="form-inline">
						<select class="form-control" id="adminGroupId" name="adminGroupId" data-label="<spring:message code='ui.admingroup.label.adminGroup'/>">
							<option value=""><spring:message code='ui.common.label.select'/></option> <!-- 선택 -->
						  <c:forEach items="${adminGroupList}" var="row">
							<option value="${row.adminGroupId}">${row.adminGroupName}</option>
						  </c:forEach>
						</select>
					</div>
				</td>
			</tr>
			<tr>
				<th><spring:message code='ui.admin.label.phoneNumber'/></th>
				<td>
					<div class="form-inline">
						<input type="hidden" id="phoneNumber" name="phoneNumber" data-label="<spring:message code='ui.admin.label.phoneNumber'/>">
						<select class="form-control" id="phoneNumber1">
							<option value=""><spring:message code='ui.common.label.select'/></option> <!-- 선택 -->
							<option value="010">010</option>
							<option value="011">011</option>
							<option value="016">016</option>
							<option value="017">017</option>
							<option value="018">018</option>
							<option value="019">019</option>
						</select>
						 - 
						<input type="text" class="form-control w-70" id="phoneNumber2" name="phoneNumber2" data-label="<spring:message code='ui.admin.label.phoneNumber'/>" maxlength="4" />
						 - 
						<input type="text" class="form-control w-70" id="phoneNumber3" name="phoneNumber3" data-label="<spring:message code='ui.admin.label.phoneNumber'/>" maxlength="4" />
					</div>
				</td>
				<th><spring:message code='ui.admin.label.email'/></th>
				<td><input type="email" class="form-control" id="email" name="email" data-label="<spring:message code='ui.admin.label.email'/>" placeholder="<spring:message code='ui.admin.label.email'/>" maxlength="100"></td>
			</tr>
			<tr>
				<th><spring:message code='ui.common.label.useYn'/><em class="fc-red">*</em></th>
				<td colspan="3">
					<div class="form-inline">
						<select class="form-control" id="useYn" name="useYn" data-label="<spring:message code='ui.common.label.useYn'/>">
							<option value=""><spring:message code='ui.common.label.select'/></option> <!-- 선택 -->
							<option value="Y"><spring:message code='ui.common.label.use'/></option>
							<option value="N"><spring:message code='ui.common.label.useNot'/></option>
						</select>
					</div>
				</td>
			</tr>
		</tbody>
	</table>
	</form>
</div>

<!-- 버튼 영역 -->
<div class="row btn-group-area">
	<div class="col-xs-6 text-left">
		<a href="#" class="btn btn-default" role="button" id="btn-list"><spring:message code='ui.common.button.list'/></a>
	</div>
	<div class="col-xs-6 text-right">
		<a href="#" class="btn btn-primary" role="button" id="btn-regist"><spring:message code='ui.common.button.save'/></a>
	</div>
</div>
