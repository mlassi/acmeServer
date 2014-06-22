package com.acme.test.util;

import java.io.IOException;
import java.nio.charset.Charset;

import org.springframework.http.MediaType;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestUtil {

  public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(
      MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(),
      Charset.forName("utf8"));

  private static final String CHARACTER = "a";

  public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    return mapper.writeValueAsBytes(object);
  }

  public static String createStringWithLength(int length) {
    StringBuilder builder = new StringBuilder();

    for (int index = 0; index < length; index++) {
      builder.append(CHARACTER);
    }

    return builder.toString();
  }
}
