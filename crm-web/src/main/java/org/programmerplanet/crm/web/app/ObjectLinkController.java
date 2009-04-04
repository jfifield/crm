package org.programmerplanet.crm.web.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.programmerplanet.crm.data.DataManager;
import org.programmerplanet.crm.data.ObjectData;
import org.programmerplanet.crm.metadata.MetadataManager;
import org.programmerplanet.crm.metadata.ObjectDefinition;
import org.programmerplanet.crm.metadata.Relationship;
import org.programmerplanet.crm.web.RequestUtil;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class ObjectLinkController extends ObjectController {

	private MetadataManager metadataManager;
	private DataManager dataManager;

	private void setMetadataManager(MetadataManager metadataManager) {
		this.metadataManager = metadataManager;
	}

	public void setDataManager(DataManager dataManager) {
		this.dataManager = dataManager;
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

		ObjectDefinition parentObjectDefinition = metadataManager.getObjectDefinition(parentObjectName);
		ObjectDefinition childObjectDefinition = metadataManager.getObjectDefinition(childObjectName);
		List fieldDefinitions = metadataManager.getFieldDefinitionsForObjectList(childObjectDefinition);
		Relationship relationship = metadataManager.getRelationship(parentObjectDefinition.getId(), childObjectDefinition.getId());
		List<ObjectData> objects = dataManager.getObjectsAvailableForLinking(childObjectDefinition, fieldDefinitions, relationship, parentObjectDefinition, id);
		
		List<Map> data = new ArrayList<Map>();
		for (ObjectData objectData : objects) {
			data.add(objectData.getData());
		}

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

		ObjectDefinition parentObjectDefinition = metadataManager.getObjectDefinition(parentObjectName);
		ObjectDefinition childObjectDefinition = metadataManager.getObjectDefinition(childObjectName);

		UUID parentId = RequestUtil.getRequestId(request, "source_object_id");
		UUID childId = RequestUtil.getRequestId(request);

		dataManager.saveObjectRelationship(parentObjectDefinition, parentId, childObjectDefinition, childId);

		String view = "redirect:" + parentObjectName + ".view?id=" + parentId;
		return new ModelAndView(view);
	}

	public ModelAndView handleDelete(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String parentObjectName = request.getParameter("source_object");
		String childObjectName = getObjectName(request);

		ObjectDefinition parentObjectDefinition = metadataManager.getObjectDefinition(parentObjectName);
		ObjectDefinition childObjectDefinition = metadataManager.getObjectDefinition(childObjectName);

		UUID parentId = RequestUtil.getRequestId(request, "source_object_id");
		UUID childId = RequestUtil.getRequestId(request);

		dataManager.deleteObjectRelationship(parentObjectDefinition, parentId, childObjectDefinition, childId);

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
