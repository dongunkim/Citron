
/**
 * 
* Code Value를 가져와 SelectBox에 Setting하는 공통 스크립트
*  
*/
(function ($) {
	$.fn.codeSetter = function (options) {		
		var s = $.extend({            
        	url: "/member/codeValueList.json",
        	targets: false
		 }, options);
        
		if (!s.targets) {
			if ( window.console ) {
				console.warn( "Nothing targets, can't be continue, returning nothing." );
			}
			return;
		}
		
		var self = this;
		
		var setter = $.data( this[0], "setter" );
		if ( setter ) {
			return setter;
		}
				
		setter = new $.setter(s);
		$.data( this[0], "setter", setter );
		                        
        $.each($.setter.settings, function(key, options) {
        	_getCode(s, key, options);        	
		});
        
        // 코드목록을 갱신할때 호출
        this.changeCode = function(target, codeId) {
        	var isMatched = false;
        	
        	if (isNull(target) || isNull(codeId)) {
        		if ( window.console ) {
    				console.warn( "Nothing targets, can't be continue, returning nothing." );
    			}
    			return;
        	}
        	
        	$.each($.setter.settings, function(key, options) {
        		if (target == key) {        			
        			isMatched = true;
        			var newOptions = $.extend( true, {}, options, { codeId: codeId, selected: 0 });
        			_getCode(s, key, newOptions);
        		}
        	});
        	
        	if (!isMatched) {
        		if ( window.console ) {
    				console.warn( "There's noting matched with target, returning nothing." );
    			}
    			return;
        	}        	
        };
        
        return this;
	};
	
	// setter 객체 생성
	$.setter = function(s) {		
		$.each(s.targets, function(key, options) {			
			$.setter.settings[key] = $.extend( true, {}, $.setter.defaults, options );			
		});
	};
	
	$.extend( $.setter, {
		defaults: {			
			type: 'select',													// 'select', 'radio', 'checkbox'
			codeId: false,
			blankOption: {
				name: "<spring:message code='ui.common.label.select'/>",	//선택
				value: ""
			},
			selected: 0
		},
		settings: {}
	});
	
	// 코드값 목록 조회
	function _getCode(s, key, options) {		
		
    	$.post({
			url: s.url,
			data: {	codeId : options.codeId },
			dataType: "json",
			success: function(data) {
				//console.log("data \t:\n" + JSON.stringify(data));
				_draw(key, options, data);
			},
			error: function(e) {
				return null;
			}
		});
    }
	
	// 데이터 설정
	// type에 따라 구분 ('select', 'radio', 'checkbox') --> 우선은 'select'만
	function _draw(key, options, data) {		
		$("#"+key).empty();
		
    	if (options.type == 'select') _draw_select(key, options, data);
	}
	
	// 'SelectBox' 데이터 설정
	function _draw_select(key, options, data) {
		var optionStr = "";		
		
		if (options.blankOption) {
			optionStr += "<option value='" + options.blankOption.value + "'>" + options.blankOption.name + "</option>";
		}

		var list = data.list;
		for (var i=0; i<list.length; i++) {
			optionStr += "<option value='" + list[i].value + "'>" + list[i].name + "</option>";
		}
		
		$("#"+key).append(optionStr);

		if (options.selected) {
			$("#"+key).val(options.selected);
		}
    }
	
}(jQuery));