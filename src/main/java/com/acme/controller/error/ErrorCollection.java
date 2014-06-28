package com.acme.controller.error;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ErrorCollection implements Serializable {

  private static final long serialVersionUID = 1L;

  private List<ErrorInfo> errors = new ArrayList<ErrorInfo>();

  public void addError(ErrorInfo errorInfo) {
    errors.add(errorInfo);
  }

  public List<ErrorInfo> getErrors() {
    return errors;
  }

  public void addError(String errorCode, String errorMessage) {
    addError(new ErrorInfo(errorCode, errorMessage));
  }

}
