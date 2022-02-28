<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/include-header.jspf" %>
<script src="/js/jquery.uploadfile.js"></script>
<script>      
	function viewFilter() {		
		var comSubmit = new commonSubmit('frm2');
		comSubmit.setUrl("<c:url value='/sample/formFilter'/>");		
		comSubmit.submit();
	}
	
	function multipartViewFilter() {		
		var comSubmit = new commonSubmit('frm');
		comSubmit.setUrl("<c:url value='/sample/multipartFormFilter'/>");		
		comSubmit.submit();
	}
    
	function ajaxFilter(ex) {
		var title = $("#title").val();
		var contents = $("#contents").val();
				
		var data = {
				title: title,
				contents: contents
		}
		$.ajax({
			url: "<c:url value='/sample/ajaxFilter.json'/>",
			type: "POST",
			data: JSON.stringify(data),
			contentType: "application/json",
			dataType: "json",
			success: function(data) {
				console.log("data \t:\n" + JSON.stringify(data));
									
			},
			error: function(e) {				
			}
		});
	}
	   
</script>
	<c:if test="${boardVO != null}">
	<p>${boardVO.title} - ${boardVO.contents}</p>
	</c:if>
	<h2>Filter 체크</h2>
	<br>
	<form id="frm" name="frm" enctype="multipart/form-data">
	제목
	<input type="text" id="title" name="title" class="wdp_90" value="XSS 테스트 입니다."/>
	<br>
	내용
	<textarea rows="20" cols="50" title="내용" id="contents" name="contents"><script>alter('xss 확인합니다.')</script><br><img src='http://localhost:8080/test.jpg'></textarea>
	<input type="file" id="file" name="file"/>
	<br/><br/>
	<div id="fileuploader">Upload</div>
	</form>	
	
	<form id="frm2" name="frm2">	
	<input type="hidden" id="title" name="title" value="XSS 테스트 입니다.<script>alter('xss 확인합니다.')</script>"/>
	<input type="hidden" id="contents" name="contents" value="<script>alter('xss 확인합니다.')</script><br><img src='http://localhost:8080/test.jpg'>"/>	
	</form>			
	<br/><br/>
	<p style="margin-left:5px;">
	<a href="#this" class="btn" id="formFilter">Form Filter</a>
	  
	<a href="#this" class="btn" id="multipartFormFilter">Multipart Form Filter</a>
	  
	<a href="#this" class="btn" id="ajaxFilter">Ajax Filter</a>
	  
	<a href="#this" class="btn" id="multipartAjaxFilter">Multipart Ajax Filter</a>
	</p>
	
	<script type="text/javascript">
		$(document).ready(function(){ 
			// Form Submit
			$("#formFilter").on("click", function(e) {
				e.preventDefault();
				viewFilter();
			});
			// Form Submit
			$("#multipartFormFilter").on("click", function(e) {
				e.preventDefault();
				multipartViewFilter();
			});
			// Ajax - JSON
			$("#ajaxFilter").on("click", function(e) {
				e.preventDefault();
				ajaxFilter();
			});	
			// Ajax - Form Serialize
			$("#multipartAjaxFilter").on("click", function(e) {
				e.preventDefault();
				uploadObj.startUpload();
			});
			
			var uploadObj = $("#fileuploader").uploadFile({
				formName:"frm",
				url:"/sample/multipartAjaxFilter.json",	
				uploadStr:"파일선택 For Ajax",
				maxFileSize:10*1024*1024,				
				maxFileCount:3,
				showFileCounter:false,
				showFileSize:true,
				allowDuplicates:false,
				onSuccess: function (data, message) {
					console.log("data \t:\n" + JSON.stringify(data));
					console.log("message \t:\n" + JSON.stringify(message));
					
					return;
					if (data.forward) document.location.href = data.forward;
					else alert('처리중 오류가 발생했습니다.\n잠시 후 다시 시도하십시오.');
					
				},
				onError: function (status, errMsg) {
					console.log(status);
					console.log(errMsg);
					
					alert('처리중 오류가 발생했습니다.\n잠시 후 다시 시도하십시오.');
				}
			});
		});
	</script>
