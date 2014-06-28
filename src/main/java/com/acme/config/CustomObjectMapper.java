package com.acme.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

/**
 * 
 * This mapper class is used so to be able to override the default implementation in Spring for dates
 * so that we can return ISO 8601 compliant dates instead of Epoch time.
 *
 */
public class CustomObjectMapper extends ObjectMapper {
	
	public CustomObjectMapper() {
		configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		setDateFormat(new ISO8601DateFormat());
	}
}
