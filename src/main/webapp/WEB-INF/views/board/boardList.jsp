<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/include-taglib.jspf" %>
<%@ include file="/WEB-INF/include/include-list.jspf" %>

<script type="text/javascript">
	
	// 세션 읽어오기
	var searchData = {};
	readSession("board.cond");
	
	$(document).ready(function() {
				
		$("#btn-regist").on("click", function(e) {
			e.preventDefault();
			goRegist();
		});
		
		$("#btn-search").on("click", function(e) {
			e.preventDefault();
			
			if (validatorObj.customValidate()) {				
				goSearch();
			}							
		});
		
		$("#btn-excel").on("click", function(e) {				
			e.preventDefault();
			
			if (validatorObj.customValidate()) {
				downloadExcel("/board/boardListExcel",$("#frm").serialize());
			}				
		});
		
		// jqGrid Wrapper 설정
		$.jqGridWrapper({
			url: "/board/ajaxBoardList.json",			
			colNames:  [
						"<spring:message code='ui.common.label.no'/>",			// 번호
						"<spring:message code='ui.common.label.title'/>", 		// 제목
						"<spring:message code='ui.common.label.hitCount'/>",	// 조회수 
						"<spring:message code='ui.common.label.createId'/>",	// 작성자 
						"<spring:message code='ui.common.label.createDate'/>"	// 작성일
						],
			colModel: [
						{ name: 'boardId',     index: 'boardId',     width: 100,  align: 'center', hidden: true },
						{ name: 'title',       index: 'title',       width: 420,  align: 'left'   },
						{ name: 'hitCount',    index: 'hitCount',    width: 80,   align: 'center' },
						{ name: 'createId',    index: 'createId',    width: 80,   align: 'center' },
						{ name: 'createTime',  index: 'createTime',  width: 150,  align: 'center' }
				],
			onSelectRow: function(rowid) {			
				var boardId = $("#jqGrid").getRowData(rowid).boardId;
				goDetail(boardId);
			}
		});
		
		// Validation Rule 설정
		var validatorObj = $("#frm").validate({			
			rules: {
				startDate: {					
					lessThanEqualDate: endDate
			    },
			    endDate: {
				   withDate: startDate
				}
			}
		});
		
		// 페이지 로딩 시, 검색조건(searchData) 설정
		setCondSearch();
	});
	
	// 검색
	function goSearch() {
		// 조건검색 시 페이지는 1로 초기화
		var nowPage = 1;
		$("#nowPage").val(nowPage);
				
		//jqGrid 조회
		var formData = $("#frm").serializeObject();		
		$("#jqGrid").setGridParam({'page': nowPage}).setGridParam({'postData': formData}).trigger('reloadGrid');
		
	}
		
	// 등록화면 이동
	function goRegist() {
		var comSubmit = new commonSubmit();
		comSubmit.setUrl("<c:url value='/board/boardRegist'/>");
		comSubmit.submit();
	}
	
	// 상세화면 이동
	function goDetail(boardId) {
		// 세션 저장하기
		writeSession("board.cond");
				
		var comSubmit = new commonSubmit();
		comSubmit.setUrl("<c:url value='/board/boardDetail'/>");
		comSubmit.addParam("boardId", boardId);
		comSubmit.submit();
	}	
</script>

<!-- 조회 조건 -->
<div class="row well search-group form-inline">
	<form id="frm" name="frm">
		<input type="hidden" id="nowPage" name="nowPage" value="">
		<input type="hidden" id="rowNum" name="rowNum" value="">
		<div class="form-group">
			<label for="exampleInputName2"><spring:message code="ui.common.label.title"/></label> <!-- 제목 -->
			<input type="hidden" id="condSearchType" name="condSearchType" value="title">
			<input type="text" class="form-control" id="condSearchStr" name="condSearchStr" maxlength="20" placeholder="<spring:message code='ui.common.label.searchString'/>"><!-- 검색어 -->
		</div>
		<div class="form-group">
			<label for="exampleInputName2"><spring:message code="ui.common.label.createDate"/></label><!-- 등록일자 --> 
			<input type="text" class="form-control datePicker w-150" id="startDate" name="startDate" data-label="<spring:message code='ui.common.label.createDate'/>" placeholder="From">
			<input type="text" class="form-control datePicker w-150" id="endDate" name="endDate" data-label="<spring:message code='ui.common.label.createDate'/>" placeholder="To">
		</div>
		<a href="#" class="btn btn-secondary" role="button" id="btn-search"><spring:message code="ui.common.button.search"/></a><!-- 조회 -->
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
