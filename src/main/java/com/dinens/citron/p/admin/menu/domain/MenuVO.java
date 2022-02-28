package com.dinens.citron.p.admin.menu.domain;

import com.dinens.citron.p.admin.common.domain.ExtendVO;

/**
 * <PRE>
 * 1. ClassName: MenuVO
 * 2. FileName : MenuVO.java
 * 3. Package  : com.dinens.citron.menu.domain
 * 4. Comment  : 메뉴 Domain
 *               TABLE : T_MENU
 * 5. 작성자   : 김동언
 * 6. 작성일   : 2019. 8. 26.
 * 7. 변경이력
 *		이름	:	일자	: 변경내용
 *     ———————————————————————————————————
 *		김동언:	2019. 08. 26.	: 신규 개발.
  * </PRE>
 */

public class MenuVO extends ExtendVO {

	private static final long serialVersionUID = -4208416723876775855L;

	/*
	 * Table 항목 
	 */
	private String menuId;
	private String menuName;
	private String[] localeCode;
	private String[] menuNameLocale;
	private String upperMenuId;
	private String upperMenuName;
	private String screenLinkYn;
	private String screenUri;
	private String depth;
	private String displayOrder;
	private String useYn;
	private String createId;
	private String createTime;
	private String updateId;
	private String updateTime;
	
	public String getMenuId() {
		return menuId;
	}
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public String[] getLocaleCode() {
		return localeCode;
	}
	public void setLocaleCode(String[] localeCode) {
		this.localeCode = localeCode;
	}
	public String[] getMenuNameLocale() {
		return menuNameLocale;
	}
	public void setMenuNameLocale(String[] menuNameLocale) {
		this.menuNameLocale = menuNameLocale;
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
	public String getDepth() {
		return depth;
	}
	public void setDepth(String depth) {
		this.depth = depth;
	}
	public String getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(String displayOrder) {
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

}
