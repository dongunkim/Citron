<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/include-taglib.jspf" %>
<link rel="stylesheet" href="/lib/jstree-3.3.8/themes/default/style.min.css" />
<script src="/lib/jstree-3.3.8/jstree.min.js"></script>

<script type="text/javascript">
$(function () {
	$("#btn-list").on("click", function(e) {
		e.preventDefault();
		goAdminGroupList();
	});

	$("#btn-modify").on("click", function(e) {
		e.preventDefault();
		goAdminGroupModify();
	});

	$("#btn-delete").on("click", function(e) {
		e.preventDefault();
		Confirm("<spring:message code='ui.common.label.delete'/>", // 삭제
				"${info.adminGroupName} [ " + "${info.adminGroupId}" + " ]</br></br>" + "<spring:message code='ui.common.alert.action.delete'/>", // 삭제 하시겠습니까?
				true,adminGroupDelete);
	});

	$('#jstree_menu').jstree({
	    "plugins" : [ "json_data", "wholerow", "types", "checkbox"],
		core : {
			data : {
				url : "/admingroup/ajaxAdminGroupAuthList.json",
				data : {
					"adminGroupId" : "${info.adminGroupId}"					
				}
			} 
		},
	    "types" : {
			"default" : {
				"valid_children" : ["default","leaf"]
			},
			"leaf" : {
				"icon" : "glyphicon glyphicon-file",
				"valid_children" : []
			}
		}
	})
	.bind("ready.jstree", function (event, data) {
		$(this).jstree("open_all");
		
		var jsonNodes = $('#jstree_menu').jstree(true).get_json('#', {'flat': true});
		$.each(jsonNodes, function (i, node) {
			var checkYn = node.data["0"];
			if(node.type == "leaf" && checkYn == "Y"){
				$(this).jstree().check_node(node.id);
			}
			$(this).jstree().disable_node(node.id);
		});
	})

});

function goAdminGroupList() {
	var comSubmit = new commonSubmit();
	comSubmit.setUrl("<c:url value='/admingroup/adminGroupList'/>");
	comSubmit.submit();
}

function goAdminGroupModify() {
	var adminGroupId = '${info.adminGroupId}';
	var comSubmit = new commonSubmit();
	comSubmit.setUrl("<c:url value='/admingroup/adminGroupModify'/>");
	comSubmit.addParam("adminGroupId", adminGroupId);
	comSubmit.submit();
}

function adminGroupDelete() {
	var form = {
		adminGroupId: "${info.adminGroupId}"
	}
	$.ajax({
		url: "<c:url value='/admingroup/ajaxAdminGroupDelete.json'/>",
		type: "POST",
		data: form,
		dataType: "json",
		success: function(data) {
			//console.log("data : " + JSON.stringify(data));
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
				<th><spring:message code='ui.common.label.id'/></th><!-- ID -->
				<td>${info.adminGroupId}</td>
				<th><spring:message code='ui.common.label.name'/></th><!-- 이름 -->
				<td>${info.adminGroupName}</td>
			</tr>
			<tr>
				<th><spring:message code='ui.common.label.useYn'/></th><!-- 사용 여부 -->
				<td colspan="3">${info.useYn}</td>
			</tr>
			<tr>
				<th><spring:message code='ui.common.label.createInfo'/></th><!-- 등록 정보 -->
				<td>${info.createTime} ( ${info.createId} )</td>
				<th><spring:message code='ui.common.label.updateInfo'/></th><!-- 수정 정보 -->
				<td>${info.updateTime}<c:if test="${fn:length(info.updateId) > 0}"> ( ${info.updateId} )</c:if></td>
			</tr>
		</tbody>
	</table>
	<p class="sub-item-header"><spring:message code='ui.admingroup.label.accessableMenu'/></p><!-- 접근 가능한 메뉴 -->
	<div class="col-xs-12 menu-tree mh-300">
		<div class="tree-body" id="jstree_menu"></div>
	</div>
</div>

<!-- 버튼 영역 -->
<div class="row btn-group-area">
	<div class="col-xs-6 text-left">
		<a href="#" class="btn btn-default" role="button" id="btn-list"><spring:message code='ui.common.button.list'/></a><!-- 목록 -->
	</div>
	<div class="col-xs-6 text-right">
		<a href="#" class="btn btn-secondary" role="button" id="btn-delete"><spring:message code='ui.common.button.delete'/></a><!-- 삭제 -->
		<a href="#" class="btn btn-primary" role="button" id="btn-modify"><spring:message code='ui.common.button.modify'/></a><!-- 수정 -->
	</div>
</div>
