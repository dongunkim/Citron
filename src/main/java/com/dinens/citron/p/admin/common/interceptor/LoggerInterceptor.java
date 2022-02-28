package com.dinens.citron.p.admin.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * <PRE>
 * 1. ClassName: LoggerInterceptor
 * 2. FileName : LoggerInterceptor.java
 * 3. Package  : com.dinens.citron.common.logger
 * 4. Comment  : Handler가 호출되기 전에 요청정보 logging
 * 5. 작성자   : JYPark
 * 6. 작성일   : 2019. 7. 31.
 * 7. 변경이력
 *		이름	:	일자	: 변경내용
 *     ———————————————————————————————————
 *		JYPark:	2019. 07. 31.	: 신규 개발.
  * </PRE>
 */

@Component
public class LoggerInterceptor extends HandlerInterceptorAdapter {
	
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("============================= START ==========================");
			logger.debug("Request URI \t: " + request.getRequestURI());
		}
						
		return super.preHandle(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("============================= END ==========================");			
		}
		super.postHandle(request, response, handler, modelAndView);
	}
	
	
}
