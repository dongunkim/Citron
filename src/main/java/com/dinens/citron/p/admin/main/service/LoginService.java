package com.dinens.citron.p.admin.main.service;

import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dinens.citron.p.admin.admin.domain.AdminVO;
import com.dinens.citron.p.admin.common.constants.Constants;
import com.dinens.citron.p.admin.common.domain.EmailSendHistVO;
import com.dinens.citron.p.admin.common.domain.IssueHistVO;
import com.dinens.citron.p.admin.common.domain.KeyInfoVO;
import com.dinens.citron.p.admin.common.domain.Result;
import com.dinens.citron.p.admin.common.mapper.CommonMapper;
import com.dinens.citron.p.admin.common.service.CommonService;
import com.dinens.citron.p.admin.common.util.CipherUtils;
import com.dinens.citron.p.admin.common.util.ContextUtils;
import com.dinens.citron.p.admin.common.util.DateUtils;
import com.dinens.citron.p.admin.common.util.MailUtils;
import com.dinens.citron.p.admin.common.util.MessageUtils;
import com.dinens.citron.p.admin.common.util.StringUtils;
import com.dinens.citron.p.admin.main.mapper.LoginMapper;
import com.dinens.citron.p.admin.main.mapper.MainMapper;

/**
 * <PRE>
 * 1. ClassName: LoginService
 * 2. FileName : LoginService.java
 * 3. Package  : com.dinens.citron.p.admin.main.service
 * 4. Comment  : Login Service Class
 * 5. 작성자   : JYPark
 * 6. 작성일   : 2019. 9. 10.
 * 7. 변경이력
 *		이름	:	일자	: 변경내용
 *     ———————————————————————————————————
 *		JYPark:	2019. 09. 10.	: 신규 개발.
  * </PRE>
 */

@Service("loginService")
public class LoginService {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired	
	private CommonService commonService;
	
	@Autowired	
	private LoginMapper loginMapper;
	
	@Autowired	
	private MainMapper mainMapper;
	
	@Autowired	
	private CommonMapper commonMapper;
	
	@Value("${email.resource.url}")
	private String emailResourceUrl;
	
	/*
	 * AdminUser 로그인 처리
	 * 2 타입 로그인 처리 ("ENCRYPT"로 구분)
	 * - ("ENCRYPT"="Y") 1번째는 PlainText 형태의 password를 그대로 처리하는 방식
	 * - ("ENCRYPT"="N") 2번째는 암호화된 password를 처리하는 방식
	 */
	public Result login(AdminVO adminVO, String encrypt) throws Exception {				
				
		// Custom 암호화 전송인 경우에만 복호화 진행
		if ("Y".equals(encrypt)) {			
			// Key정보 조회
			KeyInfoVO keyInfo = commonService.selectKeyInfo();
			PrivateKey privateKey = keyInfo.getPrivateKey();
			
			// 데이터 복호화        	
        	adminVO.setAdminId(CipherUtils.decryptRSA(adminVO.getAdminId(), privateKey));
        	adminVO.setPassword(CipherUtils.decryptRSA(adminVO.getPassword(), privateKey));	        
		}
				
		AdminVO adminInfo = loginMapper.loginCheck(adminVO);
		if (adminInfo != null) {

			// 임시비밀번호인 경우 유효기간 만료 체크
			IssueHistVO issueHist = new IssueHistVO();
			issueHist.setIssueType(Constants.Issue.ISSUE_TYPE_TEMP_PASSWORD);
			issueHist.setIssueKey(adminVO.getPassword());
			issueHist.setExpire(-Integer.parseInt(Constants.Issue.EXPIRE_TERM_TEMP_PASSWORD));

			int chkCount = loginMapper.checkIssueHist(issueHist);
			if (chkCount != 0) {
				logger.debug("The password has expired.");
				// 임시비빌번호 유효기간이 만료되었습니다.
				return new Result(Constants.Error.RESULT_FAIL, Constants.Error.EXPIRED_TEMP_PASSWORD, MessageUtils.getMessage("server.common.result.error.108.message"));
			}
				
			ContextUtils.setAttrToSession("userType", "ADMIN");
			ContextUtils.setAttrToSession("id", adminInfo.getAdminId());
			ContextUtils.setAttrToSession("groupId", adminInfo.getAdminGroupId());
			ContextUtils.setAttrToSession("name",adminInfo.getAdminName());
			logger.debug("Session is created.");		
		}
		else {
			logger.debug("ID or Password doesn't match!");
			// 아이디 또는 비밀번호가 일치하지 않습니다.
			return new Result(Constants.Error.RESULT_FAIL, Constants.Error.LOGIN_NOT_MATCH, MessageUtils.getMessage("server.common.result.error.102.message"));			
		}
		
		return new Result(Constants.Error.RESULT_OK);
	}
	
	
	/*
	 * 비밀번호 찾기
	 * 1단계 : 아이디 인증
	 * 2단계 : 이름 + 이메일 인증 (임시 비밀번호 발송) 
	 * 
	 */
	public Result checkAdminInfo(AdminVO adminVO, String step, String encrypt) throws Exception {
		
		// Custom 암호화 전송인 경우에만 복호화 진행
		if ("Y".equals(encrypt)) {			
			// Key정보 조회
			KeyInfoVO keyInfo = commonService.selectKeyInfo();
			PrivateKey privateKey = keyInfo.getPrivateKey();
			
			// 데이터 복호화        	
        	adminVO.setAdminId(CipherUtils.decryptRSA(adminVO.getAdminId(), privateKey));
		}
		
		String adminId = adminVO.getAdminId();		
		if (StringUtils.isNull(adminId)) {
			logger.debug("Input Value(adminId) is invalid.");
			// 입력값이 유효하지 않습니다.
			return new Result(Constants.Error.RESULT_FAIL, Constants.Error.VALUE_INVALID, MessageUtils.getMessage("server.common.result.error.002.message"));
		}
		
		AdminVO admin = loginMapper.checkAdminInfo(adminId);
		if (admin == null) {
			logger.debug("adminId doesn't exist.");
			// 존재하지 않는 아이디 입니다.
			return new Result(Constants.Error.RESULT_FAIL, Constants.Error.ID_NOT_EXIST, MessageUtils.getMessage("server.common.result.error.106.message"));
		}
		
		// 2단계일때만 수행
		if ("2".equals(step)) {
			String adminName = adminVO.getAdminName();
			String email = adminVO.getEmail();
			
			if (!email.equals(admin.getEmail()) || !adminName.equals(admin.getAdminName())) {
				logger.debug("AdminName or Email doesn't match.");
				// 등록된 정보와 일치하지 않습니다.
				return new Result(Constants.Error.RESULT_FAIL, Constants.Error.VALUE_NOT_EXIST, MessageUtils.getMessage("server.common.result.error.107.message"));
			}			
		}
				
		return new Result(Constants.Error.RESULT_OK);
	}
	
	
	/*
	 * 임시 비밀번호 발송
	 */
	public Result sendMailForTemporaryPasswd(AdminVO adminVO, String encrypt) throws Exception {
		
		// Custom 암호화 전송인 경우에만 복호화 진행
		if ("Y".equals(encrypt)) {
			// Key정보 조회
			KeyInfoVO keyInfo = commonService.selectKeyInfo();
			PrivateKey privateKey = keyInfo.getPrivateKey();
			
        	// 데이터 복호화        	
        	adminVO.setAdminId(CipherUtils.decryptRSA(adminVO.getAdminId(), privateKey));
		}
		
		// 메일폼 읽어오기
		String mailContent = MailUtils.getMailHtml("/form/mail/passwdForm.html");
		
		// 10자리 임시 비밀번호 생성
		String temporaryPassword = UUID.randomUUID().toString().replaceAll("-", "");
		temporaryPassword = temporaryPassword.substring(0, 10);		
				
		// Secure Random Salt 생성(14자리)
		String salt = CipherUtils.genSecureRandomSalt(14);		
		
		// 임시 비밀번호 DB 저장
		adminVO.setNewPassword(temporaryPassword);
		adminVO.setPasswordSalt(salt);
		adminVO.setUpdateId("SYSTEM");				
		mainMapper.updateAdminPassword(adminVO);
		logger.debug("adminVO : " + adminVO);
		
		// 임시 비밀번호 발급이력 생성
		IssueHistVO issueHist = new IssueHistVO();
		issueHist.setIssueType(Constants.Issue.ISSUE_TYPE_TEMP_PASSWORD);
		issueHist.setIssueKey(temporaryPassword);
		commonMapper.insertIssueHist(issueHist);
		
		// 메일 본문 Content 설정
		String mailTitle = MessageUtils.getMessage("ui.mail.temporaryPassword.title");		// [시트론] 임시 비밀번호가 발급되었습니다.
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
		EmailSendHistVO emailSendHist = new EmailSendHistVO();
		emailSendHist.setEmail(receiverEmail);
		emailSendHist.setCreateId("SYSTEM");
		emailSendHist.setSendStatus((sendResult ? Constants.Email.SEND_STATUS_OK : Constants.Email.SEND_STATUS_FAIL));	// 1: 성공, 2: 실패
		emailSendHist.setSendReasonCode(Constants.Email.SEND_REASON_FIND_PASSWORD);										// 02 : 비밀번호 찾기
		
		// 메일 발송 이후에 발생한 Exception은 logging만 처리
		try { commonService.insertEmailSendHistInfo(emailSendHist); } 
		catch (Exception e) { e.printStackTrace(); }
		
		return new Result(Constants.Error.RESULT_OK);
	}	
}
