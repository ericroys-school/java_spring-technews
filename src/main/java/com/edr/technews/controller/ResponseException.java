package com.edr.technews.controller;

public class ResponseException extends RuntimeException {

  public ResponseException(String exception) {
    super(exception);
  }
}
