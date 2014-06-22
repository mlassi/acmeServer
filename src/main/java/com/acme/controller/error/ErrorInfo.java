package com.acme.controller.error;

import java.io.Serializable;

public class ErrorInfo implements Serializable {

  private static final long serialVersionUID = 1L;

  private String code;
  private String message;
  private String source;

  public ErrorInfo(String code, String message, String source) {
    this.code = code;
    this.message = message;
    this.source = source;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }
}
