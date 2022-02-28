<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/include-taglib.jspf" %>

<link rel="stylesheet" href="<c:url value='/css/simplePagination.css'/>"/>
<style type="text/css">
.pagination li a.current-select {
  z-index: 2;
  color: #23527c;
  background-color: #eeeeee;
  border-color: #ddd;
}
</style>
<script src="<c:url value="/js/jquery.fileDownload.js"/>"></script>
<script src="<c:url value="/js/jquery.paging.js"/>"></script>

<script type="text/javascript">
	var localeVal = "<%=session.getAttribute("locale")%>";
	if (localeVal == "null") localeVal = "ko";	
	
	function openBoardWrite() {
		var comSubmit = new commonSubmit();
		comSubmit.setUrl("<c:url value='/sample/openBoardWrite'/>");
		comSubmit.submit();
	}
	
	function openBoardDetail(obj) {
		var checkVal = $("input:radio[name='submit_type']:checked").val();
				
		if (checkVal == "FORM")
			formBasedBoardDetail(obj);
		else
			ajaxBasedBoardDetail(obj);		
	}
	
	function formBasedBoardDetail(obj) {
		var comSubmit = new commonSubmit();
		comSubmit.setUrl("<c:url value='/sample/openBoardDetail'/>");
		comSubmit.addParam("idx", obj.parent().find("#idx").val());		
		comSubmit.submit();
	}
	
	function ajaxBasedBoardDetail(obj) {
		var idx = obj.parent().find("#idx").val();
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
	
	function goPage(pageNumber) {
		var comSubmit = new commonSubmit('frm');
		$("#nowPage").val(pageNumber);
		
		comSubmit.setUrl("<c:url value='/sample/openBoardList'/>");		
		comSubmit.submit();
	}
	
	function changeCounter(countNumber) {
		var comSubmit = new commonSubmit('frm');
		$("#countPerPage").val(countNumber);
		
		comSubmit.setUrl("<c:url value='/sample/openBoardList'/>");		
		comSubmit.submit();
	}
</script>

	<form id="frm" name="frm">
		<input type="hidden" id="nowPage" name="nowPage" value="${pagingVO.nowPage}" />
		<input type="hidden" id="countPerPage" name="countPerPage" value="${pagingVO.countPerPage}" />
		<input type="hidden" id="totalCount" name="totalCount" value="${pagingVO.totalCount}" />
	</form>
	<h2><spring:message code="UI.sample.list.title"/></h2>
	<table style="width:99%;">		
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
	<table class="board_list">
		<colgroup>
			<col width="10%" />
			<col width="*" />
			<col width="15%" />
			<col width="20%" />
		</colgroup>
		<thead>
			<tr>
				<th scope="col"><spring:message code="UI.sample.list.table.column.no"/></th>
				<th scope="col"><spring:message code="UI.sample.list.table.column.title"/></th>
				<th scope="col"><spring:message code="UI.sample.list.table.column.hitcount"/></th>
				<th scope="col"><spring:message code="UI.sample.list.table.column.cretime"/></th>
			</tr>
		</thead>
		<tbody>
			<c:choose>
				<c:when test="${fn:length(list) > 0}">
					<c:forEach items="${list}" var="row">
						<tr>
							<td align="center">${row.idx}</td>
							<td class="title">
								<a href="#this" name="title">${row.title}</a>
								<input type="hidden" id="idx" value="${row.idx}" />
							</td>
							<td align="center">${row.hitCnt}</td>
							<td align="center">${row.creaDtm}</td>
						</tr>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<tr>
						<td colspan="4" align="center"><spring:message code="UI.sample.common..error.nodata"/></td>
					</tr>
				</c:otherwise>
			</c:choose>
		</tbody>
	</table>
	<br/>
	<a href="#this" class="btn" id="write"><spring:message code="UI.sample.common.button.write"/></a>
	<div id="pageInfo"></div>
	<!-- 페이징 처리 -->
            
	<div id="boardInfo" style="display:hidden;"></div>
	
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
			
			$("a[name='title']").on("click", function(e) {
				e.preventDefault();				
				openBoardDetail($(this));
			});
			
			$("#locale").on("change", function(e) {
				e.preventDefault();
				changeLocale($(this));
			});
			
			$("#btn-excel").on("click", function(e) {				
				e.preventDefault();				
				downloadExcel();
			});
			
			$("#pageInfo").pagination({
				totalCount: "${pagingVO.totalCount}",
				nowPage: "${pagingVO.nowPage}",
				countPerPage: "${pagingVO.countPerPage}",
		        onPageClick: function(pageNumber, event) {
					console.log("pageNumber : " + pageNumber);
					goPage(pageNumber);
				},
				onCounterSelect: function(countNumber, event) {
					console.log("countNumber : " + countNumber);
					changeCounter(countNumber);
				}			
		    });
			
			$("#locale").val(localeVal);
		});		
		
	</script>
