<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/include-taglib.jspf" %>
<link rel="stylesheet" href="/lib/jstree-3.3.8/themes/default/style.min.css" />
<script src="/lib/jstree-3.3.8/jstree.min.js"></script>

<script type="text/javascript">
$(function() {
	$("#btn-list").on("click", function(e) {
		e.preventDefault();
		goAdminGroupList();
	});

	$("#btn-regist").on("click", function(e) {
		e.preventDefault();
		if (validatorObj.customValidate()) registConfirm();			
	});

	$('#jstree_menu').jstree({
	    "plugins" : [ "json_data", "wholerow", "types", "checkbox"],
	    core : {
			data : {
				url : "/menu/ajaxMenuList.json" }
	    },
	    "types" : {
				"default" : {
					"valid_children" : ["default","leaf"]
				},
				"leaf" : {
					"icon" : "glyphicon glyphicon-file",
					"valid_children" : []
				}
		},
	    "checkbox" : { 
	        "three_state" : true
	    }
	})
	.bind("ready.jstree", function (event, data) {
	     $(this).jstree("open_all");
	});

	// Validation Rule 설정
	var validatorObj = $("#frm").validate({
		rules: {
			adminGroupName: {required: true},
			useYn: {required: true}
		},
		customHandler: function(form) {
			if($("#jstree_menu").jstree().get_checked().length < 1) {
				Alert("<spring:message code='ui.admingroup.alert.required.menu' />");	// 하나 이상의 메뉴를 선택하세요.
				return false;
			}
			return true;			
		}
	});
});

function goAdminGroupList() {
	var comSubmit = new commonSubmit();
	comSubmit.setUrl("<c:url value='/admingroup/adminGroupList'/>");
	comSubmit.submit();
}

function registConfirm() {
	Confirm("<spring:message code='ui.common.label.regist'/>",
			$("#adminGroupName").val()+"</br></br>" + "<spring:message code='ui.common.alert.action.regist'/>",
			true,adminGroupRegist);
}

function adminGroupRegist() {
	var menuList = $("#jstree_menu").jstree().get_checked();
	for(var i=0 ; i < menuList.length ; i++){
		if(menuList[i] == 'root')
			menuList.splice(i,1);
	}

	$("#jstree_menu").find(".jstree-undetermined").each(function (i, element) {
    	var menuId = $(element).closest('.jstree-node').attr("id");
    	if(menuId != 'root'){
    		menuList += ',' + menuId;
    	}
    });
    $("#menuIds").val(menuList);

    var form = $("#frm").serialize();
	$.ajax({
		url: "<c:url value='/admingroup/ajaxAdminGroupRegist.json'/>",
		type: "POST",
		data: form,
		traditional : true,
		dataType: "json",
		success: function(data) {
			console.log("data : " + JSON.stringify(data));
			if(data.status == "OK") {
				goAdminGroupList();
			} else {
				Alert(data.message);
			}
		}
	});
}
</script>

<!-- 컨텐츠 영역 -->
<div class="row">
	<form id="frm">
	<input type="hidden" id="menuIds" name="menuIds">
	<p class="sub-item-header"><spring:message code='ui.admingroup.label.adminGroup'/></p><!-- 관리자 그룹 -->
	<table class="table table-bordered table-info">
		<colgroup>
			<col class="col-xs-2" />
			<col class="col-xs-4" />
			<col class="col-xs-2" />
			<col class="col-xs-4" />
		</colgroup>
		<tbody>
			<tr>
				<th><spring:message code='ui.common.label.name'/><em class="fc-red">*</em></th><!-- 이름 -->
				<td><input type="text" class="form-control" id="adminGroupName" name="adminGroupName" data-label="<spring:message code='ui.common.label.name'/>" maxlength="50" autofocus></td>
				<th><spring:message code='ui.common.label.useYn'/><em class="fc-red">*</em></th><!-- 사용 여부 -->
				<td>
					<div class="form-inline">
						<select class="form-control" id="useYn" name="useYn" data-label="<spring:message code='ui.common.label.useYn'/>"><!-- 사용 여부 -->
							<option value=""><spring:message code='ui.common.label.select'/></option> <!-- 선택 -->
							<option value="Y"><spring:message code='ui.common.label.use'/></option>
							<option value="N"><spring:message code='ui.common.label.useNot'/></option>
						</select>
					</div>
				</td>
			</tr>
		</tbody>
	</table>
	<p class="sub-item-header"><spring:message code='ui.admingroup.label.accessableMenu'/></p><!-- 접근 가능한 메뉴 -->
	<div class="col-xs-12 menu-tree mh-420">
		<div class="tree-body" id="jstree_menu"></div>
	</div>
	</form>
</div>

<!-- 버튼 영역 -->
<div class="row btn-group-area">
	<div class="col-xs-6 text-left">
		<a href="#" class="btn btn-default" role="button" id="btn-list"><spring:message code='ui.common.button.list'/></a><!-- 목록 -->
	</div>
	<div class="col-xs-6 text-right">
		<a href="#" class="btn btn-primary" role="button" id="btn-regist"><spring:message code='ui.common.button.save'/></a><!-- 저장 -->
	</div>
</div>
