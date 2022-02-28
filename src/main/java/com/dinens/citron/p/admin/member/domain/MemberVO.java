package com.dinens.citron.p.admin.member.domain;

import com.dinens.citron.p.admin.common.domain.ExtendVO;

/**
 * <PRE>
 * 1. ClassName: MemberVO
 * 2. FileName : MemberVO.java
 * 3. Package  : com.dinens.citron.p.admin.member.domain
 * 4. Comment  : 멤버(회원) Domain
 *               TABLE : T_MEMBER
 * 5. 작성자   : JYPark
 * 6. 작성일   : 2019. 9. 7.
 * 7. 변경이력
 *		이름	:	일자	: 변경내용
 *     ———————————————————————————————————
 *		JYPark:	2019. 09. 07.	: 신규 개발.
  * </PRE>
 */

public class MemberVO extends ExtendVO {

	private static final long serialVersionUID = 3757784119729749004L;
	
	/*
	 * Table 항목 
	 */
	private String memberId;
	private String memberName;
	private String password;
	private String passwordUpdateTime;
	private String idTypeCode;
	private String memberStatusCode;
	private String joinTime;
	private String useYn;
	private String createId;
	private String createTime;
	private String updateId;
	private String updateTime;
	
	
	/*
	 * 추가항목
	 */
	private String idTypeName;
	private String memberStatusName;
	private String condIdType;
	private String condMemberStatus;
	
	
	/*
	 * Getter & Setter
	 */
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPasswordUpdateTime() {
		return passwordUpdateTime;
	}
	public void setPasswordUpdateTime(String passwordUpdateTime) {
		this.passwordUpdateTime = passwordUpdateTime;
	}
	public String getIdTypeCode() {
		return idTypeCode;
	}
	public void setIdTypeCode(String idTypeCode) {
		this.idTypeCode = idTypeCode;
	}
	public String getMemberStatusCode() {
		return memberStatusCode;
	}
	public void setMemberStatusCode(String memberStatusCode) {
		this.memberStatusCode = memberStatusCode;
	}
	public String getJoinTime() {
		return joinTime;
	}
	public void setJoinTime(String joinTime) {
		this.joinTime = joinTime;
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
	public String getIdTypeName() {
		return idTypeName;
	}
	public void setIdTypeName(String idTypeName) {
		this.idTypeName = idTypeName;
	}
	public String getMemberStatusName() {
		return memberStatusName;
	}
	public void setMemberStatusName(String memberStatusName) {
		this.memberStatusName = memberStatusName;
	}
	public String getCondIdType() {
		return condIdType;
	}
	public void setCondIdType(String condIdType) {
		this.condIdType = condIdType;
	}
	public String getCondMemberStatus() {
		return condMemberStatus;
	}
	public void setCondMemberStatus(String condMemberStatus) {
		this.condMemberStatus = condMemberStatus;
	}
}
