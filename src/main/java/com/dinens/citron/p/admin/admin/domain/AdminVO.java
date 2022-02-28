package com.dinens.citron.p.admin.admin.domain;

import com.dinens.citron.p.admin.common.domain.ExtendVO;

/**
 * @author dongu
 *
 */
public class AdminVO extends ExtendVO {

	private static final long serialVersionUID = -5443960170890254663L;

	/*
	 * Table 항목 
	 */
	private String adminId;
	private String adminName;
	private String adminGroupId;
	private String adminGroupName;
	private String password;
	private String passwordSalt;
	private String passwordUpdateTime;
	private String phoneNumber;
	private String email;
	private String useYn;
	private String createId;
	private String createTime;
	private String updateId;
	private String updateTime;

	/*
	 * 추가 항목
	 */
	private String condAdminGroupId;
	private String newPassword;
	
	public String getAdminId() {
		return adminId;
	}
	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}
	public String getAdminName() {
		return adminName;
	}
	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPasswordSalt() {
		return passwordSalt;
	}
	public void setPasswordSalt(String passwordSalt) {
		this.passwordSalt = passwordSalt;
	}
	public String getPasswordUpdateTime() {
		return passwordUpdateTime;
	}
	public void setPasswordUpdateTime(String passwordUpdateTime) {
		this.passwordUpdateTime = passwordUpdateTime;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public String getCondAdminGroupId() {
		return condAdminGroupId;
	}
	public void setCondAdminGroupId(String condAdminGroupId) {
		this.condAdminGroupId = condAdminGroupId;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}	
}
