<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/include/include-taglib.jspf" %>

<%@ taglib prefix="citron" uri="/WEB-INF/tld/showMenu.tld" %>

<!-- 페이지 타이틀 -->
<div class="row">
	<c:set var = "uri" >${requestScope['javax.servlet.forward.request_uri']}</c:set>
	<c:choose>
		<c:when test="${fn:endsWith(uri,'List')}">
			<h2 class="page-header">${currentMenu.menuName} <spring:message code="ui.common.label.list"/></h2>
		</c:when>
		<c:when test="${fn:endsWith(uri,'Detail')}">
			<h2 class="page-header">${currentMenu.menuName} <spring:message code="ui.common.label.detail"/></h2>
		</c:when>
		<c:when test="${fn:endsWith(uri,'Regist')}">
			<h2 class="page-header">${currentMenu.menuName} <spring:message code="ui.common.label.regist"/></h2>
		</c:when>
		<c:when test="${fn:endsWith(uri,'Modify')}">
			<h2 class="page-header">${currentMenu.menuName} <spring:message code="ui.common.label.modify"/></h2>
		</c:when>
		<c:otherwise>
			<h2 class="page-header">${currentMenu.menuName}</h2>
		</c:otherwise>
	</c:choose>
</div>

<!-- 사이트 이동경로 -->
<div class="row">
	<ol class="breadcrumb">
		<li>Home</li>
		<li>${currentMenu.topMenuName}</li>
		<c:if test="${currentMenu.depth eq 3}"><li>${currentMenu.upperMenuName}</li></c:if>
		<li class="active">${currentMenu.menuName}</li>
	</ol>
</div>
