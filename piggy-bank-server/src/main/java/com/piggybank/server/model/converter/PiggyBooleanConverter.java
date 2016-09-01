package com.piggybank.server.model.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply=true)
public class PiggyBooleanConverter implements AttributeConverter<Boolean, String> {
	@Override
	public String convertToDatabaseColumn(Boolean attribute) {
		return attribute? "T" : "F";
	}

	@Override
	public Boolean convertToEntityAttribute(String stringBoolean) {
		return stringBoolean.equals("T")? true : false;
	}
}
