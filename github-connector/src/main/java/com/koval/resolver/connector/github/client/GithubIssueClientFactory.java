package com.koval.resolver.connector.github.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.koval.resolver.common.api.auth.Credentials;
import com.koval.resolver.common.api.component.connector.IssueClient;
import com.koval.resolver.common.api.component.connector.IssueClientFactory;
import com.koval.resolver.common.api.configuration.bean.connectors.GithubConnectorConfiguration;


public class GithubIssueClientFactory implements IssueClientFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(GithubIssueClientFactory.class);

    private final Credentials credentials;

    public GithubIssueClientFactory(GithubConnectorConfiguration connectorConfiguration) {
        if (connectorConfiguration.isAnonymous()) {
            credentials = null;
        } else {
            credentials = Credentials.getCredentials(connectorConfiguration.getCredentialsFolder());
        }
    }


    @Override
    public IssueClient getClient() {
        if (credentials == null) {
            return getAnonymousClient();
        } else {
            return getBasicClient();
        }
    }

    public IssueClient getAnonymousClient() {
        LOGGER.info("Creating Github client with Anonymous authentication...");
        return new GithubIssueClient();
    }

    public IssueClient getBasicClient() {
        LOGGER.info("Creating Github client with Basic authentication...");
        return new GithubIssueClient();
    }
}
