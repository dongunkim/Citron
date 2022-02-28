package com.dinens.citron.p.admin.common.exception;

/**
 * <PRE>
 * 1. ClassName: AjaxException
 * 2. FileName : AjaxException.java
 * 3. Package  : com.dinens.citron.common.exception
 * 4. Comment  : Ajax 예외 처리를 위한 사용자 정의 Exception 
 * 5. 작성자   : JYPark
 * 6. 작성일   : 2019. 7. 31.
 * 7. 변경이력
 *		이름	:	일자	: 변경내용
 *     ———————————————————————————————————
 *		JYPark:	2019. 07. 31.	: 신규 개발.
  * </PRE>
 */

public class AjaxException extends RuntimeException {
		
	private static final long serialVersionUID = -3294568672839028565L;
	
	private int code;	
	private String status;
	private String message;
		
	public AjaxException(int code, String status, String message) {
		super(message);
		
		this.code = code;
		this.status = status;
		this.message = message;
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}	
}
