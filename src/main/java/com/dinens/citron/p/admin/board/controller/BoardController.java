package com.dinens.citron.p.admin.board.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.dinens.citron.p.admin.board.domain.BoardFileVO;
import com.dinens.citron.p.admin.board.domain.BoardVO;
import com.dinens.citron.p.admin.board.service.BoardService;
import com.dinens.citron.p.admin.common.domain.PagingVO;
import com.dinens.citron.p.admin.common.domain.Result;
import com.dinens.citron.p.admin.common.util.ContextUtils;
import com.dinens.citron.p.admin.common.util.ExcelUtils;
import com.dinens.citron.p.admin.common.util.FileUtils;
import com.nhncorp.lucy.security.xss.XssSaxFilter;

/**
 * <PRE>
 * 1. ClassName: BoardController
 * 2. FileName : BoardController.java
 * 3. Package  : com.dinens.citron.p.admin.board.controller
 * 4. Comment  : 게시판 Controller
 * 5. 작성자   : JYPark
 * 6. 작성일   : 2019. 9. 4.
 * 7. 변경이력
 *		이름	:	일자	: 변경내용
 *     ———————————————————————————————————
 *		JYPark:	2019. 09. 04.	: 신규 개발.
  * </PRE>
 */

@Controller
@RequestMapping(value="/board/*")
public class BoardController {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private BoardService boardService;

	/*
	 * 게시판 > 목록화면 이동
	 */
	@RequestMapping(value="/boardList")
	public String boardList() throws Exception {

		return "/board/boardList";
	}
	
	
	/*
	 * 게시판 > 목록 조회
	 */
	@RequestMapping(value="/ajaxBoardList.json", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> ajaxBoardList(@ModelAttribute BoardVO boardVO, @ModelAttribute PagingVO pagingVO, Model model) throws Exception {
		
		//logger.debug("boardVO : " + boardVO);
		//logger.debug("pagingVO : " + pagingVO);
		
		Map<String, Object> resultMap = boardService.selectBaordList(boardVO, pagingVO);
		
		model.addAttribute("pagingVO", resultMap.get("pagingVO"));
		model.addAttribute("list", resultMap.get("list"));
		
		return resultMap;		
	}

	
	/*
	 * 게시판 상세 조회
	 */
	@RequestMapping(value="/boardDetail", method = RequestMethod.POST)
	public String boardDetail(@ModelAttribute BoardVO boardVO, Model model) throws Exception {
		
		Map<String, Object> map = boardService.selectBoardDetail(boardVO, "true");
		model.addAttribute("board", map.get("board"));
		model.addAttribute("list", map.get("list"));
		
		return "/board/boardDetail";
	}

	
	/*
	 * 게시판 첨부파일 다운로드
	 */
	@RequestMapping(value="/downloadFile", method = RequestMethod.POST)
	public void downloadFile(@ModelAttribute BoardFileVO boardFileVO, HttpServletResponse response) throws Exception {
		
		BoardFileVO downloadFile = boardService.selectFileDetail(boardFileVO);
				
		String storedFileName = downloadFile.getFilePath();
		String originalFileName = downloadFile.getOrgFileName();
				
		FileUtils.fileDownload(originalFileName, storedFileName, response);		
	}
	
	
	/*
	 * 게시판 등록화면 오픈
	 */
	@RequestMapping(value="/boardRegist", method = RequestMethod.POST)
	public String boardRegist() throws Exception {

		return "/board/boardRegist";
	}

	
	/*
	 * 게시판 등록
	 */
	@RequestMapping(value="/ajaxBoardRegist.json", method = RequestMethod.POST)
	@ResponseBody
	public Result ajaxBoardRegist(@ModelAttribute BoardVO boardVO, MultipartHttpServletRequest request) throws Exception {

		// Content XSS Filter 적용
		XssSaxFilter filter = XssSaxFilter.getInstance("lucy-xss-superset-sax.xml", true);		
		boardVO.setContent(filter.doFilter(boardVO.getContent()));
		
		// 작성자 세팅
		boardVO.setCreateId(ContextUtils.getAttrFromSession("id").toString());
		
		logger.debug("boardVO : " + boardVO);
		
		// 게시글 등록
		boardService.insertBoard(boardVO, request);

		// 결과 리턴
		Result resultVO = new Result("OK");
		resultVO.setForward("/board/boardList");		
		
		return resultVO;
	}
	
	
	/*
	 * 게시판 수정화면 오픈
	 */
	@RequestMapping(value="/boardModify", method = RequestMethod.POST)
	public String boardModify(@ModelAttribute BoardVO boardVO, Model model) throws Exception {

		Map<String, Object> map = boardService.selectBoardDetail(boardVO, "false");
		model.addAttribute("board", map.get("board"));
		model.addAttribute("list", map.get("list"));
		
		return "/board/boardModify";
	}
	
	
	/*
	 * 게시판 수정
	 */
	@RequestMapping(value="/ajaxBoardModify.json", method = RequestMethod.POST)
	@ResponseBody
	public Result ajaxBoardModify(@ModelAttribute BoardVO boardVO, MultipartHttpServletRequest request) throws Exception {
		
		// Content XSS Filter 적용
		XssSaxFilter filter = XssSaxFilter.getInstance("lucy-xss-superset-sax.xml", true);		
		boardVO.setContent(filter.doFilter(boardVO.getContent()));
		
		// 수정자 세팅
		boardVO.setUpdateId(ContextUtils.getAttrFromSession("id").toString());
		
		logger.debug("boardVO : " + boardVO);
		
		// 게시글 업데이트
		boardService.updateBoard(boardVO, request);		
		
		// 결과 리턴
		Result result = new Result("OK");
		result.setForward("/board/boardDetail");		
		
		return result;
	}
	
	
	/*
	 * 게시판 삭제
	 */
	@RequestMapping(value="/ajaxBoardDelete.json", method = RequestMethod.POST)
	@ResponseBody
	public Result ajaxBoardDelete(@ModelAttribute BoardVO boardVO) throws Exception {
		
		// 수정자 세팅
		boardVO.setUpdateId(ContextUtils.getAttrFromSession("id").toString());
		
		logger.debug("boardVO : " + boardVO);
		
		// 게시글 삭제
		boardService.deleteBoard(boardVO);
		
		// 결과 리턴
		Result result = new Result("OK");
		result.setForward("/board/boardList");		
		
		return result;
	}
	
	
	/*
	 * 게시판 엑셀 다운로드
	 */
	@RequestMapping(value="/boardListExcel", method = RequestMethod.POST)	                       
	public void boardListExcel(@ModelAttribute BoardVO boardVO, HttpServletResponse response) throws Exception {
		long start = System.currentTimeMillis();
						
		List<Map<String, Object>> list = boardService.selectBaordListExcel(boardVO);
		
		logger.debug("list Count : " + list.size());
		
		String fileName = "Board List (게시판)";
		String[] columnNames = {"글번호", "제목", "조회수", "작성자", "작성일"};
		String[] columnIds = {"BOARD_ID:C", "TITLE:L", "HIT_COUNT:C", "CREATE_ID:C", "CREATE_TIME:C"};
		String[] columnWidths = {"1920", "8400", "1920", "2800", "5490"};
		
		List<String[]> columnInfo = new ArrayList<String[]>();
		columnInfo.add(columnNames);
		columnInfo.add(columnIds);
		columnInfo.add(columnWidths);
		
		ExcelUtils.downloadExcel(fileName, columnInfo, list, response);
				
		long end = System.currentTimeMillis();
		logger.debug("RunTime : " + Math.round(end - start)/1000.0000 + " (초)");
		
	}

}
