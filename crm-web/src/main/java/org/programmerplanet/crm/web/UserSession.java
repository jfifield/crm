package org.programmerplanet.crm.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.programmerplanet.crm.model.User;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class UserSession {

	private static final String USER_SESSION_KEY = UserSession.class.getName();

	private User user;
	private Long selectedApplicationId;

	public static void initialize(HttpServletRequest request, User user) {
		UserSession userSession = new UserSession(user);
		request.getSession().setAttribute(USER_SESSION_KEY, userSession);
	}

	public static void destroy(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.removeAttribute(USER_SESSION_KEY);
			session.invalidate();
		}
	}

	public static UserSession getUserSession(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		UserSession userSession = null;
		if (session != null) {
			userSession = (UserSession)session.getAttribute(USER_SESSION_KEY);
		}
		return userSession;
	}

	public UserSession(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public void setSelectedApplicationId(Long selectedApplicationId) {
		this.selectedApplicationId = selectedApplicationId;
	}

	public Long getSelectedApplicationId() {
		return selectedApplicationId;
	}

}
