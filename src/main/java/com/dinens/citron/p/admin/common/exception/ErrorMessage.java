package com.dinens.citron.p.admin.common.exception;

/**
 * <PRE>
 * 1. ClassName: ErrorMessage
 * 2. FileName : ErrorMessage.java
 * 3. Package  : com.dinens.citron.common.exception
 * 4. Comment  : 예외 정보를 담는 Bean Class 
 * 5. 작성자   : JYPark
 * 6. 작성일   : 2019. 7. 31.
 * 7. 변경이력
 *		이름	:	일자	: 변경내용
 *     ———————————————————————————————————
 *		JYPark:	2019. 07. 31.	: 신규 개발.
  * </PRE>
 */

public class ErrorMessage {

	protected int code;	
	protected String status;
	protected String message;
	
	public ErrorMessage(int code, String status, String message) {
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
