
/**
 * 
* uploadFile의 공통 설정에 대한 Wrapper
*  
*/

(function($){

	$.extend({
		uploadWrapper: function (options) {
			var settings = $.extend({
				formName:"frm",
				url:"",
				uploadStr:i18n("ui_common_label_selectFile"),
				maxFileSize:10*1024*1024,				
				maxFileCount:3,
				showFileCounter:false,
				showFileSize:true,
				allowDuplicates:false,
				onSuccess: function (data, message) {
					console.log("data \t:\n" + JSON.stringify(data));
								
					if (data.forward) document.location.href = data.forward;
					else Alert(i18n("ui_common_ajax_error"));	// 처리중 오류가 발생했습니다.\n잠시 후 다시 시도하십시오.					
				},
				onError: function (status, errMsg) {}			
			}, options);
			
			var uploadObj = $("#fileuploader").uploadFile(settings);
			     
			this.startUpload = function () {
				uploadObj.startUpload();
			}
			
			return uploadObj;
		}
	});
})(jQuery);
