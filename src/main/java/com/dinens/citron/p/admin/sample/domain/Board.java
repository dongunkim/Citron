package com.dinens.citron.p.admin.sample.domain;

import com.dinens.citron.p.admin.common.domain.ExtendVO;

/**
 * <PRE>
 * 1. ClassName: Board
 * 2. FileName : Board.java
 * 3. Package  : com.dinens.citron.sample.domain
 * 4. Comment  : Sample 게시판 Domain
 *               TABLE : TB_BOARD
 * 5. 작성자   : JYPark
 * 6. 작성일   : 2019. 7. 31.
 * 7. 변경이력
 *		이름	:	일자	: 변경내용
 *     ———————————————————————————————————
 *		JYPark:	2019. 07. 31.	: 신규 개발.
  * </PRE>
 */

public class Board extends ExtendVO {
	
	private static final long serialVersionUID = 8940224744662050155L;
	
	/*
	 * Table 항목 
	 */
	private String idx;
	private String parentIdx;
	private String title;
	private String contents;
	private String hitCnt;
	private String delGb;
	private String creaDtm;
	private String creaId;
	
	/*
	 * 추가 항목
	 */
	private String[] fileIdx;		// 첨부파일 ID
	private String[] imageUrl;		// 삽입 이미지 URL
		
	public String getIdx() {
		return idx;
	}
	public void setIdx(String idx) {
		this.idx = idx;
	}
	public String getParentIdx() {
		return parentIdx;
	}
	public void setParentIdx(String parentIdx) {
		this.parentIdx = parentIdx;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public String getHitCnt() {
		return hitCnt;
	}
	public void setHitCnt(String hitCnt) {
		this.hitCnt = hitCnt;
	}
	public String getDelGb() {
		return delGb;
	}
	public void setDelGb(String delGb) {
		this.delGb = delGb;
	}
	public String getCreaDtm() {
		return creaDtm;
	}
	public void setCreaDtm(String creaDtm) {
		this.creaDtm = creaDtm;
	}
	public String getCreaId() {
		return creaId;
	}
	public void setCreaId(String creaId) {
		this.creaId = creaId;
	}
	public String[] getFileIdx() {
		return fileIdx;
	}
	public void setFileIdx(String[] fileIdx) {
		this.fileIdx = fileIdx;
	}
	public String[] getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String[] imageUrl) {
		this.imageUrl = imageUrl;
	}	
}
