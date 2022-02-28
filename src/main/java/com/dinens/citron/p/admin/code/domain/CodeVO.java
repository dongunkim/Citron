package com.dinens.citron.p.admin.code.domain;

import com.dinens.citron.p.admin.common.domain.ExtendVO;

public class CodeVO extends ExtendVO {

	private static final long serialVersionUID = 4888826161591891248L;

	/*
	 * Table 항목 
	 */
	private String codeId;
	private String codeName;
	private String displayOrder;
	private String description;
	private String useYn;
	private String createId;
	private String createTime;
	private String updateId;
	private String updateTime;
	
	private String condCodeId;
	private String condCodeName;

	public String getCodeId() {
		return codeId;
	}
	public void setCodeId(String codeId) {
		this.codeId = codeId;
	}
	public String getCodeName() {
		return codeName;
	}
	public void setCodeName(String codeName) {
		this.codeName = codeName;
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
	public String getCondCodeId() {
		return condCodeId;
	}
	public void setCondCodeId(String condCodeId) {
		this.condCodeId = condCodeId;
	}
	public String getCondCodeName() {
		return condCodeName;
	}
	public void setCondCodeName(String condCodeName) {
		this.condCodeName = condCodeName;
	}

}
