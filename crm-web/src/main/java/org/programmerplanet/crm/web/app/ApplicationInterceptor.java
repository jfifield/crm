package org.programmerplanet.crm.web.app;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.programmerplanet.crm.metadata.Application;
import org.programmerplanet.crm.metadata.MetadataManager;
import org.programmerplanet.crm.web.RequestUtil;
import org.programmerplanet.crm.web.UserSession;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class ApplicationInterceptor extends HandlerInterceptorAdapter {

	private MetadataManager metadataManager;

	public void setMetadataManager(MetadataManager metadataManager) {
		this.metadataManager = metadataManager;
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		UserSession userSession = UserSession.getUserSession(request);
		request.setAttribute("userSession", userSession);

		List applications = metadataManager.getApplications();
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

		UUID selectedApplicationId = userSession.getSelectedApplicationId();
		if (selectedApplicationId != null) {
			Application application = metadataManager.getApplication(selectedApplicationId);
			List objects = metadataManager.getObjectDefinitionsForApplication(application);
			request.setAttribute("crm_objects", objects);
		}

		return super.preHandle(request, response, handler);
	}

	private boolean changeSelectedApplication(HttpServletRequest request) {
		boolean changed = false;
		UserSession userSession = UserSession.getUserSession(request);
		UUID selectedApplicationId = userSession.getSelectedApplicationId();
		UUID requestedApplicationId = RequestUtil.getRequestId(request, "application");
		if (requestedApplicationId != null && !requestedApplicationId.equals(selectedApplicationId)) {
			userSession.setSelectedApplicationId(requestedApplicationId);
			changed = true;
		}
		return changed;
	}

	private boolean defaultSelectedApplication(HttpServletRequest request, List applications) {
		boolean changed = false;
		UserSession userSession = UserSession.getUserSession(request);
		UUID selectedApplicationId = userSession.getSelectedApplicationId();
		if (selectedApplicationId == null && !applications.isEmpty()) {
			Application application = (Application)applications.get(0);
			selectedApplicationId = application.getId();
			userSession.setSelectedApplicationId(selectedApplicationId);
			changed = true;
		}
		return changed;
	}

}
