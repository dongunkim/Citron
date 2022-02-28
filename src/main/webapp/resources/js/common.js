
/********************************************************************
 * Citron common.js
 * 공통적으로 사용되는 Javascript 함수 모음
 * 
 * $.ajaxSetup : Ajax 호출 전 Request Header에 사용자 정의 속성 추가
 * $(document).ajaxError : Ajax 에러 시 공통 메시지 처리 
 * $.validator.setDefaults :  jQuery Validation 설정
 * $.validator.addMethod : Validation 사용자 정의 함수
 *                                lessThanEqualDate : 날짜비교 - fromDate에 설정(fromDate <= toDate)
 *                                greaterThanEqualDate : 날짜비교 - toDate에 설정(fromDate <= toDate)
 *                                withDate : 기간 설정이 필수가 아닌 경우에 둘다 값이 있거나 둘다 값이 없어야 함
 *                                
 * 1. commonSubmit : Form Submit
 * 2. imageUupload : 이미지를 서버의 임시경로로 업로드
 *                   summernote에서 이미지 삽입하는 경우에 호출
 * 3. downloadExcel : Excel File Download
 * 4. Alert : alert 팝업 레이어
 * 5. Confirm : confirm 팝업 레이어
 * 6. getPublicKey : 암호화 로그인을 위해 Public Key 조회
 * 7. loadi18nMessage : i18n 메시지 로딩
 * 8. i18n : jQuery에서 메시지 사용시 호출
 * 9. readSession : 세션 읽고 지우기
 *                      주로 리스트 화면에서 검색조건 유지를 위해 페이지 로딩 시 호출
 * 10. writeSession : 세션 저장하기
 *                        주로 리스트 화면에서 검색조건 유지를 위해 다른 페이지로 이동 시 호출
 * 11. setCondSearch : 검색조건 객체(searchData)로부터 각 Element에 값 설정                  
 *                            주로 리스트 화면에서 검색조건 유지를 위해 readSession 다음에 호출
 * 12. RichTextArea : Rich Text Editor인 Summernote 호출                  
 * 
 ********************************************************************/

/**
 * Ajax 호출 전 Request Header에 사용자 정의 속성 추가 
**/
$.ajaxSetup({
	beforeSend: function(jqXhr, settings) {
		jqXhr.setRequestHeader("AJAX", true);		
	}
});


/**
 * Ajax 에러 시 공통 메시지 처리 
**/
$(document).ajaxError(function(event, jqXhr, settings, thrownError) {
	if (jqXhr.status == 0)
		Alert(i18n("ui_common_ajax_error_status_0"));
	else if (jqXhr.status == 403)
		Alert(i18n("ui_common_ajax_error_status_403", jqXhr.status));
	else if (jqXhr.status == 404)
		Alert(i18n("ui_common_ajax_error_status_404", jqXhr.status));
	else
		Alert(i18n("ui_common_ajax_error_status_500", jqXhr.status));
	
	console.log("jqXhr \t: " + JSON.stringify(jqXhr));	
});


/**
 * jQuery Validation 설정
 * showAll : Custom option (true / false)
 *           Alert가 아닌 기본 에러 표시방법을 사용하는 경우에만 유효
 *           true : 에러메시지를 한번에 모두 표시 / false : 에러메시지를 하나씩만 표시
 *           디폴트는 false
**/
$.validator.setDefaults({		
	onkeyup:false,
    onclick:false,
    onfocusout:false,	    
    showErrors:function(errorMap, errorList){	    	
        if(this.numberOfInvalids()) {
        	var label = $(errorList[0].element).data('label');
            Alert("[" + label + "] " + errorList[0].message, function() {
            	$(errorList[0].element).focus();
            });	            
        }
    }
});
	
	
/**
  * Validation 사용자 정의 함수
 **/
// 날짜비교 - fromDate에 설정(fromDate <= toDate)
// toDate에 "greaterThanEqualDate"을 설정한 경우 설정 안해도 됨
$.validator.addMethod ("lessThanEqualDate", function(value, element, param) {		
	if (isNull($(param).val())) return true;
	
	if (value <= $(param).val()) return true;
	return false;
	}, 
	i18n("ui_common_validation_lessThanEqualDate")
);

// 날짜비교 - toDate에 설정(fromDate <= toDate)
// fromDate에 "lessThanEqualDate"을 설정한 경우 설정 안해도 됨
$.validator.addMethod ("greaterThanEqualDate", function(value, element, param) {		
	if (isNull($(param).val())) return true;
	
	if (value >= $(param).val()) return true;
	return false;
	}, 
	i18n("ui_common_validation_greaterThanEqualDate")
);

// 기간 설정이 필수가 아닌 경우에 둘다 값이 있거나 둘다 값이 없거나
// fromDate or toDate 중 한곳에만 설정(!fromDate && !toDate or fromDate && toDate)	
$.validator.addMethod ("withDate", function(value, element, param) {			
	if (isNull(value) && !isNull($(param).val()) ||
			!isNull(value) && isNull($(param).val())) return false;
	
	return true;		
	}, 
	i18n("ui_common_validation_withDate")
);		


/**
  * 공통 Form Sunmit
 **/
function commonSubmit(formId) {	
	this.formId = (isNull(formId) ? "commonForm" : formId);	
	this.url = "";
	
	
	if (this.formId == "commonForm") {
		$("#commonForm")[0].reset();		
	}
	
	this.setUrl = function setUrl(url) {
		this.url = url;
	}
	
	this.addParam = function addParam(key, value) {
		var checkVal = $("#"+this.formId).find("#" + key).val();
		if (typeof checkVal == 'undefined') {
			$("#"+this.formId).append($("<input type='hidden' name='" + key + "' id='" + key + "' value='" + value + "'>"));
		}
		else {
			$("#"+this.formId).find("#" + key).val(value);
		}				
	}
	
	this.submit = function submit() {
		var form = $("#" + this.formId)[0];
		form.action = this.url;
		form.method = "post";
		form.submit();		
	}				
}


/**
  * 임시 이미지 파일 업로드 (공통 임시경로)
  * summernote에서 이미지 삽입 시 호출
 **/
function imageUpload(file, el, form) {
	var formData = new FormData();
	formData.append("file", file);
	   
	$.ajax({
	  type: "POST",
	  url: "/common/imageUpload.json",
	  data: formData,
	  enctype: "multipart/form-data",
	  cache: false,
	  contentType: false,
	  processData: false,
	  success: function(result) {
		  console.log("result : " + JSON.stringify(result));
			  
		  $(el).summernote("editor.insertImage", result.url);
		  var inputStr = "<input type='hidden' name='imageUrl' value='" + result.url + "'>";
		  $("#"+form).append(inputStr);
	  }
	});
}


/**
 * Excel File Download
 * 주로 List 화면에서 Excel 다운로드 시 호출
**/
function downloadExcel(uri, condition) {				
	var preparingFileModal = $("#preparing-file-modal");
	
	preparingFileModal.dialog({modal: true});		
	$("#progressbar").progressbar({value: false});
			
	$.fileDownload(uri, {
		httpMethod: "POST",
		data : condition,
		successCallback: function(url) {
			preparingFileModal.dialog('close');	
		},
		
		failCallback: function(responseHtml, url) {
			console.log(responseHtml);
			preparingFileModal.dialog('close');
			$("#error-modal").dialog({modal: true});
		}
	});
}


/**
 * alert 팝업 레이어
**/
function Alert(message,callbackFunction){
	
	$("#alert_modal").modal();

	$('#btn-alert-ok').bind("click",function(){
		$('#alert_modal').modal('hide');
		if(callbackFunction != undefined){
			$('#btn-alert-ok').unbind("click");
			callbackFunction();
		}
	});

	message = message.replace(/\n/g, "<BR/>");
	$('#alert_modal .modal-body > p').html(message);
}


/**
 * confirm 팝업 레이어
**/
function Confirm(title,message,isCancelButton,callbackFunction){
	
	$("#common_modal").modal();

	$("#common_modal .modal-title").html(title);

	$('#btn-common-ok').bind("click",function(){
		$('#common_modal').modal('hide');
		if(callbackFunction != undefined){
			$('#btn-common-ok').unbind("click");
			callbackFunction();
		}
	});

	if(isCancelButton){
		$('#btn-common-cancel').show();
	} else {
		$('#btn-common-cancel').hide();
	}

	message = message.replace(/\n/g, "<BR/>");
	$('#common_modal .modal-body > p').html(message);
}


/**
 * 암호화 로그인을 위해 Public Key 조회
 */
function getPublicKey(callbackfunction) {
	$.ajax({
		url: "/common/getPubKey.json",
		type: "POST",
		dataType: "json",
		success: function(data) {	
			if (data.publicKey) {
				console.log("data \t:\n" + JSON.stringify(data));
				
				if (data.publicKey) callbackfunction(data);
				else Alert(i18n("ui_common_ajax_error"));	// 처리중 오류가 발생했습니다.\n잠시 후 다시 시도하십시오.				
			}
								
			else Alert(i18n("ui_common_ajax_error"));	// 처리중 오류가 발생했습니다.\n잠시 후 다시 시도하십시오.
		}
	});
}


/**
 * i18n 메시지 로딩
**/
function loadi18nMessage(lang) {	
	jQuery.i18n.properties({
	    name:'i18n_messages', 
	    path:'/i18n/', 
	    mode:'both',
	    cache: true,
	    language: lang,
	    async: true
	});
}


/**
  * jQuery에서 메시지 사용시 호출
 **/
function i18n() {	
    return jQuery.i18n.prop.apply(this, arguments);    
}


/**
 * 세션 읽고 지우기
 * 리스트 화면에서 검색조건 유지를 위해 페이지 로딩 시 호출
**/
function readSession(attrName) {
	
	if (sessionStorage.getItem(attrName) != null) {
		searchData = JSON.parse(sessionStorage.getItem(attrName));		
	}
	
	sessionStorage.removeItem(attrName);
}


/**
 * 세션 저장하기 
 * 리스트 화면에서 검색조건 유지를 위해 다른 페이지로 이동 시 호출
**/
function writeSession(attrName) {
	var formData = $("#frm").serializeObject();			
	sessionStorage.setItem(attrName, JSON.stringify(formData));
}


/**
 * 검색조건 객체(searchData)로부터 각 Element에 값 설정
 * 리스트 화면에서 검색조건 유지를 위해 readSession 다음에 호출
**/
function setCondSearch() {
	$.each(searchData, function(key, value) {
		$("#" + key).val(value);
	});		
}


/**
 * Rich Text Editor인 Summernote 호출
 * 주로 Textarea를 사용하는 게시판 등록, 수정화면에서 사용
**/
function RichTextArea(lang) {
	$('#summernote').summernote({			
		lang: lang,
		shortcuts: false,
		placeholder: i18n("ui_common_placeholder_content"),	// 내용을 입력하세요.
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
					imageUpload(files[i], this, "frm");
				}
			}
		}
	});
}

