package com.dinens.citron.p.admin.board.domain;

import com.dinens.citron.p.admin.common.domain.ExtendVO;

/**
 * <PRE>
 * 1. ClassName: BoardFileVO
 * 2. FileName : BoardFileVO.java
 * 3. Package  : com.dinens.citron.board.domain
 * 4. Comment  : 게시판 첨부파일 Domain
 *               TABLE : T_BOARD_FILE
 * 5. 작성자   : JYPark
 * 6. 작성일   : 2019. 9. 4.
 * 7. 변경이력
 *		이름	:	일자	: 변경내용
 *     ———————————————————————————————————
 *		JYPark:	2019. 09. 04.	: 신규 개발.
  * </PRE>
 */

public class BoardFileVO extends ExtendVO {
	
	private static final long serialVersionUID = 1730723529664725738L;
	
	/*
	 * Table 항목 
	 */
	private String boardId;	
	private String fileSeq;
	private String orgFileName;
	private String filePath;
	private String fileSize;
	private String createId;
	private String createTime;
	private String updateId;
	private String updateTime;
	
	
	/*
	 * 추가 항목
	 */
	private String fileSizeStr;
	
	
	/*
	 * Getter & Setter
	 */
	public String getBoardId() {
		return boardId;
	}
	public void setBoardId(String boardId) {
		this.boardId = boardId;
	}
	public String getFileSeq() {
		return fileSeq;
	}
	public void setFileSeq(String fileSeq) {
		this.fileSeq = fileSeq;
	}
	public String getOrgFileName() {
		return orgFileName;
	}
	public void setOrgFileName(String orgFileName) {
		this.orgFileName = orgFileName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getFileSize() {
		return fileSize;
	}
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
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
	public String getFileSizeStr() {
		return fileSizeStr;
	}
	public void setFileSizeStr(String fileSizeStr) {
		this.fileSizeStr = fileSizeStr;
	}
}
