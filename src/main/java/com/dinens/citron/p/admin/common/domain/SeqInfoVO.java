package com.dinens.citron.p.admin.common.domain;

/**
 * <PRE>
 * 1. ClassName: SeqInfoVO
 * 2. FileName : SeqInfoVO.java
 * 3. Package  : com.dinens.citron.common.domain
 * 4. Comment  : 시퀀스정보 Domain
 *               TABLE : T_SEQ_INFO
 * 5. 작성자   : JYPark
 * 6. 작성일   : 2019. 9. 4.
 * 7. 변경이력
 *		이름	:	일자	: 변경내용
 *     ———————————————————————————————————
 *		JYPark:	2019. 09. 04.	: 신규 개발.
  * </PRE>
 */
public class SeqInfoVO extends BaseVO {

	private static final long serialVersionUID = -6582005018171250532L;
	
	/*
	 * Table 항목 
	 */
	private String tableName;
	private String seqType;
	private String prefix;
	private String currYear;
	private String currMonth;
	private String currDay;
	private int currNo;
	private int cipers;
	private String fillChar;
	
	/*
	 * Getter & Setter
	 */
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getSeqType() {
		return seqType;
	}
	public void setSeqType(String seqType) {
		this.seqType = seqType;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getCurrYear() {
		return currYear;
	}
	public void setCurrYear(String currYear) {
		this.currYear = currYear;
	}
	public String getCurrMonth() {
		if (Integer.parseInt(currMonth) < 10)
			return "0" + currMonth;
		else
			return currMonth;
	}
	public void setCurrMonth(String currMonth) {
		this.currMonth = currMonth;
	}
	public String getCurrDay() {
		if (Integer.parseInt(currDay) < 10)
			return "0" + currDay;
		else
			return currDay;
	}
	public void setCurrDay(String currDay) {
		this.currDay = currDay;
	}
	public int getCurrNo() {
		return currNo;
	}
	public void setCurrNo(int currNo) {
		this.currNo = currNo;
	}
	public int getCipers() {
		return cipers;
	}
	public void setCipers(int cipers) {
		this.cipers = cipers;
	}
	public String getFillChar() {
		return fillChar;
	}
	public void setFillChar(String fillChar) {
		this.fillChar = fillChar;
	}
}
