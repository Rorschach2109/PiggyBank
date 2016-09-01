package com.piggybank.server.model.converter;

import java.sql.Date;
import java.time.LocalDate;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply=true)
public class PiggyLocalDateConverter implements AttributeConverter<LocalDate, Date> {

	@Override
	public Date convertToDatabaseColumn(LocalDate localDate) {
		return (null == localDate)? null : Date.valueOf(localDate);
	}

	@Override
	public LocalDate convertToEntityAttribute(Date sqlDate) {
		return (null == sqlDate)? null : sqlDate.toLocalDate();
	}
}