package org.programmerplanet.crm.web.app;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.programmerplanet.crm.converter.ConversionException;
import org.programmerplanet.crm.converter.Converter;
import org.programmerplanet.crm.model.CrmObject;
import org.programmerplanet.crm.model.FieldDefinition;
import org.programmerplanet.crm.model.FileInfo;
import org.programmerplanet.crm.model.ObjectDefinition;
import org.programmerplanet.crm.service.ApplicationService;
import org.programmerplanet.crm.web.RequestUtil;
import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class ObjectEditController extends ObjectController {

	private ApplicationService applicationService;
	private Map converters;

	public void setApplicationService(ApplicationService applicationService) {
		this.applicationService = applicationService;
	}

	public void setConverters(Map converters) {
		this.converters = converters;
	}

	/**
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (isCancelAction(request)) {
			return handleCancel(request, response);
		}
		else if (isSaveAction(request)) {
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
		String objectName = getObjectName(request);

		CrmObject crmObject = formBackingObject(request);

		Map model = new HashMap();
		model.put("objectDefinition", crmObject.getObjectDefinition());
		model.put("fieldDefinitions", crmObject.getFieldDefinitions());
		model.put("data", crmObject.getData());
		model.put("object", crmObject);

		return new ModelAndView("edit", model);
	}

	public ModelAndView handleCancel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String objectName = getObjectName(request);
		Long id = RequestUtil.getRequestId(request);

		String source = request.getParameter("source");
		String sourceObject = request.getParameter("source_object");
		String sourceObjectId = request.getParameter("source_object_id");

		String destinationObject = null;
		String destinationObjectId = null;
		if (StringUtils.isNotEmpty(sourceObject)) {
			destinationObject = sourceObject;
			destinationObjectId = sourceObjectId;
		}
		else {
			destinationObject = objectName;
			destinationObjectId = ObjectUtils.toString(id, null);
		}

		String destination = null;
		// if source == view, go to view
		if ("view".equals(source) && StringUtils.isNotEmpty(destinationObjectId)) {
			destination = "view";
		}
		// if source == list, go to list
		else {
			destination = "list";
		}

		String view = "redirect:" + destinationObject + "." + destination;
		if ("view".equals(destination)) {
			view += "?id=" + destinationObjectId;
		}
		return new ModelAndView(view);
	}

	public ModelAndView handleSave(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String objectName = getObjectName(request);

		CrmObject crmObject = formBackingObject(request);
		BindException bindException = bindAndValidate(request, crmObject);

		if (bindException.hasErrors()) {
			Map model = new HashMap();

			Map errorsModel = bindException.getModel();
			model.putAll(errorsModel);

			model.put("objectDefinition", crmObject.getObjectDefinition());
			model.put("fieldDefinitions", crmObject.getFieldDefinitions());
			model.put("data", crmObject.getData());

			return new ModelAndView("edit", model);
		}

		// TODO: how to encapsulate this in the insert/update function?
		// insert files...
		if (request instanceof MultipartHttpServletRequest) {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)request;
			Map fileMap = multipartRequest.getFileMap();
			for (Iterator i = fileMap.entrySet().iterator(); i.hasNext();) {
				Map.Entry entry = (Map.Entry)i.next();
				String name = (String)entry.getKey();
				MultipartFile multipartFile = (MultipartFile)entry.getValue();
				if (StringUtils.isNotEmpty(multipartFile.getOriginalFilename())) {
					FileInfo fileInfo = new FileInfo();
					fileInfo.setFileName(multipartFile.getOriginalFilename());
					fileInfo.setFileSize(multipartFile.getSize());
					fileInfo.setMimeType(multipartFile.getContentType());
					applicationService.insertFile(fileInfo, multipartFile.getInputStream());
					crmObject.getData().put(name, fileInfo.getId());
				}
			}
		}

		Long id = crmObject.getId();
		boolean newObject = (id == null);
		if (newObject) {
			id = applicationService.insertCrmObject(crmObject.getObjectDefinition(), crmObject.getFieldDefinitions(), crmObject.getData());
		}
		else {
			applicationService.updateCrmObject(crmObject.getObjectDefinition(), crmObject.getFieldDefinitions(), crmObject.getData(), id);
		}

		String source = request.getParameter("source");
		String sourceObject = request.getParameter("source_object");
		Long sourceObjectId = RequestUtil.getRequestId(request, "source_object_id");

		if (StringUtils.isNotEmpty(sourceObject) && newObject) {
			String parentObjectName = sourceObject;
			String childObjectName = objectName;

			ObjectDefinition parentObjectDefinition = applicationService.getObjectDefinition(parentObjectName);
			ObjectDefinition childObjectDefinition = applicationService.getObjectDefinition(childObjectName);

			Long parentId = sourceObjectId;
			Long childId = id;

			applicationService.insertCrmObjectRelationship(parentObjectDefinition, parentId, childObjectDefinition, childId);
		}

		String destinationObject = null;
		String destinationObjectId = null;
		if (StringUtils.isNotEmpty(sourceObject)) {
			destinationObject = sourceObject;
			destinationObjectId = ObjectUtils.toString(sourceObjectId, null);
		}
		else {
			destinationObject = objectName;
			destinationObjectId = ObjectUtils.toString(id, null);
		}

		String destination = null;
		// if source == view, go to view
		if ("view".equals(source) && StringUtils.isNotEmpty(destinationObjectId)) {
			destination = "view";
		}
		// if source == list, action == add, go to view
		else if ("list".equals(source) && StringUtils.isNotEmpty(destinationObjectId) && newObject) {
			destination = "view";
		}
		// if source == list, action == update, go to list
		else {
			destination = "list";
		}

		String view = "redirect:" + destinationObject + "." + destination;
		if ("view".equals(destination)) {
			view += "?id=" + destinationObjectId;
		}
		return new ModelAndView(view);
	}

	private CrmObject formBackingObject(HttpServletRequest request) throws Exception {
		String objectName = getObjectName(request);

		ObjectDefinition objectDefinition = applicationService.getObjectDefinition(objectName);
		List fieldDefinitions = applicationService.getFieldDefinitionsForObjectView(objectDefinition);

		Map data = null;
		Long id = RequestUtil.getRequestId(request);
		if (id != null) {
			data = applicationService.getCrmObject(objectDefinition, fieldDefinitions, id);
		}
		else {
			data = new HashMap();
		}

		CrmObject crmObject = new CrmObject();
		crmObject.setId(id);
		crmObject.setData(data);
		crmObject.setObjectDefinition(objectDefinition);
		crmObject.setFieldDefinitions(fieldDefinitions);

		return crmObject;
	}

	private BindException bindAndValidate(HttpServletRequest request, CrmObject crmObject) {
		BindException bindException = new BindException(crmObject, "object");

		// convert values
		bind(request, crmObject, bindException);

		// validate object
		if (!bindException.hasErrors()) {
			validate(crmObject, bindException);
		}
		return bindException;
	}

	private void bind(HttpServletRequest request, CrmObject crmObject, BindException bindException) {
		for (Iterator i = crmObject.getFieldDefinitions().iterator(); i.hasNext();) {
			FieldDefinition fieldDefinition = (FieldDefinition)i.next();
			String columnName = fieldDefinition.getColumnName();
			Object value = request.getParameter(columnName);

			// attempt to convert value
			if (value instanceof String || value == null) {
				Converter converter = (Converter)converters.get(fieldDefinition.getDataType());
				if (converter != null) {
					try {
						value = converter.convert((String)value, fieldDefinition);
					}
					catch (ConversionException e) {
						bindException.rejectValue("data[" + columnName + "]", "typeMismatch.dataType." + fieldDefinition.getDataType().getName());
					}
				}
			}

			// set value
			crmObject.getData().put(columnName, value);
		}
	}

	private void validate(CrmObject crmObject, BindException bindException) {
		CrmObjectValidator objectValidator = new CrmObjectValidator();
		objectValidator.validate(crmObject, bindException);
	}

	public ModelAndView handleDelete(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String objectName = getObjectName(request);
		ObjectDefinition objectDefinition = applicationService.getObjectDefinition(objectName);
		Long id = RequestUtil.getRequestId(request);
		applicationService.deleteCrmObject(objectDefinition, id);

		String source = request.getParameter("source");
		String sourceObject = request.getParameter("source_object");
		String sourceObjectId = request.getParameter("source_object_id");

		String destinationObject = null;
		String destinationObjectId = null;
		if (StringUtils.isNotEmpty(sourceObject)) {
			destinationObject = sourceObject;
			destinationObjectId = sourceObjectId;
		}
		else {
			destinationObject = objectName;
		}

		String destination = null;
		// if source == view, go to view
		if ("view".equals(source) && StringUtils.isNotEmpty(destinationObjectId)) {
			destination = "view";
		}
		// if source == list, go to list
		else {
			destination = "list";
		}

		String view = "redirect:" + destinationObject + "." + destination;
		if ("view".equals(destination)) {
			view += "?id=" + destinationObjectId;
		}
		return new ModelAndView(view);
	}

	private boolean isSaveAction(HttpServletRequest request) {
		return WebUtils.hasSubmitParameter(request, "__save");
	}

	private boolean isDeleteAction(HttpServletRequest request) {
		return WebUtils.hasSubmitParameter(request, "__delete");
	}

	private boolean isCancelAction(HttpServletRequest request) {
		return WebUtils.hasSubmitParameter(request, "__cancel");
	}

}
