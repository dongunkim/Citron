package com.dinens.citron.p.admin.common.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.dinens.citron.p.admin.common.exception.UnAuthorizedException;
import com.dinens.citron.p.admin.common.service.InitService;
import com.dinens.citron.p.admin.common.util.ContextUtils;
import com.dinens.citron.p.admin.menu.domain.InitMenuVO;

/**
 * <PRE>
 * 1. ClassName: AuthInterceptor
 * 2. FileName : AuthInterceptor.java
 * 3. Package  : com.dinens.citron.common.interceptor
 * 4. Comment  : Handler가 호출되기 전에 세션 체크
 * 5. 작성자   : JYPark
 * 6. 작성일   : 2019. 7. 31.
 * 7. 변경이력
 *		이름	:	일자	: 변경내용
 *     ———————————————————————————————————
 *		JYPark:	2019. 07. 31.	: 신규 개발.
  * </PRE>
 */

@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private SessionLocaleResolver localeResolver;
		
	// Properties
	private String redirectPage;	
	private List<String> noSession;
	
	@Autowired
	private InitService initService;
	
	public String getRedirectPage() {
		return redirectPage;
	}
	public void setRedirectPage(String redirectPage) {
		this.redirectPage = redirectPage;
	}
	public List<String> getNoSession() {
		return noSession;
	}
	public void setNoSession(List<String> noSession) {
		this.noSession = noSession;
	}

	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {				
		HttpSession session = request.getSession();
		
		// 세션에 locale 설정
		session.setAttribute("locale", localeResolver.resolveLocale(request).toString());
				
		if (logger.isDebugEnabled()) {			
			logger.debug("noSession : " + noSession);
			logger.debug("redirectPage : " + redirectPage);		
			logger.debug("getRequestURI : " + request.getRequestURI());	
			logger.debug("groupId : " + session.getAttribute("groupId"));
			logger.debug("locale : " + session.getAttribute("locale"));
		}
		
		// 세션 예외 처리		
		String uri = request.getRequestURI();		
		if (noSession.contains(uri)) {		
			if (logger.isDebugEnabled()) {
				logger.debug("Session Check is skipped.");				
			}
			return true;
		}
				
		// 세션 체크
		if (session == null||session.getAttribute("id")==null) {
			if (logger.isDebugEnabled()) {
				logger.debug("Session is Invaild. Request is redirected to Login.");				
			}
						
			if (ContextUtils.isAjaxRequest()) {
				throw new UnAuthorizedException();
			}
			else {
				response.sendRedirect("/main/login");		// 추후 메인페이지로 변경
				return false;
			}
		}
				
		if (logger.isDebugEnabled()) {
			logger.debug("Session Check is passed.");			
		}
		
		// 현재 호출된 메뉴정보 설정
		setCurrentMenu(session, uri);
				
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {				
		super.postHandle(request, response, handler, modelAndView);		
	}
	
	/*
	 * 현재 요청된 URI의 메뉴정보 설정
	 * URI가 매칭되면 session 업데이트
	 * URI가 매칭되지 않으면, SKIP(기존 세션정보 그대로 사용)
	 */
	private void setCurrentMenu(HttpSession session, String requestUri) throws Exception {
		
		if (requestUri.equals("/main/main")) {
			session.removeAttribute("currentMenu");
			return;
		}
		
		String groupId = (String) session.getAttribute("groupId");
		String locale = (String) session.getAttribute("locale");
		
		List<InitMenuVO> list = initService.getMenuInfo(groupId, locale);
		for(int i=0; i<list.size(); i++) {
			if (requestUri.equals(list.get(i).getScreenUri()) && list.get(i).getDepth() > 1) {				
				session.setAttribute("currentMenu", list.get(i));				
				break;
			}
		}		
	}
}
