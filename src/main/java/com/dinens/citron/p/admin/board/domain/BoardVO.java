package com.dinens.citron.p.admin.board.domain;

import com.dinens.citron.p.admin.common.domain.ExtendVO;

/**
 * <PRE>
 * 1. ClassName: BoardVO
 * 2. FileName : BoardVO.java
 * 3. Package  : com.dinens.citron.board.domain
 * 4. Comment  : 게시판 Domain
 *               TABLE : T_BOARD
 * 5. 작성자   : JYPark
 * 6. 작성일   : 2019. 9. 4.
 * 7. 변경이력
 *		이름	:	일자	: 변경내용
 *     ———————————————————————————————————
 *		JYPark:	2019. 09. 04.	: 신규 개발.
  * </PRE>
 */

public class BoardVO extends ExtendVO {
	
	private static final long serialVersionUID = 730925606651038510L;
	
	/*
	 * Table 항목 
	 */
	private String boardId;	
	private String title;
	private String content;
	private String hitCount;
	private String useYn;
	private String createId;
	private String createTime;
	private String updateId;
	private String updateTime;
	
	
	/*
	 * 추가 항목
	 */
	private String[] fileSeq;		// 첨부파일 SEQ
	private String[] imageUrl;		// 삽입 이미지 URL
		
	
	/*
	 * Getter & Setter
	 */
	public String getBoardId() {
		return boardId;
	}
	public void setBoardId(String boardId) {
		this.boardId = boardId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getHitCount() {
		return hitCount;
	}
	public void setHitCount(String hitCount) {
		this.hitCount = hitCount;
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
	public String[] getFileSeq() {
		return fileSeq;
	}
	public void setFileSeq(String[] fileSeq) {
		this.fileSeq = fileSeq;
	}
	public String[] getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String[] imageUrl) {
		this.imageUrl = imageUrl;
	}
}
