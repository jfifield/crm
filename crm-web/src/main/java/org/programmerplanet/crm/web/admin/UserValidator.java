package org.programmerplanet.crm.web.admin;

import org.apache.commons.lang.StringUtils;
import org.programmerplanet.crm.user.User;
import org.programmerplanet.crm.user.UserManager;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class UserValidator implements Validator {

	private UserManager userManager;
	
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}
	
	public boolean supports(Class clazz) {
		return clazz.equals(User.class);
	}

	public void validate(Object obj, Errors errors) {
		User user = (User)obj;

		// username is required
		if (StringUtils.isEmpty(user.getUsername())) {
			errors.rejectValue("username", "error.required");
		}
		// username must be regular non-whitespace characters
		else if (!user.getUsername().matches("\\w+")) {
			errors.rejectValue("username", "error.username.invalid");
		}
		// username must be unique
		else if (!userManager.isUsernameUnique(user.getId(), user.getUsername())) {
			errors.rejectValue("username", "error.username.exists");
		}
		
		// password is required
		if (StringUtils.isEmpty(user.getPassword())) {
			errors.rejectValue("password", "error.required");
		}
		// password must be atleast 6 characters
		else if (user.getPassword().length() < 6) {
			errors.rejectValue("password", "error.password.length", new Object[] { new Integer(6) }, null);
		}
		
		// email address is required
		if (StringUtils.isEmpty(user.getEmailAddress())) {
			errors.rejectValue("emailAddress", "error.required");
		}
		// email address must be a valid email format
		else if (!user.getEmailAddress().matches("^([a-zA-Z0-9_\\-\\.])+@(([0-2]?[0-5]?[0-5]\\.[0-2]?[0-5]?[0-5]\\.[0-2]?[0-5]?[0-5]\\.[0-2]?[0-5]?[0-5])|((([a-zA-Z0-9\\-])+\\.)+([a-zA-Z\\-])+))$")) {
			errors.rejectValue("emailAddress", "error.email.invalid");
		}
	}

}
