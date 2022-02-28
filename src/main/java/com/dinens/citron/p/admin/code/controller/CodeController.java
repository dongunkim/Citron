package com.dinens.citron.p.admin.code.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dinens.citron.p.admin.admin.domain.AdminVO;
import com.dinens.citron.p.admin.code.domain.CodeVO;
import com.dinens.citron.p.admin.code.domain.CodeValueVO;
import com.dinens.citron.p.admin.code.service.CodeService;
import com.dinens.citron.p.admin.common.domain.Result;
import com.dinens.citron.p.admin.common.util.ContextUtils;

@Controller
@RequestMapping(value="/code/*")
public class CodeController {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CodeService codeService;

	@RequestMapping(value="/codeList")
	public String codeList(@ModelAttribute CodeVO codeVO, Model model) throws Exception {

		return "/code/codeList";
	}

	@RequestMapping(value="/ajaxCodeList.json")
	@ResponseBody
	public Map<String, Object> ajaxCodeList(HttpServletRequest request, @ModelAttribute CodeVO codeVO) throws Exception {
		
		// 세션에서 locale을 읽어옴
		codeVO.setLocale(ContextUtils.getLocaleFromSession());
		
		logger.debug("codeVO : " + codeVO);

		return codeService.selectCodeList(codeVO);
	}

	@RequestMapping(value="/ajaxCodeDetail.json")
	@ResponseBody
	public CodeVO ajaxCodeDetail(HttpServletRequest request, @ModelAttribute CodeVO codeVO) throws Exception {
		
		// 세션에서 locale을 읽어옴
		codeVO.setLocale(ContextUtils.getLocaleFromSession());
		
		logger.debug("codeVO : " + codeVO);

		return codeService.selectCodeDetail(codeVO);
	}

	@RequestMapping(value="/ajaxCodeRegist.json")
	@ResponseBody
	public Result ajaxCodeRegist(HttpServletRequest request, @ModelAttribute CodeVO codeVO, HttpSession session) throws Exception {
		
		logger.debug("codeVO : " + codeVO);
		codeVO.setCreateId(session.getAttribute("id").toString());
		
		Result result = codeService.insertCode(codeVO);
		
		return result;
	}

	@RequestMapping(value="/ajaxCodeModify.json")
	@ResponseBody
	public Result ajaxCodeModify(HttpServletRequest request, @ModelAttribute CodeVO codeVO, HttpSession session) throws Exception {
		
		logger.debug("codeVO : " + codeVO);
		codeVO.setUpdateId(session.getAttribute("id").toString());
		
		codeService.updateCode(codeVO);
		
		return new Result("OK");
	}

	@RequestMapping(value="/ajaxCodeDelete.json")
	@ResponseBody
	public Result ajaxCodeDelete(HttpServletRequest request, @ModelAttribute CodeVO codeVO) throws Exception {
		
		logger.debug("codeVO : " + codeVO);
		
		codeService.deleteCode(codeVO);
		
		return new Result("OK");
	}

	@RequestMapping(value="/ajaxCodeValueList.json")
	@ResponseBody
	public Map<String, Object> ajaxCodeValueList(HttpServletRequest request, @ModelAttribute CodeValueVO codeValueVO) throws Exception {
		
		// 세션에서 locale을 읽어옴
		codeValueVO.setLocale(ContextUtils.getLocaleFromSession());
		
		logger.debug("codeVO : " + codeValueVO);

		return codeService.selectCodeValueList(codeValueVO);
	}
	
	@RequestMapping(value="/ajaxCodeValueDetail.json")
	@ResponseBody
	public CodeValueVO ajaxCodeValueDetail(HttpServletRequest request, @ModelAttribute CodeValueVO codeValueVO) throws Exception {
		
		// 세션에서 locale을 읽어옴
		codeValueVO.setLocale(ContextUtils.getLocaleFromSession());
		
		logger.debug("codeValueVO : " + codeValueVO);

		return codeService.selectCodeValueDetail(codeValueVO);
	}

	@RequestMapping(value="/ajaxCodeValueRegist.json")
	@ResponseBody
	public Result ajaxCodeValueRegist(HttpServletRequest request, @ModelAttribute CodeValueVO codeValueVO, HttpSession session) throws Exception {
		
		logger.debug("codeValueVO : " + codeValueVO);
		codeValueVO.setCreateId(session.getAttribute("id").toString());
		
		Result result = codeService.insertCodeValue(codeValueVO);
		
		return result;
	}

	@RequestMapping(value="/ajaxCodeValueModify.json")
	@ResponseBody
	public Result ajaxCodeValueModify(HttpServletRequest request, @ModelAttribute CodeValueVO codeValueVO, HttpSession session) throws Exception {
		
		logger.debug("codeValueVO : " + codeValueVO);
		codeValueVO.setUpdateId(session.getAttribute("id").toString());
		
		codeService.updateCodeValue(codeValueVO);
		
		return new Result("OK");
	}

	@RequestMapping(value="/ajaxCodeValueDelete.json")
	@ResponseBody
	public Result ajaxCodeValueDelete(HttpServletRequest request, @ModelAttribute CodeValueVO codeValueVO) throws Exception {
		
		logger.debug("codeValueVO : " + codeValueVO);
		
		codeService.deleteCodeValue(codeValueVO);
		
		return new Result("OK");
	}
}
