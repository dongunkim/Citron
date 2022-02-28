<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/include-taglib.jspf" %>

<script type="text/javascript">
	$(document).ready(function() {
		$("#btn-list").on("click", function(e) {
			e.preventDefault();
			goList();
		});
			
		$("#btn-modify").on("click", function(e) {
			e.preventDefault();
			goUpdate();
		});
		
		$("#btn-delete").on("click", function(e) {
			e.preventDefault();
			
			Confirm("<spring:message code='ui.common.label.delete'/>",			// 삭제
				    "${board.title}</br></br>" + 
					"<spring:message code='ui.common.alert.action.delete'/>",	// 삭제하시겠습니까? 
					true, goDelete);			
		});
		
		$("a[name='file']").on("click", function(e) {
			e.preventDefault();
			downloadFile($(this));
			
		});
	});
	
	function goList() {
		var comSubmit = new commonSubmit();
		comSubmit.setUrl("<c:url value='/board/boardList'/>");
		comSubmit.submit();
	}
	
	function goUpdate() {
		var boardId = "${board.boardId}";
		var comSubmit = new commonSubmit();		
		comSubmit.setUrl("<c:url value='/board/boardModify'/>");
		comSubmit.addParam("boardId", boardId);
		comSubmit.submit();
	}
	
	function goDelete() {				
		var formData = {
				boardId: "${board.boardId}"
		};
		
		serverCall(formData, "/board/ajaxBoardDelete.json", function(data) {
			onSuccess(data);
		});
	}
		
	function downloadFile(obj) {
		var fileSeq = obj.parent().find("#fileSeq").val();
		var comSubmit = new commonSubmit();
		comSubmit.setUrl("<c:url value='/board/downloadFile'/>");
		comSubmit.addParam("fileSeq", fileSeq);			
		comSubmit.submit();
	}
</script>
	
<!-- 컨텐츠 영역 -->
<div class="row">
	<table class="table table-bordered table-info">
		<colgroup>
			<col class="col-xs-2" />
			<col class="col-xs-4" />			
		</colgroup>
		<tbody>
			<tr>
				<th>
					<spring:message code="ui.common.label.title"/><!-- 제목 -->
				</th>
				<td colspan="3">${board.title}</td>
			</tr>
			<tr>
				<th>
					<spring:message code="ui.common.label.content"/><!-- 내용 -->
				</th>
				<td colspan="3">${board.content}</td>
			</tr>
			<tr>
				<th>
					<spring:message code="ui.common.label.attachment"/><!-- 첨부 -->
				</th>
				<td colspan="3">
					<c:choose>
						<c:when test="${fn:length(list) > 0}">
							<c:forEach var="row" items="${list}">
								<p>
								<input type="hidden" id="fileSeq" value="${row.fileSeq}"/>
								<a href="#this" name="file">${row.orgFileName}</a>
								(${row.fileSizeStr})											
								</p>
							</c:forEach>	
						</c:when>
						<c:otherwise>
							<spring:message code="ui.common.message.nofile"/><!-- 첨부파일이 없습니다. -->
						</c:otherwise>
					</c:choose>	
				</td>
			</tr>
			<tr>
				<th>
					<spring:message code="ui.common.label.hitCount"/><!-- 조회수 -->
				</th>
				<td colspan="3">${board.hitCount}</td>
			</tr>			
			<tr>
				<th>
					<spring:message code="ui.common.label.createId"/><!-- 등록자 ID -->
				</th>
				<td>${board.createId}</td>
				<th>
					<spring:message code="ui.common.label.createTime"/><!-- 등록 시간 -->
				</th>
				<td>${board.createTime}</td>
			</tr>
			<c:if test="${board.updateId != '' && board.updateId != null}">
				<tr>
					<th>
						<spring:message code="ui.common.label.updateId"/><!-- 수정자 ID -->
					</th>
					<td>${board.updateId}</td>
					<th>
						<spring:message code="ui.common.label.updateTime"/><!-- 수정 시간 -->
					</th>
					<td>${board.updateTime}</td>
				</tr>
			</c:if>			
		</tbody>
	</table>
</div>

<!-- 버튼 영역 -->
<div class="row btn-group-area">
	<div class="col-xs-6 text-left">
		<a href="#" class="btn btn-default" role="button" id="btn-list"><spring:message code="ui.common.button.list"/></a><!-- 목록 -->
	</div>
	<div class="col-xs-6 text-right">
		<a href="#" class="btn btn-secondary" role="button" id="btn-delete"><spring:message code="ui.common.button.delete"/></a><!-- 삭제 -->
		<a href="#" class="btn btn-primary" role="button" id="btn-modify"><spring:message code="ui.common.button.modify"/></a><!-- 수정 -->
	</div>
</div>
