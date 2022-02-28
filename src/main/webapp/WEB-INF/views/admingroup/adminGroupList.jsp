<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/include-taglib.jspf" %>
<%@ include file="/WEB-INF/include/include-list.jspf" %>

<script type="text/javascript">
var searchData = {};
readSession("admingroup.cond");
$(function() {
	$("#btn-search").on("click", function(e) {
		e.preventDefault();
		search();
	});

	$("#btn-excel").on("click", function(e) {				
		e.preventDefault();				
		downloadExcel("/admingroup/excelAdminGroupList",$("#frm").serialize());
	});

	$("#btn-regist").on("click", function(e) {				
		e.preventDefault();				
		goAdminGroupRegist();
	});
	
	$.jqGridWrapper({
		url: "/admingroup/ajaxAdminGroupList.json",
		colNames: [
			"<spring:message code='ui.common.label.id'/>",
			"<spring:message code='ui.common.label.name'/>",
			"<spring:message code='ui.common.label.useYn'/>"
		],
		colModel: [
			{ name: 'adminGroupId',   index: 'adminGroupId',   width: 100, align:"left" },
			{ name: 'adminGroupName', index: 'adminGroupName', width: 300, align:"left"   },
			{ name: 'useYn',          index: 'useYn',          width: 100, align:"center" }
		],
		onSelectRow: function(rowid) {
			var adminGroupId = $("#jqGrid").getRowData(rowid).adminGroupId;
			goAdminGroupDetail(adminGroupId);
		}
	});

	// 페이지 로딩 시, 검색조건(searchData) 설정
	setCondSearch();
});

function search() {
	var nowPage = 1;
	$("#nowPage").val(nowPage);
	
	var formData = $("#frm").serializeObject();		
	$("#jqGrid").setGridParam({'page': nowPage}).setGridParam({'postData': formData}).trigger('reloadGrid');
}

function goAdminGroupDetail(adminGroupId) {
	writeSession("admingroup.cond");
	var comSubmit = new commonSubmit();
	comSubmit.setUrl("<c:url value='/admingroup/adminGroupDetail'/>");
	comSubmit.addParam("adminGroupId", adminGroupId);
	comSubmit.submit();
}

function goAdminGroupRegist() {
	writeSession("admingroup.cond");
	var comSubmit = new commonSubmit();
	comSubmit.setUrl("<c:url value='/admingroup/adminGroupRegist'/>");
	comSubmit.submit();
}
</script>

<!-- 조회 조건 -->
<div class="row well search-group form-inline">
	<form id="frm">
	<input type="hidden" id="nowPage" name="nowPage" value="">
	<input type="hidden" id="rowNum" name="rowNum" value="">
	<div class="form-group">
		<label for="condAdminGroupName"><spring:message code="ui.common.label.name"/></label><!-- 이름 -->
		<input type="text" class="form-control" id="condAdminGroupName" name="condAdminGroupName" placeholder="<spring:message code="ui.common.label.searchString"/>" maxlength="20">
	</div>
	<a href="#" class="btn btn-secondary" role="button" id="btn-search"><spring:message code="ui.common.button.search"/></a><!-- 조회 -->
	</form>
</div>

<!-- 조회 결과 -->
<!-- Total 건수 및 데이터 다운로드 -->
<div class="row">
	<div class="col-xs-6 table-total">
		<p>
			<spring:message code="ui.common.label.total"/> <span id="totalCount"></span> <spring:message code="ui.common.label.count"/><!-- 총 xx건 -->
		</p>
	</div>
	<div class="col-xs-6 text-right btn-group-download">
		<a href="#" class="btn btn-outline-primary btn-sm" id="btn-excel"><spring:message code="ui.common.button.excel"/></a><!-- 엑셀 -->
	</div>
</div>

<!-- 데이터 목록 -->
<div class="row">
	<div>
	   	<table id="jqGrid"></table>
	   	<div id="jqGridPager"></div>
	</div>
</div>

<!-- 버튼 영역 -->
<div class="row btn-group-area">
	<div class="col-xs-12 text-right">
		<a href="#" class="btn btn-primary" role="button" id="btn-regist"><spring:message code="ui.common.button.regist"/></a><!-- 등록 -->
	</div>
</div>
