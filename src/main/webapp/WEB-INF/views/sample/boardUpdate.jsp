<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/include-header.jspf" %>

<script type="text/javascript">
    var fileCount = "${fn:length(list)+1}";
    
	function openBoardList() {
		var comSubmit = new commonSubmit();
		comSubmit.setUrl("<c:url value='/sample/openBoardList'/>");
		comSubmit.submit();
	}
	
	function updateBoard() {		
		var comSubmit = new commonSubmit("frm");		
		comSubmit.setUrl("<c:url value='/sample/updateBoard'/>");
		comSubmit.submit();
	}
	
	function deleteBoard() {
		var idx = "${map.IDX}";
		var comSubmit = new commonSubmit();
		comSubmit.setUrl("<c:url value='/sample/deleteBoard'/>");
		comSubmit.addParam("IDX", idx);
		comSubmit.submit();
	}
	
	function addFile() {
		var str = "<p>" +
		          "<input type='file' id='file_" + fileCount + "' name='file_" + fileCount + "'>" +
		          "<a href='#this' class='btn' id='delete_" + fileCount + "' name='delete_" + fileCount + "'>삭제</a>" +
		          "</p>";
		$("#fileDiv").append(str);
		$("#delete_" + (fileCount++)).on("click", function(e) {
			e.preventDefault();
			deleteFile($(this));
		});
	}
	
	function deleteFile(obj) {
		obj.parent().remove();
	}
</script>

	<form id="frm" name="frm" enctype="multipart/form-data">
	<table class="board_view">
		<colgroup>
			<col width="15%"/>
			<col width="35%"/>
			<col width="15%"/>
			<col width="35%"/>
		</colgroup>
		<caption>게시글 상세</caption>
		<tbody>
			<tr>
				<th scope="row">글 번호</th>
				<td>${map.IDX}<input type="hidden" id="IDX" name="IDX" value="${map.IDX}"/></td>
				<th scope="row">조회수</th>
				<td>${map.HIT_CNT}</td>
			</tr>
			<tr>
				<th scope="row">작성자</th>
				<td>${map.CREA_ID}</td>
				<th scope="row">작성시간</th>
				<td>${map.CREA_DTM}</td>
			</tr>
			<tr>
				<th scope="row">제목</th>
				<td colspan="3"><input type="text" id="TITLE" name="TITLE" class="wdp_90" value="${map.TITLE}"/></td>
			</tr>
			<tr>
				<td colspan="4" class="view_text"><textarea rows="20" cols="100" title="내용" id="CONTENTS" name="CONTENTS">${map.CONTENTS}</textarea></td>
			</tr>
			<tr>
				<th scope="row">첨부파일</th>
				<td colspan="3">
					<div id="fileDiv">
						<c:forEach var="row" items="${list}" varStatus="var">
							<p>
								<input type="hidden" id="IDX" name="IDX_${var.index}" value="${row.IDX}"/>
								<a href="#this" id="name_${var.index}" name="name_${var.index}">${row.ORIGINAL_FILE_NAME}</a>
								<input type="file" id="file_${var.index}" name="file_${var.index}"/>
								(${row.FILE_SIZE} kb)
								<a href="#this" class="btn" id="delete_${var.index}" name="delete_${var.index}">삭제</a>
							</p>						
						</c:forEach>
					</div>
				</td>
			</tr>
		</tbody>
	</table>
	</form>
	
	<a href="#this" class="btn" id="addFile">파일추가</a>
	<a href="#this" class="btn" id="list">목록으로</a>
	<a href="#this" class="btn" id="save">저장하기</a>
	<a href="#this" class="btn" id="delete">삭제하기</a>
		
	<script type="text/javascript">
		$(document).ready(function() {
			$("#list").on("click", function(e) {
				e.preventDefault();
				openBoardList();
			});
			
			$("#save").on("click", function(e) {
				e.preventDefault();
				updateBoard();
			});
			
			$("#delete").on("click", function(e) {
				e.preventDefault();
				deleteBoard();
			});
			
			$("#addFile").on("click", function(e) {
				e.preventDefault();
				addFile();
			});
			
			$("a[name='delete']").on("click", function(e) {
				e.preventDefault();
				deleteFile($(this));
			});
		});
	</script>
