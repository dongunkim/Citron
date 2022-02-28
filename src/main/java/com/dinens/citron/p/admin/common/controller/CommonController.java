package com.dinens.citron.p.admin.common.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.dinens.citron.p.admin.common.constants.Constants;
import com.dinens.citron.p.admin.common.domain.KeyInfoVO;
import com.dinens.citron.p.admin.common.domain.Result;
import com.dinens.citron.p.admin.common.service.CommonService;
import com.dinens.citron.p.admin.common.util.ContextUtils;
import com.dinens.citron.p.admin.common.util.FileUtils;

/**
 * <PRE>
 * 1. ClassName: CommonController
 * 2. FileName : CommonController.java
 * 3. Package  : com.dinens.citron.common.controller
 * 4. Comment  : Common Controller
 * 5. 작성자   : JYPark
 * 6. 작성일   : 2019. 7. 31.
 * 7. 변경이력
 *		이름	:	일자	: 변경내용
 *     ———————————————————————————————————
 *		JYPark:	2019. 07. 31.	: 신규 개발.
  * </PRE>
 */

@Controller
@RequestMapping(value="/common/*")
public class CommonController {
	Logger logger = LoggerFactory.getLogger(this.getClass());
		
	@Value("${citron.domain}")
	private String domainUrl;
	
	@Autowired
	private CommonService commonService;
	
	/**
	 * PublicKey 조회
	 * Client에서 특정 필드(예를들면, password 등)를 암호화하기 위해 서버에 요청
	 */
	@RequestMapping(value = "/getPubKey.json", method = RequestMethod.POST)
	@ResponseBody
	public Result getPubKey(@ModelAttribute KeyInfoVO keyInfoVO) throws Exception {		
		return commonService.getKeyInfo(keyInfoVO);        
	}
	
	
	/**
	 * 삽입 이미지 업로드
	 */
	@RequestMapping(value="/imageUpload.json", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> imageUpload(MultipartFile file) throws Exception {
		
		// 파일 임시 경로 지정
		String userId = ContextUtils.getAttrFromSession("id").toString();		
		String tempPath = FileUtils.getTempPath(userId);
		
		// 파일 저장 및 저장정보 리턴
		Map<String, Object> resultMap = FileUtils.parseFileInfo(file, tempPath);
		
		String tempUrl = domainUrl + Constants.Upload.IMAGE_TEMP_URI + "/" + userId + "/" + resultMap.get("storedFileName");
		resultMap.put("url", tempUrl);
		
		logger.debug("resultMap : " + resultMap);		
		return resultMap;
	}

	@RequestMapping(value="/changeLocale.json", method = RequestMethod.POST)
	@ResponseBody
	public Result changeLocale() throws Exception {
		return new Result("OK");
	}
}
