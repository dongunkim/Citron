package com.dinens.citron.p.admin.board.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.dinens.citron.p.admin.board.domain.BoardFileVO;
import com.dinens.citron.p.admin.board.domain.BoardVO;
import com.dinens.citron.p.admin.board.mapper.BoardMapper;
import com.dinens.citron.p.admin.common.constants.Constants;
import com.dinens.citron.p.admin.common.domain.PagingVO;
import com.dinens.citron.p.admin.common.service.SeqInfoService;
import com.dinens.citron.p.admin.common.util.CommonUtils;
import com.dinens.citron.p.admin.common.util.ContextUtils;
import com.dinens.citron.p.admin.common.util.FileUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * <PRE>
 * 1. ClassName: BoardService
 * 2. FileName : BoardService.java
 * 3. Package  : com.dinens.citron.p.admin.board.service
 * 4. Comment  : Board Service Class
 * 5. 작성자   : JYPark
 * 6. 작성일   : 2019. 9. 4.
 * 7. 변경이력
 *		이름	:	일자	: 변경내용
 *     ———————————————————————————————————
 *		JYPark:	2019. 09. 04.	: 신규 개발.
  * </PRE>
 */

@Service("boardService")
public class BoardService {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired	
	private BoardMapper boardMapper;
	
	@Autowired
	private SeqInfoService seqInfoService;
	
	@Value("${citron.domain}")
	private String domainUrl;
	
	@Value("${file.path}")
	private String filePath;
	
	@Value("${image.temp.path}")
	private String imageTempPath;
	
	private final String BOARD_TABLE_NAME = "T_BOARD";
	private final String BOARD_FILE_TABLE_NAME = "T_BOARD_FILE";
	
	/*
	 * 게시판 목록 조회
	 */
	public Map<String, Object> selectBaordList(BoardVO boardVO, PagingVO pagingVO) throws Exception {
		int totalCount = boardMapper.selectBoardListCount(boardVO);		
		if (totalCount > 0) {
			pagingVO.setTotalCount(totalCount);
			CommonUtils.setPaging(pagingVO);
		}
		logger.debug("pagingVO : " + pagingVO);
		List<BoardVO> list = boardMapper.selectBoardList(boardVO, pagingVO);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("pagingVO", pagingVO);
		resultMap.put("list", list);
		
		return resultMap;
	}
	
	
	/*
	 * 게시판 목록 Excel 조회
	 */
	public List<Map<String, Object>> selectBaordListExcel(BoardVO boardVO) throws Exception {
		return boardMapper.selectBoardListExcel(boardVO);
	}
	
	
	
	/*
	 * 게시판 상세정보 조회
	 */
	public Map<String, Object> selectBoardDetail(BoardVO boardVO, String hitUpdate) throws Exception {				
		String boardId = boardVO.getBoardId();
		if ((hitUpdate != null) && (hitUpdate.equals("true"))) {
			boardMapper.updateHitCount(boardId);
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		BoardVO board = boardMapper.selectBoardDetail(boardId);
		logger.debug("board =============> " + board);
		resultMap.put("board", board);
		
		List<BoardFileVO> list = boardMapper.selectFileList(boardVO.getBoardId());
		resultMap.put("list", list);
		
		return resultMap;
	}
	
	
	/*
	 * 게시판 등록
	 */
	public void insertBoard(BoardVO boardVO, MultipartHttpServletRequest request) throws Exception {
		
		// 삽입 이미지 경로 재설정
		String[] imageUrl = boardVO.getImageUrl();
		List<String> moveFiles = new ArrayList<String>();
		if (imageUrl != null) {			
			String content = boardVO.getContent();			
			for(String url : imageUrl) {
				if (!content.contains(url)) continue;
				
				String storedFileName = FileUtils.getFileName(url);
				String newUrl = domainUrl + Constants.Upload.IMAGE_URI + "/" + Constants.Upload.UPLOAD_CATEGORY_BOARD + "/" + storedFileName;
								
				moveFiles.add(storedFileName);
				content = content.replace(url, newUrl);			
			}			
			boardVO.setContent(content);
		}
		
		// ID Generation
		String boardId = seqInfoService.selectNextId(BOARD_TABLE_NAME);
		boardVO.setBoardId(boardId);
		
		// 게시판 정보 INSERT
		boardMapper.insertBoard(boardVO);
		
		// 파일 경로 지정
		String uploadPath = FileUtils.getUploadPath(Constants.Upload.UPLOAD_CATEGORY_BOARD, Constants.Upload.UPLOAD_TYPE_ATTACH);
		
		// 신규 파일 INSERT
		List<Map<String, Object>> list = FileUtils.parseFileInfo(request, uploadPath);						
		Map<String, Object> listMap = null;		
		ObjectMapper objectMapper = new ObjectMapper();
		BoardFileVO boardFile = null;
		for(int i=0; i<list.size(); i++) {
			listMap = list.get(i);			
			boardFile = objectMapper.convertValue(listMap, BoardFileVO.class);
			
			// BoardId 및 작성자 세팅
			boardFile.setBoardId(boardId);
			boardFile.setCreateId(boardVO.getCreateId());
			
			// ID Generation
			String fileSeq = seqInfoService.selectNextId(BOARD_FILE_TABLE_NAME);
			boardFile.setFileSeq(fileSeq);
			
			boardMapper.insertFile(boardFile);
		}
		
		// 삽입 이미지 임시 폴더에서 image 폴더로 Move		
		String userId = ContextUtils.getAttrFromSession("id").toString();
		String tempPath = imageTempPath + userId + "/";
		String imagePath = filePath + Constants.Upload.UPLOAD_TYPE_IMAGE + "/" + Constants.Upload.UPLOAD_CATEGORY_BOARD + "/";
		for(String fileName : moveFiles) {			
			FileUtils.fileMove(new File(tempPath + fileName), new File(imagePath + fileName));			
		}
	}
	
	
	/*
	 * 게시판 수정
	 */
	public void updateBoard(BoardVO boardVO, MultipartHttpServletRequest request) throws Exception {
		
		// 삽입 이미지 경로 재설정
		String[] imageUrl = boardVO.getImageUrl();
		List<String> moveFiles = new ArrayList<String>();
		if (imageUrl != null) {			
			String content = boardVO.getContent();			
			for(String url : imageUrl) {
				if (!content.contains(url)) continue;
				
				String storedFileName = FileUtils.getFileName(url);
				String newUrl = domainUrl + Constants.Upload.IMAGE_URI + "/" + Constants.Upload.UPLOAD_CATEGORY_BOARD + "/" + storedFileName;
				
				moveFiles.add(storedFileName);
				content = content.replace(url, newUrl);			
			}			
			boardVO.setContent(content);
		}
				
		// 게시판 정보 UPDATE
		boardMapper.updateBoard(boardVO);
		
		// 기존 파일을 임시로 모두 사용안함으로 UPDATE
		boardMapper.deleteFileList(boardVO.getUpdateId(), boardVO.getBoardId());
		
		// 파일 경로 지정
		String uploadPath = FileUtils.getUploadPath(Constants.Upload.UPLOAD_CATEGORY_BOARD, Constants.Upload.UPLOAD_TYPE_ATTACH);
				
		// 신규 파일 INSERT
		List<Map<String, Object>> list = FileUtils.parseFileInfo(request, uploadPath);		
		String boardId = boardVO.getBoardId();
		Map<String, Object> listMap = null;
		
		ObjectMapper objectMapper = new ObjectMapper();		
		for(int i=0; i<list.size(); i++) {
			listMap = list.get(i);			
			BoardFileVO boardFile = objectMapper.convertValue(listMap, BoardFileVO.class);
			
			// BoardId 및 작성자 세팅
			boardFile.setBoardId(boardId);
			boardFile.setCreateId(boardVO.getUpdateId());
			
			// ID Generation
			String fileSeq = seqInfoService.selectNextId(BOARD_FILE_TABLE_NAME);
			boardFile.setFileSeq(fileSeq);
			
			boardMapper.insertFile(boardFile);
		}
		
		// 기존 파일 중에서 남은 파일을 다시 사용으로 UPDATE		
		String[] fileSeq = boardVO.getFileSeq();
		if (fileSeq != null) {
			for (String key : fileSeq) {			
				boardMapper.updateFile(boardVO.getUpdateId(), key);
			}	
		}		
		
		// 삽입 이미지 임시 폴더에서 image 폴더로 Move		
		String userId = ContextUtils.getAttrFromSession("id").toString();
		String tempPath = imageTempPath + userId + "/";
		String imagePath = filePath + Constants.Upload.UPLOAD_TYPE_IMAGE + "/" + Constants.Upload.UPLOAD_CATEGORY_BOARD + "/";
		logger.debug("tempPath : " + tempPath);
		logger.debug("imagePath : " + imagePath);
		for(String fileName : moveFiles) {
			logger.debug("from : " + tempPath + fileName);
			logger.debug("to : " + imagePath + fileName);
			FileUtils.fileMove(new File(tempPath + fileName), new File(imagePath + fileName));			
		}
	}
	
	
	/*
	 * 게시판 삭제
	 */
	public void deleteBoard(BoardVO boardVO) throws Exception {
		String updateId = boardVO.getUpdateId();
		String boardId = boardVO.getBoardId();
				
		boardMapper.deleteBoard(updateId, boardId);
		boardMapper.deleteFileList(updateId, boardId);
	}
	
	
	/*
	 * 게시판 첨부파일 정보 조회
	 */
	public BoardFileVO selectFileDetail(BoardFileVO boardFileVO) throws Exception {
		return boardMapper.selectFileDetail(boardFileVO.getFileSeq());
	}
	
}
