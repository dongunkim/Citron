/********************************************************************
 * Citron common.util.js
 * 공통적으로 사용되는 Utils Javascript 모음
 * 
 * 1. isNull : Null 체크
 * 2. numberWithCommas : 숫자에 콤마 표시 
 * 3. Ajax 호출
 ********************************************************************/

/**
  * null 체크
 **/
function isNull(str) {
	if (str == null) return true;
	if (str == "NaN") return true;
	
	
	var chkStr = new String(str);
	if (chkStr.valueOf() == "undefined") return true;
	if (chkStr == null) return true;
	if (chkStr.toString().length == 0) return true;
	
	return false;
}


/**
  * 숫자에 콤마 찍어주기
 **/
function numberWithCommas(x) {
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}


/**
  * Ajax 호출함수
 **/
function serverCall(sendData, sendUrl, callback) {
	$.ajax({
		url: sendUrl,
		type: "POST",
		data: sendData,
		dataType: "json",
		success: function(data) {
			console.log("data \t:\n" + JSON.stringify(data));			
			callback(data);
		}
	});
}
