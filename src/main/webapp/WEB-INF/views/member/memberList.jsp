<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/include-taglib.jspf" %>
<%@ include file="/WEB-INF/include/include-list.jspf" %>
<%@ page import="com.dinens.citron.p.admin.common.constants.Constants" %>

<script type="text/javascript">

	//세션 읽어오기
	var searchData = {};
	readSession("member.cond");

	$(document).ready(function() {		
		$("#btn-search").on("click", function(e) {
			e.preventDefault();
			
			if (validatorObj.customValidate()) {
				goSearch();
			}
		});
		
		$("#btn-excel").on("click", function(e) {				
			e.preventDefault();
			
			if (validatorObj.customValidate()) {
				downloadExcel("/member/memberListExcel",$("#frm").serialize());			
			}
		});
		
		// 코드 설정
		var setter = $("#frm").codeSetter({			
			targets: {
				condMemberStatus: {					
					codeId: '<%=Constants.Code.MEMBER_STATUS%>',
					blankOption: {
						name: "<spring:message code='ui.common.label.all'/>",	//전체
						value: ""
					},
					selected: searchData.condMemberStatus
				},
				condIdType: {					
					codeId: '<%=Constants.Code.MEMBER_TYPE%>',
					blankOption: {
						name: "<spring:message code='ui.common.label.all'/>",	//전체
						value: ""
					},
					selected: searchData.condIdType
				}
			}
		});
				
		// jqGrid Wrapper 설정
		$.jqGridWrapper({
			url: "/member/ajaxMemberList.json",			
			colNames:  [
						"<spring:message code='ui.member.label.memberId'/>",		// 아이디 
						"<spring:message code='ui.member.label.memberName'/>", 		// 이름
						"<spring:message code='ui.member.label.IdType'/>", 			// 회원유형
						"<spring:message code='ui.member.label.memberStatus'/>", 	// 상태
						"<spring:message code='ui.member.label.joinDate'/>"			// 가입일
						],
			colModel: [
						{ name: 'memberId',            index: 'memberId',            width: 200,  align: 'left'   },
						{ name: 'memberName',          index: 'memberName',          width: 320,  align: 'left'   },
						{ name: 'idTypeName',          index: 'idTypeName',          width: 80,   align: 'center' },
						{ name: 'memberStatusName',    index: 'memberStatusName',    width: 80,   align: 'center' },
						{ name: 'joinTime',            index: 'joinTime',            width: 150,  align: 'center' }
				],
			onSelectRow: function(rowid) {			
				var memberId = $("#jqGrid").getRowData(rowid).memberId;
				goDetail(memberId);
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
			},
			customHandler: function(form) {
				if ( !isNull($("#condSearchStr").val()) && isNull($("#condSearchType").val()) ) {					
					var label = $("#condSearchType").data('label');
					Alert("<spring:message code='ui.common.alert.require' arguments='" + label + "' />", function() {
						$("#condSearchType").focus();
					});					
					return false;
				}	
				return true;
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
				
		// jqGrid 조회
		var formData = $("#frm").serializeObject();		
		$("#jqGrid").setGridParam({'page': nowPage}).setGridParam({'postData': formData}).trigger('reloadGrid');
	}
				
	// 상세화면 이동
	function goDetail(memberId) {
		// 세션 저장하기
		writeSession("member.cond");
		
		var comSubmit = new commonSubmit();
		comSubmit.setUrl("<c:url value='/member/memberDetail'/>");
		comSubmit.addParam("memberId", memberId);
		comSubmit.submit();
	}	
</script>

<!-- 조회 조건 -->
<div class="row well search-group form-inline">
	<form id="frm" name="frm">
		<input type="hidden" id="nowPage" name="nowPage" value="">
		<input type="hidden" id="rowNum" name="rowNum" value="">
		<div class="form-group">
			<select class="form-control" id="condSearchType" name="condSearchType" data-label="<spring:message code='ui.common.label.searchType'/>">
				<option value="" selected><spring:message code="ui.common.label.select"/></option><!-- 선택 -->
				<option value="id"><spring:message code="ui.common.label.id"/></option><!-- ID -->
				<option value="name"><spring:message code="ui.common.label.name"/></option><!-- 이름 -->
			</select> 
			<input type="text" class="form-control" id="condSearchStr"	name="condSearchStr" maxlength="20" data-label="<spring:message code='ui.common.label.searchString'/>" placeholder="<spring:message code='ui.common.label.searchString'/>"><!-- 검색어 -->
		</div>
		<div class="form-group">
			<label for="exampleInputName2"><spring:message code="ui.member.label.IdType"/></label><!-- ID 유형 --> 
			<select	class="form-control" style="width:110px" id="condIdType" name="condIdType">				
			</select>
		</div>
		<div class="form-group">
			<label for="exampleInputName2"><spring:message code="ui.member.label.memberStatus"/></label><!-- 회원 상태 -->
			<select class="form-control" style="width:110px" id="condMemberStatus" name="condMemberStatus">				
			</select>
		</div>
		<div class="form-group"">
			<label for="exampleInputName2"><spring:message code="ui.common.label.createDate"/></label><!-- 등록일자 -->
			<input type="text" class="form-control datePicker w-150"  id="startDate" name="startDate" data-label="<spring:message code='ui.common.label.createDate'/>" placeholder="From">
			<input type="text" class="form-control datePicker w-150"  id="endDate" name="endDate" data-label="<spring:message code='ui.common.label.createDate'/>" placeholder="To">
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
