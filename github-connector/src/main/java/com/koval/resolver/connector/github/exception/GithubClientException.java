package com.koval.resolver.connector.github.exception;

import com.koval.resolver.common.api.exception.ClientException;


public class GithubClientException extends ClientException {

  public GithubClientException(String message) {
    super(message);
  }

  public GithubClientException(String message, Throwable cause) {
    super(message, cause);
  }
}
