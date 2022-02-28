package com.dinens.citron.p.admin.sample.controller;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.dinens.citron.p.admin.common.domain.KeyInfoVO;
import com.dinens.citron.p.admin.common.domain.PagingVO;
import com.dinens.citron.p.admin.common.domain.Result;
import com.dinens.citron.p.admin.common.service.CommonService;
import com.dinens.citron.p.admin.common.util.ContextUtils;
import com.dinens.citron.p.admin.sample.domain.Admin;
import com.dinens.citron.p.admin.sample.domain.Board;
import com.dinens.citron.p.admin.sample.service.SampleService;
import com.nhncorp.lucy.security.xss.XssSaxFilter;

/**
 * <PRE>
 * 1. ClassName: SampleRestController
 * 2. FileName : SampleRestController.java
 * 3. Package  : com.dinens.citron.sample.controller
 * 4. Comment  : Sample REST Controller
 * 5. 작성자   : JYPark
 * 6. 작성일   : 2019. 7. 31.
 * 7. 변경이력
 *		이름	:	일자	: 변경내용
 *     ———————————————————————————————————
 *		JYPark:	2019. 07. 31.	: 신규 개발.
  * </PRE>
 */
@RestController
@RequestMapping(value="/sample/*")
public class SampleRestController {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SampleService sampleService;
	
	@Autowired
	private CommonService commonService;
		
	@RequestMapping(value = "/responseTest.json")	
	public Map<String, String> responseTest() {
		
		logger.info("Welcome responseTest!");
		
		Map<String, String> result = new HashMap<String, String>();
        result.put("id", "user");
        result.put("name", "lee");
        result.put("addr", "서울");
        
        return result;        
	}
	
	/*
	 * Client에서 특정 필드(예를들면, password 등)를 암호화하기 위해 서버에 PublicKey 요청
	 */
	@RequestMapping(value = "/getPubKey.json")	
	public Result getPubKey(@ModelAttribute KeyInfoVO keyInfoVO) throws Exception {		
		return commonService.getKeyInfo(keyInfoVO);        
	}
	
	/*
	 * PlainText 형태의 password를 그대로 사용해서 로그인하는 방식
	 */
	@RequestMapping(value = "/login.json")	
	public Result login(@ModelAttribute Admin adminVO) throws Exception {
				
		Result resultVO = sampleService.login(adminVO, "N");		
		resultVO.setForward("/sample/openBoardList");
				
        return resultVO;
	}
	
	/*
	 * 암호화된 password를 사용해서 로그인하는 방식
	 */
	@RequestMapping(value = "/loginEncrypt.json")	
	public Result loginEncrypt(@ModelAttribute Admin adminVO) throws Exception {
		
		Result resultVO = sampleService.login(adminVO, "Y");
		resultVO.setForward("/sample/openBoardList");
		
        return resultVO;
	}
	
	
	@RequestMapping(value = "/logout.json")	
	public Result logout(HttpSession session) throws Exception {
		
		try {session.invalidate();} catch (IllegalStateException e) {e.printStackTrace();}
		logger.debug("Session is destoryed.");	
		
		Result resultVO = new Result("OK");
		resultVO.setForward("/sample/openLogin");		
		
        return resultVO;
	}
	
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/restBoardList.json")
	public Map<String, Object> restBoardList(HttpServletRequest request, @ModelAttribute Board boardVO, @ModelAttribute PagingVO pagingVO, Model model) throws Exception {
		
		Enumeration params = request.getParameterNames();
		while (params.hasMoreElements()){
		    String name = (String)params.nextElement();
		    logger.debug(name + " : " +request.getParameter(name));
		}		
		
		// 세션에서 locale을 읽어옴
		boardVO.setLocale(ContextUtils.getLocaleFromSession());
		
		logger.debug("boardVO : " + boardVO);
		logger.debug("pagingVO : " + pagingVO);
		Map<String, Object> resultMap = sampleService.selectBaordList(boardVO, pagingVO);
		
		//model.addAttribute("pagingVO", resultMap.get("pagingVO"));
		//model.addAttribute("list", resultMap.get("list"));
		
		return resultMap;
	}
	
	
	/*
	 * @RestController(@ResponseBody)를 이용하는 방법 - JSON
	 * 형태 : xxxx.json으로 호출
	 */
	@RequestMapping(value="/restOpenBoardDetail.json")	
	public Map<String, Object> restOpenBoardDetail(@ModelAttribute Board boardVO) throws Exception {
		
		String hitUpdate = "true";
		Map<String, Object> map = sampleService.selectBoardDetail(boardVO, hitUpdate);
		
		Map<String, Object> result = new HashMap<String, Object>();		
		result.put("boardVO", map.get("boardVO"));
		result.put("list", map.get("list"));		
				
		return result;
	}
	
	/*
	 * @RestController(@ResponseBody)를 이용하는 방법 - XML
	 * 형태 : xxxx.xml로 호출
	 *
	@RequestMapping(value="/sample/restOpenBoardDetail.xml")	
	public Map<String, Object> restOpenBoardDetail(HttpServletRequest request, ParamMap paramMap) throws Exception {
		
		paramMap.put("HIT_UPDATE", "true");
		Map<String, Object> map = sampleService.selectBoardDetail(paramMap.getMap());
		
		Map<String, Object> mv = new HashMap<String, Object>();
		
		mv.put("map", map.get("map"));
		mv.put("list", map.get("list"));
		mv.put("result", "OK, jinilamp!");
		
		return mv;
	}
	*/
	
	@RequestMapping(value="/insertBoard.json")
	public Result insertBoardAjax(@ModelAttribute Board boardVO, MultipartHttpServletRequest request) throws Exception {
						
		XssSaxFilter filter = XssSaxFilter.getInstance("lucy-xss-superset-sax.xml", true);		
		boardVO.setContents(filter.doFilter(boardVO.getContents()));
		
		logger.debug("boardVO : " + boardVO);
		
		sampleService.insertBoard(boardVO, request);

		Result resultVO = new Result("OK");
		resultVO.setForward("/sample/openBoardList");		
		
		return resultVO;
	}
	
	@RequestMapping(value="/updateBoard.json")
	public Result updateBoard(@ModelAttribute Board boardVO, MultipartHttpServletRequest request) throws Exception {
		
		logger.debug("boardVO : " + boardVO);
		
		XssSaxFilter filter = XssSaxFilter.getInstance("lucy-xss-superset-sax.xml", true);		
		boardVO.setContents(filter.doFilter(boardVO.getContents()));
		
		sampleService.updateBoard(boardVO, request);		
		
		Result resultVO = new Result("OK");
		resultVO.setForward("/sample/openBoardDetail");		
		
		return resultVO;
	}
	
		
	@RequestMapping(value="/ajaxChangeLang.json")
	public Result ajaxChangeLang(@Param (value="lang")String lang) throws Exception {
		Result result = new Result("OK");
		return result;
	}
	
}
