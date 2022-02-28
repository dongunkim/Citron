package com.dinens.citron.p.admin.common.domain;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.dinens.citron.p.admin.common.util.ContextUtils;

/**
 * <PRE>
 * 1. ClassName: BaseVO
 * 2. FileName : BaseVO.java
 * 3. Package  : com.dinens.citron.common.common
 * 4. Comment  : 모든 Domain의 Base bean               
 * 5. 작성자   : JYPark
 * 6. 작성일   : 2019. 7. 31.
 * 7. 변경이력
 *		이름	:	일자	: 변경내용
 *     ———————————————————————————————————
 *		JYPark:	2019. 07. 31.	: 신규 개발.
  * </PRE>
 */

public class BaseVO implements Serializable {

	private static final long serialVersionUID = -4646884430465610026L;
			
	private String locale;
		
	public BaseVO() {
		super();
		this.locale = ContextUtils.getLocaleFromSession();
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}	
	
}
