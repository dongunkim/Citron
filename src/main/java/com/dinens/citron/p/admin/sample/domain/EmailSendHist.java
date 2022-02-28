package com.dinens.citron.p.admin.sample.domain;

import com.dinens.citron.p.admin.common.domain.ExtendVO;

/**
 * <PRE>
 * 1. ClassName: EmailSendHist
 * 2. FileName : EmailSendHist.java
 * 3. Package  : com.dinens.citron.sample.domain
 * 4. Comment  : Sample 이메일 전송 히스토리 Domain
 *               TABLE : T_EMAIL_SEND_HIST
 * 5. 작성자   : JYPark
 * 6. 작성일   : 2019. 7. 31.
 * 7. 변경이력
 *		이름	:	일자	: 변경내용
 *     ———————————————————————————————————
 *		JYPark:	2019. 07. 31.	: 신규 개발.
  * </PRE>
 */

public class EmailSendHist extends ExtendVO {
	
	private static final long serialVersionUID = -128165538901106607L;
	
	/*
	 * Table 항목 
	 */
	private String emailSendId;
	private String email;
	private String sendReasonCode;
	private String sendStatus;
	private String createId;
	private String createTime;
	
	public String getEmailSendId() {
		return emailSendId;
	}
	public void setEmailSendId(String emailSendId) {
		this.emailSendId = emailSendId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSendReasonCode() {
		return sendReasonCode;
	}
	public void setSendReasonCode(String sendReasonCode) {
		this.sendReasonCode = sendReasonCode;
	}
	public String getSendStatus() {
		return sendStatus;
	}
	public void setSendStatus(String sendStatus) {
		this.sendStatus = sendStatus;
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
