package org.programmerplanet.crm.web.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.programmerplanet.crm.model.DataType;
import org.programmerplanet.crm.model.FieldDefinition;
import org.programmerplanet.crm.model.ObjectDefinition;
import org.programmerplanet.crm.service.AdministrationService;
import org.programmerplanet.crm.util.ValuedEnumPropertyEditor;
import org.programmerplanet.crm.web.RequestUtil;
import org.programmerplanet.crm.web.SimpleMultiActionFormController;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;
import org.springframework.web.util.WebUtils;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class FieldEditController extends SimpleMultiActionFormController {

	private AdministrationService administrationService;

	public void setAdministrationService(AdministrationService administrationService) {
		this.administrationService = administrationService;
	}

	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		super.initBinder(request, binder);
		binder.registerCustomEditor(DataType.class, new ValuedEnumPropertyEditor(DataType.class));
	}

	protected boolean isFormChangeRequest(HttpServletRequest request) {
		try {
			String methodName = this.getMethodNameResolver().getHandlerMethodName(request);
			return false;
		}
		catch (NoSuchRequestHandlingMethodException e) {
			return true;
		}
	}

	protected void onFormChange(HttpServletRequest request, HttpServletResponse response, Object command) throws Exception {
		FieldDefinition fieldDefinition = (FieldDefinition)command;
		if (DataType.SHORT_TEXT.equals(fieldDefinition.getDataType())) {
			fieldDefinition.setDataTypeExt(new Long(50));
		}
		else if (DataType.LONG_TEXT.equals(fieldDefinition.getDataType())) {
			fieldDefinition.setDataTypeExt(new Long(1));
		}
		else if (DataType.NUMBER.equals(fieldDefinition.getDataType())) {
			fieldDefinition.setDataTypeExt(new Long(0));
		}
		else if (DataType.MONEY.equals(fieldDefinition.getDataType())) {
			fieldDefinition.setDataTypeExt(new Long(2));
		}
		else if (DataType.PERCENT.equals(fieldDefinition.getDataType())) {
			fieldDefinition.setDataTypeExt(new Long(0));
		}
		else {
			fieldDefinition.setDataTypeExt(null);
		}
	}

	public ModelAndView save(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		FieldDefinition fieldDefinition = (FieldDefinition)command;

		String generatedColumnName = fieldDefinition.generateColumnName();
		fieldDefinition.setColumnName(generatedColumnName);

		if (fieldDefinition.getId() != null) {
			administrationService.updateFieldDefinition(fieldDefinition);
		}
		else {
			administrationService.insertFieldDefinition(fieldDefinition);
		}

		return new ModelAndView(getSuccessView(), "id", fieldDefinition.getObjectId());
	}

	public ModelAndView delete(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		FieldDefinition fieldDefinition = (FieldDefinition)command;
		administrationService.deleteFieldDefinition(fieldDefinition);
		return new ModelAndView(getSuccessView(), "id", fieldDefinition.getObjectId());
	}

	public ModelAndView cancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		FieldDefinition fieldDefinition = (FieldDefinition)command;
		return new ModelAndView(getSuccessView(), "id", fieldDefinition.getObjectId());
	}

	public ModelAndView move(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		FieldDefinition fieldDefinition = (FieldDefinition)command;
		String direction = request.getParameter("__move");
		administrationService.moveFieldDefinitionViewIndex(fieldDefinition, direction);
		return new ModelAndView(getSuccessView(), "id", fieldDefinition.getObjectId());
	}

	public ModelAndView addList(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		FieldDefinition fieldDefinition = (FieldDefinition)command;
		administrationService.addFieldDefinitionListIndex(fieldDefinition);
		return new ModelAndView(getSuccessView(), "id", fieldDefinition.getObjectId());
	}

	public ModelAndView removeList(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		FieldDefinition fieldDefinition = (FieldDefinition)command;
		administrationService.removeFieldDefinitionListIndex(fieldDefinition);
		return new ModelAndView(getSuccessView(), "id", fieldDefinition.getObjectId());
	}

	public ModelAndView moveList(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		FieldDefinition fieldDefinition = (FieldDefinition)command;
		String direction = request.getParameter("__moveList");
		administrationService.moveFieldDefinitionListIndex(fieldDefinition, direction);
		return new ModelAndView(getSuccessView(), "id", fieldDefinition.getObjectId());
	}

	protected boolean suppressBinding(HttpServletRequest request) {
		return !WebUtils.hasSubmitParameter(request, "__save") && !isFormChangeRequest(request);
	}

	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		UUID id = RequestUtil.getRequestId(request);
		if (id != null) {
			FieldDefinition fieldDefinition = administrationService.getFieldDefinition(id);
			return fieldDefinition;
		}
		else {
			FieldDefinition fieldDefinition = new FieldDefinition();
			UUID objectId = RequestUtil.getRequestId(request, "object_id");
			fieldDefinition.setObjectId(objectId);
			return fieldDefinition;
		}
	}

	protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
		FieldDefinition fieldDefinition = (FieldDefinition)command;
		Map data = new HashMap();

		ObjectDefinition objectDefinition = administrationService.getObjectDefinition(fieldDefinition.getObjectId());
		data.put("objectDefinition", objectDefinition);

		data.put("dataTypes", DataType.getEnumList());

		List optionLists = administrationService.getAllOptionLists();
		data.put("optionLists", optionLists);

		List objectDefinitions = administrationService.getAllObjectDefinitions();
		data.put("objectDefinitions", objectDefinitions);

		return data;
	}

}
