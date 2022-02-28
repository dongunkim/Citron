package com.dinens.citron.p.admin.common.tags;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.dinens.citron.p.admin.common.service.InitService;
import com.dinens.citron.p.admin.common.util.ContextUtils;
import com.dinens.citron.p.admin.menu.domain.InitMenuVO;

@Component
public class MenuCustomTag extends TagSupport {

	private static final long serialVersionUID = 3247787306553891724L;

	Logger logger = LoggerFactory.getLogger(this.getClass());
		
	private String level;
	
	@Override
	public int doStartTag() throws JspException {		
		
		// InitService 직접 가져오기
		InitService initService = (InitService)ContextUtils.getBean("initService");
		HttpSession session = pageContext.getSession();		
		
		// 권한에 해당하는 메뉴정보 조회
		String locale = ContextUtils.getLocaleFromSession();
		String groupId = (String) ContextUtils.getAttrFromSession("groupId");		
		List<InitMenuVO> list = null;
		
		try {
			list = initService.getMenuInfo(groupId, locale);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspException("Exception Error - " + e.getMessage());
		}
				
		// Current 메뉴정보 조회 (Interceptor에서 세션에 저장)
		InitMenuVO currentMenu = (InitMenuVO) session.getAttribute("currentMenu");		
		String topMenuId = (currentMenu != null ? currentMenu.getTopMenuId() : "");
		String menuId = (currentMenu != null ? currentMenu.getMenuId() : "");
		String upperMenuId = (currentMenu != null ? currentMenu.getUpperMenuId() : "");
		
		if ("".equals(menuId)) {
			logger.debug("CurrentMenu is missing.");
		}		
		
		StringBuilder menuStr = new StringBuilder();		
		// TOP 메뉴 구성
		if ("TOP".equals(level)) {			
			menuStr.append("<ul class='nav navbar-nav navbar-left'>");
			
			for(InitMenuVO menu : list) {
				if (menu.getDepth() != 1) continue;
				
				if (topMenuId.equals(menu.getMenuId()))
					menuStr.append("<li class='active'><a href='#' name='menu' data-url='" + menu.getScreenUri() + "'>" + menu.getMenuName() + "</a></li>");
				else
					menuStr.append("<li><a href='#' name='menu' data-url='" + menu.getScreenUri() + "'>" + menu.getMenuName() + "</a></li>");
			}
			menuStr.append("</ul>");
		}
		// SUB(LEFT) 메뉴 구성
		else {			
			menuStr.append("<ul class='nav nav-sidebar'>");
			
			int depth = 1;
			for(InitMenuVO menu : list) {
				if (menu.getDepth() == 1) continue;				
				if (!topMenuId.equals(menu.getTopMenuId())) continue;
			
				if (depth != menu.getDepth()) {					
					if (depth == 2) menuStr.append("<ul class='nav'>");
					if (depth == 3) menuStr.append("</ul></li>");
					depth = menu.getDepth();
				}
				
				String activeClass = menuId.equals(menu.getMenuId()) ? " class='active'" : "";
				String openClass = upperMenuId.equals(menu.getMenuId()) ? " open" : "";
				if ("Y".equals(menu.getScreenLinkYn()))
					menuStr.append("<li" + activeClass+"><a href='#' name='menu' data-url='" + menu.getScreenUri() + "'>" + menu.getMenuName() + "</a></li>");
				else 
					menuStr.append("<li class='has-child" + openClass + "'><a href='#'>" + menu.getMenuName() + "</a>");
			}
			if (depth == 3) menuStr.append("</ul></li>");
			menuStr.append("</ul>");
		}
				
		try {
			pageContext.getOut().print(menuStr.toString());
			
		} catch (IOException e) {
			e.printStackTrace();
			throw new JspException("IOException Error - " + e.getMessage());
		}
		
		return SKIP_BODY;
	}
	
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {		
		this.level = level;
	}	
}
