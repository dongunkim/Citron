<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/include-header.jspf" %>

<link href="/css/uploadfile.css" rel="stylesheet">
<script src="/js/jquery.uploadfile.js"></script>

<link href="http://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.12/summernote.css" rel="stylesheet">
<script src="http://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.12/summernote.js"></script>
 
<style>
blockquote {
 border-left:5px solid #f86466;
 background:white
}
</style>
<script>
   function openBoardList() {
	  var comSubmit = new commonSubmit();
	  comSubmit.setUrl("<c:url value='/sample/openBoardList'/>");
	  comSubmit.submit();
   }
   
</script>
	<div id="doc">
	<form id="frm" name="frm" >
		<table class="board_view">
			<colgroup>
				<col width="15%">
				<col width="*">
			</colgroup>
			<caption>게시글 작성</caption>
			<tbody>
				<tr>
					<th scope="row">제목</th>
					<td><input type="text" id="title" name="title" class="wdp_90" /></td>					
				</tr>
				<tr>
					<th scope="row">내용</th>
					<td class="view_text">						
						<textarea id="summernote" name="contents"></textarea>						 
					</td>
				</tr>
				<tr>
					<th scope="row">첨부</th>
					<td class="view_file">						 
						<div id="fileuploader">Upload</div>						
					</td>
				</tr>
			</tbody>
		</table>		
		<br/><br/>
		
		<a href="#this" class="btn" id="upload">Ajax 저장하기</a>		
		<a href="#this" class="btn" id="list">목록으로</a>
	</form>
	</div>
	
	<script type="text/javascript">
		$("#frm").hide();
		$(document).ready(function(){			
			$("#list").on("click", function(e) {
				e.preventDefault();
				openBoardList();
			});			
						
			$("#upload").on("click", function(e) {
				e.preventDefault();
				uploadObj.startUpload();
			});
			
			// SingleFile 설정 : maxFileCount = 1
			// MultiFile 설정 : maxFileCount > 1
			// dragDrop은 항상 true
			var uploadObj = $("#fileuploader").uploadFile({
				formName:"frm",
				url:"/sample/insertBoard.json",	
				uploadStr:"파일선택",
				maxFileSize:10*1024*1024,				
				maxFileCount:3,
				showFileCounter:false,
				showFileSize:true,
				allowDuplicates:false,
				onSuccess: function (data, message) {
					console.log("data \t:\n" + JSON.stringify(data));
								
					if (data.forward) document.location.href = data.forward;
					else alert('처리중 오류가 발생했습니다.\n잠시 후 다시 시도하십시오.');
					
				},
				onError: function (status, errMsg) {
					console.log(status);
					console.log(errMsg);
					
					alert('처리중 오류가 발생했습니다.\n잠시 후 다시 시도하십시오.');
				}
			});
			
			$('#summernote').summernote({
				shortcuts: false,					
				placeholder: '내용을 입력하세요.',
				height: 150,
				codeviewFilter: false,				
				fontNames: ['Arial', 'Arial Black', 'Comic Sans MS', 'Courier New', '굴림체', '돋움체', '맑은 고딕'],
				toolbar: [
				  ['style', ['style']],
				  ['font', ['bold', 'italic', 'underline', 'strikethrough', 'clear']],
				  ['fontname', ['fontname']],
				  ['fontsize', ['fontsize']],
				  ['color', ['color']],
				  ['para', ['ul', 'ol', 'paragraph']],
				  ['table', ['table']],
				  ['insert', ['link', 'picture']],				  
				  ['undo', ['undo', 'redo']]
				],
				callbacks: {
					onImageUpload: function(files, editor, welEditable) {
						for (var i=0; i<files.length; i++) {
							uploadFile(files[i], this, "frm");
						}
					}
				}
			});
			
			$("#frm").show();
		});
	</script>
