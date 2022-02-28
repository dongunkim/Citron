package com.dinens.citron.p.admin.admingroupauth.domain;

import com.dinens.citron.p.admin.common.domain.ExtendVO;

public class AdminGroupAuthVO extends ExtendVO {
	private static final long serialVersionUID = -654422121196261670L;

	/*
	 * Table 항목 
	 */
	private String adminGroupId;
	private String menuId;
	private String createId;
	private String createTime;
	
	public String getAdminGroupId() {
		return adminGroupId;
	}
	public void setAdminGroupId(String adminGroupId) {
		this.adminGroupId = adminGroupId;
	}
	public String getMenuId() {
		return menuId;
	}
	public void setMenuId(String menuId) {
		this.menuId = menuId;
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

}
