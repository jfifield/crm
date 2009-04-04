package org.programmerplanet.crm.web.admin;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.programmerplanet.crm.metadata.ApplicationObject;
import org.programmerplanet.crm.metadata.MetadataManager;
import org.programmerplanet.crm.web.RequestUtil;
import org.programmerplanet.crm.web.SimpleMultiActionFormController;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class ApplicationObjectEditController extends SimpleMultiActionFormController {

	private MetadataManager metadataManager;

	public void setMetadataManager(MetadataManager metadataManager) {
		this.metadataManager = metadataManager;
	}

	public ModelAndView add(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		ApplicationObject applicationObject = (ApplicationObject)command;
		metadataManager.saveApplicationObject(applicationObject);
		return new ModelAndView(getSuccessView(), "id", applicationObject.getApplicationId());
	}

	public ModelAndView remove(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		ApplicationObject applicationObject = (ApplicationObject)command;
		metadataManager.deleteApplicationObject(applicationObject);
		return new ModelAndView(getSuccessView(), "id", applicationObject.getApplicationId());
	}

	public ModelAndView move(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		ApplicationObject applicationObject = (ApplicationObject)command;
		String direction = request.getParameter("__move");
		metadataManager.moveApplicationObjectViewIndex(applicationObject, direction);
		return new ModelAndView(getSuccessView(), "id", applicationObject.getApplicationId());
	}

	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		UUID applicationId = RequestUtil.getRequestId(request, "applicationId");
		UUID objectId = RequestUtil.getRequestId(request, "objectId");
		ApplicationObject applicationObject = new ApplicationObject();
		applicationObject.setApplicationId(applicationId);
		applicationObject.setObjectId(objectId);
		return applicationObject;
	}

}
