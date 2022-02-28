<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/include-taglib.jspf" %>

<script type="text/javascript">
	function openBoardList() {
		var comSubmit = new commonSubmit();
		comSubmit.setUrl("<c:url value='/sample/openBoardList'/>");
		comSubmit.submit();
	}
	
	function openBoardUpdate() {
		var idx = "${boardVO.idx}";
		var comSubmit = new commonSubmit();		
		comSubmit.setUrl("<c:url value='/sample/openBoardUpdate'/>");
		comSubmit.addParam("idx", idx);
		comSubmit.submit();
	}
	
	function downloadFile(obj) {
		var idx = obj.parent().find("#idx").val();
		var comSubmit = new commonSubmit();
		comSubmit.setUrl("<c:url value='/common/downloadFile'/>");
		comSubmit.addParam("idx", idx);			
		comSubmit.submit();
	}
</script>

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
				<td>${boardVO.idx}</td>
				<th scope="row">조회수</th>
				<td>${boardVO.hitCnt}</td>
			</tr>
			<tr>
				<th scope="row">작성자</th>
				<td>${boardVO.creaId}</td>
				<th scope="row">작성시간</th>
				<td>${boardVO.creaDtm}</td>
			</tr>
			<tr>
				<th scope="row">제목</th>
				<td colspan="3">${boardVO.title}</td>
			</tr>
			<% pageContext.setAttribute("newLineChar", "\n"); %>
			<tr>
				<td colspan="4">${fn:replace(boardVO.contents, newLineChar, "<br/>")}</td>
			</tr>
			<tr>
				<th scope="row">첨부</th>
				<td colspan="3">
					<c:choose>
						<c:when test="${fn:length(list) > 0}">
							<c:forEach var="row" items="${list}">
								<p>
								<input type="hidden" id="idx" value="${row.idx}"/>
								<a href="#this" name="file">${row.originalFileName}</a>								
								(${row.fileSizeStr})											
								</p>
							</c:forEach>	
						</c:when>
						<c:otherwise>
							첨부파일이 없습니다.
						</c:otherwise>
					</c:choose>					
				</td>
			</tr>
		</tbody>
	</table>
	
	<a href="#this" class="btn" id="list">목록으로</a>
	<a href="#this" class="btn" id="update">수정하기</a>
		
	<script type="text/javascript">
		$(document).ready(function() {
			$("#list").on("click", function(e) {
				e.preventDefault();
				openBoardList();
			});
			
			$("#update").on("click", function(e) {
				e.preventDefault();
				openBoardUpdate();
			});
			$("a[name='file']").on("click", function(e) {
				e.preventDefault();
				downloadFile($(this));
				
			});
		});
	</script>
