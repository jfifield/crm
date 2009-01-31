package org.programmerplanet.crm.web.admin;

import org.apache.commons.lang.StringUtils;
import org.programmerplanet.crm.dao.OptionListItemDao;
import org.programmerplanet.crm.model.OptionListItem;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class OptionListItemValidator implements Validator {

	private OptionListItemDao optionListItemDao;

	public void setOptionListItemDao(OptionListItemDao optionListItemDao) {
		this.optionListItemDao = optionListItemDao;
	}

	public boolean supports(Class clazz) {
		return clazz.equals(OptionListItem.class);
	}

	public void validate(Object obj, Errors errors) {
		OptionListItem optionListItem = (OptionListItem)obj;

		// value is required
		if (StringUtils.isEmpty(optionListItem.getValue())) {
			errors.rejectValue("value", "error.required");
		}
		// value must be unique (within option list)
		else if (!optionListItemDao.isValueUnique(optionListItem.getOptionListId(), optionListItem.getId(), optionListItem.getValue())) {
			errors.rejectValue("value", "error.value.exists");
		}
	}

}
