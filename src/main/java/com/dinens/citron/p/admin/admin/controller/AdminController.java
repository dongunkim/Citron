package com.dinens.citron.p.admin.admin.controller;

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

import com.dinens.citron.p.admin.admin.domain.AdminVO;
import com.dinens.citron.p.admin.admin.service.AdminService;
import com.dinens.citron.p.admin.admingroup.domain.AdminGroupVO;
import com.dinens.citron.p.admin.admingroup.service.AdminGroupService;
import com.dinens.citron.p.admin.common.domain.PagingVO;
import com.dinens.citron.p.admin.common.domain.Result;
import com.dinens.citron.p.admin.common.util.ContextUtils;
import com.dinens.citron.p.admin.common.util.ExcelUtils;
import com.dinens.citron.p.admin.common.util.MessageUtils;

@Controller
@RequestMapping(value="/admin/*")
public class AdminController {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SessionLocaleResolver localeResolver;

	@Autowired
	private AdminService adminService;

	@Autowired
	private AdminGroupService adminGroupService;

	@RequestMapping(value="/adminList")
	public String adminList(@ModelAttribute AdminVO adminVO, @ModelAttribute PagingVO pagingVO, Model model) throws Exception {

		AdminGroupVO adminGroupVO = new AdminGroupVO();
		adminGroupVO.setCondUseYn("Y");
		model.addAttribute("adminGroupList", adminGroupService.selectAdminGroupList(adminGroupVO));

		return "/admin/adminList";
	}

	@RequestMapping(value="/ajaxAdminList.json")
	@ResponseBody
	public Map<String, Object> ajaxAdminList(HttpServletRequest request, @ModelAttribute AdminVO adminVO, @ModelAttribute PagingVO pagingVO) throws Exception {
		
		// 세션에서 locale을 읽어옴
		adminVO.setLocale(ContextUtils.getLocaleFromSession());
		
		logger.debug("adminVO : " + adminVO);
		logger.debug("pagingVO : " + pagingVO);
		
		Map<String, Object> resultMap = adminService.selectAdminList(adminVO, pagingVO);
		
		return resultMap;
	}
	
	@RequestMapping(value="/excelAdminList")	                       
	public void excelAdminList(@ModelAttribute AdminVO adminVO, HttpServletResponse response) throws Exception {
		
		List<Map<String, Object>> list = adminService.selectAdminListExcel(adminVO);
		
		String fileName = "Admin List";
		String[] columnNames = {
									MessageUtils.getMessage("ui.common.label.id"),
									MessageUtils.getMessage("ui.common.label.name"),
									MessageUtils.getMessage("ui.admingroup.label.adminGroupId"),
									MessageUtils.getMessage("ui.admingroup.label.adminGroupName"),
									MessageUtils.getMessage("ui.admin.label.phoneNumber"),
									MessageUtils.getMessage("ui.admin.label.email"),
									MessageUtils.getMessage("ui.common.label.useYn")
								};
		String[] columnIds = {"ADMIN_ID:L", "ADMIN_NAME:L", "ADMIN_GROUP_ID:C", "ADMIN_GROUP_NAME:C", "PHONE_NUMBER:C", "EMAIL:L", "USE_YN:C"};
		String[] columnWidths = {"4000", "4000", "4000", "4000", "4000", "4000", "4000"};
		
		List<String[]> columnInfo = new ArrayList<String[]>();
		columnInfo.add(columnNames);
		columnInfo.add(columnIds);
		columnInfo.add(columnWidths);
		
		ExcelUtils.downloadExcel(fileName, columnInfo, list, response);
	}

	@RequestMapping(value="/adminDetail")
	public String adminDetail(@ModelAttribute AdminVO adminVO, Model model) throws Exception {

		logger.debug("adminVO : " + adminVO);
		model.addAttribute("info", adminService.selectAdminDetail(adminVO));

		return "/admin/adminDetail";
	}

	@RequestMapping(value="/adminRegist")
	public String adminRegist(Model model) throws Exception {

		AdminGroupVO adminGroupVO = new AdminGroupVO();
		adminGroupVO.setCondUseYn("Y");
		model.addAttribute("adminGroupList", adminGroupService.selectAdminGroupList(adminGroupVO));

		return "/admin/adminRegist";
	}

	@RequestMapping(value="/ajaxAdminRegist.json")
	@ResponseBody
	public Result ajaxAdminRegist(HttpServletRequest request, @ModelAttribute AdminVO adminVO, HttpSession session) throws Exception {
		
		logger.debug("adminVO : " + adminVO);
		adminVO.setCreateId(session.getAttribute("id").toString());
		
		Result result = adminService.insertAdmin(adminVO,"N");
		
		return result;
	}
	
	@RequestMapping(value="/ajaxDuplicateIdCheck.json")
	@ResponseBody
	public boolean ajaxDuplicateIdCheck(HttpServletRequest request, @ModelAttribute AdminVO adminVO, HttpSession session) throws Exception {
		
		logger.debug("adminVO : " + adminVO);
		
		AdminVO info = adminService.selectAdminDetail(adminVO);
		if(info != null)
			return true;
		
		return false;
	}
	
	@RequestMapping(value="/adminModify")
	public String adminModify(@ModelAttribute AdminVO adminVO, Model model) throws Exception {

		logger.debug("adminVO : " + adminVO);

		AdminGroupVO adminGroupVO = new AdminGroupVO();
		adminGroupVO.setCondUseYn("Y");
		model.addAttribute("adminGroupList", adminGroupService.selectAdminGroupList(adminGroupVO));

		model.addAttribute("info", adminService.selectAdminDetail(adminVO));

		return "/admin/adminModify";
	}

	@RequestMapping(value="/ajaxAdminModify.json")
	@ResponseBody
	public Result ajaxAdminModify(HttpServletRequest request, @ModelAttribute AdminVO adminVO, HttpSession session) throws Exception {
		
		logger.debug("adminVO : " + adminVO);
		adminVO.setUpdateId(session.getAttribute("id").toString());
		
		adminService.updateAdmin(adminVO);
		
		return new Result("OK");
	}

	@RequestMapping(value="/ajaxAdminDelete.json")
	@ResponseBody
	public Result ajaxAdminDelete(HttpServletRequest request, @ModelAttribute AdminVO adminVO) throws Exception {
		
		logger.debug("adminVO : " + adminVO);
		
		adminService.deleteAdmin(adminVO);
		
		return new Result("OK");
	}

}
