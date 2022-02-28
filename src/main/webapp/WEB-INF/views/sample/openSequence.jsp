<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/include-header.jspf" %>

<script>      
	    
	function ajaxSequence() {		
		$.ajax({
			url: "<c:url value='/sample/getSequence'/>",
			type: "POST",			
			dataType: "json",
			success: function(data) {
				console.log("data \t:\n" + JSON.stringify(data));
									
			},
			error: function(e) {				
			}
		});
	}
	   
</script>
	
	<br/><br/>
	<p style="margin-left:5px;">
	<a href="#this" class="btn" id="getSequence">Get Sequence</a>	
	</p>
	
	<script type="text/javascript">
		$(document).ready(function(){			
			$("#getSequence").on("click", function(e) {
				e.preventDefault();
				ajaxSequence();			
			});
			
		});
	</script>
