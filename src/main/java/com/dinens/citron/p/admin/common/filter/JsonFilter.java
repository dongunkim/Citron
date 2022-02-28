package com.dinens.citron.p.admin.common.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonFilter implements Filter {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		//logger.debug("******************** START Filter ********************************");
		JsonRequestWrapper wrapper = new JsonRequestWrapper((HttpServletRequest)request);
		chain.doFilter(wrapper, response);
		//logger.debug("******************** END Filter ********************************");
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
