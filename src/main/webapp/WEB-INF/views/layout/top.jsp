<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/include/include-taglib.jspf" %>

<%@ taglib prefix="citron" uri="/WEB-INF/tld/showMenu.tld" %>
<% String id = (String)session.getAttribute("id"); %>
<% String name = (String)session.getAttribute("name"); %>

<script type="text/javascript">
$(function(){
	// Top Menu 링크 Form Submit으로 변경
	$("a[name='menu']").on("click", function(e) {
		e.preventDefault();
		
		var url = $(this).data('url');
		var comSubmit = new commonSubmit();
		comSubmit.setUrl("<c:url value='" + url + "'/>");
		comSubmit.submit();
	});
	
	$("#logout").on("click", function(e) {
		e.preventDefault();
		logout();
	});
	
	$("#locale").change(function(){
		var form = { lang: $("#locale").val()}
		$.ajax({
			url: "<c:url value='/common/changeLocale.json'/>",
			type: "POST",
			data:form,
			dataType: "json",
			success: function(data) {
				location.reload();
			}
		});
	});
	
	// 로그아웃 처리
	function logout() {
		$.ajax({
			url: "<c:url value='/main/logout.json'/>",
			type: "POST",
			dataType: "json",
			success: function(data) {				
				if (data.forward) document.location.href = data.forward;
				else Alert("<spring:message code='ui.common.ajax.error'/>");	// 처리중 오류가 발생했습니다.\n잠시 후 다시 시도하십시오.
			}
		});
	}
});

</script>

<nav class="navbar navbar-inverse navbar-fixed-top">
	<div class="container-fluid">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
				<span class="sr-only">Toggle navigation</span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="/main/main">HOME</a>
		</div>		
		<div id="navbar" class="navbar-collapse collapse">
			<!-- TOP Menu -->
			<citron:showMenu level="TOP" />
			
			<ul class="nav navbar-nav navbar-right">
				<div class="navbar-form">
					<select class="form-control" id="locale" name="locale">
						<option value="ko" <c:if test="${locale eq 'ko'}">selected</c:if> >한국어</option>
						<option value="en" <c:if test="${locale eq 'en'}">selected</c:if> >English</option>
					</select>
				</div>
			</ul>
			<ul class="nav navbar-nav navbar-right">
				<li class="dropdown">
					<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">${name}(${id})<span class="caret"></span></a>
					<ul class="dropdown-menu">
						<li><a href="#" name='menu' data-url='/main/myPage'><spring:message code='ui.admin.button.myPage'/></a></li> <!-- 내 정보 -->
						<li><a href="#" name='menu' data-url='/main/changePassword'><spring:message code='ui.admin.button.changePassword'/></a></li> <!-- 비밀번호 변경 -->
						<li role="separator" class="divider"></li>
						<li><a href="#this" id="logout"><spring:message code='ui.admin.button.logout'/></a></li> <!-- 로그아웃 -->
					</ul>
				</li>
			</ul>
		</div>
	</div>
</nav>
