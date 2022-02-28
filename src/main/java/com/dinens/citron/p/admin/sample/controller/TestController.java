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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.dinens.citron.p.admin.common.common.ParamMap;
import com.dinens.citron.p.admin.common.domain.PagingVO;
import com.dinens.citron.p.admin.common.exception.UnAuthorizedException;
import com.dinens.citron.p.admin.common.service.InitService;
import com.dinens.citron.p.admin.common.util.ContextUtils;
import com.dinens.citron.p.admin.common.util.ExcelUtils;
import com.dinens.citron.p.admin.common.util.MessageUtils;
import com.dinens.citron.p.admin.menu.domain.InitMenuVO;
import com.dinens.citron.p.admin.menu.service.MenuService;
import com.dinens.citron.p.admin.sample.domain.Admin;
import com.dinens.citron.p.admin.sample.domain.Board;
import com.dinens.citron.p.admin.sample.service.SampleService;

@Controller
@RequestMapping(value="/test/*")
public class TestController {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SessionLocaleResolver localeResolver;

	@RequestMapping(value="/testMenu1")
	public String testMenu1() throws Exception {

		return "/test/testMenu1";
	}

	@RequestMapping(value="/testMenu2")
	public String testMenu2() throws Exception {

		return "/test/testMenu2";
	}
	
}
