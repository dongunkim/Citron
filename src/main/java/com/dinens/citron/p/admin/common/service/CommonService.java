package com.dinens.citron.p.admin.common.service;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dinens.citron.p.admin.common.domain.EmailSendHistVO;
import com.dinens.citron.p.admin.common.domain.KeyInfoVO;
import com.dinens.citron.p.admin.common.domain.Result;
import com.dinens.citron.p.admin.common.exception.ApplicationException;
import com.dinens.citron.p.admin.common.mapper.CommonMapper;
import com.dinens.citron.p.admin.common.util.CipherUtils;
import com.dinens.citron.p.admin.common.util.ContextUtils;
import com.dinens.citron.p.admin.common.util.MessageUtils;

/**
 * <PRE>
 * 1. ClassName: CommonService
 * 2. FileName : CommonService.java
 * 3. Package  : com.dinens.citron.common.service
 * 4. Comment  : Common Service Class
 * 5. 작성자   : JYPark
 * 6. 작성일   : 2019. 7. 31.
 * 7. 변경이력
 *		이름	:	일자	: 변경내용
 *     ———————————————————————————————————
 *		JYPark:	2019. 07. 31.	: 신규 개발.
  * </PRE>
 */

@Service
public class CommonService {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private CommonMapper commonMapper;
	
		
	/*
	 * DB에 저장된 PrivateKey 정보 조회
	 * 동일한 SessionID로 생성된 다른 privateKey가 존재할 수 있어 가장 마지막 정보를 조회함
	 */
	public KeyInfoVO selectKeyInfo() throws Exception {
		// 세션 유효성 체크
		if (ContextUtils.getSession() != null) {
			String sessionId = ContextUtils.getSession().getId();

			KeyInfoVO keyInfo = commonMapper.selectKeyInfo(sessionId);
			if (keyInfo == null) {
				// 키값이 유효하지 않습니다.
        		throw new ApplicationException(MessageUtils.getMessage("server.common.result.error.101.message"));
			}
			
			String base64PrivateKey = keyInfo.getPrivateKeyStr();
	    	byte[] bytePrivateKey = Base64.getDecoder().decode(base64PrivateKey);
	    	
	    	KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	    	KeySpec privateKeySpec = new PKCS8EncodedKeySpec(bytePrivateKey);
	    	PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);	    			
	    	keyInfo.setPrivateKey(privateKey);
	    	
	    	// DB privateKey 삭제
        	deleteKeyInfo(sessionId);
        	
			return keyInfo;
		}
		else {
        	// 세션정보가 유효하지 않습니다.
        	throw new ApplicationException(MessageUtils.getMessage("server.common.result.error.001.message"));        	
        }		
	}
	
	/*
	 * 비대칭 키를 생성해서 SessionID와 함께 PrivateKey를 DB에 저장
	 * PublicKey는 response에 담아서 Client에 전달
	 */
	public Result getKeyInfo(KeyInfoVO keyInfoVO) throws Exception {
		KeyPair keyPair = CipherUtils.genRSAKeyPair();
        
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        
        // 공개키를 Base64 인코딩해서 Client로 전달
        byte[] bytePublicKey = publicKey.getEncoded();
        String base64PublicKey = Base64.getEncoder().encodeToString(bytePublicKey);
        logger.debug("base64PublicKey : " + base64PublicKey);
                        
        if (ContextUtils.getSession() != null) {
        	keyInfoVO.setSessionId(ContextUtils.getSession().getId());
        	
        	// 비공개키를 Base64 인코딩해서 DB 저장
        	byte[] bytePrivateKey = privateKey.getEncoded();
            String base64PrivateKey = Base64.getEncoder().encodeToString(bytePrivateKey);
            
            keyInfoVO.setPrivateKeyStr(base64PrivateKey);            
            
            commonMapper.insertKeyInfo(keyInfoVO);
            
            Result resultVO = new Result("OK");            
            resultVO.setPublicKey(base64PublicKey);
            return resultVO;
        }
        else {
        	// 세션정보가 유효하지 않습니다.
        	return new Result("FAIL", "001", MessageUtils.getMessage("server.common.result.error.001.message"));        	
        }
	}
	
	/*
	 * 인증이 완료된 privateKey는 바로 삭제함
	 */
	public void deleteKeyInfo(String sessionId) throws Exception {
		commonMapper.deleteKeyInfo(sessionId);
	}
	
	/*
	 * Email 발송이력 목록 조회
	 */
	public List<EmailSendHistVO> selectEmailSendHistList(EmailSendHistVO emailSendHistVO) throws Exception {
		return commonMapper.selectEmailSendHistList(emailSendHistVO);		
	}
	
	/*
	 * Email 발송이력 정보 저장
	 */
	public void insertEmailSendHistInfo(EmailSendHistVO emailSendHistVO) throws Exception {
		commonMapper.insertEmailSendHistInfo(emailSendHistVO);		
	}
}
