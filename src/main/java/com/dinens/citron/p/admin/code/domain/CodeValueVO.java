package com.dinens.citron.p.admin.code.domain;

import com.dinens.citron.p.admin.common.domain.ExtendVO;

public class CodeValueVO extends ExtendVO {

	private static final long serialVersionUID = -7249033450381171883L;
	
	/*
	 * Table 항목 
	 */
	private String codeId;
	private String value;
	private String name;
	private String[] LocaleCode;
	private String[] nameLocale;
	private String displayOrder;
	private String description;
	private String useYn;
	private String createId;
	private String createTime;
	private String updateId;
	private String updateTime;

	public String getCodeId() {
		return codeId;
	}
	public void setCodeId(String codeId) {
		this.codeId = codeId;
	}	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String[] getLocaleCode() {
		return LocaleCode;
	}
	public void setLocaleCode(String[] localeCode) {
		LocaleCode = localeCode;
	}
	public String[] getNameLocale() {
		return nameLocale;
	}
	public void setNameLocale(String[] nameLocale) {
		this.nameLocale = nameLocale;
	}
	public String getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(String displayOrder) {
		this.displayOrder = displayOrder;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
