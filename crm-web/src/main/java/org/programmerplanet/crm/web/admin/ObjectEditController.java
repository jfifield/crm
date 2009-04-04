package org.programmerplanet.crm.web.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.programmerplanet.crm.metadata.MetadataManager;
import org.programmerplanet.crm.metadata.ObjectDefinition;
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
public class ObjectEditController extends SimpleMultiActionFormController {

	private MetadataManager metadataManager;

	public void setMetadataManager(MetadataManager metadataManager) {
		this.metadataManager = metadataManager;
	}

	public ModelAndView save(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		ObjectDefinition objectDefinition = (ObjectDefinition)command;

		String generatedTableName = objectDefinition.generateTableName();
		objectDefinition.setTableName(generatedTableName);

		boolean isNew = (objectDefinition.getId() == null);
		metadataManager.saveObjectDefinition(objectDefinition);
		if (isNew) {
			return new ModelAndView("redirect:objectEdit", "id", objectDefinition.getId());
		}
		else {
			return new ModelAndView(getSuccessView());
		}
	}

	public ModelAndView delete(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		ObjectDefinition objectDefinition = (ObjectDefinition)command;
		metadataManager.deleteObjectDefinition(objectDefinition);
		return new ModelAndView(getSuccessView());
	}

	public ModelAndView cancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		return new ModelAndView(getSuccessView());
	}

	protected boolean suppressBinding(HttpServletRequest request) {
		return !WebUtils.hasSubmitParameter(request, "__save");
	}

	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		UUID id = RequestUtil.getRequestId(request);
		if (id != null) {
			ObjectDefinition objectDefinition = metadataManager.getObjectDefinition(id);
			return objectDefinition;
		}
		else {
			return super.formBackingObject(request);
		}
	}

	protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
		ObjectDefinition objectDefinition = (ObjectDefinition)command;
		if (objectDefinition.getId() != null) {
			Map data = new HashMap();

			List fieldDefinition = metadataManager.getFieldDefinitionsForObject(objectDefinition);
			data.put("fieldDefinition", fieldDefinition);

			List selectedListFieldDefinition = metadataManager.getFieldDefinitionsForObjectList(objectDefinition);
			data.put("selectedListFieldDefinition", selectedListFieldDefinition);

			List availableListFieldDefinition = new ArrayList(fieldDefinition);
			availableListFieldDefinition.removeAll(selectedListFieldDefinition);
			data.put("availableListFieldDefinition", availableListFieldDefinition);

			Map selectedRelationships = metadataManager.getRelatedObjectDefinitionsForObject(objectDefinition);
			data.put("selectedRelationships", selectedRelationships);

			List availableObjectDefinition = metadataManager.getObjectDefinitions();
			availableObjectDefinition.removeAll(selectedRelationships.values());
			availableObjectDefinition.remove(objectDefinition);
			data.put("availableObjectDefinition", availableObjectDefinition);

			return data;
		}
		else {
			return super.referenceData(request, command, errors);
		}
	}

}
