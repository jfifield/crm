package org.programmerplanet.crm.web.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.programmerplanet.crm.AuthorizationException;
import org.programmerplanet.crm.web.UserSession;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class AdministrationInterceptor extends HandlerInterceptorAdapter {

	/**
	 * @see org.springframework.web.servlet.HandlerInterceptor#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		UserSession userSession = UserSession.getUserSession(request);
		if (userSession != null && userSession.getUser().isAdministrator()) {
			return true;
		}
		else {
			throw new AuthorizationException("User is not an administrator!");
		}
	}

}
