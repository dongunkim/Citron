<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<!DOCTYPE html>
<html>
<head>
<title><tiles:getAsString name="title" /></title>
<tiles:insertAttribute name="header" />
</head>
<body>
	<div id="top">
		<tiles:insertAttribute name="top" />
	</div>
	<div class="container">
		<div class="row">
			<!-- 왼쪽 메뉴 영역 -->
			<div class="col-xs-2 sidebar">
				<div id="left">
					<tiles:insertAttribute name="left" />
				</div>
			</div>
			<!-- 오른쪽 Content 영역 -->
			<div class="col-xs-10 main">
				<div class="container-fluid">

					<tiles:insertAttribute name="pageHeader" />

					<div id="body">
						<tiles:insertAttribute name="body" />
					</div>
				</div>
			</div>
		</div>
	</div>
	<div id="footer">
		<tiles:insertAttribute name="footer" />
	</div>
</body>
</html>