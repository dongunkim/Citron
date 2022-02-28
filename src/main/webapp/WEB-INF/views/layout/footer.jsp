<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/include/include-taglib.jspf" %>
<form id="commonForm" name="commonForm"></form>

<!-- Footer -->
<footer>
	<div class="container">
	 	<hr>
	 	<p>Copyright &copy; 2019 dinens</p>
 	</div>
</footer>

<div class="modal" id="alert_modal" tabindex="-1">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title"></h4>
			</div>
			<div class="modal-body text-center">
				<p></p>
			</div>
			<div class="modal-footer">
				<a href="#" role="button" class="btn btn-primary" id="btn-alert-ok"><spring:message code="ui.common.button.confirm"/></a><!-- 확인 -->
			</div>
		</div>
	</div>
</div>

<div class="modal" id="common_modal" tabindex="-1">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title"></h4>
			</div>
			<div class="modal-body text-center">
				<p></p>
			</div>
			<div class="modal-footer">
				<a role="button" class="btn btn-default" data-dismiss="modal" id="btn-common-cancel"><spring:message code="ui.common.button.cancel"/></a><!-- 취소 -->
				<a href="#" role="button" class="btn btn-primary" id="btn-common-ok"><spring:message code="ui.common.button.confirm"/></a><!-- 확인 -->
			</div>
		</div>
	</div>
</div>

<!-- 파일 생성중 보여질 진행막대를 포함하고 있는 다이얼로그 입니다. --> 
<div title="Data Download" id="preparing-file-modal" style="display: none;"> 
	<div id="progressbar" style="width: 100%; height: 22px; margin-top: 20px;"></div> 
</div> 

<!-- 에러발생시 보여질 메세지 다이얼로그 입니다. --> 
<div title="Error" id="error-modal" style="display: none;"> 
	<p>생성실패.</p> 
</div>	
