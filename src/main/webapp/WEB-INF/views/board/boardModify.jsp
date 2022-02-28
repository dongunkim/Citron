<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/include-taglib.jspf" %>
<%@ include file="/WEB-INF/include/include-write.jspf" %>

<script type="text/javascript">		
	$(document).ready(function(){
		$("#btn-list").on("click", function(e) {
			e.preventDefault();
			goList();
		});
		
		$("#btn-cancel").on("click", function(e) {
			e.preventDefault();
			goDetail();
		});
		
		$("#btn-save").on("click", function(e) {
			e.preventDefault();
			
			if (validatorObj.customValidate()) uploadObj.startUpload();
		});
		
		// 첨부 File Upload 설정
		// 수정화면에서는 기존 첨부파일을 처리하기 위해 onLoad와 deleteCallback 추가 
		var uploadObj = $.uploadWrapper({			
			url:"/board/ajaxBoardModify.json",
			onLoad: function (obj) {
				var lists = $("#fileDiv").find('input[type=hidden]');
				
				$.each(lists, function(index, list) {					
					obj.createProgress(list.value, $(list).data('orgfilename'), $(list).data('filesize'));
				});
			},
			deleteCallback: function (fileSeq, fileName) {					
				$("#fileSeq_" + fileSeq).remove();
			}
		});
		
		// Textarea(Summernote) 설정
		RichTextArea('<%="ko".equals((String)session.getAttribute("locale")) ? "ko-KR" : ""%>');
		
		// jQuery Validation 설정
		var validatorObj = $("#frm").validate({			
			rules: {
			    title: {
			      required: true
			    }
			},
			customHandler: function(form) {
				if ($("#summernote").summernote('isEmpty')) {
					var label = $("#summernote").data('label');
					Alert("<spring:message code='ui.common.alert.require' arguments='" + label + "' />");	// 필수 항목입니다
					$("#summernote").summernote('focus');	
					return false;
				}	
				return true;
			}
		});
				
		$("#form-div").show();		
	});
	
	// 목록 이동
	function goList() {
		var comSubmit = new commonSubmit();
		comSubmit.setUrl("<c:url value='/board/boardList'/>");
		comSubmit.submit();
	}
	
	// 취소 또는 저장완료 시, 상세화면 이동
	function goDetail() {
		var boardId = "${board.boardId}";
		var comSubmit = new commonSubmit();
		comSubmit.setUrl("<c:url value='/board/boardDetail'/>");
		comSubmit.addParam("boardId", boardId);
		comSubmit.submit();
	}
</script>

<!-- 컨텐츠 영역 -->
<div id="form-div" style="display:none;">
	<div class="row">
		<form id="frm" name="frm" >
		<input type="hidden" id="boardId" name="boardId" value="${board.boardId}"/>
			<table class="table table-bordered table-info">
				<colgroup>
					<col class="col-xs-2" />
					<col class="col-xs-4" />			
				</colgroup>
				<tbody>
					<tr>
						<th>
							<spring:message code="ui.common.label.title"/><!-- 제목 -->
							<em class="fc-red">*</em>
						</th>
						<td colspan="3">
							<input type="text" class="form-control" id="title" name="title" data-label="<spring:message code='ui.common.label.title'/>" value="${board.title}" placeholder="<spring:message code='ui.common.placeholder.title'/>" autofocus>
						</td>
					</tr>
					<tr>
						<th>
							<spring:message code="ui.common.label.content"/><!-- 내용 -->
							<em class="fc-red">*</em>
						</th>
						<td colspan="3">
							<textarea class="form-control" id="summernote" name="content" rows="11" data-label="<spring:message code='ui.common.label.content'/>">${board.content}</textarea>
						</td>
					</tr>
					<tr>
						<th>
							<spring:message code="ui.common.label.attachment"/><!-- 첨부 -->
						</th>
						<td colspan="3">
							<div id="fileuploader"></div>
							<div id="fileDiv">
							<p>
							<c:forEach var="row" items="${list}" varStatus="var">
								<input type="hidden" id="fileSeq_${row.fileSeq}" name="fileSeq" value="${row.fileSeq}" data-orgFileName="${row.orgFileName}" data-fileSize="${row.fileSize}" />						
							</c:forEach>
							</p>
						</div>
						</td>
					</tr>			
				</tbody>
			</table>
		</form>
	</div>
	
	<!-- 버튼 영역 -->
	<div class="row btn-group-area">
		<div class="col-xs-6 text-left">
			<a href="#" class="btn btn-default" role="button" id="btn-list"><spring:message code="ui.common.button.list"/></a> <!-- 목록 -->
		</div>
		<div class="col-xs-6 text-right">
			<a href="#" class="btn btn-outline-primary" role="button" id="btn-cancel"><spring:message code="ui.common.button.cancel"/></a> <!-- 취소 -->
			<a href="#" class="btn btn-primary" role="button" id="btn-save"><spring:message code="ui.common.button.save"/></a> <!-- 저장 -->
		</div>
	</div>
</div>