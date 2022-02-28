package com.dinens.citron.p.admin.board.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.dinens.citron.p.admin.board.domain.BoardFileVO;
import com.dinens.citron.p.admin.board.domain.BoardVO;
import com.dinens.citron.p.admin.common.domain.PagingVO;

/**
 * <PRE>
 * 1. ClassName: BoardMapper
 * 2. FileName : BoardMapper.java
 * 3. Package  : com.dinens.citron.p.admin.board.mapper
 * 4. Comment  : 게시판 Mapper Class
 * 5. 작성자   : JYPark
 * 6. 작성일   : 2019. 9. 4.
 * 7. 변경이력
 *		이름	:	일자	: 변경내용
 *     ———————————————————————————————————
 *		JYPark:	2019. 09. 04.	: 신규 개발.
  * </PRE>
 */

public interface BoardMapper {
	// Board Table
	public int selectBoardListCount(@Param(value="boardVO") BoardVO boardVO);
	public List<BoardVO> selectBoardList(@Param(value="boardVO") BoardVO boardVO,
									   @Param(value="pagingVO") PagingVO pagingVO);
	public List<Map<String, Object>> selectBoardListExcel(@Param(value="boardVO") BoardVO boaredVO);
	public BoardVO selectBoardDetail(@Param(value="boardId") String boardId);
	public void insertBoard(BoardVO boaredVO);		
	public void updateHitCount(@Param(value="boardId") String boardId);
	public void updateBoard(BoardVO boaredVO);	
	public void deleteBoard(@Param(value="updateId") String updateId,
			                @Param(value="boardId") String boardId);
	
	// Upload BoardFile Table
	public List<BoardFileVO> selectFileList(@Param(value="boardId") String boardId);	
	public BoardFileVO selectFileDetail(@Param(value="fileSeq") String fileSeq);
	public void insertFile(BoardFileVO boardFileVO);
	public void deleteFileList(@Param(value="updateId") String updateId,
			                   @Param(value="boardId") String boardId);
	public void updateFile(@Param(value="updateId") String updateId,
			               @Param(value="fileSeq") String fileSeq);
}
