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
			<div class="col-xs-12">
				<div id="body">
					<tiles:insertAttribute name="body" />
				</div>
			</div>
		</div>
	</div>
	<div id="footer">
		<tiles:insertAttribute name="footer" />
	</div>
</body>
</html>