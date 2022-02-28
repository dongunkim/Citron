package com.dinens.citron.p.admin.common.util;

import org.springframework.context.support.MessageSourceAccessor;

/**
 * <PRE>
 * 1. ClassName: MessageUtils
 * 2. FileName : MessageUtils.java
 * 3. Package  : com.dinens.citron.common.util
 * 4. Comment  : Message Properties로부터 Message를 쉽게 읽어오기 위한 Function 모음
 * 5. 작성자   : JYPark
 * 6. 작성일   : 2019. 7. 31.
 * 7. 변경이력
 *		이름	:	일자	: 변경내용
 *     ———————————————————————————————————
 *		JYPark:	2019. 07. 31.	: 신규 개발.
  * </PRE>
 */
public class MessageUtils {	
	private static MessageSourceAccessor messageSourceAccessor = null;
	
	public void setMessageSourceAccessor(MessageSourceAccessor msAcc) {
		MessageUtils.messageSourceAccessor = msAcc;
	}
	
	public static String getMessage(String code) {		
		return messageSourceAccessor.getMessage(code);
	}
	
	public static String getMessage(String code, Object[] objs) {		
		return messageSourceAccessor.getMessage(code, objs);
	}
}
