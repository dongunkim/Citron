package com.dinens.citron.p.admin.common.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * <PRE>
 * 1. ClassName: ContextUtils
 * 2. FileName : ContextUtils.java
 * 3. Package  : com.dinens.citron.common.util
 * 4. Comment  : Contenxt 관련 Function 모음
 *               Request/Response 객체, HttpSession 객체 등을 직접 핸들링
 * 5. 작성자   : JYPark
 * 6. 작성일   : 2019. 7. 31.
 * 7. 변경이력
 *		이름	:	일자	: 변경내용
 *     ———————————————————————————————————
 *		JYPark:	2019. 07. 31.	: 신규 개발.
  * </PRE>
 */

public class ContextUtils {
		
    /**
     * 빈을 직접 얻습니다.
     *
     * @param beanName
     * @return
     */
    public static Object getBean(String beanName) {
        WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
        return context.getBean(beanName);
    }

    /**
     * HttpServletReqeust 객체를 직접 얻습니다.
     * @return
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attr = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();        
        return attr.getRequest();
    }

    /**
     * HttpServletResponse 객체를 직접 얻습니다.
     * @return
     */
    public static HttpServletResponse getResponse() {
        ServletRequestAttributes attr = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
        return attr.getResponse();
    }

    /**
     * HttpSession 객체를 직접 얻습니다.
     *
     * @param
     * @return
     */
    public static HttpSession getSession() {
        return getSession(true);
    }
    
    /**
     * HttpSession 객체를 직접 얻습니다.
     *
     * @param gen 새 세션 생성 여부
     * @return
     */
    public static HttpSession getSession(boolean gen) {
        return getRequest().getSession(gen);
    }

    /**
     * REQUEST 영역에서 가져오기
     *
     * @param key
     * @return
     */
    public static Object getAttrFromRequest(String key) {
        ServletRequestAttributes attr = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
        return attr.getAttribute(key, ServletRequestAttributes.SCOPE_REQUEST);
    }

    /**
     * REQUEST 영역에 객체 저장
     *
     * @param key
     * @param obj
     */
    public static void setAttrToRequest(String key, Object obj) {
        ServletRequestAttributes attr = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
        attr.setAttribute(key, obj, ServletRequestAttributes.SCOPE_REQUEST);
    }

    /**
     * SESSION 영역에서 가져오기
     *
     * @param key
     * @return
     */
    public static Object getAttrFromSession(String key) {
        ServletRequestAttributes attr = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
        return attr.getAttribute(key, ServletRequestAttributes.SCOPE_SESSION);
    }

    /**
     * Session 영역에 객체 저장
     *
     * @param key
     * @param obj
     */
    public static void setAttrToSession(String key, Object obj) {
        ServletRequestAttributes attr = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
        attr.setAttribute(key, obj, ServletRequestAttributes.SCOPE_SESSION);
    }
    
    /**
     * 현재 Locale 정보를 Session 영역에서 가져옴
     * 세션에 해당값이 없을 경우, Default로 'ko' 세팅     
     */
    public static String getLocaleFromSession() {    	
    	return (getAttrFromSession("locale") == null ? "ko" : getAttrFromSession("locale").toString());
    }
    
    
    /**
     * 특정 쿠키값을 읽어옴
     */
    public static String getCookie(String name) {
    	Cookie[] cookies = getRequest().getCookies();
    	if (cookies != null) {
    		for(Cookie cookie : cookies) {
    			if (name.equals(cookie.getName())) {
    				return cookie.getValue();
    			}
    		}
    	}
    	return null;
    }
    
    
    /**
     * 특정 쿠키값을 삭제함
     */
    public static void removeCookie(String name) {
    	Cookie cookie = new Cookie(name, null);
    	cookie.setMaxAge(0);
    	
    	getResponse().addCookie(cookie);    	
    }
    
    
    /**
     * Ajax 요청인지 체크     
     */
    public static boolean isAjaxRequest() {
    	HttpServletRequest request = getRequest();
    	
    	if ((request.getHeader("X-Requested-With") != null
				&& request.getHeader("X-Requested-With").toLowerCase() == "xmlhttprequest") 
					|| request.getHeader("AJAX") != null) {
    		return true;
    	}
    	
    	return false;
    }
    
}
