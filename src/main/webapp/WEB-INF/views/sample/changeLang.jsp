<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/include-header.jspf" %>

<script>      
	    
	function ajaxChangeLang(lang) {
		var data = {
				lang: lang
		};
		
		$.ajax({
			url: "<c:url value='/sample/ajaxChangeLang.json'/>",
			type: "POST",
			data: data,
			dataType: "json",
			success: function(data) {
				console.log("data \t:\n" + JSON.stringify(data));									
			}
		});
	}
	   
</script>

<br/><br/>
	<p style="margin-left:5px;">
	<a href="#this" class="btn" id="lang-en">Change Language - English</a>
	<a href="#this" class="btn" id="lang-kr">Change Language - Korean</a>
	</p>
	
	<script type="text/javascript">
		$(document).ready(function(){			
			$("#lang-en").on("click", function(e) {
				e.preventDefault();
				ajaxChangeLang('en');			
			});
			
			$("#lang-kr").on("click", function(e) {
				e.preventDefault();
				ajaxChangeLang('ko');			
			});
			
		});
	</script>