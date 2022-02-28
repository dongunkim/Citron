package com.dinens.citron.p.admin.sample.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.dinens.citron.p.admin.common.common.ParamMap;
import com.dinens.citron.p.admin.common.constants.Constants;
import com.dinens.citron.p.admin.common.domain.PagingVO;
import com.dinens.citron.p.admin.common.exception.UnAuthorizedException;
import com.dinens.citron.p.admin.common.service.InitService;
import com.dinens.citron.p.admin.common.service.SeqInfoService;
import com.dinens.citron.p.admin.common.util.ContextUtils;
import com.dinens.citron.p.admin.common.util.ExcelUtils;
import com.dinens.citron.p.admin.common.util.FileUtils;
import com.dinens.citron.p.admin.common.util.MessageUtils;
import com.dinens.citron.p.admin.menu.domain.InitMenuVO;
import com.dinens.citron.p.admin.sample.domain.Admin;
import com.dinens.citron.p.admin.sample.domain.Board;
import com.dinens.citron.p.admin.sample.service.SampleService;

@Controller
@RequestMapping(value="/sample/*")
public class SampleController {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SessionLocaleResolver localeResolver;
		
	@Autowired
	private SampleService sampleService;
		
	@Autowired
	private InitService initService;
	
	@Autowired
	private SeqInfoService seqInfoService;
	
	@Value("${file.path}")
	private String filePath;
	
	@RequestMapping(value="/openAccountRegister")
	public String openAccountRegister() throws Exception {						
		return "/sample/accountRegister";
	}
	
	@RequestMapping(value="/insertAccount")
	public String insertAccount(@ModelAttribute Admin adminVO) throws Exception {
		
		sampleService.insertAdmin(adminVO, "N");
		
		return "/sample/login";
	}
	
	@RequestMapping(value="/encInsertAccount")
	public String encInsertAccount(@ModelAttribute Admin adminVO) throws Exception {
						
		sampleService.insertAdmin(adminVO, "Y");
		
		return "/sample/login";
	}
	
	@RequestMapping(value="/openLogin")
	public ModelAndView openLogin() throws Exception {
		ModelAndView mv = new ModelAndView("/sample/login");
				
		return mv;
	}
	
	@RequestMapping(value="/openBoardList")
	public String openBoardList(@ModelAttribute Board boardVO, @ModelAttribute PagingVO pagingVO, Model model) throws Exception {
		
		// 세션에서 locale을 읽어옴
		boardVO.setLocale(ContextUtils.getLocaleFromSession());
		
		logger.debug("boardVO : " + boardVO);
		logger.debug("pagingVO : " + pagingVO);
		Map<String, Object> resultMap = sampleService.selectBaordList(boardVO, pagingVO);
		
		model.addAttribute("pagingVO", resultMap.get("pagingVO"));
		model.addAttribute("list", resultMap.get("list"));
		
		return "/sample/boardList2";
	}
			
	/*
	 * Excel 다운로드
	 * 가이드 : Excel 다운로드는 Excel에 보여줄 항목만 Select하는 별도의 SQL을 만들고,
	 *         VO형식이 아닌 Map으로 Return해서, 
	 *         ColumnName과 ColumnId와 함께 ExcelUtils 함수에 넘겨줌
	 *          
	 * 참고    : 1) Service단에서 ExcelUtils를 호출해도 되나, response 처리가 포함되어 있어 
	 *            아래 예제는 Controller단에서 처리함
	 *         2) Mapper에서 Bean 타입으로 리턴을 받아 처리해야 하는 경우에는
	 *            아래의 BeanToMap 변환을 통해 우선 Map으로 전환 후에 ExcelUtils 호출
	 *            
	 * 추가     : 사이즈값 : 100 입력시 , 엑셀의 열 너비 사이즈 0.2정도와 비슷하다.
	 *          엑셀의 기본 셀 WIDTH : 2800 정도
	 */
	@RequestMapping(value="/excelBoardList")	                       
	public void exelBoardList(@ModelAttribute Board boardVO, HttpServletResponse response) throws Exception {
		
		List<Map<String, Object>> list = sampleService.selectBaordExcelList(boardVO);
		
		// Bean타입 List를 Map타입 List로 전환
		/*
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		Map<String, Object> map;
		ObjectMapper objectMapper = new ObjectMapper();
		for(int i=0; i<list.size(); i++) {
			map = objectMapper.convertValue(list.get(i), Map.class);			
			mapList.add(map);
		}
		*/
		
		String fileName = "Board List (게시판)";
		String[] columnNames = {"글번호", "제목", "조회수", "작성일"};
		String[] columnIds = {"idx:C", "title:L", "hitCnt:C", "creaDtm:C"};
		
		List<String[]> columnInfo = new ArrayList<String[]>();
		columnInfo.add(columnNames);
		columnInfo.add(columnIds);
		
		ExcelUtils.downloadExcel(fileName, columnInfo, list, response);		
	}
		
	@RequestMapping(value="/openBoardDetail")
	public String openBoardDetail(@ModelAttribute Board boardVO, Model model) throws Exception {
						
		String hitUpdate = "true";
		Map<String, Object> map = sampleService.selectBoardDetail(boardVO, hitUpdate);
		model.addAttribute("boardVO", map.get("boardVO"));
		model.addAttribute("list", map.get("list"));
						
		return "/sample/boardDetail";
	}
	
	@RequestMapping(value="/openBoardWrite")
	public String openBoardWrite() throws Exception {		
		return "/sample/boardWrite2";
	}
	
	@RequestMapping(value="/openBoardUpdate")
	public String openBoardUpdate(@ModelAttribute Board boardVO, Model model) throws Exception {
		
		Map<String, Object> map = sampleService.selectBoardDetail(boardVO, null);
		model.addAttribute("boardVO", map.get("boardVO"));
		model.addAttribute("list", map.get("list"));
		
		return "/sample/boardUpdate2";
	}
		
	@RequestMapping(value="/deleteBoard")
	public String deleteBoard(@ModelAttribute Board boardVO) throws Exception {
		sampleService.deleteBoard(boardVO);
		
		return "redirect:/sample/openBoardList";
	}	
		
	@RequestMapping(value="/openMailSend")
	public String openMailSend() throws Exception {				
		return "/sample/mailSend";
	}
	
	@RequestMapping(value="/sendMail")
	public String sendMail(@ModelAttribute Admin adminVO, HttpSession session) throws Exception {
		
		if (session == null || session.getAttribute("id") == null) return "/sample/login";
				
		String adminId = session.getAttribute("id").toString();
		adminVO.setAdminId(adminId);
		adminVO.setCreateId(adminId);
		sampleService.sendPasswdMail(adminVO);
		
		return "/sample/mailSend";
	}
	
	/****************************************************************************************/
	// 테스트 Pages
	/****************************************************************************************/
	@RequestMapping(value="/testMapArgumentResolver")
	public ModelAndView testMapARgumentResolver(Locale locale, HttpServletRequest request, ParamMap paramMap) throws Exception {
		ModelAndView mv = new ModelAndView("");
		logger.debug("properties(filePath) : " + filePath);
		
		logger.debug("The client locale is {}.", locale);
		logger.debug("Session local is {}.", localeResolver.resolveLocale(request));
		logger.debug("Common error message is {}", MessageUtils.getMessage("UI.sample.error.common"));
		logger.debug("Minlength error message is {}", MessageUtils.getMessage("UI.sample.error.minlength", new String[] {"테스트", "2"}));
				
		return mv;
	}
	
	
	@RequestMapping(value="/openException")
	public String openException(HttpServletRequest request) throws Exception {
		
		/*
		String locale = ContextUtils.getLocaleFromSession();
		List<MenuVO> list = menuService.selectMenuList(locale);
		logger.debug("menuList : " + list);
		*/	
		
		return "/sample/testException";
	}
	
	@RequestMapping(value="/checkException")
	public Map<String, Object> checkException(HttpServletRequest request, @RequestParam(value = "exception") String exType) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		
		/*
		Enumeration<String> params = request.getParameterNames();
		while (params.hasMoreElements()){
		    String name = (String)params.nextElement();
		    logger.debug(name + " : " +request.getParameter(name));
		}
		*/
		
		Enumeration<String> params = request.getHeaderNames();
		while (params.hasMoreElements()){
		    String name = (String)params.nextElement();
		    logger.debug(name + " : " +request.getHeader(name));
		}
				
		if (exType.equals("view500")) {
			throw new Exception("Exception 예외를 발생시킵니다.");
		}
		else if (exType.equals("view403")) {
			throw new UnAuthorizedException("UnAuthorizedException 예외를 발생시킵니다.");
		}
		else if (exType.equals("viewSQL")) {
			throw new SQLException("SQLException 예외를 발생시킵니다.");
		}
		else if (exType.equals("ajax403")) {
			throw new UnAuthorizedException("[Ajax] UnAuthorizedException 예외를 발생시킵니다.");
		}
		else if (exType.equals("ajax500")) {
			throw new Exception("[Ajax] Exception 예외를 발생시킵니다.");
		}
		
		
		return result;
	}
	
	@RequestMapping(value="/openMenu")
	public String openMenu(HttpServletRequest request) throws Exception {
					
		initService.refresh();
		
		List<InitMenuVO> list = initService.getMenuInfo("001", "ko");
		logger.debug("list : " + list);		
		
		return "/sample/showMenu";
	}
	
	@RequestMapping(value="/openFilter")
	public String openFilter() throws Exception {
		
		return "/sample/openFilter";
	}
	
	@RequestMapping(value="/multipartFormFilter")
	public String multipartFormFilter(MultipartHttpServletRequest request, @ModelAttribute Board boardVO, Model model) throws Exception {
		logger.debug("boardVO : " + boardVO);
		
		// 파일 경로 지정
		String uploadPath = FileUtils.getUploadPath(Constants.Upload.UPLOAD_CATEGORY_BOARD, Constants.Upload.UPLOAD_TYPE_ATTACH);
				
		List<Map<String, Object>> list = FileUtils.parseFileInfo(request, uploadPath);
				
		model.addAttribute("boardVO", boardVO);
		
		return "/sample/openFilter";
	}
	
	@RequestMapping(value="/formFilter")
	public String formFilter(HttpServletRequest request, @ModelAttribute Board boardVO, Model model) throws Exception {
		logger.debug("boardVO : " + boardVO);		
		
		model.addAttribute("boardVO", boardVO);
		
		return "/sample/openFilter";
	}
	
	@RequestMapping(value="/ajaxFilter.json")
	@ResponseBody
	public Map<String, Object> ajaxFilter(HttpServletRequest request, @RequestBody Board boardVO, Model model) throws Exception {
		logger.debug("boardVO : " + boardVO);
				
		Map<String, Object> map = new HashMap<String, Object>();			
		map.put("boardVO", boardVO);
		
		return map;
	}
	
	@RequestMapping(value="/multipartAjaxFilter.json")
	@ResponseBody
	public Map<String, Object> multipartAjaxFilter(MultipartHttpServletRequest request, @ModelAttribute Board boardVO, Model model) throws Exception {
		logger.debug("boardVO : " + boardVO);
		
		Enumeration<String> iterator = request.getParameterNames();
		while(iterator.hasMoreElements()) {
			String name = iterator.nextElement();
			String value = request.getParameter(name);
			logger.debug("[" + name + "] : " + value);
		}
		
		// 파일 경로 지정
		String uploadPath = FileUtils.getUploadPath(Constants.Upload.UPLOAD_CATEGORY_BOARD, Constants.Upload.UPLOAD_TYPE_ATTACH);
				
		List<Map<String, Object>> list = FileUtils.parseFileInfo(request, uploadPath);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("boardVO", boardVO);
		
		return map;
	}
	
	@RequestMapping(value="/openSequence")
	public String openSequence() throws Exception {
		
		return "/sample/openSequence";
	}
	
	@RequestMapping(value="/getSequence")
	@ResponseBody
	public String getSequence() throws Exception {
		
		int cnt = 5;
		for (int i=0; i<cnt; i++) {
			logger.debug("nextId : " + seqInfoService.selectNextId("T_BOARD"));
		}
		
		return "ok";
	}
	
	@RequestMapping(value="/openValidate")
	public String openValidate() throws Exception {
		
		return "/sample/openValidate";
	}
	
	
	@RequestMapping(value="/submitValidate")
	public String submitValidate(HttpServletRequest request) throws Exception {
		
		Enumeration<String> iterator = request.getParameterNames();
		while(iterator.hasMoreElements()) {
			String name = iterator.nextElement();
			String value = request.getParameter(name);
			logger.debug("[" + name + "] : " + value);
		}
		
		return "ok";
	}
	
	
	@RequestMapping(value="/openLanguage")
	public String openLanguage() throws Exception {
		
		return "/sample/changeLang";
	}
}
