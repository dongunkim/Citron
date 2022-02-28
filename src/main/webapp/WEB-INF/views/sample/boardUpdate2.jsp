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

<script type="text/javascript">
    var fileCount = "${fn:length(list)+1}";
    
	function openBoardList() {
		var comSubmit = new commonSubmit();
		comSubmit.setUrl("<c:url value='/sample/openBoardList'/>");
		comSubmit.submit();
	}
	
	function deleteBoard() {
		var idx = "${boardVO.idx}";
		var comSubmit = new commonSubmit();
		comSubmit.setUrl("<c:url value='/sample/deleteBoard'/>");
		comSubmit.addParam("idx", idx);
		comSubmit.submit();
	}
	
	function postNaviWithParm(response) {
		var idx = "${boardVO.idx}";
		var objs = document.createElement('input');
		objs.setAttribute('type', 'hidden');
		objs.setAttribute('name', 'idx');
		objs.setAttribute('value', idx);

		var form = document.createElement('form');
		form.appendChild(objs);
		form.setAttribute('method', 'post');
		form.setAttribute('action', response.forward);
		
		document.body.appendChild(form);

		form.submit();
	}
		
</script>

	<form id="frm" name="frm" enctype="multipart/form-data">
	<table class="board_view">
		<colgroup>
			<col width="15%"/>
			<col width="35%"/>
			<col width="15%"/>
			<col width="35%"/>
		</colgroup>
		<caption>게시글 상세</caption>
		<tbody>
			<tr>
				<th scope="row">글 번호</th>
				<td>${boardVO.idx}<input type="hidden" id="idx" name="idx" value="${boardVO.idx}"/></td>
				<th scope="row">조회수</th>
				<td>${boardVO.hitCnt}</td>
			</tr>
			<tr>
				<th scope="row">작성자</th>
				<td>${boardVO.creaId}</td>
				<th scope="row">작성시간</th>
				<td>${boardVO.creaDtm}</td>
			</tr>
			<tr>
				<th scope="row">제목</th>
				<td colspan="3"><input type="text" id="title" name="title" class="wdp_90" value="${boardVO.title}"/></td>
			</tr>
			<tr>
				<th scope="row">내용</th>
				<td colspan="3" class="view_text"><textarea id="summernote" name="contents">${boardVO.contents}</textarea></td>
			</tr>
			<tr>
				<th scope="row">첨부</th>
				<td colspan="3" class="view_file">					
					<div id="fileuploader">Upload</div>
					<div id="fileDiv">
						<p>
						<c:forEach var="row" items="${list}" varStatus="var">
							<input type="hidden" id="fileIdx_${row.idx}" name="fileIdx" value="${row.idx}"/>						
						</c:forEach>
						</p>
					</div>
				</td>
			</tr>
		</tbody>
	</table>
	
	<a href="#this" class="btn" id="list">목록으로</a>
	<a href="#this" class="btn" id="upload">Ajax 저장하기</a>	
	<a href="#this" class="btn" id="delete">삭제하기</a>
		
	</form>		
		
	<script type="text/javascript">
		$("#frm").hide();
		
		$(document).ready(function() {			
			
			$("#list").on("click", function(e) {
				e.preventDefault();
				openBoardList();
			});
			
			$("#upload").on("click", function(e) {
				e.preventDefault();
				uploadObj.startUpload();				
			});
			
			$("#delete").on("click", function(e) {
				e.preventDefault();
				deleteBoard();
			});
						
			// SingleFile 설정 : maxFileCount = 1
			// MultiFile 설정 : maxFileCount > 1
			// dragDrop은 항상 true
			// 수정화면에서는 파일 처리를 위해 onLoad와 deleteCallback 추가
			var uploadObj = $("#fileuploader").uploadFile({
				formName:"frm",
				url:"/sample/updateBoard.json",	
				uploadStr:"파일선택",
				maxFileSize:10*1024*1024,				
				maxFileCount:3,
				showFileCounter:false,
				showFileSize:true,
				allowDuplicates:false,				
				onLoad: function (obj) {
					<c:forEach var="row" items="${list}" varStatus="var">					
						obj.createProgress("${row.idx}", "${row.originalFileName}", "${row.fileSize}");
					</c:forEach>
				},
				deleteCallback: function (fileIdx, fileName) {					
					$("#fileIdx_" + fileIdx).remove();
				},
				onSuccess: function (data, message, xhr) {
					console.log("data \t:\n" + JSON.stringify(data));
										
					if (data.forward) postNaviWithParm(data);						
					else alert('처리중 오류가 발생했습니다.\n잠시 후 다시 시도하십시오.');
				},
				onError: function (status, errMsg) {					
					alert('처리중 오류가 발생했습니다.\n잠시 후 다시 시도하십시오.');
				}
			});
			
			$('#summernote').summernote({				
				shortcuts: false,						
				placeholder: '내용을 입력하세요.',
				height: 150,				
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
			console.log($("#summernote").val());
			
		});
	</script>
