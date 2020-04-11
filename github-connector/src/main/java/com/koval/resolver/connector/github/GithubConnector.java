package com.koval.resolver.connector.github;

import com.koval.resolver.common.api.component.connector.Connector;
import com.koval.resolver.common.api.component.connector.IssueClient;
import com.koval.resolver.common.api.component.connector.IssueReceiver;
import com.koval.resolver.common.api.configuration.bean.connectors.GithubConnectorConfiguration;
import com.koval.resolver.connector.github.core.GithubIssueReceiver;


public class GithubConnector implements Connector {

  private final IssueClient issueClient;
  private final GithubConnectorConfiguration connectorProperties;

  public GithubConnector(IssueClient issueClient, GithubConnectorConfiguration connectorProperties) {
    this.issueClient = issueClient;
    this.connectorProperties = connectorProperties;
  }

  @Override
  public IssueReceiver getResolvedIssuesReceiver() {
    return new GithubIssueReceiver(issueClient, connectorProperties, true);
  }

  @Override
  public IssueReceiver getUnresolvedIssuesReceiver() {
    return new GithubIssueReceiver(issueClient, connectorProperties, false);
  }
}
