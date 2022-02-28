package com.dinens.citron.p.admin.common.exception;

/**
 * <PRE>
 * 1. ClassName: ApplicationException
 * 2. FileName : ApplicationException.java
 * 3. Package  : com.dinens.citron.common.exception
 * 4. Comment  : 사용자 정의 Exception (Application Process 과정에서의 예외 처리) 
 * 5. 작성자   : JYPark
 * 6. 작성일   : 2019. 7. 31.
 * 7. 변경이력
 *		이름	:	일자	: 변경내용
 *     ———————————————————————————————————
 *		JYPark:	2019. 07. 31.	: 신규 개발.
  * </PRE>
 */

public class ApplicationException extends RuntimeException {
	
	private static final long serialVersionUID = -6774269906040542601L;

	public ApplicationException() {}
	
	public ApplicationException(String message) { super(message); }
}
