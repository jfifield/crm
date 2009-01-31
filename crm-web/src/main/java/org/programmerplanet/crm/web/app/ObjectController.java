package org.programmerplanet.crm.web.app;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.mvc.Controller;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public abstract class ObjectController implements Controller {

	private static final Pattern OBJECT_PATH_PATTERN = Pattern.compile("/([\\w ]+)\\.\\w+");
	
	/**
	 * Gets the object name from the URL (/ObjectName.Action)
	 */
	protected String getObjectName(HttpServletRequest request) {
		String pathInfo = request.getPathInfo();
		Matcher matcher = OBJECT_PATH_PATTERN.matcher(pathInfo);
		String objectName = null;
		if (matcher.find()) {
			objectName = matcher.group(1);
		}
		return objectName;
	}
	
}
