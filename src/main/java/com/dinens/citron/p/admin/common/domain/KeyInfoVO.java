package com.dinens.citron.p.admin.common.domain;

import java.security.PrivateKey;

/**
 * <PRE>
 * 1. ClassName: KeyInfoVO
 * 2. FileName : KeyInfoVO.java
 * 3. Package  : com.dinens.citron.sample.domain
 * 4. Comment  : Sample 암호화 키 정보 Domain
 *               TABLE : TB_KEY_INFO
 * 5. 작성자   : JYPark
 * 6. 작성일   : 2019. 7. 31.
 * 7. 변경이력
 *		이름	:	일자	: 변경내용
 *     ———————————————————————————————————
 *		JYPark:	2019. 07. 31.	: 신규 개발.
  * </PRE>
 */

public class KeyInfoVO extends BaseVO {
	
	private static final long serialVersionUID = 8173106505119219583L;
	/*
	 * Table 항목 
	 */
	private String sessionId;
	private String privateKeyStr;	
	private String createTime;
	
	/*
	 * 추가 항목 
	 */
	private PrivateKey privateKey;
	
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getPrivateKeyStr() {
		return privateKeyStr;
	}
	public void setPrivateKeyStr(String privateKeyStr) {
		this.privateKeyStr = privateKeyStr;
	}
	public PrivateKey getPrivateKey() {
		return privateKey;
	}
	public void setPrivateKey(PrivateKey privateKey) {
		this.privateKey = privateKey;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}	
}
