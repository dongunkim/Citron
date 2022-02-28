package com.dinens.citron.p.admin.menu.domain;

import com.dinens.citron.p.admin.common.domain.BaseVO;


public class MenuTreeVO extends BaseVO {

	private static final long serialVersionUID = 8560014381794228403L;

	private String id;
	private String parent;
	private String text;
	private String type;
	private String data;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
}
