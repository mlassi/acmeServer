package com.acme.controller;

import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.acme.controller.error.ErrorCollection;

@ControllerAdvice
public class RestErrorHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(RestErrorHandler.class);

  private MessageSource messageSource;

  @Autowired
  public RestErrorHandler(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @ExceptionHandler(value = Exception.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ErrorCollection processValidationError(Exception ex) {
    ErrorCollection errors = new ErrorCollection();
    errors.addFieldError("bogus", "error message");
    LOGGER.debug("exception ex");
    return errors;
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ErrorCollection processValidationError(MethodArgumentNotValidException ex) {
    BindingResult result = ex.getBindingResult();
    List<FieldError> fieldErrors = result.getFieldErrors();

    return processFieldErrors(fieldErrors);
  }

  private ErrorCollection processFieldErrors(List<FieldError> fieldErrors) {
    ErrorCollection errors = new ErrorCollection();

    try {
      LOGGER.debug("processfielderrors");
    } finally {
    }

    for (FieldError fieldError : fieldErrors) {
      try {
        LOGGER.debug(fieldError.getField());
        LOGGER.debug(fieldError.getDefaultMessage());
        LOGGER.debug(fieldError.getCode());
      } catch (Exception e) {
      }


      // String localizedErrorMessage = resolveLocalizedErrorMessage(fieldError);
      errors.addFieldError(fieldError.getField(), fieldError.getDefaultMessage());
    }

    return errors;
  }

  private String resolveLocalizedErrorMessage(FieldError fieldError) {
    Locale currentLocale = LocaleContextHolder.getLocale();
    String localizedErrorMessage = messageSource.getMessage(fieldError, currentLocale);

    // If the message was not found, return the most accurate field error code instead.
    // You can remove this check if you prefer to get the default error message.
    if (localizedErrorMessage.equals(fieldError.getDefaultMessage())) {
      String[] fieldErrorCodes = fieldError.getCodes();
      localizedErrorMessage = fieldErrorCodes[0];
    }

    return localizedErrorMessage;
  }

  // @ExceptionHandler(TodoNotFoundException.class)
  // @ResponseStatus(HttpStatus.NOT_FOUND)
  // public void handleTodoNotFoundException(TodoNotFoundException ex) {
  // LOGGER.debug("handling 404 error on a todo entry");
  // }
}
