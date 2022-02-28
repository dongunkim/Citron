package com.dinens.citron.p.admin.menu.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.dinens.citron.p.admin.common.domain.Result;
import com.dinens.citron.p.admin.common.util.ContextUtils;
import com.dinens.citron.p.admin.menu.domain.MenuTreeVO;
import com.dinens.citron.p.admin.menu.domain.MenuVO;
import com.dinens.citron.p.admin.menu.service.MenuService;

@Controller
@RequestMapping(value="/menu/*")
public class MenuController {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SessionLocaleResolver localeResolver;

	@Autowired
	private MenuService menuService;

	@RequestMapping(value="/menuList")
	public String menuList() throws Exception {

		return "/menu/menuList";
	}

	@RequestMapping(value="/ajaxMenuList.json")
	@ResponseBody
	public List<MenuTreeVO> ajaxMenuList(HttpServletRequest request, @ModelAttribute MenuVO menuVO) throws Exception {
		
		// 세션에서 locale을 읽어옴
		menuVO.setLocale(ContextUtils.getLocaleFromSession());
		
		logger.debug("menuVO : " + menuVO);
				
		return menuService.selectMenuTree(menuVO);
	}

	@RequestMapping(value="/ajaxMenuDetail.json")
	@ResponseBody
	public MenuVO ajaxMenuDetail(HttpServletRequest request, @ModelAttribute MenuVO menuVO) throws Exception {
		
		// 세션에서 locale을 읽어옴
		menuVO.setLocale(ContextUtils.getLocaleFromSession());
		
		logger.debug("menuVO : " + menuVO);
		
		return menuService.selectMenuDetail(menuVO);
	}

	@RequestMapping(value="/ajaxMenuRegist.json")
	@ResponseBody
	public Result ajaxMenuRegist(HttpServletRequest request, @ModelAttribute MenuVO menuVO, HttpSession session) throws Exception {
		
		if(!"Y".equals(menuVO.getScreenLinkYn()))
			menuVO.setScreenLinkYn("N");

		if("N".equals(menuVO.getScreenLinkYn()))
			menuVO.setScreenUri(null);

		logger.debug("menuVO : " + menuVO);

		menuVO.setCreateId(session.getAttribute("id").toString());
		
		return menuService.insertMenu(menuVO);
	}
	
	@RequestMapping(value="/ajaxMenuModify.json")
	@ResponseBody
	public Result ajaxMenuModify(HttpServletRequest request, @ModelAttribute MenuVO menuVO, HttpSession session) throws Exception {
		
		logger.debug("menuVO : " + menuVO);
		menuVO.setUpdateId(session.getAttribute("id").toString());
		
		if(!"Y".equals(menuVO.getScreenLinkYn()))
			menuVO.setScreenLinkYn("N");

		if("N".equals(menuVO.getScreenLinkYn()))
			menuVO.setScreenUri(null);
		
		return menuService.updateMenu(menuVO);
	}

	@RequestMapping(value="/ajaxMenuDelete.json")
	@ResponseBody
	public Result ajaxMenuDelete(HttpServletRequest request, @ModelAttribute MenuVO menuVO) throws Exception {
		
		logger.debug("menuVO : " + menuVO);
		
		menuService.deleteMenu(menuVO);
		
		return new Result("OK");
	}

}
