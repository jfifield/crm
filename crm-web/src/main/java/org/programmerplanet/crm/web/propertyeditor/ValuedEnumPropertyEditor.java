package org.programmerplanet.crm.web.propertyeditor;

import java.beans.PropertyEditorSupport;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.enums.EnumUtils;
import org.apache.commons.lang.enums.ValuedEnum;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class ValuedEnumPropertyEditor extends PropertyEditorSupport {

	private Class enumClass;
	private boolean allowEmpty;

	public ValuedEnumPropertyEditor(Class enumClass) {
		this(enumClass, true);
	}
	
	public ValuedEnumPropertyEditor(Class enumClass, boolean allowEmpty) {
		if (!ValuedEnum.class.isAssignableFrom(enumClass)) {
			throw new IllegalArgumentException("Class must be subclass of " + ValuedEnum.class);
		}
		this.enumClass = enumClass;
		this.allowEmpty = allowEmpty;
	}

	/**
	 * @see java.beans.PropertyEditorSupport#getAsText()
	 */
	public String getAsText() {
		if (this.getValue() != null) {
			ValuedEnum valuedEnum = (ValuedEnum)this.getValue();
			return Integer.toString(valuedEnum.getValue());
		} else {
			return "";
		}
	}

	/**
	 * @see java.beans.PropertyEditorSupport#setAsText(java.lang.String)
	 */
	public void setAsText(String text) throws IllegalArgumentException {
		if (this.allowEmpty && StringUtils.isEmpty(text)) {
			this.setValue(null);
		} else {
			ValuedEnum valuedEnum = EnumUtils.getEnum(enumClass, Integer.parseInt(text));
			if (valuedEnum != null) {
				this.setValue(valuedEnum);
			} else {
				throw new IllegalArgumentException("Invalid ValueEnum value: " + text);
			}
		}
	}

}