package com.dinens.citron.p.admin.common.resolver;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.dinens.citron.p.admin.common.common.ParamMap;

/**
 * <PRE>
 * 1. ClassName: CustomMapArgumentResolver
 * 2. FileName : CustomMapArgumentResolver.java
 * 3. Package  : com.dinens.citron.common.resolver
 * 4. Comment  : Controller에서 ParamMap을 파라미터로 받기 전에 파라미터를 핸들링하는 Class
 * 5. 작성자   : JYPark
 * 6. 작성일   : 2019. 7. 31.
 * 7. 변경이력
 *		이름	:	일자	: 변경내용
 *     ———————————————————————————————————
 *		JYPark:	2019. 07. 31.	: 신규 개발.
  * </PRE>
 */

public class CustomMapArgumentResolver implements HandlerMethodArgumentResolver {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return ParamMap.class.isAssignableFrom(parameter.getParameterType());
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory) throws Exception {
				
		ParamMap paramMap = new ParamMap();
		paramMap.put("user", "JINILAMP");
		
		HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest();
		
		Enumeration<?> enumeration = request.getParameterNames();
		String key = null;
		String[] values = null;
		while(enumeration.hasMoreElements()) {
			key = (String)enumeration.nextElement();
			values = request.getParameterValues(key);
			if (values != null) {
				paramMap.put(key, (values.length > 1 ? values : values[0]));
				
				logger.debug("key : " + key + ", value : " + values[0]);
			}
		}
		
		if (paramMap.containsKey("lang")) {
			String locale = (String)paramMap.get("lang");
			request.getSession().setAttribute("locale", locale);
		}
		return paramMap;
	}

}
