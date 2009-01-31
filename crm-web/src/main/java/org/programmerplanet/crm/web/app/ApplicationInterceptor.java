package org.programmerplanet.crm.web.app;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.programmerplanet.crm.model.Application;
import org.programmerplanet.crm.service.ApplicationService;
import org.programmerplanet.crm.web.RequestUtil;
import org.programmerplanet.crm.web.UserSession;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class ApplicationInterceptor extends HandlerInterceptorAdapter {

	private ApplicationService applicationService;

	public void setApplicationService(ApplicationService applicationService) {
		this.applicationService = applicationService;
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		UserSession userSession = UserSession.getUserSession(request);
		request.setAttribute("userSession", userSession);

		List applications = applicationService.getAllApplications();
		request.setAttribute("crm_applications", applications);

		// change selected application if requested...
		if (changeSelectedApplication(request)) {
			response.sendRedirect("home");
			return false;
		}

		// default selected application if none selected...
		if (defaultSelectedApplication(request, applications)) {
			response.sendRedirect("home");
			return false;
		}

		Long selectedApplicationId = userSession.getSelectedApplicationId();
		if (selectedApplicationId != null) {
			Application application = applicationService.getApplication(selectedApplicationId);
			List objects = applicationService.getObjectDefinitionsForApplication(application);
			request.setAttribute("crm_objects", objects);
		}

		return super.preHandle(request, response, handler);
	}

	private boolean changeSelectedApplication(HttpServletRequest request) {
		boolean changed = false;
		UserSession userSession = UserSession.getUserSession(request);
		Long selectedApplicationId = userSession.getSelectedApplicationId();
		Long requestedApplicationId = RequestUtil.getRequestId(request, "application");
		if (requestedApplicationId != null && !requestedApplicationId.equals(selectedApplicationId)) {
			userSession.setSelectedApplicationId(requestedApplicationId);
			changed = true;
		}
		return changed;
	}

	private boolean defaultSelectedApplication(HttpServletRequest request, List applications) {
		boolean changed = false;
		UserSession userSession = UserSession.getUserSession(request);
		Long selectedApplicationId = userSession.getSelectedApplicationId();
		if (selectedApplicationId == null && !applications.isEmpty()) {
			Application application = (Application)applications.get(0);
			selectedApplicationId = application.getId();
			userSession.setSelectedApplicationId(selectedApplicationId);
			changed = true;
		}
		return changed;
	}

}
