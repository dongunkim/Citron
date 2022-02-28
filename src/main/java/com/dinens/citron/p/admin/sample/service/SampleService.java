package com.dinens.citron.p.admin.sample.service;

import java.io.File;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.dinens.citron.p.admin.common.constants.Constants;
import com.dinens.citron.p.admin.common.domain.EmailSendHistVO;
import com.dinens.citron.p.admin.common.domain.KeyInfoVO;
import com.dinens.citron.p.admin.common.domain.PagingVO;
import com.dinens.citron.p.admin.common.domain.Result;
import com.dinens.citron.p.admin.common.service.CommonService;
import com.dinens.citron.p.admin.common.util.CipherUtils;
import com.dinens.citron.p.admin.common.util.CommonUtils;
import com.dinens.citron.p.admin.common.util.ContextUtils;
import com.dinens.citron.p.admin.common.util.DateUtils;
import com.dinens.citron.p.admin.common.util.FileUtils;
import com.dinens.citron.p.admin.common.util.MailUtils;
import com.dinens.citron.p.admin.common.util.MessageUtils;
import com.dinens.citron.p.admin.sample.domain.Admin;
import com.dinens.citron.p.admin.sample.domain.Board;
import com.dinens.citron.p.admin.sample.domain.BoardFile;
import com.dinens.citron.p.admin.sample.mapper.SampleMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * <PRE>
 * 1. ClassName: SampleService
 * 2. FileName : SampleService.java
 * 3. Package  : com.dinens.citron.sample.service
 * 4. Comment  : Sample Service Class
 * 5. 작성자   : JYPark
 * 6. 작성일   : 2019. 7. 31.
 * 7. 변경이력
 *		이름	:	일자	: 변경내용
 *     ———————————————————————————————————
 *		JYPark:	2019. 07. 31.	: 신규 개발.
  * </PRE>
 */

@Service("sampleService")
public class SampleService {
	Logger logger = LoggerFactory.getLogger(this.getClass());
		
	@Autowired	
	private SampleMapper sampleMapper;
	
	@Autowired	
	private CommonService commonService;
	
	@Value("${email.resource.url}")
	private String emailResourceUrl;
	
	@Value("${citron.domain}")
	private String domainUrl;
	
	@Value("${file.path}")
	private String filePath;
	
	@Value("${image.temp.path}")
	private String imageTempPath;
	
	/*
	 * AdminUser 로그인 처리
	 * 2 타입 로그인 처리 ("ENCRYPT"로 구분)
	 * - ("ENCRYPT"="Y") 1번째는 PlainText 형태의 password를 그대로 처리하는 방식
	 * - ("ENCRYPT"="N") 2번째는 암호화된 password를 처리하는 방식
	 */
	public Result login(Admin adminVO, String encrypt) throws Exception {				
		String passwd = null;
		
		if ("Y".equals(encrypt)) {
			// Key정보 조회
			KeyInfoVO keyInfo = commonService.selectKeyInfo();
			PrivateKey privateKey = keyInfo.getPrivateKey();
			
			passwd = CipherUtils.decryptRSA(adminVO.getPassword(), privateKey);
        	adminVO.setPassword(passwd);
        	logger.debug("Decrypted passwd : " + passwd);        	
		}
		else {
			passwd = adminVO.getPassword();
		}
		
		Admin adminInfo = sampleMapper.loginCheck(adminVO);	
		if (adminInfo != null) {
			ContextUtils.setAttrToSession("userType", "ADMIN");
			ContextUtils.setAttrToSession("id", adminInfo.getAdminId());
			ContextUtils.setAttrToSession("groupId", adminInfo.getAdminGroupId());
			ContextUtils.setAttrToSession("name",adminInfo.getAdminName());
			logger.debug("Session is created.");			
		}
		else {
			logger.debug("ID or Password doesn't match : " + passwd);
			// 아이디 또는 비밀번호가 일치하지 않습니다.
			return new Result("FAIL", "102", MessageUtils.getMessage("server.common.result.error.102.message"));			
		}
		
		return new Result("OK");		
	}
	
	/*
	 * AdminUser 목록 조회
	 */
	public List<Admin> selectAdminList(Admin adminVO) throws Exception {
		return sampleMapper.selectAdminList(adminVO);
	}
	
	/*
	 * AdminUser 상세 조회
	 */
	public Admin selectAdminDetail(Admin adminVO) throws Exception {
		return sampleMapper.selectAdminDetail(adminVO);
	}
	
	/*
	 * AdminUser 등록
	 */
	public Result insertAdmin(Admin adminVO, String encrypt) throws Exception {
				
		// Custom 암호화 전송인 경우
		if ("Y".equals(encrypt)) {
			// Key정보 조회
			KeyInfoVO keyInfo = commonService.selectKeyInfo();
			PrivateKey privateKey = keyInfo.getPrivateKey();
			
			// 데이터 복호화        	
        	adminVO.setPassword(CipherUtils.decryptRSA(adminVO.getPassword(), privateKey));
        	adminVO.setAdminName(CipherUtils.decryptRSA(adminVO.getAdminName(), privateKey));
        	adminVO.setPhoneNumber(CipherUtils.decryptRSA(adminVO.getPhoneNumber(), privateKey));
        	adminVO.setEmail(CipherUtils.decryptRSA(adminVO.getEmail(), privateKey));
		}
		
		// Secure Random Salt 생성(14자리)
		String salt = CipherUtils.genSecureRandomSalt(14);		
		adminVO.setPasswordSalt(salt);
		
		// Administrator 정보 저장
		sampleMapper.insertAdmin(adminVO);
		
		return new Result("OK");
	}
	
	/*
	 * AdminUser 수정
	 */
	public void updateAdmin(Admin adminVO) throws Exception {
		sampleMapper.updateAdmin(adminVO);
	}
	
	/*
	 * AdminUser 비밀번호 변경
	 */
	public void updateAdminPassword(Admin adminVO) throws Exception {
		sampleMapper.updateAdminPassword(adminVO);
	}
	
	/*
	 * AdminUser 삭제 (USE_YN='N')
	 */
	public void deleteAdmin(Admin adminVO) throws Exception {
		sampleMapper.deleteAdmin(adminVO);
	}
	
	/*
	 * 게시판 목록 조회
	 */
	public Map<String, Object> selectBaordList(Board boardVO, PagingVO pagingVO) throws Exception {
		int totalCount = sampleMapper.selectBoardListCount(boardVO);		
		if (totalCount > 0) {
			pagingVO.setTotalCount(totalCount);
			CommonUtils.setPaging(pagingVO);
		}
		logger.debug("pagingVO : " + pagingVO);
		List<Board> list = sampleMapper.selectBoardList(boardVO, pagingVO);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("pagingVO", pagingVO);
		resultMap.put("list", list);
		
		return resultMap;
	}
	
	/*
	 * 게시판 목록 Excel 조회
	 */
	public List<Map<String, Object>> selectBaordExcelList(Board sampleVO) throws Exception {
		return sampleMapper.selectBoardExcelList(sampleVO);
	}

	/*
	 * 게시판 등록
	 */
	public void insertBoard(Board boardVO, MultipartHttpServletRequest request) throws Exception {
		
		// 삽입 이미지 경로 재설정
		String[] imageUrl = boardVO.getImageUrl();
		List<String> moveFiles = new ArrayList<String>();
		if (imageUrl != null) {			
			String contents = boardVO.getContents();			
			for(String url : imageUrl) {
				if (!contents.contains(url)) continue;
				
				String storedFileName = FileUtils.getFileName(url);
				String newUrl = domainUrl + Constants.Upload.IMAGE_URI + "/" + Constants.Upload.UPLOAD_CATEGORY_BOARD + "/" + storedFileName;
								
				moveFiles.add(storedFileName);
				contents = contents.replace(url, newUrl);			
			}			
			boardVO.setContents(contents);
		}
						
		// 게시판 정보 INSERT
		sampleMapper.insertBoard(boardVO);
		
		// 파일 경로 지정
		String uploadPath = FileUtils.getUploadPath(Constants.Upload.UPLOAD_CATEGORY_BOARD, Constants.Upload.UPLOAD_TYPE_ATTACH);
		
		// 신규 파일 INSERT
		List<Map<String, Object>> list = FileUtils.parseFileInfo(request, uploadPath);		
		String boardIdx = boardVO.getIdx();
				
		Map<String, Object> listMap = null;		
		ObjectMapper objectMapper = new ObjectMapper();
		BoardFile fileVO = null;
		for(int i=0; i<list.size(); i++) {
			listMap = list.get(i);
			listMap.put("boardIdx", boardIdx);
			fileVO = objectMapper.convertValue(listMap, BoardFile.class);
			logger.debug("fileVO : \t" + fileVO);
			sampleMapper.insertFile(fileVO);
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
	 * 게시판 상세정보 조회
	 */
	public Map<String, Object> selectBoardDetail(Board sampleVO, String hitUpdate) throws Exception {				
		if ((hitUpdate != null) && (hitUpdate.equals("true"))) sampleMapper.updateHitCount(sampleVO);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		Board boardVO = sampleMapper.selectBoardDetail(sampleVO);
		resultMap.put("boardVO", boardVO);		
				
		List<BoardFile> list = sampleMapper.selectFileList(sampleVO);
		resultMap.put("list", list);
		
		return resultMap;
	}	
	
	/*
	 * 게시판 수정
	 */
	public void updateBoard(Board boardVO, MultipartHttpServletRequest request) throws Exception {
		
		// 삽입 이미지 경로 재설정
		String[] imageUrl = boardVO.getImageUrl();
		List<String> moveFiles = new ArrayList<String>();
		if (imageUrl != null) {			
			String contents = boardVO.getContents();			
			for(String url : imageUrl) {
				if (!contents.contains(url)) continue;
				
				String storedFileName = FileUtils.getFileName(url);
				String newUrl = domainUrl + Constants.Upload.IMAGE_URI + "/" + Constants.Upload.UPLOAD_CATEGORY_BOARD + "/" + storedFileName;
								
				moveFiles.add(storedFileName);
				contents = contents.replace(url, newUrl);			
			}			
			boardVO.setContents(contents);
		}
				
		// 게시판 정보 UPDATE
		sampleMapper.updateBoard(boardVO);
		
		// 기존 파일을 임시로 모두 사용안함으로 UPDATE
		sampleMapper.deleteFileList(boardVO);
		
		// 파일 경로 지정
		String uploadPath = FileUtils.getUploadPath(Constants.Upload.UPLOAD_CATEGORY_BOARD, Constants.Upload.UPLOAD_TYPE_ATTACH);
				
		// 신규 파일 INSERT
		List<Map<String, Object>> list = FileUtils.parseFileInfo(request, uploadPath);		
		String boardIdx = boardVO.getIdx();
		Map<String, Object> listMap = null;
		
		ObjectMapper objectMapper = new ObjectMapper();		
		for(int i=0; i<list.size(); i++) {
			listMap = list.get(i);
			listMap.put("boardIdx", boardIdx);
			BoardFile fileVO = objectMapper.convertValue(listMap, BoardFile.class);
			
			sampleMapper.insertFile(fileVO);
		}
		
		// 기존 파일 중에서 남은 파일을 다시 사용으로 UPDATE		
		String[] fileIdxs = boardVO.getFileIdx();
		if (fileIdxs == null) return;
				
		for (String key : fileIdxs) {
			BoardFile fileVO = new BoardFile();
			fileVO.setIdx(key);
			sampleMapper.updateFile(fileVO);	
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
	 * 게시판 삭제
	 */
	public void deleteBoard(Board boardVO) throws Exception {
		sampleMapper.deleteBoard(boardVO);
		sampleMapper.deleteFileList(boardVO);
	}
	
	/*
	 * 임시 비밀번호 메일 전송
	 */
	public void sendPasswdMail(Admin adminVO) throws Exception {
		
		// 메일폼 읽어오기
		String mailContent = MailUtils.getMailHtml("/form/mail/passwdForm.html");
		
		// 10자리 임시 비밀번호 생성
		String temporaryPassword = UUID.randomUUID().toString().replaceAll("-", "");
		temporaryPassword = temporaryPassword.substring(0, 10);		
				
		// Secure Random Salt 생성(14자리)
		String salt = CipherUtils.genSecureRandomSalt(14);		
		
		// 임시 비밀번호 DB 저장
		adminVO.setPassword(temporaryPassword);
		adminVO.setPasswordSalt(salt);
		adminVO.setCreateId("SYSTEM");
		
		logger.debug("adminVO : " + adminVO);
		
		sampleMapper.updateAdminPassword(adminVO);
		
		// 메일 본문 Content 설정
		String mailTitle   = "[시트론] 임시 비밀번호가 발급되었습니다.";
		String requestDate = DateUtils.getFormatDate(DateUtils.getCurrDateTime(), "-");
		String receiverName = adminVO.getAdminName();
		String receiverEmail = adminVO.getEmail();
		
		mailContent =  mailContent.replaceAll("\\{REQUEST_DATE\\}", requestDate);
		mailContent =  mailContent.replaceAll("\\{TEMPORARY_PASSWORD\\}", temporaryPassword);
		mailContent =  mailContent.replaceAll("\\{USER_NAME\\}", receiverName);
		mailContent =  mailContent.replaceAll("\\{EMAIL_RESOURCE_URL\\}", emailResourceUrl);
		
		// 메일 전송정보 설정
		Map<String, Object> mailInfoMap = new HashMap<String, Object>();
		mailInfoMap.put("toAddr", receiverEmail);
		mailInfoMap.put("title", mailTitle);
		mailInfoMap.put("content", mailContent);
		
		// 메일 전송
		boolean sendResult = MailUtils.sendMail(mailInfoMap);		
		
		// 메일 전송 이력 저장
		EmailSendHistVO emailSendHistVO = new EmailSendHistVO();
		emailSendHistVO.setEmail(receiverEmail);
		emailSendHistVO.setCreateId("SYSTEM");
		emailSendHistVO.setSendStatus((sendResult ? "1" : "2"));	// 1: 성공, 2: 실패
		emailSendHistVO.setSendReasonCode("02");					// 02 : 비밀번호 찾기
		
		// 메일 발송 이후에 발생한 Exception은 logging만 처리
		try { commonService.insertEmailSendHistInfo(emailSendHistVO); } 
		catch (Exception e) { e.printStackTrace(); }		
	}
	
	
}
