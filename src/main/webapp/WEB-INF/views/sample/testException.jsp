<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/include-header.jspf" %>

<script>      
	function viewException(ex) {
		var comSubmit = new commonSubmit();
		comSubmit.setUrl("<c:url value='/sample/checkException'/>");
		comSubmit.addParam("exception", ex);
		comSubmit.submit();
	}
      
	function view404Exception() {
		var comSubmit = new commonSubmit();
		comSubmit.setUrl("<c:url value='/sample/check404Exception'/>");
		comSubmit.addParam("exception", "view404");
		comSubmit.submit();
	}
   	
	function ajaxException(ex) {
		var data = {
				exception: ex
		}
		$.ajax({
			url: "<c:url value='/sample/checkException'/>",
			type: "POST",
			data: data,
			dataType: "json",
			success: function(data) {
				console.log("data \t:\n" + JSON.stringify(data));
									
			},
			error: function(e) {				
			}
		});
	}
	
	function ajax404Exception() {
		var data = {
				exception: "ajax404"
		}
		$.ajax({
			url: "<c:url value='/sample/check404Exception'/>",
			type: "POST",
			data: data,
			dataType: "json",
			success: function(data) {
				console.log("data \t:\n" + JSON.stringify(data));
									
			},
			error: function(e) {				
			}
		});
	}
   
</script>
		
	<h2>Exception 체크</h2>			
	<br/><br/>
	<p style="margin-left:5px;">
	<a href="#this" class="btn" id="v404">View 404에러</a>
	<a href="#this" class="btn" id="v403">View 403에러</a>
	<a href="#this" class="btn" id="v500">View 500에러</a>
	<a href="#this" class="btn" id="vSQL">View SQL에러</a>	
	<br/><br/>
	<a href="#this" class="btn" id="a403">Ajax 403에러</a>
	<a href="#this" class="btn" id="a404">Ajax 404에러</a>
	<a href="#this" class="btn" id="a500">Ajax 500에러</a>	
	</p>
	
	<script type="text/javascript">
		$(document).ready(function(){ 
			$("#v500").on("click", function(e) {
				e.preventDefault();
				viewException("view500");
			});
			$("#v404").on("click", function(e) {
				e.preventDefault();
				view404Exception();
			});
			$("#v403").on("click", function(e) {
				e.preventDefault();
				viewException("view403");
			});
			$("#vSQL").on("click", function(e) {
				e.preventDefault();
				viewException("viewSQL");
			});
			$("#a403").on("click", function(e) {
				e.preventDefault();
				ajaxException("ajax403");
			});
			$("#a404").on("click", function(e) {
				e.preventDefault();
				ajax404Exception();
			});
			$("#a500").on("click", function(e) {
				e.preventDefault();
				ajaxException("ajax500");
			});
			
		});
	</script>
