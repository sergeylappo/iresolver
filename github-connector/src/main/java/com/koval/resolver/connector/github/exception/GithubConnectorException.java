package com.koval.resolver.connector.github.exception;

import com.koval.resolver.common.api.exception.ConnectorException;


public class GithubConnectorException extends ConnectorException {

  public GithubConnectorException(String message, Throwable cause) {
    super(message, cause);
  }
}
