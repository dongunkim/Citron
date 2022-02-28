package com.dinens.citron.p.admin.code.domain;

import com.dinens.citron.p.admin.common.domain.ExtendVO;

public class CodeValueLocaleVO extends ExtendVO {

	private static final long serialVersionUID = -2201531258898219725L;

	private String codeId;
	private String value;
	private String localeCode;
	private String name;
	private String displayOrder;
	private String createId;
	private String createTime;
	
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
	public String getLocaleCode() {
		return localeCode;
	}
	public void setLocaleCode(String localeCode) {
		this.localeCode = localeCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(String displayOrder) {
		this.displayOrder = displayOrder;
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
