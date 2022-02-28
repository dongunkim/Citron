<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/include-taglib.jspf" %>
<link rel="stylesheet" href="/lib/jstree-3.3.8/themes/default/style.min.css" />
<script src="/lib/jstree-3.3.8/jstree.min.js"></script>

<script type="text/javascript">
var id = 'root';
var depth = 1;
$(function () {
	// Validation Rule 설정
	var rules =  {
			menuName: {required: true},
			displayOrder: {required: true, digits: true},
			useYn: {required: true}
		}
	var validatorRegist = $("#frmRegist").validate({
		rules: rules,
		customHandler: customValidation
	});
	// Validation Rule 설정
	var validatorModify = $("#frmModify").validate({
		rules: rules,
		customHandler: customValidation
	});
	
	$("#btn-regist").on("click", function(e) {
		e.preventDefault();
		if (validatorRegist.customValidate()) menuRegist();			
	});

	$("#btn-modify").on("click", function(e) {				
		e.preventDefault();
		if (validatorModify.customValidate()) menuModify();			
	});

	$("#frmRegist #screenUri").attr("disabled",true);
    $("#frmRegist #screenLinkYn").change(function() {
        if(this.checked) {
        	$("#frmRegist #screenUri").attr("disabled",false);
        } else {
        	$("#frmRegist #screenUri").attr("disabled",true);
        }
    });
    
    $("#frmModify #screenLinkYn").change(function() {
        if(this.checked) {
        	$("#frmModify #screenUri").attr("disabled",false);
        } else {
        	$("#frmModify #screenUri").attr("disabled",true);
        }
    });
    
	$('#jstree_menu').jstree({
	    plugins : [ "json_data", "contextmenu", "wholerow", "types"],
	    core : {
	    	multiple: false,
			data : {
				url : "/menu/ajaxMenuList.json" }
	    },
	    types : {
				"default" : {
					"valid_children" : ["default","leaf"]
				},
				"leaf" : {
					icon : "glyphicon glyphicon-file",
					valid_children : []
				}
		},
		contextmenu:{
			items: customMenu
		}
	})
	.bind('select_node.jstree', selectMenu)
	.bind("ready.jstree", function (event, data) {
	     $(this).jstree("open_all");
	});
});

function customValidation(form) {
	if ($(form.screenLinkYn).prop("checked") == true && $(form.screenUri).val() == "") {
		var label = $(form.screenUri).data('label');					
		Alert("<spring:message code='ui.common.alert.require' arguments='" + label + "' />",function(){$(form.screenUri).focus();});	// 필수 항목입니다.
		return false;
	}

	if ($(form.menuNameKr).val() == "") {
		var label = $(form.menuNameKr).data('label');					
		Alert("<spring:message code='ui.common.alert.require' arguments='" + label + "' />",function(){$(form.menuNameKr).focus();});	// 필수 항목입니다.
		return false;
	}

	if ($(form.menuNameEn).val() == "") {
		var label = $(form.menuNameKr).data('label');					
		Alert("<spring:message code='ui.common.alert.require' arguments='" + label + "' />",function(){$(form.menuNameEn).focus();});	// 필수 항목입니다.
		return false;
	}

	return true;
}

function customMenu(node) {
    var items = {
		"createItem": {
			"label": '<spring:message code="ui.common.label.regist"/>', // 등록
			"action": function (data) {
				popupMenuRegist(node);
			}
		},
		"modifyItem": {
			"label": '<spring:message code="ui.common.label.modify"/>', //수정
			"action": function (data) {
				popupMenuModify(node);
			}
		},
		"deleteItem": {
			"label": '<spring:message code="ui.common.label.delete"/>', //삭제
			"action": function (data) {
				if(node.children != "") {
					Alert('<spring:message code="ui.menu.desc.subMenuExist"/>') // 하위 메뉴가 존재합니다.
					return;
				}
				Confirm('<spring:message code="ui.common.label.delete"/>',
						node.text + " [ " + node.id + " ]</br></br>" + '<spring:message code="ui.common.alert.action.delete"/>',
						true,
						deleteMenu); //삭제하시겠습니까?
			}
		}
    };
    
    if(node.id == 'root') {
        delete items.modifyItem;
        delete items.deleteItem;
    }
    if(node.type == 'leaf') {
        delete items.createItem;
    }

    return items;
}

function selectMenu(event, data){
    id = data.node.id;
    depth = data.node.parents.length-1;

    if(id == 'root'){
    	$("#info").hide();
    	$("#noData").show();
    }else{
    	menuDetail(id);
    	$("#info").show();
    	$("#noData").hide();
    }
}

function menuDetail(menuId) {		
	var form = { menuId: menuId }
	$.ajax({
		url: "<c:url value='/menu/ajaxMenuDetail.json'/>",
		type: "POST",
		data: form,
		dataType: "json",
		success: function(data) {
			//console.log("data \t:\n" + JSON.stringify(data));
			
			displayMenuDetail(data);				
		}
	});
}
function displayMenuDetail(data){
	$("#frmDetail #menuIdText").text(data.menuId);
	$("#frmDetail #menuName").val(data.menuName);
	$("#frmDetail #menuNameKrText").text(data.menuNameLocale[0]);
	$("#frmDetail #menuNameEnText").text(data.menuNameLocale[1]);
	$("#frmDetail #upperMenuId").val(data.upperMenuId);
	$("#frmDetail #upperMenuNameText").text(data.upperMenuName);
	$("#frmDetail #screenLinkYnText").text(data.screenLinkYn);
	$("#frmDetail #screenUriText").text(data.screenUri);
	$("#frmDetail #depthText").text(data.depth);
	$("#frmDetail #displayOrderText").text(data.displayOrder);
	$("#frmDetail #useYnText").text(data.useYn);
	$("#frmDetail #createIdText").text(data.createId);
	$("#frmDetail #createTimeText").text(data.createTime);
	$("#frmDetail #updateIdText").text(data.updateId);
	$("#frmDetail #updateTimeText").text(data.updateTime);
}

function popupMenuRegist(node) {
	$("#frmRegist")[0].reset();
	if(depth+1 == 3){
		$("#frmRegist #screenLinkYn").prop("checked",true);
		$("#frmRegist #screenLinkYn").attr("disabled",true);
		$("#frmRegist #screenUri").attr("disabled",false);
	}
	if(id=='root')
		$("#frmRegist #upperMenuNameText").text('');
	else
		$("#frmRegist #upperMenuNameText").text($("#frmDetail #menuNameText").text());
	
	$("#frmRegist #depthText").text(depth+1);
	$('#menu_regist').modal();
	$("#frmRegist #menuName").focus();
}
function menuRegist() {		
	$("#frmRegist #upperMenuId").val(id);
	$("#frmRegist #menuName").val($("#frmRegist #menuNameKr").val());
	$("#frmRegist #depth").val(depth+1);
	$("#frmRegist #screenLinkYn").attr("disabled",false);
	var form = $("#frmRegist").serialize();
	$.ajax({
		url: "<c:url value='/menu/ajaxMenuRegist.json'/>",
		type: "POST",
		data: form,
		dataType: "json",
		success: function(data) {
			//console.log("data \t:\n" + JSON.stringify(data));
			if(data.status == "OK") {
				$('#menu_regist').modal('hide');
				location.reload();
			} else {
				Alert(data.message);
			}
		}
	});
}

function popupMenuModify(node){
	$("#frmModify #menuId").val(id);
	$("#frmModify #menuIdText").text(id);
	$("#frmModify #menuNameKr").val($("#frmDetail #menuNameKrText").text());
	$("#frmModify #menuNameEn").val($("#frmDetail #menuNameEnText").text());
	$("#frmModify #upperMenuId").val($("#frmDetail #upperMenuId").val());
	$("#frmModify #upperMenuNameText").text($("#frmDetail #upperMenuNameText").text());
	
	if(depth == 3 || node.children != ""){
		$("#frmModify #screenLinkYn").attr("disabled",true);
		$("#frmModify #screenUri").attr("disabled",false);
	} else {
		$("#frmModify #screenLinkYn").attr("disabled",false);
		$("#frmModify #screenUri").attr("disabled",true);
	}

	if($("#frmDetail #screenLinkYnText").text() == "Y") {
		$("#frmModify #screenLinkYn").prop("checked",true);
		$("#frmModify #screenUri").attr("disabled",false);
	}
	else {
		$("#frmModify #screenLinkYn").prop("checked",false);
		$("#frmModify #screenUri").attr("disabled",true);
	}
	
	$("#frmModify #screenUri").val($("#frmDetail #screenUriText").text());
	$("#frmModify #depthText").text($("#frmDetail #depthText").text());
	$("#frmModify #displayOrder").val($("#frmDetail #displayOrderText").text());
	$("#frmModify #useYn").val($("#frmDetail #useYnText").text());

	$('#menu_modify').modal();
}
function menuModify() {
	$("#frmModify #menuName").val($("#frmModify #menuNameKr").val());
	$("#frmModify #screenLinkYn").attr("disabled",false);
	var form = $("#frmModify").serialize();
	$.ajax({
		url: "<c:url value='/menu/ajaxMenuModify.json'/>",
		type: "POST",
		data: form,
		dataType: "json",
		success: function(data) {
			//console.log("data \t:\n" + JSON.stringify(data));
			if(data.status == "OK") {
				$('#menu_modify').modal('hide');
				location.reload();
			} else {
				Alert(data.message);
			}
		}
	});
}

function deleteMenu() {		
	var form = {
		menuId: id
	}
	$.ajax({
		url: "<c:url value='/menu/ajaxMenuDelete.json'/>",
		type: "POST",
		data: form,
		dataType: "json",
		success: function(data) {
			//console.log("data \t:\n" + JSON.stringify(data));
			location.reload();
		}
	});
}
</script>
<style>
.table-info>tbody>tr>th, 
.table-info>tbody>tr>td {
    height: 45px;
}
</style>

<!-- 컨텐츠 영역 -->
<div class="row">
	<form id="frmDetail">
	<input type="hidden" id="upperMenuId" name="upperMenuId">
	<input type="hidden" id="menuName" name="menuName">
	<div class="col-xs-4 menu-tree mh-586">
		<div class="tree-body" id="jstree_menu"></div>
	</div>
	<div class="col-xs-8 table-right">
		<div id="info" style="display: none;">
			<table class="table table-bordered table-info">
				<colgroup>
					<col class="col-xs-4" />
					<col class="col-xs-8" />
				</colgroup>
				<tbody>
					<tr>
						<th><spring:message code="ui.common.label.id"/></th><!-- ID -->
						<td id="menuIdText"></td>
					</tr>
					<tr>
						<th><spring:message code="ui.common.label.name"/>(한국어)</th><!-- 이름 -->
						<td id="menuNameKrText"></td>
					</tr>
					<tr>
						<th><spring:message code="ui.common.label.name"/>(English)</th><!-- 이름 -->
						<td id="menuNameEnText"></td>
					</tr>
					<tr>
						<th><spring:message code="ui.menu.label.upperMenu"/></th><!-- 상위 메뉴 -->
						<td id="upperMenuNameText"></td>
					</tr>
					<tr>
						<th><spring:message code="ui.menu.label.screenLinkYn"/></th><!-- 화면 연결 여부 -->
						<td id="screenLinkYnText"></td>
					</tr>
					<tr>
						<th><spring:message code="ui.menu.label.screenUri"/></th>
						<td id="screenUriText"></td><!-- 화면 URI -->
					</tr>
					<tr>
						<th><spring:message code="ui.common.label.depth"/></th><!-- 깊이 -->
						<td id="depthText"></td>
					</tr>
					<tr>
						<th><spring:message code="ui.common.label.displayOrder"/></th><!-- 표시 순서 -->
						<td id="displayOrderText"></td>
					</tr>
					<tr>
						<th><spring:message code="ui.common.label.useYn"/></th><!-- 사용 여부 -->
						<td id="useYnText"></td>
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
		<div id="noData" class="no-select-menu" style="display: block;">
			<p class="no-select-menu-text"><spring:message code="ui.menu.desc.selectMenu"/></p><!-- 메뉴를 선택하세요. -->
		</div>
	</div>
	</form>
</div>
<!-- 컨텐츠 영역 끝 -->

<!-- 메뉴 등록 팝업 -->
<div class="modal" id="menu_regist" tabindex="-1">
	<form id="frmRegist" name="frmRegist">
	<input type="hidden" id="upperMenuId" name="upperMenuId">
	<input type="hidden" id="depth" name="depth">
	<input type="hidden" id="menuName" name="menuName">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				<h4 class="modal-title"><spring:message code="ui.common.label.regist"/></h4><!-- 등록 -->
			</div>
			<div class="modal-body">
				<table class="table table-bordered table-info">
					<colgroup>
						<col class="col-xs-4" />
						<col class="col-xs-8" />
					</colgroup>
					<tbody>
						<tr>
							<input type="hidden" id="localeCodeKr" name="localeCode" value="ko">
							<th><spring:message code="ui.common.label.name"/>(한국어)<em class="fc-red">*</em></th><!-- 이름 -->
							<td><input class="form-control" id="menuNameKr" name="menuNameLocale" data-label="<spring:message code='ui.common.label.name'/>" maxlength="50"></td>
						</tr>
						<tr>
							<input type="hidden" id="localeCodeEn" name="localeCode" value="en">
							<th><spring:message code="ui.common.label.name"/>(English)<em class="fc-red">*</em></th><!-- 이름 -->
							<td><input class="form-control" id="menuNameEn" name="menuNameLocale" data-label="<spring:message code='ui.common.label.name'/>" maxlength="50"></td>
						</tr>
						<tr>
							<th><spring:message code="ui.menu.label.upperMenu"/></th><!-- 상위 메뉴 -->
							<td id="upperMenuNameText"></td>
						</tr>
						<tr>
							<th><spring:message code="ui.menu.label.screenLinkYn"/><em class="fc-red">*</em></th><!-- 화면 연결 여부 -->
							<td>
								<div class="checkbox">
									<label> <input type="checkbox" id="screenLinkYn" name="screenLinkYn" value="Y"> <spring:message code="ui.menu.label.link"/></label><!-- 연결 -->
								</div>
							</td>
						</tr>
						<tr>
							<th><spring:message code="ui.menu.label.screenUri"/><em class="fc-red"></th><!-- 화면 URI -->
							<td><input class="form-control" id="screenUri" name="screenUri" data-label="<spring:message code='ui.menu.label.screenUri'/>" maxlength="100"></td>
						</tr>
						<tr>
							<th><spring:message code="ui.common.label.depth"/></th><!-- 깊이 -->
							<td id="depthText"></td>
						</tr>
						<tr>
							<th><spring:message code="ui.common.label.displayOrder"/><em class="fc-red">*</em></th><!-- 표시 순서 -->
							<td><input class="form-control" id="displayOrder" name="displayOrder" data-label="<spring:message code='ui.common.label.displayOrder'/>" maxlength="3"></td>
						</tr>
						<tr>
							<th><spring:message code='ui.common.label.useYn'/><em class="fc-red">*</em></th>
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
				<a role="button" class="btn btn-outline-primary" data-dismiss="modal"><spring:message code="ui.common.button.cancel"/></a><!-- 취소 -->
				<a href="#" role="button" class="btn btn-primary" id="btn-regist"><spring:message code="ui.common.button.regist"/></a><!-- 등록 -->
			</div>
		</div>
	</div>
	</form>
</div>

<!-- 메뉴 수정 팝업 -->
<div class="modal" id="menu_modify" tabindex="-1">
	<form id="frmModify" name="frmModify">
	<input type="hidden" id="menuId" name="menuId">
	<input type="hidden" id="upperMenuId" name="upperMenuId">
	<input type="hidden" id="menuName" name="menuName">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				<h4 class="modal-title"><spring:message code="ui.common.label.modify"/></h4><!-- 수정 -->
			</div>
			<div class="modal-body">
				<table class="table table-bordered table-info">
					<colgroup>
						<col class="col-xs-4" />
						<col class="col-xs-8" />
					</colgroup>
					<tbody>
						<tr>
							<th><spring:message code="ui.common.label.id"/></th><!-- ID -->
							<td id="menuIdText"></td>
						</tr>
						<tr>
							<input type="hidden" id="localeCodeKr" name="localeCode" value="ko">
							<th><spring:message code="ui.common.label.name"/>(한국어)<em class="fc-red">*</em></th><!-- 이름 -->
							<td><input type="text" class="form-control" id="menuNameKr" name="menuNameLocale" data-label="<spring:message code='ui.common.label.name'/>(한국어)" maxlength="50"></td>
						</tr>
						<tr>
							<input type="hidden" id="localeCodeEn" name="localeCode" value="en">
							<th><spring:message code="ui.common.label.name"/>(English)<em class="fc-red">*</em></th><!-- 이름 -->
							<td><input type="text" class="form-control" id="menuNameEn" name="menuNameLocale" data-label="<spring:message code='ui.common.label.name'/>(English)" maxlength="50"></td>
						</tr>
						<tr>
							<th><spring:message code="ui.menu.label.upperMenu"/></th><!-- 상위 메뉴 -->
							<td id="upperMenuNameText"></td>
						</tr>
						<tr>
							<th><spring:message code="ui.menu.label.screenLinkYn"/><em class="fc-red">*</em></th><!-- 화면 연결 여부 -->
							<td>
								<div class="checkbox">
									<label> <input type="checkbox" id="screenLinkYn" name="screenLinkYn" value="Y"> <spring:message code="ui.menu.label.link"/></label><!-- 연결 -->
								</div>
							</td>
						</tr>
						<tr>
							<th><spring:message code="ui.menu.label.screenUri"/></th><!-- 화면 URI -->
							<td><input type="text" class="form-control" id="screenUri" name="screenUri" data-label="<spring:message code='ui.menu.label.screenUri'/>" maxlength="100"></td>
						</tr>
						<tr>
							<th><spring:message code="ui.common.label.depth"/></th><!-- 깊이 -->
							<td id="depthText"></td>
						</tr>
						<tr>
							<th><spring:message code="ui.common.label.displayOrder"/><em class="fc-red">*</em></th><!-- 표시 순서 -->
							<td><input type="text" class="form-control" id="displayOrder" name="displayOrder" data-label="<spring:message code='ui.common.label.displayOrder'/>" maxlength="3"></td>
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
				<a role="button" class="btn btn-outline-primary" data-dismiss="modal"><spring:message code="ui.common.button.cancel"/></a><!-- 취소 -->
				<a href="#" role="button" class="btn btn-primary" id="btn-modify"><spring:message code="ui.common.button.modify"/></a><!-- 수정 -->
			</div>
		</div>
	</div>
	</form>
</div>
