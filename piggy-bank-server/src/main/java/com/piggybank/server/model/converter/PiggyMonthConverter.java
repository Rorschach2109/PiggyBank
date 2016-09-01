package com.piggybank.server.model.converter;

import java.time.Month;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply=true)
public class PiggyMonthConverter implements AttributeConverter<Month, Integer> {

	@Override
	public Integer convertToDatabaseColumn(Month month) {
		return (null == month)? null : month.getValue();
	}

	@Override
	public Month convertToEntityAttribute(Integer monthInteger) {
		return (null == monthInteger)? null : Month.of(monthInteger);
	}

}
