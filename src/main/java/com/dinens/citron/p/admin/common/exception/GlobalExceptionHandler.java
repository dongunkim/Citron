package com.dinens.citron.p.admin.common.exception;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.dinens.citron.p.admin.common.util.ContextUtils;
import com.dinens.citron.p.admin.common.util.MessageUtils;

/**
 * <PRE>
 * 1. ClassName: GlobalExceptionHandler
 * 2. FileName : GlobalExceptionHandler.java
 * 3. Package  : com.dinens.citron.common.exception
 * 4. Comment  : REST Controller에서 발생하는 예외를 처리하는 Exception Handler 
 * 5. 작성자   : JYPark
 * 6. 작성일   : 2019. 7. 31.
 * 7. 변경이력
 *		이름	:	일자	: 변경내용
 *     ———————————————————————————————————
 *		JYPark:	2019. 07. 31.	: 신규 개발.
  * </PRE>
 */

@ControllerAdvice
public class GlobalExceptionHandler {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private final String ERROR_VIEW = "/error/error";
	private final String JSON_VIEW = "jsonView";
		
	// Application 처리 과정에서의 논리적 예외에 대한 사용자정의 오류 (사용자 정의 Exception)
	@ExceptionHandler(ApplicationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ModelAndView handleApplicationException(Exception ex) throws AjaxException {		
		logger.info("Exception :================================================\t");		
		logger.info("Exception :\t", ex);
		
		ModelAndView mv = null;
		String message = null;
		ErrorMessage error = null;
		
		if (ContextUtils.isAjaxRequest()) {
			mv = new ModelAndView(JSON_VIEW);
			message = (ex.getMessage() != null ? ex.getMessage() : MessageUtils.getMessage("server.common.exception.400.ajax.message"));
			error = new ErrorMessage(400, MessageUtils.getMessage("server.common.exception.400.status"), message);			
		}
		else {
			mv = new ModelAndView(ERROR_VIEW);
			message = (ex.getMessage() != null ? ex.getMessage() : MessageUtils.getMessage("server.common.exception.400.view.message"));					
			error = new ErrorMessage(400, MessageUtils.getMessage("server.common.exception.400.status"), message);
		}		
		
		mv.addObject("error", error);
		return mv;
	}
	
	
	// 인증정보가 없는 경우를 위한 사용자정의 오류 (사용자 정의 Exception)
	@ExceptionHandler(UnAuthorizedException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ModelAndView handleUnAuthorizedException(Exception ex) throws AjaxException {		
		logger.info("Exception :================================================\t");		
		logger.info("Exception :\t", ex);
		
		ModelAndView mv = null;
		ErrorMessage error = null;
		
		if (ContextUtils.isAjaxRequest()) {
			mv = new ModelAndView(JSON_VIEW);
			error = new ErrorMessage(403, MessageUtils.getMessage("server.common.exception.403.status"),
					 					  MessageUtils.getMessage("server.common.exception.403.ajax.message"));
		}
		else {
			mv = new ModelAndView(ERROR_VIEW);		
			error = new ErrorMessage(403, MessageUtils.getMessage("server.common.exception.403.status"), 
										  MessageUtils.getMessage("server.common.exception.403.view.message"));
		}
						
		mv.addObject("error", error);
		return mv;
	}
		
	
	// 대부분의 RunTime 오류 (500 오류)
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)	
	public ModelAndView handleException(Exception ex) throws Exception {
		logger.info("Exception :================================================\t");		
		logger.info("Exception :\t", ex);
		
		String exCause = (ex.getCause() == null ? "" : ex.getCause().toString());
		boolean isSQLException = (exCause.contains("mysql") ? true : false);
		
		ModelAndView mv = null;
		ErrorMessage error = null;
		if (ContextUtils.isAjaxRequest()) {
			mv = new ModelAndView(JSON_VIEW);
			if (isSQLException)
				error = new ErrorMessage(500, MessageUtils.getMessage("server.common.exception.500.sql.status"), 
	 					 					  MessageUtils.getMessage("server.common.exception.500.sql.ajax.message"));
			else
				error = new ErrorMessage(500, MessageUtils.getMessage("server.common.exception.500.status"), 
						 					  MessageUtils.getMessage("server.common.exception.500.ajax.message"));
		}
		else {
			mv = new ModelAndView(ERROR_VIEW);		
			if (isSQLException)		
				error = new ErrorMessage(500, MessageUtils.getMessage("server.common.exception.500.sql.status"), 
											  MessageUtils.getMessage("server.common.exception.500.sql.view.message"));
			else
				error = new ErrorMessage(500, MessageUtils.getMessage("server.common.exception.500.status"), 
											  MessageUtils.getMessage("server.common.exception.500.view.message"));
		}
				
		mv.addObject("error", error);
		return mv;		
	}
	
	// Ajax Exception 처리(사용자 정의 Exception)
	@ExceptionHandler(AjaxException.class)	
	@ResponseBody
	public ErrorMessage handleAjaxException(HttpServletResponse response, AjaxException ex) throws Exception {
		logger.info("Exception :================================================\t");
		logger.info("Exception :\t", ex);
					
		ErrorMessage error = new ErrorMessage(ex.getCode(), ex.getStatus(), ex.getMessage());
		response.setStatus(ex.getCode());
		
		return error;
	}
}
