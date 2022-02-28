package com.dinens.citron.p.admin.admingroup.domain;

import com.dinens.citron.p.admin.common.domain.ExtendVO;

public class AdminGroupVO extends ExtendVO {
	private static final long serialVersionUID = 817863202994425884L;

	/*
	 * Table 항목 
	 */
	private String adminGroupId;
	private String adminGroupName;
	private String useYn;
	private String createId;
	private String createTime;
	private String updateId;
	private String updateTime;

	/*
	 * 추가 항목
	 */
	private String condUseYn;
	private String condAdminGroupName;
	private String menuIds;

	public String getAdminGroupId() {
		return adminGroupId;
	}
	public void setAdminGroupId(String adminGroupId) {
		this.adminGroupId = adminGroupId;
	}
	public String getAdminGroupName() {
		return adminGroupName;
	}
	public void setAdminGroupName(String adminGroupName) {
		this.adminGroupName = adminGroupName;
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
	public String getCondUseYn() {
		return condUseYn;
	}
	public void setCondUseYn(String condUseYn) {
		this.condUseYn = condUseYn;
	}
	public String getCondAdminGroupName() {
		return condAdminGroupName;
	}
	public void setCondAdminGroupName(String condAdminGroupName) {
		this.condAdminGroupName = condAdminGroupName;
	}
	public String getMenuIds() {
		return menuIds;
	}
	public void setMenuIds(String menuIds) {
		this.menuIds = menuIds;
	}

}
