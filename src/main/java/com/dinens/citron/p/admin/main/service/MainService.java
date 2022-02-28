package com.dinens.citron.p.admin.main.service;

import java.security.PrivateKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dinens.citron.p.admin.admin.domain.AdminVO;
import com.dinens.citron.p.admin.common.constants.Constants;
import com.dinens.citron.p.admin.common.domain.KeyInfoVO;
import com.dinens.citron.p.admin.common.domain.Result;
import com.dinens.citron.p.admin.common.service.CommonService;
import com.dinens.citron.p.admin.common.util.CipherUtils;
import com.dinens.citron.p.admin.common.util.MessageUtils;
import com.dinens.citron.p.admin.main.mapper.LoginMapper;
import com.dinens.citron.p.admin.main.mapper.MainMapper;

/**
 * <PRE>
 * 1. ClassName: MainService
 * 2. FileName : MainService.java
 * 3. Package  : com.dinens.citron.p.admin.main.service
 * 4. Comment  : Main Service Class
 * 5. 작성자   : JYPark
 * 6. 작성일   : 2019. 9. 10.
 * 7. 변경이력
 *		이름	:	일자	: 변경내용
 *     ———————————————————————————————————
 *		JYPark:	2019. 09. 10.	: 신규 개발.
  * </PRE>
 */

@Service("mainService")
public class MainService {

	Logger logger = LoggerFactory.getLogger(this.getClass());
		
	@Autowired
	private MainMapper mainMapper;
	
	@Autowired
	private LoginMapper loginMapper;
	
	@Autowired
	private CommonService commonService;
	
	
	/*
	 * 관리자 상세 조회
	 */
	public AdminVO selectAdminDetail(String adminId) {
		return mainMapper.selectAdminDetail(adminId);
	}
	
	
	/*
	 * 관리자 업데이트 (Self)
	 */
	public Result updateMyPage(AdminVO adminVO, String encrypt) throws Exception {
		// Custom 암호화 전송인 경우에만 복호화 진행
		if ("Y".equals(encrypt)) {			
			// Key정보 조회
			KeyInfoVO keyInfo = commonService.selectKeyInfo();
			PrivateKey privateKey = keyInfo.getPrivateKey();
			
			// 데이터 복호화
        	adminVO.setAdminId(CipherUtils.decryptRSA(adminVO.getAdminId(), privateKey));	        	
        	adminVO.setPhoneNumber(CipherUtils.decryptRSA(adminVO.getPhoneNumber(), privateKey));
        	adminVO.setEmail(CipherUtils.decryptRSA(adminVO.getEmail(), privateKey));
		}
		
		// Administrator 정보 저장
		mainMapper.updateAdmin(adminVO);
		
		return new Result(Constants.Error.RESULT_OK);
	}
	
	
	/*
	 * 관리자 비밀번호 변경 (Self)
	 */
	public Result updateAdminPassword(AdminVO adminVO, String encrypt) throws Exception {
		// Custom 암호화 전송인 경우에만 복호화 진행
		if ("Y".equals(encrypt)) {
			// Key정보 조회
			KeyInfoVO keyInfo = commonService.selectKeyInfo();
			PrivateKey privateKey = keyInfo.getPrivateKey();
			
			// 데이터 복호화        	
        	adminVO.setPassword(CipherUtils.decryptRSA(adminVO.getPassword(), privateKey));   	
        	adminVO.setNewPassword(CipherUtils.decryptRSA(adminVO.getNewPassword(), privateKey));
		}
		
		AdminVO admin = loginMapper.loginCheck(adminVO);	
		if (admin == null) {
			logger.debug("ID or Password doesn't match.");
			// 비밀번호가 일치하지 않습니다.
			return new Result(Constants.Error.RESULT_FAIL, Constants.Error.PASSWORD_NOT_MATCH, MessageUtils.getMessage("server.common.result.error.103.message"));			
		}
		
		// Secure Random Salt 생성(14자리)
		String salt = CipherUtils.genSecureRandomSalt(14);		
		adminVO.setPasswordSalt(salt);
				
		mainMapper.updateAdminPassword(adminVO);
		
		return new Result(Constants.Error.RESULT_OK);
	}
	
}
