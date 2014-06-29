package com.acme.dom.exception;

/**
 * 
 * This purpose of this exception is that it should be thrown if an ad or newspaper 
 * can't be found while posting and ad or cancelling an ad that hasn't been
 * published in a newspaper yet.
 *
 */
public class PublishAdException extends Exception {

  private static final long serialVersionUID = 1L;

  public PublishAdException(String message) {
    super(message);
  }
}
