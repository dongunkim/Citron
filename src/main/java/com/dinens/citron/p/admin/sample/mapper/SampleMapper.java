package com.dinens.citron.p.admin.sample.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.dinens.citron.p.admin.common.domain.PagingVO;
import com.dinens.citron.p.admin.sample.domain.Admin;
import com.dinens.citron.p.admin.sample.domain.Board;
import com.dinens.citron.p.admin.sample.domain.BoardFile;

/**
 * <PRE>
 * 1. ClassName: SampleMapper
 * 2. FileName : SampleMapper.java
 * 3. Package  : com.dinens.citron.sample.mapper
 * 4. Comment  : Sample Mapper Class
 * 5. 작성자   : JYPark
 * 6. 작성일   : 2019. 7. 31.
 * 7. 변경이력
 *		이름	:	일자	: 변경내용
 *     ———————————————————————————————————
 *		JYPark:	2019. 07. 31.	: 신규 개발.
  * </PRE>
 */

public interface SampleMapper {
	
	// Administrator(Account) Table
	public Admin loginCheck(Admin adminVO);
	public List<Admin> selectAdminList(Admin adminVO);	
	public Admin selectAdminDetail(Admin adminVO);
	public void insertAdmin(Admin adminVO);
	public void updateAdmin(Admin adminVO);
	public void updateAdminPassword(Admin adminVO);
	public void deleteAdmin(Admin adminVO);
	
	// Board Table
	public int selectBoardListCount(@Param(value="boardVO") Board boardVO);
	public List<Board> selectBoardList(@Param(value="boardVO") Board boardVO,
									   @Param(value="pagingVO") PagingVO pagingVO);
	public List<Map<String, Object>> selectBoardExcelList(Board boaredVO);
	public void insertBoard(Board boaredVO);	
	public Board selectBoardDetail(Board boaredVO);	
	public void updateHitCount(Board sampleVO);	
	public void updateBoard(Board boaredVO);	
	public void deleteBoard(Board boaredVO);
	
	// Upload BoardFile Table
	public List<BoardFile> selectFileList(Board boaredVO);
	public void insertFile(BoardFile fileVO);	
	public void deleteFileList(Board boaredVO);	
	public void updateFile(BoardFile fileVO);
}
