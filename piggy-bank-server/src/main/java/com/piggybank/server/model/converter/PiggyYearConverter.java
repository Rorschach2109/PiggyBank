package com.piggybank.server.model.converter;

import java.time.Year;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply=true)
public class PiggyYearConverter implements AttributeConverter<Year, Integer> {

	@Override
	public Integer convertToDatabaseColumn(Year year) {
		return (null == year)? null : year.getValue();
	}
	
	@Override
	public Year convertToEntityAttribute(Integer yearInteger) {
		return (null == yearInteger)? null : Year.of(yearInteger);
	}
}
