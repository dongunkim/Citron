package com.dinens.citron.p.admin.common.domain;

/**
 * <PRE>
 * 1. ClassName: PagingVO
 * 2. FileName : PagingVO.java
 * 3. Package  : com.dinens.citron.common.common
 * 4. Comment  : Pagination을 위한 공통 bean               
 * 5. 작성자   : JYPark
 * 6. 작성일   : 2019. 7. 31.
 * 7. 변경이력
 *		이름	:	일자	: 변경내용
 *     ———————————————————————————————————
 *		JYPark:	2019. 07. 31.	: 신규 개발.
  * </PRE>
 */

public class PagingVO extends BaseVO {

	private static final long serialVersionUID = -1883786277852094592L;
	
	/*
	 * *********** Pagination ***********
	 */	
	
	private int nowPage = 1;			// 현재 페이지	
	private int countPerPage = 0;		// 페이지당 목록수
	private int startNum = 0;			// 현재 페이지 시작번호	
	private int totalCount = 0;			// 전체 건수
	private int totalSum = 0;			// 전체 합	
	private int totalPage = 0;			// 전체 페이지수
		
	public int getNowPage() {
		return nowPage;
	}
	
	public void setNowPage(int nowPage) {
		this.nowPage = nowPage;
	}
	
	public int getCountPerPage() {
		return countPerPage;
	}
	
	public void setCountPerPage(int countPerPage) {
		this.countPerPage = countPerPage;
	}
	
	public int getStartNum() {
		return startNum;
	}
	
	public void setStartNum(int startNum) {
		this.startNum = startNum;
	}
	
	public int getTotalCount() {
		return totalCount;
	}
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	
	public int getTotalSum() {
		return totalSum;
	}
	
	public void setTotalSum(int totalSum) {
		this.totalSum = totalSum;
	}
	
	public int getTotalPage() {
		return totalPage;
	}
	
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}	
}
