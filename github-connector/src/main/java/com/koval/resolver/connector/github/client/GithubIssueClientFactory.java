package com.koval.resolver.connector.github.client;

import com.koval.resolver.common.api.auth.Credentials;
import com.koval.resolver.common.api.component.connector.IssueClient;
import com.koval.resolver.common.api.component.connector.IssueClientFactory;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GithubIssueClientFactory implements IssueClientFactory {

  private static final Logger LOGGER = LoggerFactory.getLogger(GithubIssueClientFactory.class);
  private static final String BROWSE_SUFFIX = "/browse/";

  @Override
  public IssueClient getAnonymousClient(String host) {
    LOGGER.info("Creating Github client with Anonymous authentication...");
    GitHubClient restClient = new GitHubClient(host);
    return new GithubIssueClient(restClient, host + BROWSE_SUFFIX);
  }

  @Override
  public IssueClient getBasicClient(String host, Credentials credentials) {
    LOGGER.info("Creating Github client with Basic authentication...");
    GitHubClient restClient = new GitHubClient(host).setCredentials(
        credentials.getUsername(), credentials.getPassword());
    return new GithubIssueClient(restClient, host + BROWSE_SUFFIX);
  }
}
