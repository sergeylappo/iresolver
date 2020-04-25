package com.koval.resolver.connector.github;

import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.koval.resolver.common.api.component.connector.IssueClient;
import com.koval.resolver.common.api.component.connector.IssueReceiver;
import com.koval.resolver.common.api.configuration.bean.connectors.GithubConnectorConfiguration;


public class WhenCreatingGithubConnector {

  private GithubConnector githubConnector;

  @Before
  public void init() {
    IssueClient client = mock(IssueClient.class);
    GithubConnectorConfiguration properties = mock(GithubConnectorConfiguration.class);
    List searchResult = mock(List.class);
    when(properties.getResolvedQuery()).thenReturn("resolvedQuery");
    when(properties.getUnresolvedQuery()).thenReturn("unresolvedQuery");
    when(client.search(anyString(), anyInt(), anyInt(), anyList())).thenReturn(searchResult);
    githubConnector = new GithubConnector(client, properties);
  }

  @Test
  public void shouldBeAbleToGetResolvedIssuesReceiver() {
    IssueReceiver receiver = githubConnector.getResolvedIssuesReceiver();
    assertFalse("Check next issues", receiver.hasNextIssues());
  }

  @Test
  public void shouldBeAbleToGetUnresolvedIssuesReceiver() {
    IssueReceiver receiver = githubConnector.getUnresolvedIssuesReceiver();
    assertFalse("Check next issues", receiver.hasNextIssues());
  }
}
