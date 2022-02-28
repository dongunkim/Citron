package com.dinens.citron.p.admin.sample.domain;

import com.dinens.citron.p.admin.common.domain.BaseVO;

/**
 * <PRE>
 * 1. ClassName: BoardFile
 * 2. FileName : BoardFile.java
 * 3. Package  : com.dinens.citron.sample.domain
 * 4. Comment  : Sample 게시판 첨부파일 Domain
 *               TABLE : TB_FILE
 * 5. 작성자   : JYPark
 * 6. 작성일   : 2019. 7. 31.
 * 7. 변경이력
 *		이름	:	일자	: 변경내용
 *     ———————————————————————————————————
 *		JYPark:	2019. 07. 31.	: 신규 개발.
  * </PRE>
 */

public class BoardFile extends BaseVO {
	
	private static final long serialVersionUID = -113317000555629600L;
	
	/*
	 * Table 항목 
	 */
	private String idx;
	private String boardIdx;
	private String originalFileName;
	private String storedFileName;
	private String fileSize;
	private String fileSizeStr;
	private String delGb;
	private String creaDtm;
	private String creaId;
	
	public String getIdx() {
		return idx;
	}
	public void setIdx(String idx) {
		this.idx = idx;
	}
	public String getBoardIdx() {
		return boardIdx;
	}
	public void setBoardIdx(String boardIdx) {
		this.boardIdx = boardIdx;
	}
	public String getOriginalFileName() {
		return originalFileName;
	}
	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}
	public String getStoredFileName() {
		return storedFileName;
	}
	public void setStoredFileName(String storedFileName) {
		this.storedFileName = storedFileName;
	}
	public String getFileSize() {
		return fileSize;
	}
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
	public String getFileSizeStr() {
		return fileSizeStr;
	}
	public void setFileSizeStr(String fileSizeStr) {
		this.fileSizeStr = fileSizeStr;
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
	
}
