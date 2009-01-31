package org.programmerplanet.crm.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public final class RequestUtil {

	public static Long getRequestId(HttpServletRequest request) {
		return getRequestId(request, "id");
	}

	public static Long getRequestId(HttpServletRequest request, String name) {
		Long id = null;
		String sid = request.getParameter(name);
		if (StringUtils.isNotEmpty(sid)) {
			id = new Long(sid);
		}
		return id;
	}

}
