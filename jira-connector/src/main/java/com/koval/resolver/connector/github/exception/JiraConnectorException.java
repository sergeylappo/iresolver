package com.koval.resolver.connector.github.exception;

import com.koval.resolver.common.api.exception.ConnectorException;


public class JiraConnectorException extends ConnectorException {

  public JiraConnectorException(String message, Throwable cause) {
    super(message, cause);
  }
}
