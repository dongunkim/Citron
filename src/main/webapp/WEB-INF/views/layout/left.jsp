<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/include/include-taglib.jspf" %>

<%@ taglib prefix="citron" uri="/WEB-INF/tld/showMenu.tld" %>

<script type="text/javascript">
$(function(){
	$("a[name='menu']").on("click", function(e) {
		e.preventDefault();
		
		var url = $(this).data('url');
		var comSubmit = new commonSubmit();
		comSubmit.setUrl("<c:url value='" + url + "'/>");
		comSubmit.submit();
	});
});
</script>

<!-- LEFT(Sub) Menu -->
<citron:showMenu level="SUB" />


 