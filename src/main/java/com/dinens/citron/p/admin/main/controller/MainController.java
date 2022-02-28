package com.dinens.citron.p.admin.main.controller;

import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dinens.citron.p.admin.admin.domain.AdminVO;
import com.dinens.citron.p.admin.common.constants.Constants;
import com.dinens.citron.p.admin.common.domain.Result;
import com.dinens.citron.p.admin.common.util.ContextUtils;
import com.dinens.citron.p.admin.main.service.MainService;

/**
 * <PRE>
 * 1. ClassName: MainController
 * 2. FileName : MainController.java
 * 3. Package  : com.dinens.citron.p.admin.main.controller
 * 4. Comment  : 메인 Controller
 * 5. 작성자   : JYPark
 * 6. 작성일   : 2019. 9. 10.
 * 7. 변경이력
 *		이름	:	일자	: 변경내용
 *     ———————————————————————————————————
 *		JYPark:	2019. 09. 10.	: 신규 개발.
  * </PRE>
 */

@Controller
@RequestMapping(value="/main")
public class MainController {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MainService mainService;
	
	
	/**
	 * 메인화면 이동
	 */
	@RequestMapping(value="/main")
	public String main() throws Exception {

		return "/main/main";
	}
	
	
	/**
	 * 내정보화면 이동
	 */
	@RequestMapping(value="/myPage", method = RequestMethod.POST)
	public String myPage(Model model) throws Exception {
		String adminId = (String)ContextUtils.getAttrFromSession("id");
		
		AdminVO admin = mainService.selectAdminDetail(adminId);
		model.addAttribute("admin", admin);
		
		return "/main/myPage";
	}
	
	
	/**
	 * 내정보 수정
	 */
	@RequestMapping(value="/ajaxMyPageModify.json", method = RequestMethod.POST)
	@ResponseBody
	public Result ajaxMyPageModify(@ModelAttribute AdminVO adminVO,
										@Param (value="useEnc")String useEnc) throws Exception {
		
		// 작성자 세팅
		adminVO.setUpdateId(ContextUtils.getAttrFromSession("id").toString());
		
		Result result = mainService.updateMyPage(adminVO, useEnc);		
		if (Constants.Error.RESULT_OK.equals(result.getStatus())) result.setForward("/main/main");		
		
		return result;		
	}
	
	
	/**
	 * 비밀번호 변경화면 이동
	 */
	@RequestMapping(value="/changePassword", method = RequestMethod.POST)
	public String changePassword() throws Exception {

		return "/main/changePassword";
	}
	
	
	/**
	 * 비밀번호 변경
	 */
	@RequestMapping(value="/ajaxChangePasswd.json", method = RequestMethod.POST)
	@ResponseBody
	public Result ajaxChangePasswd(@ModelAttribute AdminVO adminVO,
										@Param (value="useEnc")String useEnc) throws Exception {
		
		logger.debug("currentPassword : " + adminVO.getPassword());
		logger.debug("newPassword : " + adminVO.getNewPassword());
		
		// adminVO 세팅		
		String adminId = (String)ContextUtils.getAttrFromSession("id");
		adminVO.setAdminId(adminId);		
		adminVO.setUpdateId(adminId);
		
		Result result = mainService.updateAdminPassword(adminVO, useEnc);
		if (Constants.Error.RESULT_OK.equals(result.getStatus())) result.setForward("/main/main");
		
		return result;
	}
}
