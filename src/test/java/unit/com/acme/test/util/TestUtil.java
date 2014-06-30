package com.acme.test.util;

import java.io.IOException;
import java.nio.charset.Charset;

import org.springframework.http.MediaType;

import com.acme.config.CustomObjectMapper;
import com.fasterxml.jackson.annotation.JsonInclude;

public class TestUtil {

  public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(
      MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(),
      Charset.forName("utf8"));

  public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
    CustomObjectMapper mapper = new CustomObjectMapper();
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    return mapper.writeValueAsBytes(object);
  }

}
