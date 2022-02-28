package com.dinens.citron.p.admin.main.controller;

import javax.servlet.http.HttpSession;

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
import com.dinens.citron.p.admin.main.service.LoginService;

/**
 * <PRE>
 * 1. ClassName: LoginController
 * 2. FileName : LoginController.java
 * 3. Package  : com.dinens.citron.p.admin.main.controller
 * 4. Comment  : 로그인 Controller
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
public class LoginController {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private LoginService loginService;
			
	
	/**
	 * 로그인화면 이동
	 */
	@RequestMapping(value="/login")
	public String login() throws Exception {

		return "/main/login";
	}
	
	
	/**
	 * 로그인 처리
	 */
	@RequestMapping(value = "/login.json", method = RequestMethod.POST)	
	@ResponseBody
	public Result login(@ModelAttribute AdminVO adminVO,
							@Param (value="useEnc")String useEnc) throws Exception {
						
		Result result = loginService.login(adminVO, useEnc);		
		if (Constants.Error.RESULT_OK.equals(result.getStatus())) result.setForward("/main/main");
		
        return result;
	}
	
	
	/**
	 * 로그아웃 처리
	 */
	@RequestMapping(value = "/logout.json", method = RequestMethod.POST)
	@ResponseBody
	public Result logout(HttpSession session) throws Exception {
						
		try {session.invalidate();} catch (IllegalStateException e) {e.printStackTrace();}
		logger.debug("Session is destoryed.");	
						
		Result result = new Result(Constants.Error.RESULT_OK);
		result.setForward("/main/login");
		
        return result;
	}
	
	
	/**
	 * 비밀번호 찾기화면 이동
	 * parameter(nextStep) 값이 없거나 유효하지 않으면 1단계로 이동
	 * parameter(nextStep) 값이 2나 3이면 각 단계로 이동
	 */
	@RequestMapping(value="/findPasswd", method = RequestMethod.POST)
	public String findPasswd(@Param (value="nextStep")String nextStep,
									@Param (value="adminId")String adminId, Model model) throws Exception {
						
		if (nextStep == null) {
			return "/main/findPasswd1st";
		}
		
		if ("2".equals(nextStep)) {
			model.addAttribute("adminId", adminId);
			
			return "/main/findPasswd2nd";
		}
		else if ("3".equals(nextStep)) {
			return "/main/findPasswd3rd";
		}
		else {
			return "/main/findPasswd1st";
		}
	}
	
	
	/**
	 * 비밀번호찾기
	 * 1단계 : 아이디 확인
	 * 2단계 : 이름 + 이메일 인증
	 */
	@RequestMapping(value="/ajaxFindPasswd.json", method = RequestMethod.POST)
	@ResponseBody
	public Result ajaxFindPasswd(@ModelAttribute AdminVO adminVO,
										@Param (value="step")String step,
											@Param (value="useEnc")String useEnc) throws Exception {		
				
		Result result = loginService.checkAdminInfo(adminVO, step, useEnc);
		
		if (Constants.Error.RESULT_OK.equals(result.getStatus())) {			
			if ("1".equals(step)) {
				result.setForward("/main/findPasswd");
			}
		}
		
		return result;
	}
	
	
	/**
	 * 비밀번호찾기 
	 * 3단계 : 사용자의 Confirm 후 임시비밀번호 발송
	 */
	@RequestMapping(value="/ajaxSendPassMail.json", method = RequestMethod.POST)
	@ResponseBody
	public Result sendPassMail(@ModelAttribute AdminVO adminVO,
									@Param (value="useEnc")String useEnc) throws Exception {
		
		Result result = loginService.sendMailForTemporaryPasswd(adminVO, useEnc);
		if (Constants.Error.RESULT_OK.equals(result.getStatus())) result.setForward("/main/findPasswd");
		
		return result;
	}
}
