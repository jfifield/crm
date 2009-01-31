package org.programmerplanet.crm.web.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.programmerplanet.crm.model.User;
import org.programmerplanet.crm.service.AdministrationService;
import org.programmerplanet.crm.web.RequestUtil;
import org.programmerplanet.crm.web.SimpleMultiActionFormController;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class UserEditController extends SimpleMultiActionFormController {

	private AdministrationService administrationService;

	public void setAdministrationService(AdministrationService administrationService) {
		this.administrationService = administrationService;
	}

	public ModelAndView save(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		User user = (User)command;

		if (user.getId() != null) {
			administrationService.updateUser(user);
		}
		else {
			administrationService.insertUser(user);
		}

		return new ModelAndView(getSuccessView());
	}

	public ModelAndView delete(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		User user = (User)command;
		administrationService.deleteUser(user);
		return new ModelAndView(getSuccessView());
	}

	public ModelAndView cancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		return new ModelAndView(getSuccessView());
	}

	protected boolean suppressBinding(HttpServletRequest request) {
		return !WebUtils.hasSubmitParameter(request, "__save");
	}

	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Long id = RequestUtil.getRequestId(request);
		if (id != null) {
			User user = administrationService.getUser(id);
			return user;
		}
		else {
			return super.formBackingObject(request);
		}
	}

}
