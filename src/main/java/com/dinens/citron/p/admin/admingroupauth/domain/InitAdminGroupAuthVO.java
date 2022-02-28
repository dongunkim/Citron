package com.dinens.citron.p.admin.admingroupauth.domain;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * <PRE>
 * 1. ClassName: InitAdminGroupAuthVO
 * 2. FileName : InitAdminGroupAuthVO.java
 * 3. Package  : com.dinens.citron.admingroupauth.domain
 * 4. Comment  : 초기로딩 AdminGroupAuth Domain으로 AdminGroupAuth Domain과 별개로 정의
 *               이유 - BaseVO를 상속받을 경우, InitService에서 오류 발생
 *               TABLE : T_ADMIN_GROUP_AUTH
 * 5. 작성자   : JYPARK
 * 6. 작성일   : 2019. 8. 27.
 * 7. 변경이력
 *		이름	:	일자	: 변경내용
 *     ———————————————————————————————————
 *		JYPARK:	2019. 08. 27.	: 신규 개발.
  * </PRE>
 */

public class InitAdminGroupAuthVO implements Serializable  {
		
	private static final long serialVersionUID = 5480059472039775143L;
	
	/*
	 * Table 항목 
	 */
	private String adminGroupId;
	private String localeCode;
	private String menuId;
	
	public String getAdminGroupId() {
		return adminGroupId;
	}
	public void setAdminGroupId(String adminGroupId) {
		this.adminGroupId = adminGroupId;
	}
	public String getLocaleCode() {
		return localeCode;
	}
	public void setLocaleCode(String localeCode) {
		this.localeCode = localeCode;
	}
	public String getMenuId() {
		return menuId;
	}
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}	
}
