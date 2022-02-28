<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/include-header.jspf" %>

<script src="/js/jquery.fileDownload.js"></script>

<script type="text/javascript">

	var localeVal = "<%=session.getAttribute("locale")%>";
	if (localeVal == "null") localeVal = "ko";	
	
	function openBoardWrite() {
		var comSubmit = new commonSubmit();
		comSubmit.setUrl("<c:url value='/sample/openBoardWrite'/>");
		comSubmit.submit();
	}
	
	function openBoardDetail(idx) {
		var checkVal = $("input:radio[name='submit_type']:checked").val();
				
		if (checkVal == "FORM")
			formBasedBoardDetail(idx);
		else
			ajaxBasedBoardDetail(idx);		
	}
	
	function formBasedBoardDetail(idx) {
		var comSubmit = new commonSubmit();
		comSubmit.setUrl("<c:url value='/sample/openBoardDetail'/>");
		comSubmit.addParam("idx", idx);
		comSubmit.submit();
	}
	
	function ajaxBasedBoardDetail(idx) {		
		var form = {
				idx: idx
		}
		$.ajax({
			url: "<c:url value='/sample/restOpenBoardDetail.json'/>",
			type: "POST",
			data: form,
			dataType: "json",
			success: function(data) {
				console.log("data \t:\n" + JSON.stringify(data));
				
				displayBoardDetail(data);				
			},
			error: function(e) {
				alert("처리 중 오류가 발생했습니다.\n에러코드 : " + e.status);
				
				console.log("Error \t: " + JSON.stringify(e));
				console.log("Error Status \t: " + e.status);				
			}
		});
	}
	
	function displayBoardDetail(data) {
		var html = 
			"<br/><br/>" + 
			"<table class=\"board_view\">" +
			"	<colgroup>" +
			"		<col width=\"15%\"/>" +
			"		<col width=\"35%\"/>" +
			"		<col width=\"15%\"/>" +
			"		<col width=\"35%\"/>" +
			"	</colgroup>" +
			"	<caption>게시글 상세</caption>" +
			"	<tbody>" +
			"		<tr>" +
			"			<th scope=\"row\">글 번호</th>" +
			"			<td>" + data.boardVO.idx + "</td>" +
			"			<th scope=\"row\">조회수</th>" +
			"			<td>" + data.boardVO.hitCnt + "</td>" +
			"		</tr>" +
			"		<tr>" +
			"			<th scope=\"row\">작성자</th>" +
			"			<td>" + data.boardVO.creaId + "</td>" +
			"			<th scope=\"row\">작성시간</th>" +
			"			<td>" + data.boardVO.creaDtm + "</td>" +
			"		</tr>" +
			"		<tr>" +
			"			<th scope=\"row\">제목</th>" +
			"			<td colspan=\"3\">" + data.boardVO.title + "</td>" +
			"		</tr>" +
			"		<tr>" +
			"			<td colspan=\"4\">" + data.boardVO.contents.replace(/\n/gi, '<br/>') + "</td>" +
			"		</tr>" +
			"		<tr>" +
			"			<th scope=\"row\">첨부파일</th>" +
			"			<td colspan=\"3\">";
			
			if (data.list.length > 0) {
				for(var i=0; i<data.list.length; i++) {
					html += "<p>" +
							"<input type=\"hidden\" id=\"idx\" value=\"" + data.list[i].idx + "\" />" +
							"<a href=\"#this\" name=\"file\">" + data.list[i].originalFileName + "</a>" +
							" (" + data.list[i].fileSize + " kb)" +
							"<p/>";
				}
			}
			else {
				html +="첨부파일이 없습니다.";
						
			}
			
			html += "			</td>" +
					"		</tr>" +
					"	</tbody>" +
					"</table>" ;
						
			$("#boardInfo").empty();
			$("#boardInfo").append(html);
			
			$("a[name='file']").on("click", function(e) {
				e.preventDefault();
				downloadFile($(this));
			});
			
			$("#boardInfo").show();
	}
	
	function downloadFile(obj) {
		var idx = obj.parent().find("#idx").val();		
		var comSubmit = new commonSubmit();
		comSubmit.setUrl("<c:url value='/common/downloadFile'/>");
		comSubmit.addParam("idx", idx);			
		comSubmit.submit();
	}
	
	function changeLocale(obj) {		
		var locale = obj.val(); 
		var comSubmit = new commonSubmit();
		comSubmit.setUrl("<c:url value='/sample/openBoardList'/>");
		comSubmit.addParam("lang", locale);	
		comSubmit.submit();
	}
	
	function downloadExcel() {				
		var preparingFileModal = $("#preparing-file-modal");
		
		preparingFileModal.dialog({modal: true});		
		$("#progressbar").progressbar({value: false});
				
		$.fileDownload("/sample/excelBoardList", {
			successCallback: function(url) {
				preparingFileModal.dialog('close');	
			},
			
			failCallback: function(responseHtml, url) {
				console.log(responseHtml);
				preparingFileModal.dialog('close');
				$("#error-modal").dialog({modal: true});
			}
		});
	}
	
	function goSearch() {
		//var postData = $("#jqGrid").getGridParam('postData');
		
		// 필요한 값만 추가
		var searchData = {
				condSearchType : $("#searchType").val(),
				condSearchStr : $("#searchStr").val()
		};
		var nowPage = 1;
						
		// page는 searchData로 변경이 안되어 직접 값을 변경함
		// 아마도 searchData는 기존 postData에 추가되는 형태인 것 같으며,
		// JqGrid의 내장 변수값은 필요시 직접 변경해야 하는 것으로 보임 
		$("#jqGrid").setGridParam({'page': nowPage}).setGridParam({'postData': searchData}).trigger('reloadGrid');
	}
	
	
</script>

	<h2><spring:message code="UI.sample.list.title"/></h2>	
	<table style="width:99%; margin-left:5px;">		
			<tr>				
				<td>
					<select id="locale" name="locale" style="font-size:12pt; width: 100px; height:30px;">
						<option value="ko" selected>한국어</option>
						<option value="en">English</option>
					</select>
				</td>
				<td align="right">
					<input type="radio" id="submit_type" name="submit_type" value="FORM">Form (Page 전환)
					<input type="radio" id="submit_type" name="submit_type" value="AJAX" checked>Ajax (하단 View)
				</td>
				<td></td>
				<td>
					<button id="btn-excel">엑셀 다운</button>
				</td>				
			</tr>		
	</table>
	<br/>
	
	<table style="width:99%; margin-left:5px;">
			<tr>
				<td>
					<p>
						<select id="searchType" name="searchType" style="font-size:12pt; width: 100px; height:30px;">
							<option value="">검색대상</option>
							<option value="title">제목</option>
							<option value="content">내용</option>
						</select>					
						<input type="text" id="searchStr" name="searchStr" style="height:30px;" />
						<a href="#this" class="btn" id="search">검색</a>
					</p>
				</td>
			</tr>
	</table>
	<br/>
		
	<div style="width: 96%; margin:5px 5px 5px 5px;">
    	<table id="jqGrid"></table>
    	<div id="jqGridPager"></div>
	</div>
	<br/>
	<a href="#this" class="btn" id="write" style="margin-left:5px;"><spring:message code="UI.sample.common.button.write"/></a>
	<br/>
	<div id="boardInfo" style="display:hidden; margin-left:5px;"></div>
	
	<!-- 파일 생성중 보여질 진행막대를 포함하고 있는 다이얼로그 입니다. --> 
	<div title="Data Download" id="preparing-file-modal" style="display: none;"> 
		<div id="progressbar" style="width: 100%; height: 22px; margin-top: 20px;"></div> 
	</div> 

	<!-- 에러발생시 보여질 메세지 다이얼로그 입니다. --> 
	<div title="Error" id="error-modal" style="display: none;"> 
		<p>생성실패.</p> 
	</div>	

	<script type="text/javascript">
		$(document).ready(function() {
			$("#write").on("click", function(e) {
				e.preventDefault();
				openBoardWrite();
			});
			
			$("#search").on("click", function(e) {
				e.preventDefault();
				goSearch();
			});			
			
			$("#locale").on("change", function(e) {
				e.preventDefault();
				changeLocale($(this));
			});
			
			$("#btn-excel").on("click", function(e) {				
				e.preventDefault();				
				downloadExcel();
			});
			
			$("#jqGrid").jqGrid({
				url: "/sample/restBoardList.json",
				mtype: "POST",
				datatype: "json",				
				autowidth: true,
				height: 'auto',
				rowNum: 15,
				rowList: [10, 15, 20, 30],				
				pager: "#jqGridPager",
				rownumbers: true,
				viewrecords: true,
				caption: false,
				sortable: false,
				loadonce: false,
				colNames:  ["번호", "제목", "조회수", "작성자"],
				colModel: [
						{ name: 'idx',     index: 'idx',     width: 78,  align:"center", hidden: true },
						{ name: 'title',   index: 'title',   width: 429, align:"left"   },
						{ name: 'hitCnt',  index: 'hitCnt',  width: 117, align:"center" },
						{ name: 'creaDtm', index: 'creaDtm', width: 156, align:"center" }					      
					],
				prmNames: { page: 'nowPage', rows: 'countPerPage' },
				gridComplete: function() {					
				},
				loadError: function(jqXHR, textStatus, errorThrown) {
					jqGridLoadErrorHandler($(this), jqXHR, textStatus, errorThrown);
				},
				loadComplete: function(obj) {
					if( obj==undefined ) {
			   			return;
			   		}					
				},
				loadError: function(xhr, status, error) {
					alert("Status : " + xhr.responseJSON.title + "("+ xhr.responseJSON.status + ")" +
							"\nMessage : " + xhr.responseJSON.message +
								"\n시스템 관리자에게 문의 하거나, 잠시후 다시 시도 바랍니다.")
					
					console.log("xhr : ");
					console.log(xhr);					
				},
				jsonReader: {
					root   : function(obj) {return obj.list;},
				    records: function(obj) {return obj.pagingVO.totalCount;},
				    page   : function(obj) {return obj.pagingVO.nowPage;},
				    total  : function(obj) {return obj.pagingVO.totalPage;},
				    repeatitems: false,
				    repeat: false
				},
				onSelectRow: function(rowid) {
					var checkVal = $("input:radio[name='submit_type']:checked").val();
					if (checkVal == 'FORM') $("#jqGrid").resetSelection();
					
					var idx = $("#jqGrid").getRowData(rowid).idx;
					openBoardDetail(idx);					
				},
				onCellSelect: function(rowid, iCol, cellcontent, e) {					
				}				
			});
						
			
			$("#locale").val(localeVal);
		});
		
		// JqGrid 반응형 적용
		// - Div width: 96% 설정
		// - Options [autowidth: true & height: 'auto'] 설정
		$(window).resize(function() {
			$("#jqGrid").setGridWidth($(this).width() * 0.96);
		});
				
	</script>
