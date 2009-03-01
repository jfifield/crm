package org.programmerplanet.crm.web.app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.programmerplanet.crm.model.ObjectDefinition;
import org.programmerplanet.crm.model.Relationship;
import org.programmerplanet.crm.service.ApplicationService;
import org.programmerplanet.crm.web.RequestUtil;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class ObjectLinkController extends ObjectController {

	private ApplicationService applicationService;

	public void setApplicationService(ApplicationService applicationService) {
		this.applicationService = applicationService;
	}

	/**
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (isSaveAction(request)) {
			return handleSave(request, response);
		}
		else if (isDeleteAction(request)) {
			return handleDelete(request, response);
		}
		else {
			return handleDefault(request, response);
		}
	}

	public ModelAndView handleDefault(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String parentObjectName = request.getParameter("source_object");
		String childObjectName = getObjectName(request);

		UUID id = RequestUtil.getRequestId(request, "source_object_id");

		ObjectDefinition parentObjectDefinition = applicationService.getObjectDefinition(parentObjectName);
		ObjectDefinition childObjectDefinition = applicationService.getObjectDefinition(childObjectName);
		List fieldDefinitions = applicationService.getFieldDefinitionsForObjectList(childObjectDefinition);
		Relationship relationship = applicationService.getRelationship(parentObjectDefinition.getId(), childObjectDefinition.getId());
		List data = applicationService.getCrmObjectsAvailableForLinking(childObjectDefinition, fieldDefinitions, relationship, parentObjectDefinition, id);

		Map model = new HashMap();
		model.put("objectDefinition", parentObjectDefinition);
		model.put("parentObjectDefinition", parentObjectDefinition);
		model.put("childObjectDefinition", childObjectDefinition);
		model.put("fieldDefinitions", fieldDefinitions);
		model.put("data", data);
		model.put("id", id);

		return new ModelAndView("link", model);
	}

	public ModelAndView handleSave(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String parentObjectName = request.getParameter("source_object");
		String childObjectName = getObjectName(request);

		ObjectDefinition parentObjectDefinition = applicationService.getObjectDefinition(parentObjectName);
		ObjectDefinition childObjectDefinition = applicationService.getObjectDefinition(childObjectName);

		UUID parentId = RequestUtil.getRequestId(request, "source_object_id");
		UUID childId = RequestUtil.getRequestId(request);

		applicationService.insertCrmObjectRelationship(parentObjectDefinition, parentId, childObjectDefinition, childId);

		String view = "redirect:" + parentObjectName + ".view?id=" + parentId;
		return new ModelAndView(view);
	}

	public ModelAndView handleDelete(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String parentObjectName = request.getParameter("source_object");
		String childObjectName = getObjectName(request);

		ObjectDefinition parentObjectDefinition = applicationService.getObjectDefinition(parentObjectName);
		ObjectDefinition childObjectDefinition = applicationService.getObjectDefinition(childObjectName);

		UUID parentId = RequestUtil.getRequestId(request, "source_object_id");
		UUID childId = RequestUtil.getRequestId(request);

		applicationService.deleteCrmObjectRelationship(parentObjectDefinition, parentId, childObjectDefinition, childId);

		String view = "redirect:" + parentObjectName + ".view?id=" + parentId;
		return new ModelAndView(view);
	}

	private boolean isSaveAction(HttpServletRequest request) {
		return WebUtils.hasSubmitParameter(request, "__save");
	}

	private boolean isDeleteAction(HttpServletRequest request) {
		return WebUtils.hasSubmitParameter(request, "__delete");
	}

}
