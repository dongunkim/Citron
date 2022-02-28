/*!
 * Reference by Below. (2019.08.10)
 * 
 * jQuery Upload File Plugin
 * version: 4.0.11
 * @requires jQuery v1.5 or later & form plugin
 * Copyright (c) 2013 Ravishanker Kusuma
 * http://hayageek.com/
 */
(function ($) {
    if($.fn.ajaxForm == undefined) {
        $.getScript("/lib/jquery-uploadfile-3.1.10/jquery.form.js");
    }
    var feature = {};
    feature.fileapi = $("<input type='file'/>").get(0).files !== undefined;
    feature.formdata = window.FormData !== undefined;
    $.fn.uploadFile = function (options) {
        // These are the default options.
        var s = $.extend({            
        	url: "",
        	formName: false,
            method: "POST",
            enctype: "multipart/form-data",
            allowDuplicates: true,
            duplicateStrict: false,            
            allowedTypes: "*",            
            acceptFiles: "*",
            fileName: "fileLists",          
            maxFileSize: -1,
            maxFileCount: -1,
            multiple: true,
            dragDrop: true,            
            showCancel: true,            
            showError: true,       
            showFileCounter: true,
            fileCounterStyle: "). ",
            showFileSize: true,
            onLoad: function (obj) {},
            onSelect: function (files) {
                return true;
            },
            onSubmit: function (formData, form, options) {
            	return true;
            },
            onSuccess: function (response, message) {},
            onError: function (status, message) {},
            deleteCallback: false,
            serialize:true,
            cancelButtonClass: "ajax-file-upload-cancel",
            dragDropContainerClass: "ajax-upload-dragdrop",
            dragDropHoverClass: "state-hover",
            errorClass: "ajax-file-upload-error",
            uploadButtonClass: "btn btn-primary btn-sm",
            multiDragErrorStr: "Multiple File Drag & Drop is not allowed.",
            dragDropStr: "<span style='margin-left: 50px;'><b>Drag &amp; Drop Files</b></span>",            
            uploadStr:"Upload",            
            cancelStr: "&times",
            deleteStr: "&times",
            extErrorStr: "is not allowed. Allowed extensions: ",
            duplicateErrorStr: "is not allowed. File already exists.",
            sizeErrorStr: "is not allowed. Allowed Max size: ",            
            maxFileCountErrorStr: " is not allowed. Maximum allowed files are:",            
            customErrorKeyStr: "jquery-upload-file-error",
//            statusBarWidth: 400,
//            dragdropWidth: 400,
            headers: {}
        }, options);
                
        this.fileCounter = 1;
        this.selectedFiles = 0;
        var formGroup = "ajax-file-upload-" + (new Date().getTime());
        this.formGroup = formGroup;
                
        this.existingFileNames = [];
        this.fileData = [];
        
        if(!feature.formdata) //check drag drop enabled.
        {
            s.dragDrop = false;
        }
        if(!feature.formdata || s.maxFileCount === 1) {
            s.multiple = false;
        }
        if(!s.formName) {
        	alert('Form Name should be specified.');
        	return;
        }
        $("#"+s.formName).attr("method", s.method);
        $("#"+s.formName).attr("action", s.url);
        $("#"+s.formName).attr("enctype", s.enctype);
        
        $(this).html("");
        var obj = this;

        var uploadLabel = $('<div>' + s.uploadStr + '</div>');
        $(uploadLabel).addClass(s.uploadButtonClass);
        
        var maxFileCountStr = $("<span><b> (" + obj.selectedFiles + "/" + s.maxFileCount + ")</b></span>");
        
        (function checkFileFormLoaded() {
            if($.fn.ajaxForm) {

                if(s.dragDrop) {
                    var dragDrop = $('<div class="' + s.dragDropContainerClass + '" style="vertical-align:top;"></div>').width(s.dragdropWidth);
                    $(obj).append(dragDrop);
                    $(dragDrop).append(uploadLabel);
                    $(dragDrop).append($(s.dragDropStr));
                    $(dragDrop).append($(maxFileCountStr));
                    setDragDropHandlers(obj, s, dragDrop);

                } else {
                    $(obj).append(uploadLabel);
                }
   				
                obj.container = $("<div class='ajax-file-upload-container'></div>").insertAfter($(obj));                
                
                // 로딩 Callback 시, Object를 인식하지 못하는 경우가 있어 Delay를 줌
                setTimeout(function() {
                	s.onLoad.call(this, obj);
				}, 100);
                
                createCustomInputFile(obj, formGroup, s, uploadLabel);
                ajaxFormSubmit();
                
                
            } else window.setTimeout(checkFileFormLoaded, 10);
        })();

        this.startUpload = function () {	   		
        	$("#"+s.formName).submit();
        }
        
        this.getFileCount = function () {
            return obj.selectedFiles;
        }               
        
        this.createProgress = function (fileidx,filename,filesize) {
        	
            var pd = new createProgressDiv(this, s);            
                        
            var fileNameStr = "";
            if(s.showFileCounter)
            	fileNameStr = obj.fileCounter + s.fileCounterStyle + filename;
            else fileNameStr = filename;

            if(s.showFileSize)
				fileNameStr += " ("+getSizeStr(filesize)+")";
            
            pd.filename.prepend("<span style='color:navy'>" + fileNameStr + "</span>");
            
            obj.fileCounter++;
            obj.selectedFiles++;
            $(maxFileCountStr).html("<span><b> (" + obj.selectedFiles + "/" + s.maxFileCount + ")</b></span>");
            
            setDeleteHandler(s, pd, fileidx, filename, obj);
            
            return pd;
        }
        
        function setDragDropHandlers(obj, s, ddObj) {
            ddObj.on('dragenter', function (e) {
                e.stopPropagation();
                e.preventDefault();
                $(this).addClass(s.dragDropHoverClass);
            });
            ddObj.on('dragover', function (e) {
                e.stopPropagation();
                e.preventDefault();
                var that = $(this);
                if (that.hasClass(s.dragDropContainerClass) && !that.hasClass(s.dragDropHoverClass)) {
                    that.addClass(s.dragDropHoverClass);
                }
            });
            ddObj.on('drop', function (e) {
                e.preventDefault();
                $(this).removeClass(s.dragDropHoverClass);
                
                var files = e.originalEvent.dataTransfer.files;                
                if(!s.multiple && files.length > 1) {
                	if(s.showError) alert(s.multiDragErrorStr);
                    return;
                }
                if(s.onSelect(files) == false) return;
                serializeAndUploadFiles(s, obj, files);
            });
            ddObj.on('dragleave', function (e) {
                $(this).removeClass(s.dragDropHoverClass);
            });

            $(document).on('dragenter', function (e) {
                e.stopPropagation();
                e.preventDefault();
            });
            $(document).on('dragover', function (e) {
                e.stopPropagation();
                e.preventDefault();
                var that = $(this);
                if (!that.hasClass(s.dragDropContainerClass)) {
                    that.removeClass(s.dragDropHoverClass);
                }
            });
            $(document).on('drop', function (e) {
                e.stopPropagation();
                e.preventDefault();
                $(this).removeClass(s.dragDropHoverClass);
            });

        }
        
        function getSizeStr(size) {        	
            var sizeStr = "";
            var sizeKB = Math.round(size / 1024);
            if(parseInt(sizeKB) >= 1024) {
                var sizeMB = Math.round(sizeKB / 1024);
                sizeStr = sizeMB + " MB";
            } else {
                sizeStr = sizeKB + " KB";
            }
            return sizeStr;
        }
       
        function serializeAndUploadFiles(s, obj, files) {        	        	        	
            for(var i = 0; i < files.length; i++) {
                if(!isFileTypeAllowed(obj, s, files[i].name)) {                    
                	if(s.showError) alert(files[i].name + s.extErrorStr + s.allowedTypes);
                    continue;
                }
                if(!s.allowDuplicates && isFileDuplicate(obj, files[i].name)) {
                	if(s.showError) alert(files[i].name + s.duplicateErrorStr);
                    continue;
                }
                if(s.maxFileSize != -1 && files[i].size > s.maxFileSize) {
                    if(s.showError) alert(files[i].name + s.sizeErrorStr + getSizeStr(s.maxFileSize));
                    continue;
                }
                if(s.maxFileCount != -1 && obj.selectedFiles >= s.maxFileCount) {
                	if(s.showError) alert(files[i].name + s.maxFileCountErrorStr + s.maxFileCount);
                    continue;
                }
                obj.selectedFiles++;
                obj.existingFileNames.push(files[i].name);
                $(maxFileCountStr).html("<span><b> (" + obj.selectedFiles + "/" + s.maxFileCount + ")</b></span>");
                   
                var fileName = s.fileName.replace("[]", "");
                obj.fileData.push(files[i]);                
                
                var pd = new createProgressDiv(obj, s);
                var fileNameStr = "";
                if(s.showFileCounter) fileNameStr = obj.fileCounter + s.fileCounterStyle + files[i].name
                else fileNameStr = files[i].name;

				if(s.showFileSize)
				fileNameStr += " ("+getSizeStr(files[i].size)+")";
				
				pd.filename.prepend("<p class='form-group'>"+fileNameStr+"</p>");
				
                var fileArray = [];
                fileArray.push(files[i].name);
                
                setCancelHandler(s, pd, fileArray, obj);
                obj.fileCounter++;
            }            
        }

        function isFileTypeAllowed(obj, s, fileName) {
            var fileExtensions = s.allowedTypes.toLowerCase().split(/[\s,]+/g);
            var ext = fileName.split('.').pop().toLowerCase();
            if(s.allowedTypes != "*" && jQuery.inArray(ext, fileExtensions) < 0) {
                return false;
            }
            return true;
        }

        function isFileDuplicate(obj, filename) {
            var duplicate = false;
            if (obj.existingFileNames.length) {
                for (var x=0; x<obj.existingFileNames.length; x++) {
                    if (obj.existingFileNames[x] == filename
                        || s.duplicateStrict && obj.existingFileNames[x].toLowerCase() == filename.toLowerCase()
                    ) {
                        duplicate = true;
                    }
                }
            }
            return duplicate;
        }

        function removeExistingFileName(obj, fileArr) {
            if (obj.existingFileNames.length) {
                for (var x=0; x<fileArr.length; x++) {
                    var pos = obj.existingFileNames.indexOf(fileArr[x]);
                    if (pos != -1) {                    	
                        obj.existingFileNames.splice(pos, 1);
                        return pos;
                    }
                }
            }
        }
        
        function updateFileCounter(s, obj) {
            if(s.showFileCounter) {
                var count = $(obj.container).find(".ajax-file-upload-filename").length;
                obj.fileCounter = count + 1;
                $(obj.container).find(".ajax-file-upload-filename").each(function (i, items) {
                    var arr = $(this).html().split(s.fileCounterStyle);
                    var fileNum = parseInt(arr[0]) - 1;
                    var name = count + s.fileCounterStyle + arr[1];
                    $(this).html(name);
                    count--;
                });
            }
        }

        function createCustomInputFile (obj, group, s, uploadLabel) {

            var fileUploadId = "ajax-upload-id-" + (new Date().getTime());           
            
            var form = $("<form method='" + s.method + "' action='" + s.url + "' enctype='" + s.enctype + "'></form>");
            var fileInputStr = "<input type='file' id='" + fileUploadId + "' name='" + s.fileName + "' accept='" + s.acceptFiles + "'/>";
            
            if(s.multiple) {            	
                fileInputStr = "<input type='file' id='" + fileUploadId + "' name='" + s.fileName + "' accept='" + s.acceptFiles + "' multiple/>";
            }
            
            var fileInput = $(fileInputStr).appendTo(form);

            fileInput.change(function () {

                var fileExtensions = s.allowedTypes.toLowerCase().split(",");
                var fileArray = [];
                if(this.files)
                {
                    for(i = 0; i < this.files.length; i++) {
                        fileArray.push(this.files[i].name);                        
                    }

                    if(s.onSelect(this.files) == false) return;
                    
                } else {
                    var filenameStr = $(this).val();
                    var flist = [];
                    fileArray.push(filenameStr);
                    if(!isFileTypeAllowed(obj, s, filenameStr)) {
                    	if(s.showError) alert(filenameStr + s.extErrorStr + s.allowedTypes);
                    	return;
                    }
                    
                    flist.push({
                        name: filenameStr,
                        size: 'NA'
                    });
                    if(s.onSelect(flist) == false) return;

                }
                updateFileCounter(s, obj);
                                
                uploadLabel.unbind("click");
                form.hide();
                createCustomInputFile(obj, group, s, uploadLabel);
                form.addClass(group);
                if(s.serialize && feature.fileapi && feature.formdata)
                {
                    form.removeClass(group);
                    var files = this.files;
                    form.remove();
                    serializeAndUploadFiles(s, obj, files);                    
                } else {
                    var fileList = "";
                    for(var i = 0; i < fileArray.length; i++) {
                        if(s.showFileCounter) fileList += obj.fileCounter + s.fileCounterStyle + fileArray[i] + "<br>";
                        else fileList += fileArray[i] + "<br>";;
                        obj.fileCounter++;

                    }
                    if(s.maxFileCount != -1 && (obj.selectedFiles + fileArray.length) > s.maxFileCount) {
                    	if(s.showError) alert(fileList + s.maxFileCountErrorStr + s.maxFileCount);
                    	return;
                    }
                    obj.selectedFiles += fileArray.length;
                    $(maxFileCountStr).html("<span><b> (" + obj.selectedFiles + "/" + s.maxFileCount + ")</b></span>");
                    
                    var pd = new createProgressDiv(obj, s);
                    pd.filename.prepend(fileList);
                    setCancelHandler(s, pd, fileArray, obj);                    
                }
            });

            form.css({
                'margin': 0,
                'padding': 0
            });
            uploadLabel.css({
                position: 'relative',
                overflow: 'hidden',
                cursor: 'default'
            });
            fileInput.css({
                position: 'absolute',
                'cursor': 'pointer',
                'top': '0px',
                'width': '100%',
                'height': '100%',
                'left': '0px',
                'z-index': '100',
                'opacity': '0.0',
                'filter': 'alpha(opacity=0)',
                '-ms-filter': "alpha(opacity=0)",
                '-khtml-opacity': '0.0',
                '-moz-opacity': '0.0'
            });
            
            form.appendTo(uploadLabel);
        }
        
		function defaultProgressBar(obj,s)
		{
			this.statusbar = $("<div class='ajax-file-upload-statusbar'></div>").width(s.statusBarWidth);            
            this.filename = $("<div class='ajax-file-upload-filename form-inline'></div>").appendTo(this.statusbar);
            this.cancel = $("<div class='ajax-file-upload-red form-group'>" + s.cancelStr + "</div>").appendTo(this.filename).hide();
            this.del = $("<div class='ajax-file-upload-red form-group'>" + s.deleteStr + "</div>").appendTo(this.filename).hide();
            
			return this;
		}
		
        function createProgressDiv(obj, s) {
	        var bar = new defaultProgressBar(obj,s); 
	        
			$(obj.container).append(bar.statusbar);
            return bar;
        }      
        
        function setCancelHandler(s, pd, fileArray, obj) {
        	pd.cancel.show();
            pd.cancel.click(function () {
            	var pos = removeExistingFileName(obj, fileArray);
            	if (pos != -1) obj.fileData.splice(pos, 1);            	            	
                pd.statusbar.remove();                
                obj.selectedFiles -= fileArray.length;
                $(maxFileCountStr).html("<span><b> (" + obj.selectedFiles + "/" + s.maxFileCount + ")</b></span>");
                updateFileCounter(s, obj);                
            });	
        }
        
        function setDeleteHandler(s, pd, fileidx, filename, obj) {
        	pd.del.show();
            pd.del.click(function () {            	
                pd.statusbar.remove();
                var arr = [filename];
            	if(s.deleteCallback) s.deleteCallback.call(this, fileidx, filename);
                obj.selectedFiles -= 1;
                $(maxFileCountStr).html("<span><b> (" + obj.selectedFiles + "/" + s.maxFileCount + ")</b></span>");
                updateFileCounter(s, obj);                
            });
        }
        
        function ajaxFormSubmit() {       	
			
			var options = {
				type: s.method,
				url: s.url,
				enctype: s.enctype,
				datatype: s.returnType,
				beforeSubmit: function (formData, $form, options) {					
					var form = new FormData($("#"+s.formName).get(0));
					var files = obj.fileData;
					
					for (var i=0; i<files.length; i++) {
						form.append("fileLists", files[i]);						
					}
					options.formData = form;
					
					if (!s.onSubmit.call(this, formData, $form, options)) return;
				},
				success: function (data, message, xhr) {
					
					s.onSuccess.call(this, data, message);					
				},
				error: function (xhr, status, errMsg) {
										
					s.onError.call(this, xhr.status, xhr.responseText);
				}
			};
			
			$("#"+s.formName).ajaxForm(options);
        }
        
        return this;
    }
}(jQuery));
