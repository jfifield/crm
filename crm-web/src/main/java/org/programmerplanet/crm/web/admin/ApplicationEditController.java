package org.programmerplanet.crm.web.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.programmerplanet.crm.model.Application;
import org.programmerplanet.crm.service.AdministrationService;
import org.programmerplanet.crm.web.RequestUtil;
import org.programmerplanet.crm.web.SimpleMultiActionFormController;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class ApplicationEditController extends SimpleMultiActionFormController {

	private AdministrationService administrationService;

	public void setAdministrationService(AdministrationService administrationService) {
		this.administrationService = administrationService;
	}

	public ModelAndView save(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		Application application = (Application)command;

		if (application.getId() != null) {
			administrationService.updateApplication(application);
			return new ModelAndView(getSuccessView());
		}
		else {
			administrationService.insertApplication(application);
			return new ModelAndView("redirect:applicationEdit", "id", application.getId());
		}
	}

	public ModelAndView delete(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		Application application = (Application)command;
		administrationService.deleteApplication(application);
		return new ModelAndView(getSuccessView());
	}

	public ModelAndView cancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		return new ModelAndView(getSuccessView());
	}

	public ModelAndView move(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		Application application = (Application)command;
		String direction = request.getParameter("__move");
		administrationService.moveApplicationViewIndex(application, direction);
		return new ModelAndView(getSuccessView());
	}

	protected boolean suppressBinding(HttpServletRequest request) {
		return !WebUtils.hasSubmitParameter(request, "__save");
	}

	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		UUID id = RequestUtil.getRequestId(request);
		if (id != null) {
			Application application = administrationService.getApplication(id);
			return application;
		}
		else {
			return super.formBackingObject(request);
		}
	}

	protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
		Application application = (Application)command;
		if (application.getId() != null) {
			Map data = new HashMap();

			List selectedObjectDefinition = administrationService.getObjectDefinitionsForApplication(application);
			data.put("selectedObjectDefinition", selectedObjectDefinition);

			List availableObjectDefinition = administrationService.getAllObjectDefinitions();
			availableObjectDefinition.removeAll(selectedObjectDefinition);
			data.put("availableObjectDefinition", availableObjectDefinition);

			return data;
		}
		else {
			return super.referenceData(request, command, errors);
		}
	}

}
