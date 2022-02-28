<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>CITRON - Error Page</title>
<style>
/* error */
html,textarea { font-family:  "Tahoma", "Lucida Grande", "Lucida Sans Unicode","Dotum", '돋움', sans-serif; font-weight: 400;}
html,body,
error_wrap { width: 100%; height: 100%; margin: 0; padding: 0; overflow: hidden; position: relative;}
.error { width: 700px; height: 200px; position: absolute; left: 50%; top: 50%; margin: -300px 0 0 -350px;}
.error_bg { overflow: hidden;}
.l_logo { float: left; padding-right: 20px; padding-bottom: 50px; width: 170px; height: 115px;}
.error_txt { position: relative; text-align: left;}
.error_txt p { margin: 0; color: #4e4e4e;}
.error_txt .tit { position: absolute; left: 13px; top: -15px; font-size: 75px; font-weight: 700;}
.error_txt .point { font-size: 21px; font-weight: 500; margin-top: 130px;}
.error_txt .tt { font-size: 14px; padding-top: 5px;}
.btn_group {margin-top: 30px; text-align: center}
.btn {
    display: inline-block;
    margin-bottom: 0;
    font-weight: 400;
    text-align: center;
    white-space: nowrap;
    vertical-align: middle;
    -ms-touch-action: manipulation;
    touch-action: manipulation;
    cursor: pointer;
    background-image: none;
    border: 1px solid transparent;
    padding: 6px 12px;
    font-size: 14px;
    line-height: 1.42857143;
    border-radius: 4px;
    -webkit-user-select: none;
    -moz-user-select: none;
    -ms-user-select: none;
    user-select: none;
}
.btn-default {
    color: #333;
    background-color: #fff;
    border-color: #ccc;
}
.btn_group > a {
    text-decoration: none;
}
</style>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
</head>
<body>
	<div id="body">
		<tiles:insertAttribute name="body" />
	</div>
</body>
</html>