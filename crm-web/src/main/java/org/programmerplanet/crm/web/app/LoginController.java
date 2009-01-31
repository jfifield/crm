package org.programmerplanet.crm.web.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.programmerplanet.crm.model.User;
import org.programmerplanet.crm.service.ApplicationService;
import org.programmerplanet.crm.web.UserSession;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class LoginController extends SimpleFormController {

	private ApplicationService applicationService;

	public void setApplicationService(ApplicationService applicationService) {
		this.applicationService = applicationService;
	}

	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		Credentials credentials = (Credentials)command;

		User user = applicationService.getUser(credentials.getUsername(), credentials.getPassword());

		if (user == null) {
			// login bad!
			errors.reject("error.loginfailed");
			return this.showForm(request, response, errors);
		}
		else {
			// login good!
			UserSession.initialize(request, user);
			return new ModelAndView(getSuccessView());
		}
	}

}
