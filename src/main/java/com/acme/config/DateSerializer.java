//package com.acme.config;
//
//import java.io.IOException;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//import com.fasterxml.jackson.core.JsonGenerator;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonSerializer;
//import com.fasterxml.jackson.databind.SerializerProvider;
//import com.fasterxml.jackson.databind.annotation.JsonSerialize;
//
//
//public class DateSerializer extends JsonSerializer<Date> {
//
//	@Override
//	public void serialize(Date date, JsonGenerator jgen, SerializerProvider provider)
//			throws IOException, JsonProcessingException {
//		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz");
//		jgen.writeString(formatter.format(date));
//	}
//
//	
//}
