package org.programmerplanet.crm.web.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.programmerplanet.crm.model.OptionList;
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
public class OptionListEditController extends SimpleMultiActionFormController {

	private AdministrationService administrationService;

	public void setAdministrationService(AdministrationService administrationService) {
		this.administrationService = administrationService;
	}

	public ModelAndView save(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		OptionList optionList = (OptionList)command;

		if (optionList.getId() != null) {
			administrationService.updateOptionList(optionList);
			return new ModelAndView(getSuccessView());
		}
		else {
			administrationService.insertOptionList(optionList);
			return new ModelAndView("redirect:optionListEdit", "id", optionList.getId());
		}
	}

	public ModelAndView delete(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		OptionList optionList = (OptionList)command;
		administrationService.deleteOptionList(optionList);
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
			OptionList optionList = administrationService.getOptionList(id);
			return optionList;
		}
		else {
			return super.formBackingObject(request);
		}
	}

	protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
		OptionList optionList = (OptionList)command;
		if (optionList.getId() != null) {
			Map data = new HashMap();
			List optionListItems = administrationService.getOptionListItems(optionList);
			data.put("optionListItems", optionListItems);
			return data;
		}
		else {
			return super.referenceData(request, command, errors);
		}
	}

}
