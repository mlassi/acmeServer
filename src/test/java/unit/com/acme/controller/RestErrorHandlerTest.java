//package com.acme.controller;
//
//import org.junit.Before;
//import org.mockito.MockitoAnnotations;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.test.context.web.WebAppConfiguration;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.method.HandlerMethod;
//import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
//import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;
//
//import com.sun.org.apache.xml.internal.serializer.Method;
//
//@Configuration
//@WebAppConfiguration
//public class RestErrorHandlerTest {
//
//	private MockMvc mockMvc;
//	
//	private RestErrorHandler restErrorHandler;
//	
//	@Before
//	  public void setup() {
//	    MockitoAnnotations.initMocks(this);
//	    mockMvc = MockMvcBuilders.standaloneSetup(restErrorHandler).build();
//	  }
//	
//	private ExceptionHandlerExceptionResolver createExceptionResolver() {
//	    ExceptionHandlerExceptionResolver exceptionResolver = new ExceptionHandlerExceptionResolver() {
//	        protected ServletInvocableHandlerMethod getExceptionHandlerMethod(HandlerMethod handlerMethod, Exception exception) {
//	            Method method = new ExceptionHandlerMethodResolver(MyControllerAdvice.class).resolveMethod(exception);
//	            return new ServletInvocableHandlerMethod(new RestErrorHandler(), method);
//	        }
//	    };
//	    exceptionResolver.afterPropertiesSet();
//	    return exceptionResolver;
//	}
//}
