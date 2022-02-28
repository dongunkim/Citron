package com.dinens.citron.p.admin.common.domain;

/**
 * <PRE>
 * 1. ClassName: EmailSendHistVO
 * 2. FileName : EmailSendHistVO.java
 * 3. Package  : com.dinens.citron.p.admin.common.domain
 * 4. Comment  : 이메일 전송 히스토리 Domain
 *               TABLE : T_EMAIL_SEND_HIST
 * 5. 작성자   : JYPark
 * 6. 작성일   : 2019. 9. 19.
 * 7. 변경이력
 *		이름	:	일자	: 변경내용
 *     ———————————————————————————————————
 *		JYPark:	2019. 09. 19.	: 신규 개발.
  * </PRE>
 */

public class EmailSendHistVO extends ExtendVO {
	
	private static final long serialVersionUID = -2324685396947181398L;

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
