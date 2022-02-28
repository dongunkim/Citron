package com.dinens.citron.p.admin.common.filter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nhncorp.lucy.security.xss.XssSaxFilter;


public class JsonRequestWrapper extends HttpServletRequestWrapper {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private byte[] rawData;
	private Map<String, String[]> params = new HashMap<>();
	
	
	public JsonRequestWrapper(HttpServletRequest request) {
		super(request);
		this.params.putAll(request.getParameterMap());
		
		try {			
			InputStream is = request.getInputStream();			
			this.rawData = IOUtils.toByteArray(is);
						
			if (request.getContentType() != null && request.getContentType().contains(ContentType.MULTIPART_FORM_DATA.getMimeType())) {
				return;
			}
						
			//logger.debug("this.rawData : " + this.rawData.hashCode());
			String collect = this.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
			if (StringUtils.isEmpty(collect)) {
				return;
			}
						
			XssSaxFilter filter = XssSaxFilter.getInstance("lucy-xss-superset-sax.xml", true);
			StringBuilder sb = new StringBuilder();			
			//logger.debug("dirty : " + collect);
			String clean = filter.doFilter(collect);
			//logger.debug("clean : " + clean);
			sb.append(clean);			
			
			this.rawData = clean.getBytes();
			
		} catch (Exception e) {
			logger.error("JsonRequestWrapper init error", e);			
		}
	}

	@Override
	public String getParameter(String name) {
		String[] paramArray = getParameterValues(name);
		if(paramArray != null && paramArray.length > 0) {
			return paramArray[0];
		}
		else {
			return null;
		}		
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		return Collections.unmodifiableMap(params);		
	}

	@Override
	public Enumeration<String> getParameterNames() {		
		return Collections.enumeration(params.keySet());				
	}

	@Override
	public String[] getParameterValues(String name) {
		String[] result = null;
		String[] dummyValue = params.get(name);
		if (dummyValue != null) {
			result = new String[dummyValue.length];
			System.arraycopy(dummyValue, 0, result, 0, dummyValue.length);
		}
		return result;		
	}	
	
	@Override
	public ServletInputStream getInputStream() throws IOException {		
		final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.rawData);
		
		return new ServletInputStream() {
			@Override
			public int read() throws IOException {				
				return byteArrayInputStream.read();
			}			
		};
	}

	@Override
	public BufferedReader getReader() throws IOException {		
		return new BufferedReader(new InputStreamReader(this.getInputStream()));
	}

	public void setParameter(String name, String value) {
		String[] values = {value};
		setParameter(name, values);
	}
	
	public void setParameter(String name, String[] values) {
		params.put(name, values);
	}
	
	
	

}
