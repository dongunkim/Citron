<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/include-taglib.jspf" %>

<script type="text/javascript">
	$(document).ready(function() {
		$("#btn-list").on("click", function(e) {
			e.preventDefault();
			goList();
		});
	});
	
	function goList() {
		var comSubmit = new commonSubmit();
		comSubmit.setUrl("<c:url value='/member/memberList'/>");
		comSubmit.submit();
	}
</script>

<!-- 컨텐츠 영역 -->
<div class="row">
	<table class="table table-bordered table-info">
		<colgroup>
			<col class="col-xs-2" />
			<col class="col-xs-4" />
			<col class="col-xs-2" />
			<col class="col-xs-4" />
		</colgroup>
		<tbody>
			<tr>
				<th>
					<spring:message code="ui.member.label.memberId"/><!-- 회원 ID -->
				</th>
				<td>${member.memberId}</td>
				<th>
					<spring:message code="ui.member.label.IdType"/><!-- ID 유형 -->
				</th>
				<td>${member.idTypeName}</td>
			</tr>
			<tr>
				<th>
					<spring:message code="ui.member.label.memberName"/><!-- 회원 이름 -->
				</th>
				<td>${member.memberName}</td>
				<th>
					<spring:message code="ui.member.label.memberStatus"/><!-- 회원 상태 -->
				</th>
				<td>${member.memberStatusName}</td>
			</tr>
			<tr>
				<th>
					<spring:message code="ui.member.label.joinTime"/><!-- 가입 시간 -->
				</th>
				<td>${member.joinTime}</td>
				<th>
					<spring:message code="ui.common.label.useYn"/><!-- 사용 여부 -->
				</th>
				<c:choose>
					<c:when test="${member.useYn == 'Y'}">
						<td><spring:message code="ui.common.label.use"/></td><!-- 사용 -->
					</c:when>
					<c:otherwise>
						<td><spring:message code="ui.common.label.useNot"/></td><!-- 사용안함 -->
					</c:otherwise>
				</c:choose>				
			</tr>			
			<tr>
				<th>
					<spring:message code="ui.common.label.createId"/><!-- 등록자 ID -->
				</th>
				<td>${member.createId}</td>
				<th>
					<spring:message code="ui.common.label.createTime"/><!-- 등록 시간 -->
				</th>
				<td>${member.createTime}</td>
			</tr>
			<c:if test="${member.updateId != '' && member.updateId != null}">
				<tr>
					<th>
						<spring:message code="ui.common.label.updateId"/><!-- 수정자 ID -->
					</th>
					<td>${member.updateId}</td>
					<th>
						<spring:message code="ui.common.label.updateTime"/><!-- 수정 시간 -->
					</th>
					<td>${member.updateTime}</td>
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
</div>

