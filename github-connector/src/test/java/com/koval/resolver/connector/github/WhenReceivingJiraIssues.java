package com.koval.resolver.connector.github;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.koval.resolver.common.api.component.connector.IssueClient;
import com.koval.resolver.common.api.component.connector.IssueReceiver;
import com.koval.resolver.common.api.configuration.bean.connectors.GithubConnectorConfiguration;
import com.koval.resolver.connector.github.core.GithubIssueReceiver;


public class WhenReceivingJiraIssues {

  private IssueReceiver receiver;

  @Before
  public void init() {
    IssueClient client = mock(IssueClient.class);
    GithubConnectorConfiguration connectorProperties = mock(GithubConnectorConfiguration.class);
    when(connectorProperties.getResolvedQuery()).thenReturn("resolvedQuery");
    when(client.getTotalIssues(anyString())).thenReturn(10);
    receiver = new GithubIssueReceiver(client, connectorProperties, true);
  }

  @Test
  public void shouldGetResolvedIssues() {
    assertTrue(receiver.hasNextIssues());
  }
}
