package com.dinens.citron.p.admin.menu.domain;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * <PRE>
 * 1. ClassName: InitMenuVO
 * 2. FileName : InitMenuVO.java
 * 3. Package  : com.dinens.citron.menu.domain
 * 4. Comment  : 초기로딩 메뉴 Domain으로 Menu Domain과 별개로 정의
 *               이유 - BaseVO를 상속받을 경우, InitService에서 오류 발생
 *               TABLE : T_MENU
 * 5. 작성자   : JYPARK
 * 6. 작성일   : 2019. 8. 27.
 * 7. 변경이력
 *		이름	:	일자	: 변경내용
 *     ———————————————————————————————————
 *		JYPARK:	2019. 08. 27.	: 신규 개발.
  * </PRE>
 */

public class InitMenuVO implements Serializable {
	
	private static final long serialVersionUID = 8880450467465541950L;
	
	/*
	 * Table 항목 
	 */
	private String menuId;
	private String localeCode;
	public String getLocaleCode() {
		return localeCode;
	}
	public void setLocaleCode(String localeCode) {
		this.localeCode = localeCode;
	}

	private String menuName;
	private String upperMenuId;
	private String upperMenuName;
	private String topMenuId;
	private String topMenuName;
	private String screenLinkYn;
	private String screenUri;
	private int depth;
	private int displayOrder;
	private String useYn;
	private String createId;
	private String createTime;
	private String updateId;
	private String updateTime;
	
	private String locale;

	public String getMenuId() {
		return menuId;
	}
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	public String getLocale() {
		return locale;
	}
	public void setLocale(String locale) {
		this.locale = locale;
	}	
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public String getUpperMenuId() {
		return upperMenuId;
	}
	public void setUpperMenuId(String upperMenuId) {
		this.upperMenuId = upperMenuId;
	}
	public String getUpperMenuName() {
		return upperMenuName;
	}
	public void setUpperMenuName(String upperMenuName) {
		this.upperMenuName = upperMenuName;
	}
	public String getTopMenuId() {
		return topMenuId;
	}
	public void setTopMenuId(String topMenuId) {
		this.topMenuId = topMenuId;
	}
	public String getTopMenuName() {
		return topMenuName;
	}
	public void setTopMenuName(String topMenuName) {
		this.topMenuName = topMenuName;
	}
	public String getScreenLinkYn() {
		return screenLinkYn;
	}
	public void setScreenLinkYn(String screenLinkYn) {
		this.screenLinkYn = screenLinkYn;
	}
	public String getScreenUri() {
		return screenUri;
	}
	public void setScreenUri(String screenUri) {
		this.screenUri = screenUri;
	}
	public int getDepth() {
		return depth;
	}
	public void setDepth(int depth) {
		this.depth = depth;
	}
	public int getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}
	public String getUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
	public String getCreateId() {
		return createId;
	}
	public void setCreateId(String createId) {
		this.createId = createId;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateId() {
		return updateId;
	}
	public void setUpdateId(String updateId) {
		this.updateId = updateId;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}
}
