package com.dinens.citron.p.admin.common.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <PRE>
 * 1. ClassName: LoggerAspect
 * 2. FileName : LoggerAspect.java
 * 3. Package  : com.dinens.citron.common.aspect
 * 4. Comment  : Handler > Service > Mapper 까지 각 처리단계 logging
 *               AOP 적용
 * 5. 작성자   : JYPark
 * 6. 작성일   : 2019. 7. 31.
 * 7. 변경이력
 *		이름	:	일자	: 변경내용
 *     ———————————————————————————————————
 *		JYPark:	2019. 07. 31.	: 신규 개발.
  * </PRE>
 */

@Aspect
public class LoggerAspect {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	static String name = "";
	static String type = "";
	
	@Around("execution(* com.dinens.citron..controller.*Controller.*(..)) or execution(* com.dinens.citron..service.*Service.*(..)) or execution(* com.dinens.citron..mapper.*Mapper.*(..))")
	public Object logPrint(ProceedingJoinPoint joinPoint) throws Throwable {
		type = joinPoint.getSignature().getDeclaringTypeName();
		
		if (type.indexOf("Controller") > -1) {
			name = "Controller \t: ";
		}
		else if (type.indexOf("Service") > -1) {
			name = "Service \t: ";
		}
		else if (type.indexOf("Mapper") > -1) {
			name = "Mapper \t\t: ";
		}
		logger.debug(name + type + "." + joinPoint.getSignature().getName() + "()");
		
		return joinPoint.proceed();
	}
}
