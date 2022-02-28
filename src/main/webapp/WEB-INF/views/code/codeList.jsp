<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/include-taglib.jspf" %>
<%@ include file="/WEB-INF/include/include-list.jspf" %>

<script type="text/javascript">
var searchData = {};
var codeId = "";
$(function() {
	
	$("input[name='displayOrder']").on('keypress', function(event){		
		if((event.keyCode<48)||(event.keyCode>57)) event.preventDefault();		
	});

	var codeRules = {
			codeId: {required: true},
			codeName: {required: true},
			useYn: {required: true}
		}
	var codeValidator = $("#frmCodeRegist").validate({
		rules: codeRules,
		customHandler: function(form) {
			if(!idCheck($("#frmCodeRegist #codeId").val())){
				Alert("<spring:message code='ui.code.alert.input.idRule'/>",function(){$("#frmCodeRegist #codeId").focus();});	// ID는 2~4자의 영문 대소문자와 숫자로만 입력 하세요."
				return false;
			}
			return true;
		}
	});

	$("#btn-code-regist-pop").on("click", function(e) {
		e.preventDefault();
		popupCodeRegist();
	});
	
	$("#btn-code-regist").on("click", function(e) {
		e.preventDefault();
		if (codeValidator.customValidate()) codeRegist();			
	});
	
	$("#btn-code-modify-pop").on("click", function(e) {
		e.preventDefault();
		popupCodeModify();
	});
	
	$("#btn-code-modify").on("click", function(e) {
		e.preventDefault();
		if ($("#frmCodeModify").validate({rules: codeRules}).customValidate()) codeModify();			
	});
	
	$("#btn-code-delete").on("click", function(e) {
		e.preventDefault();
		var codeName = $("#frmCodeDetail #codeNameText").text();
		Confirm("<spring:message code='ui.common.label.delete'/>",			// 삭제
				codeName + " [ " + codeId + " ] " + "</br></br>" + "<spring:message code='ui.common.alert.action.delete'/>",	// 삭제하시겠습니까? 
				true, codeDelete);
	});

	var valueRules = {
			value: {required: true},
			name: {required: true},
			displayOrder: {required: true, digits: true},
			useYn: {required: true}
		}
	var valueValidator = $("#frmValueRegist").validate({
		rules: valueRules,
		customHandler: function(form) {
			if(!valueCheck($("#frmValueRegist #value").val())){
				Alert("<spring:message code='ui.code.alert.input.valueRule'/>",function(){$("#frmValueRegist #value").focus();});	// 값은 1~4자의 영문 대소문자와 숫자로만 입력 하세요."
				return false;
			}

			if($("#frmValueRegist #nameKr").val()==''){
				var label = $(form.nameKr).data('label');
				Alert("<spring:message code='ui.common.alert.require' arguments='" + label + "' />",function(){$(form.nameKr).focus();});	// 필수 항목입니다.
				return false;
			}

			if($("#frmValueRegist #nameEn").val()==''){
				var label = $(form.nameKr).data('label');
				Alert("<spring:message code='ui.common.alert.require' arguments='" + label + "' />",function(){$(form.nameEn).focus();});	// 필수 항목입니다.
				return false;
			}

			return true;
		}
	});

	$("#btn-value-regist-pop").on("click", function(e) {
		e.preventDefault();
		popupValueRegist();
	});
	
	$("#btn-value-regist").on("click", function(e) {
		e.preventDefault();
		if (valueValidator.customValidate()) valueRegist();			
	});

	$("#btn-value-modify").on("click", function(e) {
		e.preventDefault();
		if ($("#frmValueModify").validate({rules: valueRules}).customValidate()) valueModify();			
	});

	$.jqGridNoPagingWrapper("#jqGrid",{
		url: "/code/ajaxCodeList.json",			
		height: 530,
		colNames:  [
					"<spring:message code='ui.code.label.codeId'/>",	// 코드 ID
					"<spring:message code='ui.common.label.name'/>", 	// 이름
					"<spring:message code='ui.common.label.useYn'/>",	// 사용여부 
					],
		colModel: [
					{ name: 'codeId',   index: 'codeId',   width: 180,  align: 'center'},
					{ name: 'codeName', index: 'codeName', width: 300,  align: 'left'   },
					{ name: 'useYn',    index: 'useYn',    width: 180,  align: 'center' }
			],
		loadComplete: function(obj) {
			if( obj==undefined ) {
	   			return;
	   		}
			
			if(obj.list.length > 0){
				$('#jqGrid').jqGrid('setSelection',1);
			}
		},
		onSelectRow: function(rowid) {			
			codeId = $("#jqGrid").getRowData(rowid).codeId;
			codeDetail(codeId);
			searchValueList(codeId);
		}
	});

	$.jqGridNoPagingWrapper("#gridValueList",{
		datatype : 'local',
		height: 223,
		colNames:  [
					"<spring:message code='ui.code.label.codeId'/>",		// 코드 ID
					"<spring:message code='ui.code.label.value'/>",			// 값
					"<spring:message code='ui.common.label.name'/>", 		// 이름
					"<spring:message code='ui.common.label.displayOrder'/>",// 표시 순서 
					"<spring:message code='ui.common.label.useYn'/>",		// 사용 여부 
					"<spring:message code='ui.common.label.delete'/>"		// 삭제 
					],
		colModel: [
					{ name: 'codeId',   index: 'codeId',   align: 'center', hidden: true },
					{ name: 'value', index: 'value', width: 200,  align: 'center'   },
					{ name: 'name', index: 'name', width: 200,  align: 'left'   },
					{ name: 'displayOrder', index: 'displayOrder', width: 150,  align: 'center'   },
					{ name: 'useYn',    index: 'useYn',    width: 150,  align: 'center' },
					{ width: 150,  align: 'center', formatter: delButton }
			],
		onSelectRow: function(rowid) {			
			var codeId = $("#gridValueList").getRowData(rowid).codeId;
			var value = $("#gridValueList").getRowData(rowid).value;
			valueDetail(codeId,value);					
		}
	});
	$("#gridValueList").jqGrid('setGridParam',{
		url:'/code/ajaxCodeValueList.json',
		datatype: "json"
	});
});

function idCheck(id){
	var re = /^[a-zA-Z0-9]{2,4}$/;
	if(re.test(id))
		return true;
	return false;
}

function valueCheck(id){
	var re = /^[a-zA-Z0-9]{1,4}$/;
	if(re.test(id))
		return true;
	return false;
}

// 버튼 생성
function delButton (cellvalue, options, rowObject) {
	return "<input type=\"button\" onclick=\"popupValueDelete(" + rowObject['codeId'] + "," + rowObject['value'] + "," + rowObject['name'] + ")\" value=\"<spring:message code='ui.common.label.delete'/>\" />";
}

//검색
function codeDetail(codeId) {
	var form = { codeId: codeId }
	$.ajax({
		url: "<c:url value='/code/ajaxCodeDetail.json'/>",
		type: "POST",
		data: form,
		dataType: "json",
		success: function(data) {
			//console.log("data \t:\n" + JSON.stringify(data));
			displayCodeDetail(data);				
		}
	});
}
function displayCodeDetail(data){
	$("#frmCodeDetail #codeIdText").text(data.codeId);
	$("#frmCodeDetail #codeNameText").text(data.codeName);
	var description = data.description.replace(/(\n|\r\n)/g, '<br>');
	$("#frmCodeDetail #descriptionText").html(description);
	$("#frmCodeDetail #useYnText").text(data.useYn);
	$("#frmCodeDetail #createIdText").text(data.createId);
	$("#frmCodeDetail #createTimeText").text(data.createTime);
	$("#frmCodeDetail #updateIdText").text(data.updateId);
	$("#frmCodeDetail #updateTimeText").text(data.updateTime);
}

function popupCodeRegist() {
	$('#code_regist').modal();
	$("#frmCodeRegist #codeId").focus();
}
function codeRegist(){
	var form = $("#frmCodeRegist").serialize();
	$.ajax({
		url: "<c:url value='/code/ajaxCodeRegist.json'/>",
		type: "POST",
		data: form,
		dataType: "json",
		success: function(data) {
			//console.log("data \t:\n" + JSON.stringify(data));
			if(data.status == "OK") {
				$('#code_regist').modal('hide');
				location.reload();
			} else {
				Alert(data.message,function(){$("#frmCodeRegist #codeId").focus();});
			}
		}
	});
}

function codeDelete(){
	var form = {codeId:codeId};
	$.ajax({
		url: "<c:url value='/code/ajaxCodeDelete.json'/>",
		type: "POST",
		data: form,
		dataType: "json",
		success: function(data) {
			//console.log("data \t:\n" + JSON.stringify(data));
			if(data.status == "OK") {
				location.reload();
			} else {
				Alert(data.message);
			}
		}
	});
}

function popupCodeModify(){
	$("#frmCodeModify #codeIdText").text($("#frmCodeDetail #codeIdText").text());
	$("#frmCodeModify #codeName").val($("#frmCodeDetail #codeNameText").text());
	var description = $("#frmCodeDetail #descriptionText").html().replace(/(<br>|<br\/>|<br \/>)/g, '\r\n');
	$("#frmCodeModify #description").val(description);
	$("#frmCodeModify #useYn").val($("#frmCodeDetail #useYnText").text());

	$('#code_modify').modal();
	$("#frmCodeModify #codeName").focus();
}
function codeModify(){
	$("#frmCodeModify #codeId").val(codeId);
	var form = $("#frmCodeModify").serialize();
	$.ajax({
		url: "<c:url value='/code/ajaxCodeModify.json'/>",
		type: "POST",
		data: form,
		dataType: "json",
		success: function(data) {
			//console.log("data \t:\n" + JSON.stringify(data));
			if(data.status == "OK") {
				$('#code_modify').modal('hide');
				location.reload();
			} else {
				Alert(data.message);
			}
		}
	});
}

function searchValueList(codeId) {
	//console.log("codeId : " + codeId);
	$("#gridValueList").setGridParam({'postData': {codeId:codeId}}).trigger('reloadGrid');
}

function popupValueRegist() {
	$("#frmValueRegist")[0].reset();
	$("#frmValueRegist #codeIdText").text(codeId);
	$('#value_regist').modal();
	$("#frmValueRegist #value").focus();
}
function valueRegist(){
	$("#frmValueRegist #codeId").val(codeId);
	$("#frmValueRegist #name").val($("#frmValueRegist #nameKr").val());
	var form = $("#frmValueRegist").serialize();
	$.ajax({
		url: "<c:url value='/code/ajaxCodeValueRegist.json'/>",
		type: "POST",
		data: form,
		dataType: "json",
		success: function(data) {
			//console.log("data \t:\n" + JSON.stringify(data));
			if(data.status == "OK") {
				$('#value_regist').modal('hide');
				searchValueList(codeId);
			} else {
				Alert(data.message,function(){$("#frmValueRegist #value").focus();});
			}
		}
	});
}


function valueDetail(codeId,value) {
	//console.log("Code : " + codeId + ", value : " + value);
	var form = { codeId: codeId, value:value }
	$.ajax({
		url: "<c:url value='/code/ajaxCodeValueDetail.json'/>",
		type: "POST",
		data: form,
		dataType: "json",
		success: function(data) {
			//console.log("data \t:\n" + JSON.stringify(data));
			displayCodeValueDetail(data);
		}
	});
}

function displayCodeValueDetail(data){
	$("#frmValueModify #codeId").val(data.codeId);
	$("#frmValueModify #codeIdText").text(data.codeId);
	$("#frmValueModify #value").val(data.value);
	$("#frmValueModify #valueText").text(data.value);
	$("#frmValueModify #nameKr").val(data.nameLocale[0]);
	$("#frmValueModify #nameEn").val(data.nameLocale[1]);
	$("#frmValueModify #displayOrder").val(data.displayOrder);
	$("#frmValueModify #description").val(data.description);
	$("#frmValueModify #useYn").val(data.useYn);
	$("#frmValueModify #createIdText").text(data.createId);
	$("#frmValueModify #createTimeText").text(data.createTime);
	$("#frmValueModify #updateIdText").text(data.updateId);
	$("#frmValueModify #updateTimeText").text(data.updateTime);
	
	$("#value_modify").modal();
	$("#frmValueModify #name").focus();
}

function valueModify() {
	$("#frmValueModify #name").val($("#frmValueModify #nameKr").val());
	var form = $("#frmValueModify").serialize();
	$.ajax({
		url: "<c:url value='/code/ajaxCodeValueModify.json'/>",
		type: "POST",
		data: form,
		dataType: "json",
		success: function(data) {
			//console.log("data \t:\n" + JSON.stringify(data));
			$('#value_modify').modal('hide');
			searchValueList(codeId);
		}
	});
}

function popupValueDelete(codeId,value,name) {
	Confirm("<spring:message code='ui.common.label.delete'/>",			// 삭제
			name + " [ " + value + " ] " + "</br></br>" + 
			"<spring:message code='ui.common.alert.action.modify'/>",	// 삭제 하시겠습니까? 
			true, function(){valueDelete(codeId,value);}
			);
}
function valueDelete(codeId,value){
	var form = { codeId: codeId, value:value }
	$.ajax({
		url: "<c:url value='/code/ajaxCodeValueDelete.json'/>",
		type: "POST",
		data: form,
		dataType: "json",
		success: function(data) {
			//console.log("data \t:\n" + JSON.stringify(data));
			searchValueList(codeId);
		}
	});
}
</script>
<style>
.table-info.ht-s>tbody>tr>th, 
.table-info.ht-s>tbody>tr>td {
    height: 35px;
}

</style>
<!-- 컨텐츠 영역 -->
<div class="row">
	<div class="col-xs-5">
		<div class="row">
			<div class="col-xs-6">
				<p class="sub-item-header"><spring:message code='ui.code.label.codeList'/></p><!-- 코드 목록 -->
			</div>
			<div class="col-xs-6 text-right">
				<a href="#" class="btn btn-primary" role="button" id="btn-code-regist-pop"><spring:message code='ui.common.button.regist'/></a><!-- 등록 -->
			</div>
		</div>
		<div>
		   	<table id="jqGrid"></table>
 		</div>
	</div>

	<div class="col-xs-7">
		<div class="row">
			<div class="col-xs-6">
				<p class="sub-item-header"><spring:message code='ui.code.label.codeDetail'/></p><!-- 코드 상세 -->
			</div>
			<div class="col-xs-6 text-right">
				<a href="#" class="btn btn-secondary" role="button" id="btn-code-delete"><spring:message code='ui.common.button.delete'/></a><!-- 삭제 -->
				<a href="#" class="btn btn-primary" role="button" id="btn-code-modify-pop"><spring:message code='ui.common.button.modify'/></a><!-- 수정 -->
			</div>
		</div>
		<form id="frmCodeDetail">
		<table class="table table-bordered table-info ht-s">
			<colgroup>
				<col class="col-xs-2" />
				<col class="col-xs-4" />
				<col class="col-xs-2" />
				<col class="col-xs-4" />
			</colgroup>
			<tbody>
				<tr>
					<th><spring:message code='ui.code.label.codeId'/></th><!-- 코드 ID -->
					<td id="codeIdText"></td>
					<th><spring:message code='ui.common.label.name'/></th><!-- 이름 -->
					<td id="codeNameText"></td>
				</tr>
				<tr>
					<th><spring:message code='ui.common.label.description'/></th><!-- 설명 -->
					<td colspan="3"><div id="descriptionText" style="height:80px;overflow-y:auto"></div></td>
				</tr>
				<tr>
					<th><spring:message code='ui.common.label.useYn'/></th><!-- 사용 여부 -->
					<td colspan="3" id="useYnText"></td>
				</tr>
				<tr>
					<th><spring:message code="ui.common.label.createId"/></th><!-- 등록자 ID -->
					<td id="createIdText"></td>
					<th><spring:message code="ui.common.label.createTime"/></th><!-- 등록 시간 -->
					<td id="createTimeText"></td>
				</tr>
				<tr>
					<th><spring:message code="ui.common.label.updateId"/></th><!-- 수정자 ID -->
					<td id="updateIdText"></td>
					<th><spring:message code="ui.common.label.updateTime"/></th><!-- 수정 시간 -->
					<td id="updateTimeText"></td>
				</tr>
			</tbody>
		</table>
		</form>

		<!-- 코드 값 목록 -->
		<div class="row">
			<div class="col-xs-6">
				<p class="sub-item-header"><spring:message code="ui.code.label.valueList"/></p><!-- 값 목록 -->
			</div>
			<div class="col-xs-6 text-right">
				<a href="#" class="btn btn-primary" role="button" id="btn-value-regist-pop"><spring:message code="ui.common.button.regist"/></a><!-- 등록 -->
			</div>
		</div>
		<div>
		   	<table id="gridValueList"></table>
		</div>
	</div>
</div>

<!-- 코드 등록 팝업 -->
<div class="modal" id="code_regist" tabindex="-1">
	<form id="frmCodeRegist">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"> <span aria-hidden="true">&times;</span></button>
				<h4 class="modal-title"><spring:message code="ui.code.label.codeRegist"/></h4><!-- 코드 등록 -->
			</div>
			<div class="modal-body">
				<table class="table table-bordered table-info">
					<colgroup>
						<col class="col-xs-4" />
						<col class="col-xs-8" />
					</colgroup>
					<tbody>
						<tr>
							<th><spring:message code="ui.code.label.codeId"/><em class="fc-red">*</em></th><!-- 코드 ID -->
							<td><input class="form-control" id="codeId" name="codeId" data-label="<spring:message code='ui.code.label.codeId'/>" maxlength="4"></td>
						</tr>
						<tr>
							<th><spring:message code="ui.common.label.name"/><em class="fc-red">*</em></th><!-- 이름 -->
							<td><input class="form-control" id="codeName" name="codeName" data-label="<spring:message code='ui.common.label.name'/>" maxlength="50"></td>
						</tr>
						<tr>
							<th><spring:message code="ui.common.label.description"/></th><!-- 설명 -->
							<td><textarea class="form-control" rows="5" id="description" name="description"></textarea></td>
						</tr>
						<tr>
							<th><spring:message code="ui.common.label.useYn"/><em class="fc-red">*</em></th><!-- 사용 여부 -->
							<td>
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
			</div>
			<div class="modal-footer">
				<a role="button" class="btn btn-default" data-dismiss="modal"><spring:message code='ui.common.button.cancel'/></a><!-- 취소 -->
				<a href="#" role="button" class="btn btn-primary" id="btn-code-regist"><spring:message code='ui.common.button.regist'/></a><!-- 등록 -->
			</div>
		</div>
	</div>
	</form>
</div>

<!-- 코드 수정 팝업 -->
<div class="modal" id="code_modify" tabindex="-1">
	<form id="frmCodeModify">
	<input type="hidden" id="codeId" name="codeId">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				<h4 class="modal-title"><spring:message code="ui.code.label.codeModify"/></h4><!-- 코드 수정 -->
			</div>
			<div class="modal-body">
				<table class="table table-bordered table-info">
					<colgroup>
						<col class="col-xs-4" />
						<col class="col-xs-8" />
					</colgroup>
					<tbody>
						<tr>
							<th><spring:message code="ui.code.label.codeId"/></th><!-- 코드 ID -->
							<td id="codeIdText"></td>
						</tr>
						<tr>
							<th><spring:message code="ui.common.label.name"/><em class="fc-red">*</em></th><!-- 이름 -->
							<td><input class="form-control" id="codeName" name="codeName" data-label="<spring:message code='ui.common.label.name'/>" maxlength="50"></td>
						</tr>
						<tr>
							<th><spring:message code="ui.common.label.description"/></th><!-- 설명 -->
							<td><textarea class="form-control" rows="5" id="description" name="description"></textarea></td>
						</tr>
						<tr>
							<th><spring:message code="ui.common.label.useYn"/><em class="fc-red">*</em></th><!-- 사용 여부 -->
							<td>
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
			</div>
			<div class="modal-footer">
				<a role="button" class="btn btn-default" data-dismiss="modal"><spring:message code="ui.common.button.cancel"/></a><!-- 취소 -->
				<a href="#" role="button" class="btn btn-primary" id="btn-code-modify"><spring:message code="ui.common.button.modify"/></a><!-- 수정 -->
			</div>
		</div>
	</div>
	</form>
</div>

<!-- 값 등록 팝업 -->
<div class="modal" id="value_regist" tabindex="-1">
	<form id="frmValueRegist">
	<input type="hidden" id="codeId" name="codeId">
	<input type="hidden" id="name" name="name">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				<h4 class="modal-title"><spring:message code="ui.code.label.valueRegist"/></h4><!-- 값 등록 -->
			</div>
			<div class="modal-body">
				<table class="table table-bordered table-info">
					<colgroup>
						<col class="col-xs-4" />
						<col class="col-xs-8" />
					</colgroup>
					<tbody>
						<tr>
							<th><spring:message code="ui.code.label.codeId"/></th><!-- 코드 ID -->
							<td id="codeIdText"></td>
						</tr>
						<tr>
							<th><spring:message code="ui.code.label.value"/><em class="fc-red">*</em></th><!-- 값 -->
							<td><input class="form-control" id="value" name="value" data-label="<spring:message code='ui.code.label.value'/>" maxlength="4"></td>
						</tr>
						<tr>
							<input type="hidden" id="localeCodeKr" name="localeCode" value="kr">
							<th><spring:message code="ui.common.label.name"/>(한국어)<em class="fc-red">*</em></th><!-- 이름 -->
							<td><input class="form-control" id="nameKr" name="nameLocale" data-label="<spring:message code='ui.common.label.name'/>" maxlength="50"></td>
						</tr>
						<tr>
							<input type="hidden" id="localeCodeEn" name="localeCode" value="en">
							<th><spring:message code="ui.common.label.name"/>(English)<em class="fc-red">*</em></th><!-- 이름 -->
							<td><input class="form-control" id="nameEn" name="nameLocale" data-label="<spring:message code='ui.common.label.name'/>" maxlength="50"></td>
						</tr>
						<tr>
							<th><spring:message code="ui.common.label.displayOrder"/><em class="fc-red">*</em></th><!-- 표시 순서 -->
							<td><input class="form-control" id="displayOrder" name="displayOrder" data-label="<spring:message code='ui.common.label.displayOrder'/>" maxlength="3"></td>
						</tr>
						<tr>
							<th><spring:message code="ui.common.label.description"/></th><!-- 설명 -->
							<td><textarea class="form-control" rows="5" id="description" name="description"></textarea></td>
						</tr>
						<tr>
							<th><spring:message code="ui.common.label.useYn"/><em class="fc-red">*</em></th><!-- 사용 여부 -->
							<td>
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
			</div>
			<div class="modal-footer">
				<a role="button" class="btn btn-default" data-dismiss="modal"><spring:message code="ui.common.button.cancel"/></a><!-- 취소 -->
				<a href="#" role="button" class="btn btn-primary" id="btn-value-regist"><spring:message code="ui.common.button.regist"/></a><!-- 등록 -->
			</div>
		</div>
	</div>
	</form>
</div>

<!-- 값 수정 팝업 -->
<div class="modal" id="value_modify" tabindex="-1">
	<form id="frmValueModify">
	<input type="hidden" id="codeId" name="codeId">
	<input type="hidden" id="value" name="value">
	<input type="hidden" id="name" name="name">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"> <span aria-hidden="true">&times;</span></button>
				<h4 class="modal-title"><spring:message code="ui.code.label.valueModify"/></h4><!-- 값 수정 -->
			</div>
			<div class="modal-body">
				<table class="table table-bordered table-info">
					<colgroup>
						<col class="col-xs-4" />
						<col class="col-xs-8" />
					</colgroup>
					<tbody>
						<tr>
							<th><spring:message code="ui.code.label.codeId"/></th><!-- 코드 ID -->
							<td id="codeIdText"></td>
						</tr>
						<tr>
							<th><spring:message code="ui.code.label.value"/></th><!-- 값 -->
							<td id="valueText"></td>
						</tr>
						<tr>
							<input type="hidden" id="localeCodeKr" name="localeCode" value="kr">
							<th><spring:message code="ui.common.label.name"/>(한국어)<em class="fc-red">*</em></th><!-- 이름 -->
							<td><input class="form-control" id="nameKr" name="nameLocale" data-label="<spring:message code='ui.common.label.name'/>" maxlength="50"></td>
						</tr>
						<tr>
							<input type="hidden" id="localeCodeEn" name="localeCode" value="en">
							<th><spring:message code="ui.common.label.name"/>(English)<em class="fc-red">*</em></th><!-- 이름 -->
							<td><input class="form-control" id="nameEn" name="nameLocale" data-label="<spring:message code='ui.common.label.name'/>" maxlength="50"></td>
						</tr>
						<tr>
							<th><spring:message code="ui.common.label.displayOrder"/><em class="fc-red">*</em></th><!-- 표시 순서 -->
							<td><input class="form-control" value="3" id="displayOrder" name="displayOrder" data-label="<spring:message code='ui.common.label.displayOrder'/>" maxlength="3"></td>
						</tr>
						<tr>
							<th><spring:message code="ui.common.label.description"/></th><!-- 설명 -->
							<td>
								<textarea class="form-control" rows="5" id="description" name="description"></textarea>
							</td>
						</tr>
						<tr>
							<th><spring:message code="ui.common.label.useYn"/><em class="fc-red">*</em></th><!-- 사용 여부 -->
							<td>
								<div class="form-inline">
									<select class="form-control" id="useYn" name="useYn" data-label="<spring:message code='ui.common.label.useYn'/>">
										<option value=""><spring:message code='ui.common.label.select'/></option> <!-- 선택 -->
										<option value="Y"><spring:message code='ui.common.label.use'/></option>
										<option value="N"><spring:message code='ui.common.label.useNot'/></option>
									</select>
								</div>
							</td>
						</tr>
						<tr>
							<th><spring:message code="ui.common.label.createId"/></th><!-- 등록자 ID -->
							<td id="createIdText"></td>
						</tr>
						<tr>
							<th><spring:message code="ui.common.label.createTime"/></th><!-- 등록 시간 -->
							<td id="createTimeText"></td>
						</tr>
						<tr>
							<th><spring:message code="ui.common.label.updateId"/></th><!-- 수정자 ID -->
							<td id="updateIdText"></td>
						</tr>
						<tr>
							<th><spring:message code="ui.common.label.updateTime"/></th><!-- 수정 시간 -->
							<td id="updateTimeText"></td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="modal-footer">
				<a role="button" class="btn btn-default" data-dismiss="modal"><spring:message code="ui.common.button.cancel"/></a><!-- 취소 -->
				<a href="#" role="button" class="btn btn-primary" id="btn-value-modify"><spring:message code="ui.common.button.modify"/></a><!-- 수정 -->
			</div>
		</div>
	</div>
	</form>
</div>
