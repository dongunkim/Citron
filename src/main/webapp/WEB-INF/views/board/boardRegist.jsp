<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/include-taglib.jspf" %>
<%@ include file="/WEB-INF/include/include-write.jspf" %>

<script type="text/javascript">	
	$(document).ready(function(){
		
		$("#btn-list").on("click", function(e) {
			e.preventDefault();
			goList();
		});
		
		$("#btn-save").on("click", function(e) {
			e.preventDefault();
						
			if (validatorObj.customValidate()) uploadObj.startUpload();
		});
				
		// 첨부 File Upload 설정
		// 화면별로 다른 설정이 필요한 부분은 Wrapper의 Options을 참고해서 url 밑에 추가
		var uploadObj = $.uploadWrapper ({
			url:"/board/ajaxBoardRegist.json"
		});
				
		
		// Textarea(Summernote) 설정
		RichTextArea('<%="ko".equals((String)session.getAttribute("locale")) ? "ko-KR" : ""%>');
		
		
		// jQuery Validation 설정
		// 화면별로 필요한 Validation 항목을 rules에 추가
		// 화면별로 다른 설정이 필요한 부분은 Wrapper에 정의된 Options을 참고해서 rules 밑에 추가 
		var validatorObj = $("#frm").validate({
			rules: {
			    title: {
			      required: true
			    }
			},
			customHandler: function(form) {
				if ($("#summernote").summernote('isEmpty')) {
					var label = $("#summernote").data('label');					
					Alert("<spring:message code='ui.common.alert.require' arguments='" + label + "' />");	// 필수 항목입니다.
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
		
</script>

<!-- 컨텐츠 영역 -->
<div id="form-div" style="display:none;">
	<div class="row">
		<form id="frm" name="frm" >
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
							<div>
								<input type="text" class="form-control" id="title" name="title" data-label="<spring:message code='ui.common.label.title'/>" placeholder="<spring:message code='ui.common.placeholder.title'/>" autofocus>
							</div>
						</td>
					</tr>
					<tr>
						<th>
							<spring:message code="ui.common.label.content"/><!-- 내용 -->
							<em class="fc-red">*</em>
						</th>
						<td colspan="3">
							<textarea class="form-control" id="summernote" name="content" rows="11" data-label="<spring:message code='ui.common.label.content'/>"></textarea>
						</td>
					</tr>
					<tr>
						<th>
							<spring:message code="ui.common.label.attachment"/><!-- 첨부 -->
						</th>
						<td colspan="3">
							<div id="fileuploader"></div>
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
			<a href="#" class="btn btn-primary" role="button" id="btn-save"><spring:message code="ui.common.button.save"/></a> <!-- 저장 -->
		</div>
	</div>
</div>