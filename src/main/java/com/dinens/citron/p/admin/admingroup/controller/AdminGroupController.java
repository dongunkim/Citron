package com.dinens.citron.p.admin.admingroup.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.dinens.citron.p.admin.admingroup.domain.AdminGroupVO;
import com.dinens.citron.p.admin.admingroup.service.AdminGroupService;
import com.dinens.citron.p.admin.admingroupauth.domain.AdminGroupAuthVO;
import com.dinens.citron.p.admin.admingroupauth.service.AdminGroupAuthService;
import com.dinens.citron.p.admin.common.domain.PagingVO;
import com.dinens.citron.p.admin.common.domain.Result;
import com.dinens.citron.p.admin.common.util.ContextUtils;
import com.dinens.citron.p.admin.common.util.ExcelUtils;
import com.dinens.citron.p.admin.common.util.MessageUtils;
import com.dinens.citron.p.admin.menu.domain.MenuTreeVO;

@Controller
@RequestMapping(value="/admingroup")
public class AdminGroupController {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SessionLocaleResolver localeResolver;

	@Autowired
	private AdminGroupService adminGroupService;

	@Autowired
	private AdminGroupAuthService adminGroupAuthService;

	@RequestMapping(value="/adminGroupList")
	public String adminGroupList(@ModelAttribute AdminGroupVO adminGroupVO) throws Exception {

		return "/admingroup/adminGroupList";
	}

	@RequestMapping(value="/ajaxAdminGroupList.json")
	@ResponseBody
	public Map<String, Object> ajaxAdminGroupList(HttpServletRequest request, @ModelAttribute AdminGroupVO adminGroupVO, @ModelAttribute PagingVO pagingVO) throws Exception {
		
		// 세션에서 locale을 읽어옴
		adminGroupVO.setLocale(ContextUtils.getLocaleFromSession());
		
		logger.debug("adminGroupVO : " + adminGroupVO);
		logger.debug("pagingVO : " + pagingVO);
		
		Map<String, Object> resultMap = adminGroupService.selectAdminGroupList(adminGroupVO, pagingVO);
		
		return resultMap;
	}
	
	@RequestMapping(value="/excelAdminGroupList")	                       
	public void excelAdminGroupList(@ModelAttribute AdminGroupVO adminGroupVO, HttpServletResponse response) throws Exception {
		
		List<Map<String, Object>> list = adminGroupService.selectAdminGroupListExcel(adminGroupVO);
		
		String fileName = "Admin List";
		String[] columnNames = {
									MessageUtils.getMessage("ui.common.label.id"),
									MessageUtils.getMessage("ui.common.label.name"),
									MessageUtils.getMessage("ui.common.label.useYn")
								};
		String[] columnIds = {"ADMIN_GROUP_ID:L", "ADMIN_GROUP_NAME:L", "USE_YN:C"};
		String[] columnWidths = {"4000", "4000", "4000"};
		
		List<String[]> columnInfo = new ArrayList<String[]>();
		columnInfo.add(columnNames);
		columnInfo.add(columnIds);
		columnInfo.add(columnWidths);
		
		ExcelUtils.downloadExcel(fileName, columnInfo, list, response);
	}

	@RequestMapping(value="/adminGroupDetail")
	public String adminGroupDetail(@ModelAttribute AdminGroupVO adminGroupVO, Model model) throws Exception {

		model.addAttribute("info", adminGroupService.selectAdminGroupDetail(adminGroupVO));

		return "/admingroup/adminGroupDetail";
	}

	@RequestMapping(value="/adminGroupRegist")
	public String adminGroupRegist(@ModelAttribute AdminGroupVO adminGroupVO) throws Exception {

		return "/admingroup/adminGroupRegist";
	}

	@RequestMapping(value="/ajaxAdminGroupRegist.json")
	@ResponseBody
	public Result ajaxAdminGroupRegist(HttpServletRequest request, @ModelAttribute AdminGroupVO adminGroupVO, HttpSession session) throws Exception {
		
		logger.debug("adminGroupVO : " + adminGroupVO);
		adminGroupVO.setCreateId(session.getAttribute("id").toString());
		
		adminGroupService.insertAdminGroup(adminGroupVO);
		
		return new Result("OK");
	}
	

	@RequestMapping(value="/adminGroupModify")
	public String adminGroupModify(@ModelAttribute AdminGroupVO adminGroupVO, Model model) throws Exception {

		model.addAttribute("info", adminGroupService.selectAdminGroupDetail(adminGroupVO));

		return "/admingroup/adminGroupModify";
	}

	@RequestMapping(value="/ajaxAdminGroupModify.json")
	@ResponseBody
	public Result ajaxAdminModify(HttpServletRequest request, @ModelAttribute AdminGroupVO adminGroupVO, HttpSession session) throws Exception {
		
		logger.debug("adminGroupVO : " + adminGroupVO);
		adminGroupVO.setUpdateId(session.getAttribute("id").toString());
		
		adminGroupService.updateAdminGroup(adminGroupVO);
		
		return new Result("OK");
	}

	@RequestMapping(value="/ajaxAdminGroupDelete.json")
	@ResponseBody
	public Result ajaxAdminGroupDelete(HttpServletRequest request, @ModelAttribute AdminGroupVO adminGroupVO) throws Exception {
		
		logger.debug("adminGroupVO : " + adminGroupVO);
		
		Result result = adminGroupService.deleteAdminGroup(adminGroupVO);
		
		return result;
	}

	@RequestMapping(value="/ajaxAdminGroupAuthList.json")
	@ResponseBody
	public List<MenuTreeVO> ajaxAdminGroupAuthList(HttpServletRequest request, @ModelAttribute AdminGroupAuthVO adminGroupAuthVO, HttpSession session) throws Exception {
		
		logger.debug("adminGroupAuthVO : " + adminGroupAuthVO);
		
		return adminGroupAuthService.selectMenuAuthTree(adminGroupAuthVO);
	}

}
