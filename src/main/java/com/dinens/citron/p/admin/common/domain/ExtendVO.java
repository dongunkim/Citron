package com.dinens.citron.p.admin.common.domain;

/**
 * <PRE>
 * 1. ClassName: ExtendVO
 * 2. FileName : ExtendVO.java
 * 3. Package  : com.dinens.citron.common.common
 * 4. Comment  : 모든 List에 적용되는 공통검색 조건 추가한 Extended bean
 *               List형태의 빈에서만 상속하도록 함               
 * 5. 작성자   : JYPark
 * 6. 작성일   : 2019. 7. 31.
 * 7. 변경이력
 *		이름	:	일자	: 변경내용
 *     ———————————————————————————————————
 *		JYPark:	2019. 07. 31.	: 신규 개발.
  * </PRE>
 */

public class ExtendVO extends BaseVO {

	private static final long serialVersionUID = 1266718618744582697L;
		
	/*
	 * *********** 리스트 화면 공통 변수  - Search ***********
	 */
	private String condSearchType;		// 검색 타입 (목록별로 다름)
	private String condSearchStr;		// 검색어
	private String startDate;			// 시작일자
	private String endDate;				// 종료일자
		
	public String getCondSearchType() {
		return condSearchType;
	}

	public void setCondSearchType(String condSearchType) {
		this.condSearchType = condSearchType;
	}

	public String getCondSearchStr() {
		return condSearchStr;
	}

	public void setCondSearchStr(String condSearchStr) {
		this.condSearchStr = condSearchStr;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	} 	
}
