package com.dinens.citron.p.admin.common.domain;

/**
 * <PRE>
 * 1. ClassName: IssueHistVO
 * 2. FileName : IssueHistVO.java
 * 3. Package  : com.dinens.citron.p.admin.common.domain
 * 4. Comment  : 발급 이력 Domain
 *               TABLE : T_ISSUE_HIST
 * 5. 작성자   : JYPark
 * 6. 작성일   : 2019. 9. 19.
 * 7. 변경이력
 *		이름	:	일자	: 변경내용
 *     ———————————————————————————————————
 *		JYPark:	2019. 09. 19.	: 신규 개발.
  * </PRE>
 */

public class IssueHistVO extends BaseVO {
	
	private static final long serialVersionUID = -817035560754288916L;
	/*
	 * Table 항목 
	 */
	private String issueSeq;
	private String issueType;
	private String issueKey;
	private String createId;
	private String createTime;
	
	/*
	 * 추가 항목 
	 */
	private int expire;

	public String getIssueSeq() {
		return issueSeq;
	}

	public void setIssueSeq(String issueSeq) {
		this.issueSeq = issueSeq;
	}

	public String getIssueType() {
		return issueType;
	}

	public void setIssueType(String issueType) {
		this.issueType = issueType;
	}

	public String getIssueKey() {
		return issueKey;
	}

	public void setIssueKey(String issueKey) {
		this.issueKey = issueKey;
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

	public int getExpire() {
		return expire;
	}

	public void setExpire(int expire) {
		this.expire = expire;
	}	
}
