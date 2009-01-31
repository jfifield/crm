package org.programmerplanet.crm.web.admin;

import org.apache.commons.lang.StringUtils;
import org.programmerplanet.crm.dao.OptionListDao;
import org.programmerplanet.crm.model.OptionList;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class OptionListValidator implements Validator {

	private OptionListDao optionListDao;
	
	public void setOptionListDao(OptionListDao optionList) {
		this.optionListDao = optionList;
	}
	
	public boolean supports(Class clazz) {
		return clazz.equals(OptionList.class);
	}

	public void validate(Object obj, Errors errors) {
		OptionList optionList = (OptionList)obj;

		// name is required
		if (StringUtils.isEmpty(optionList.getName())) {
			errors.rejectValue("name", "error.required");
		}
		// username must be unique
		else if (!optionListDao.isNameUnique(optionList.getId(), optionList.getName())) {
			errors.rejectValue("name", "error.name.exists");
		}
	}

}
