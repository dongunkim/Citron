/********************************************************************
 * Citron citron.js
 * 공통적으로 사용되는 디자인과 관련된 Javascript 모음
 * 
 * 1. $.datepicker : DatePicker 공통 설정
 ********************************************************************/

/*
 * 디자인과 관련된 javascript
 */
$(function() {
	//date picker
	if($('.datePicker').length != 0) {
		$(".datePicker").datepicker({			
			dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'],
			monthNames: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],
			dateFormat: "yy-mm-dd"
		});
		$.datepicker.setDefaults($.datepicker.regional['ko']);
	}

	if($('.nav-sidebar').length !=0){
		$('.nav-sidebar > li.has-child > a').on('click',function(){
			if($(this).parent('li').hasClass('open')){ // Open 상태이면
				$(this).parent('li').removeClass('open');
			}else{
				$(this).parent('li').addClass('open');
			}
		});
	}

});
