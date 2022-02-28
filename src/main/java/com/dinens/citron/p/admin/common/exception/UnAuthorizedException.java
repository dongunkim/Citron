package com.dinens.citron.p.admin.common.exception;

/**
 * <PRE>
 * 1. ClassName: UnAuthorizedException
 * 2. FileName : UnAuthorizedException.java
 * 3. Package  : com.dinens.citron.common.exception
 * 4. Comment  : 사용자 정의 Exception 
 * 5. 작성자   : JYPark
 * 6. 작성일   : 2019. 7. 31.
 * 7. 변경이력
 *		이름	:	일자	: 변경내용
 *     ———————————————————————————————————
 *		JYPark:	2019. 07. 31.	: 신규 개발.
  * </PRE>
 */

public class UnAuthorizedException extends RuntimeException {
	
	private static final long serialVersionUID = 2494563241904278168L;
	
	public UnAuthorizedException() {}
	
	public UnAuthorizedException(String message) { super(message); }
}
