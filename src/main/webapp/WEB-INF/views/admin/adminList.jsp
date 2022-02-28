<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/include-taglib.jspf" %>
<%@ include file="/WEB-INF/include/include-list.jspf" %>

<script type="text/javascript">

// 세션 읽어오기
var searchData = {};
readSession("admin.cond");

$(function() {
	$("#btn-regist").on("click", function(e) {
		e.preventDefault();
		goAdminRegister();
	});
	
	$("#btn-search").on("click", function(e) {
		e.preventDefault();
		search();			
	});

	$("#btn-excel").on("click", function(e) {				
		e.preventDefault();
		downloadExcel("/admin/excelAdminList",$("#frm").serialize());
	});

	$.jqGridWrapper({
		url: "/admin/ajaxAdminList.json",
		colNames: [
				"<spring:message code='ui.common.label.id'/>",
				"<spring:message code='ui.common.label.name'/>",
				"<spring:message code='ui.admingroup.label.adminGroup'/>",
				"<spring:message code='ui.admin.label.phoneNumber'/>",
				"<spring:message code='ui.admin.label.email'/>",
				"<spring:message code='ui.common.label.useYn'/>"
			],
		colModel: [
				{ name: 'adminId',        index: 'adminId',        width: 100, align:"left" },
				{ name: 'adminName',      index: 'adminName',      width: 300, align:"left"   },
				{ name: 'adminGroupName', index: 'adminGroupName', width: 150, align:"left" },
				{ name: 'phoneNumber',    index: 'phoneNumber',    width: 150, align:"left" },
				{ name: 'email',          index: 'email',          width: 150, align:"left" },
				{ name: 'useYn',          index: 'useYn',          width: 100, align:"center" }
			],
		onSelectRow: function(rowid) {
			var adminId = $("#jqGrid").getRowData(rowid).adminId;
			goAdminDetail(adminId);
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

function goAdminRegister() {
	writeSession("admin.cond");
	var comSubmit = new commonSubmit();
	comSubmit.setUrl("<c:url value='/admin/adminRegist'/>");
	comSubmit.submit();
}

function goAdminDetail(adminId) {
	writeSession("admin.cond");
	var comSubmit = new commonSubmit();
	comSubmit.setUrl("<c:url value='/admin/adminDetail'/>");
	comSubmit.addParam("adminId", adminId);
	comSubmit.submit();
}
</script>


<!-- 조회 조건 -->
<div class="row well search-group form-inline">
	<form id="frm">
	<input type="hidden" id="nowPage" name="nowPage" value="">
	<input type="hidden" id="rowNum" name="rowNum" value="">
	<div class="form-group">
		<label for="condAdminGroupId"><spring:message code="ui.admingroup.label.adminGroup"/></label><!-- 관리자 그룹 -->
		<select class="form-control" id="condAdminGroupId" name="condAdminGroupId">
			<option value="" selected><spring:message code="ui.common.label.all"/></option><!-- 전체 -->
		  <c:forEach items="${adminGroupList}" var="row">
			<option value="${row.adminGroupId}">${row.adminGroupName}</option>
		  </c:forEach>
		</select>
	</div>
	<div class="form-group">
		<select class="form-control" id="condSearchType" name="condSearchType">
			<option value="id" selected><spring:message code="ui.common.label.id"/></option><!-- ID -->
			<option value="name"><spring:message code="ui.common.label.name"/></option><!-- 이름 -->
		</select>
		<input type="text" class="form-control" id="condSearchStr" name="condSearchStr" placeholder="<spring:message code="ui.common.label.searchString"/>" maxlength="20">
	</div>
	<a href="#" class="btn btn-secondary" role="button" id="btn-search"><spring:message code="ui.common.button.search"/></a>
	</form>
</div>

<!-- 조회 결과 -->
<!-- Total 건수 및 데이터 다운로드 -->
<div class="row">
	<div class="col-xs-6 table-total">
		<p>
			<spring:message code="ui.common.label.total"/> <span id="totalCount"></span> <spring:message code="ui.common.label.count"/>
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
