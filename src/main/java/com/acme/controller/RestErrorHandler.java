package com.acme.controller;

import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.acme.controller.error.ErrorCollection;
import com.acme.dom.exception.PublishAdException;

/**
 * 
 * This class handles validation and parsing errors
 *
 */
@ControllerAdvice
public class RestErrorHandler {

  public final static String POST_AD_ERROR = "Post ad";

  private static final Logger LOGGER = LoggerFactory.getLogger(RestErrorHandler.class);

  private MessageSource messageSource;

  @Autowired
  public RestErrorHandler(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  /**
   * All posting of ad exceptions will be handled by this method
   * 
   * @param ex
   */
  @ExceptionHandler(PublishAdException.class)
  public ResponseEntity<ErrorCollection> handlePublishAdException(PublishAdException ex) {
    LOGGER.debug(ex.getLocalizedMessage());
    ErrorCollection errors = new ErrorCollection();
    errors.addError(POST_AD_ERROR, ex.getMessage());
    return new ResponseEntity<ErrorCollection>(errors, HttpStatus.NOT_FOUND);
  }

  /**
   * Any uncaught exception that occurs such as parsing errors will be caught in this method.
   * 
   * @param e
   * @return Error collection
   */
  @ExceptionHandler(value = Exception.class)
  @ResponseBody
  public ResponseEntity<ErrorCollection> processValidationError(Exception e) {
    ErrorCollection errors = new ErrorCollection();
    errors.addError("Error", e.getMessage());

    return new ResponseEntity<ErrorCollection>(errors, HttpStatus.BAD_REQUEST);
  }

  /**
   * Validation errors such as required fields that are not sent in the request will throw a
   * MethodArgumentNotValidException and be handled in this method
   * 
   * @param ex
   * @return Error collection
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseBody
  public ResponseEntity<ErrorCollection> processValidationError(MethodArgumentNotValidException ex) {
    BindingResult result = ex.getBindingResult();
    List<FieldError> fieldErrors = result.getFieldErrors();

    return new ResponseEntity<ErrorCollection>(processFieldErrors(fieldErrors),
        HttpStatus.BAD_REQUEST);
  }

  private ErrorCollection processFieldErrors(List<FieldError> fieldErrors) {
    ErrorCollection errors = new ErrorCollection();
    String localizedErrorMessage = "";

    for (FieldError fieldError : fieldErrors) {
      localizedErrorMessage = resolveLocalizedErrorMessage(fieldError);
      errors.addError(fieldError.getField(), localizedErrorMessage);
    }

    return errors;
  }

  private String resolveLocalizedErrorMessage(FieldError fieldError) {
    Locale currentLocale = LocaleContextHolder.getLocale();
    return messageSource.getMessage(fieldError, currentLocale);
  }


}
