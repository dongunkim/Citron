package com.dinens.citron.p.admin.common.domain;

/**
 * <PRE>
 * 1. ClassName: Result
 * 2. FileName : Result.java
 * 3. Package  : com.dinens.citron.common.common
 * 4. Comment  : Ajax Return bean               
 * 5. 작성자   : JYPark
 * 6. 작성일   : 2019. 7. 31.
 * 7. 변경이력
 *		이름	:	일자	: 변경내용
 *     ———————————————————————————————————
 *		JYPark:	2019. 07. 31.	: 신규 개발.
  * </PRE>
 */

public class Result extends BaseVO {

	private static final long serialVersionUID = 7734531898457581005L;
	
	/*
	 * 공통 항목
	 */
	private String status;		// Return 상태 (OK / FAIL)
	private String code;		// Return 코드 (상태값이 FAIL인 경우 해당 에러 코드 설정)
								// 공통 오류 0번대 (상세 코드는 Constants 참조)
								// 로그인 오류 100번대 (상세 코드는 Constants 참조)
								//
	private String message;		// Return 메시지 (상태값이 FAIL인 경우 해당 에러 메시지 설정)
	private String forward;		// Return 페이지 (상태값이 OK인 경우 전환할 페이지 설정 - 필요한 경우에만)
	
	private String publicKey;	// Client 데이터 암호화 공용키
	
	public Result() {
		super();
	}
	public Result(String status) {
		super();
		this.status = status;		
	}
	public Result(String status, String code, String message) {
		super();
		this.status = status;
		this.code = code;
		this.message = message;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getForward() {
		return forward;
	}
	public void setForward(String forward) {
		this.forward = forward;
	}
	
	public String getPublicKey() {
		return publicKey;
	}
	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}
		
}
