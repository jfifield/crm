package org.programmerplanet.crm.web;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.mvc.multiaction.MethodNameResolver;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class PrefixedParameterMethodNameResolver implements MethodNameResolver {

	private String prefix;

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getPrefix() {
		return prefix;
	}

	/**
	 * @see org.springframework.web.servlet.mvc.multiaction.MethodNameResolver#getHandlerMethodName(javax.servlet.http.HttpServletRequest)
	 */
	public String getHandlerMethodName(HttpServletRequest request) throws NoSuchRequestHandlingMethodException {
		String methodName = null;
		Enumeration parameterNames = request.getParameterNames();
		while (parameterNames.hasMoreElements()) {
			String parameterName = (String)parameterNames.nextElement();
			if (parameterName.startsWith(prefix)) {
				methodName = parameterName.substring(prefix.length());
				break;
			}
		}
		if (methodName == null) {
			throw new NoSuchRequestHandlingMethodException(request);
		}
		return methodName;
	}

}
