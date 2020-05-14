package com.koval.resolver.connector.github.client;

import static org.eclipse.egit.github.core.client.IGitHubConstants.CHARSET_UTF8;
import static org.eclipse.egit.github.core.client.IGitHubConstants.SEGMENT_ISSUES;
import static org.eclipse.egit.github.core.client.IGitHubConstants.SEGMENT_LEGACY;
import static org.eclipse.egit.github.core.client.IGitHubConstants.SEGMENT_SEARCH;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.NotImplementedException;
import org.eclipse.egit.github.core.IRepositoryIdProvider;
import org.eclipse.egit.github.core.IResourceProvider;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.RepositoryIssue;
import org.eclipse.egit.github.core.client.PageIterator;
import org.eclipse.egit.github.core.client.PagedRequest;
import org.eclipse.egit.github.core.service.IssueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.koval.resolver.common.api.bean.issue.Issue;
import com.koval.resolver.common.api.bean.issue.IssueField;
import com.koval.resolver.common.api.component.connector.IssueClient;
import com.koval.resolver.common.api.component.connector.IssueTransformer;
import com.koval.resolver.connector.github.exception.GithubClientException;


public class GithubIssueClient extends IssueService implements IssueClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(GithubIssueClient.class);
    private final IssueTransformer<RepositoryIssue> issueTransformer;

    GithubIssueClient() {
        this.issueTransformer = new GithubIssueTransformer();
    }

    @Override
    public int getTotalIssues(String query) {
        LOGGER.debug("Send total issues request: Query = '{}'.", query);
        // TODO change repo
        try {
            return searchIssues(RepositoryId.create("kovaloid", "iresolver"), null, 0,
                    Integer.MAX_VALUE).size();
        } catch (IOException e) {
            throw new GithubClientException("Unable to count issues.", e);
        }
    }

    @Override
    public List<Issue> search(String query, int maxResults, int startAt, List<String> fields) {
        LOGGER.debug("Send search request: Query = '{}' MaxResults = '{}' StartAt = '{}'.", query, maxResults, startAt);

        List<RepositoryIssue> searchResult;
        try {
            // TODO fix repo
            searchResult = searchIssues(RepositoryId.create("kovaloid", "iresolver"), query, startAt, maxResults);
            return issueTransformer.transform(searchResult);
        } catch (IOException e) {
            throw new GithubClientException("Unable to search issues.", e);
        }
    }

    @Override
    public Issue getIssueByKey(String issueKey) {
//    LOGGER.debug("Send issue request: IssueKey = '{}'.", issueKey);
//    org.eclipse.egit.github.core.Issue issue = checkRestExceptions(
//        () -> restClient.getIssueClient().getIssue(issueKey).claim(),
//        "Could not get issue by key: " + issueKey);
//    return issueTransformer.transform(issue);
        throw new NotImplementedException("Not imnplemented yet");
    }

    @Override
    public List<IssueField> getIssueFields() {
//    Iterable<Field> fields = checkRestExceptions(
//        () -> restClient.getMetadataClient().getFields().claim(),
//        "Could not get fields.");
//    List<IssueField> issueFields = new ArrayList<>();
//    fields.forEach(field -> {
//      IssueField issueField = new IssueField();
//      issueField.setId(field.getId());
//      issueField.setName(field.getName());
//      issueField.setType(field.getFieldType().name());
//      issueField.setValue("");
//      issueFields.add(issueField);
//    });
        throw new NotImplementedException("Not imnplemented yet");
    }

    @Override
    public void close() throws IOException {
        LOGGER.info("Closing Github client...");
    }

    /**
     * Search issues in the given repository using the given query.
     *
     * @param query, if null returns all issues
     * @return issues matching query
     * @throws IOException
     */
    private List<RepositoryIssue> searchIssues(IRepositoryIdProvider repository, String query, int startAt, int maxResults)
            throws IOException {
        String id = getId(repository);

        StringBuilder uri = new StringBuilder(SEGMENT_LEGACY + SEGMENT_ISSUES
                + SEGMENT_SEARCH);
        uri.append('/').append(id);
        if (query != null) {
            final String encodedQuery = URLEncoder.encode(query, CHARSET_UTF8)
                    .replace("+", "%20") //$NON-NLS-1$ //$NON-NLS-2$
                    .replace(".", "%2E"); //$NON-NLS-1$ //$NON-NLS-2$
            uri.append('/').append(encodedQuery);
        }
        LOGGER.error(uri.toString());

        int pageSize = 100;
        PagedRequest<RepositoryIssue> request = createPagedRequest(startAt / pageSize, pageSize);
        request.setUri(uri);
        request.setType(IssueContainer.class);
        List<RepositoryIssue> elements = new ArrayList<>();
        PageIterator<RepositoryIssue> iterator = createPageIterator(request);
        if (iterator.hasNext()) {
            Collection<RepositoryIssue> requestResult = iterator.next();
            int fromIndex = startAt % pageSize;
            int toIndex = Math.max(requestResult.size() - fromIndex, maxResults);
            // to start with exact issue
            elements.addAll(new ArrayList<>(requestResult).subList(fromIndex, toIndex));
        }
        while (iterator.hasNext()) {
            Collection<RepositoryIssue> requestResult = iterator.next();
            if (requestResult.size() + elements.size() > maxResults) {
                elements.addAll(new ArrayList<>(requestResult).subList(0, maxResults - elements.size()));
            } else {
                elements.addAll(requestResult);
            }
        }
        return elements;
    }

    private static class IssueContainer implements
            IResourceProvider<RepositoryIssue> {

        private List<RepositoryIssue> issues;

        /**
         * @see org.eclipse.egit.github.core.IResourceProvider#getResources()
         */
        @Override
        public List<RepositoryIssue> getResources() {
            return issues;
        }
    }
}
