<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/include-header.jspf" %>

<script>
   var fileCount = 1;
   
   function openBoardList() {
	  var comSubmit = new commonSubmit();
	  comSubmit.setUrl("<c:url value='/sample/openBoardList'/>");
	  comSubmit.submit();
   }
   
   function insertBoard() {	  
	  var comSubmit = new commonSubmit("frm");	  
	  comSubmit.setUrl("<c:url value='/sample/insertBoard'/>");
	  comSubmit.submit();
   }
   
   function addFile() {
	   var str = "<p><input type='file' name='file_" + (fileCount++) + "'><a href='#this' class='btn' name='delete'>삭제</a></p>";
	   $("#fileDiv").append(str);
	   $("a[name='delete']").on("click", function(e) {
			e.preventDefault();
			deleteFile($(this));
		});
   }
   
   function deleteFile(obj) {	   
	   obj.parent().remove();
   }
</script>

	<form id="frm" name="frm" enctype="multipart/form-data">
		<table class="board_view">
			<colgroup>
				<col width="15%">
				<col width="*">
			</colgroup>
			<caption>게시글 작성</caption>
			<tbody>
				<tr>
					<th scope="row">제목</th>
					<td><input type="text" id="TITLE" name="TITLE" class="wdp_90" /></td>					
				</tr>
				<tr>
					<td colspan="2" class="view_text">
						<textarea rows="20" cols="100" title="내용" id="CONTENTS" name="CONTENTS"></textarea>
					</td>
				</tr>
			</tbody>
		</table>
		<div id="fileDiv">
			<p>
				<input type="file" id="file" name="file_0"/>
				<a href="#this" class="btn" id="delete" name="delete">삭제</a>
			</p>
		</div>
		<div>
			<p>
				<input type="file" id="files" multiple>
			</p>
		</div>
		<br/><br/>
		
		<a href="#this" class="btn" id="addFile">파일추가</a>
		<a href="#this" class="btn" id="save">저장하기</a>
		<a href="#this" class="btn" id="list">목록으로</a>
	</form>
	
	<script type="text/javascript">
		$(document).ready(function(){ 
			$("#list").on("click", function(e) {
				e.preventDefault();
				openBoardList();
			});
			
			$("#save").on("click", function(e) {
				e.preventDefault();
				insertBoard();
			});
			
			$("#addFile").on("click", function(e) {
				e.preventDefault();
				addFile();
			});
			
			$("a[name='delete']").on("click", function(e) {
				e.preventDefault();
				deleteFile($(this));
			});
		});
	</script>
