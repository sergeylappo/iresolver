package com.koval.resolver.connector.github.client;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.egit.github.core.Label;
import org.eclipse.egit.github.core.RepositoryIssue;
import org.joda.time.DateTime;

import com.koval.resolver.common.api.bean.issue.Issue;
import com.koval.resolver.common.api.bean.issue.Project;
import com.koval.resolver.common.api.bean.issue.User;
import com.koval.resolver.common.api.component.connector.IssueTransformer;


public class GithubIssueTransformer implements IssueTransformer<RepositoryIssue> {

  private static final String UNKNOWN = "<unknown>";

  @Override
  public Issue transform(RepositoryIssue originalIssue) {
    Issue transformedIssue = new Issue();

    transformedIssue.setLink(URI.create(originalIssue.getHtmlUrl()));
    transformedIssue.setKey(Integer.toString(originalIssue.getNumber()));
    transformedIssue.setSummary(originalIssue.getTitle());
    transformedIssue.setDescription(originalIssue.getBody());
    transformedIssue.setStatus(originalIssue.getState());
    if (originalIssue.getState() == null) {
      transformedIssue.setResolution("");
    } else {
      transformedIssue.setResolution(originalIssue.getState());
    }

    if (originalIssue.getUser() == null) {
      transformedIssue.setReporter(getUnknownUser());
    } else {
      transformedIssue.setReporter(transformUser(originalIssue.getUser()));
    }
    if (originalIssue.getAssignee() == null) {
      transformedIssue.setAssignee(getUnknownUser());
    } else {
      transformedIssue.setAssignee(transformUser(originalIssue.getAssignee()));
    }

    transformedIssue.setProject(new Project(Long.toString(originalIssue.getRepository().getId()),
        originalIssue.getRepository().getName()));
    transformedIssue.setCreationDate(new DateTime(originalIssue.getCreatedAt()));
    transformedIssue.setUpdateDate(new DateTime(originalIssue.getUpdatedAt()));

    transformedIssue.setLabels(transformLabels(originalIssue.getLabels()));

    return transformedIssue;
  }

  private List<String> transformLabels(List<Label> originalLabels) {
    return originalLabels.stream()
            .map(Label::getName)
            .collect(Collectors.toList());
  }

  private User transformUser(org.eclipse.egit.github.core.User originalUser) {
    User transformedUser = new User();
    transformedUser.setName(originalUser.getName());
    transformedUser.setDisplayName(originalUser.getName());
    transformedUser.setEmailAddress(originalUser.getEmail());
    transformedUser.setAvatarUri(URI.create(originalUser.getAvatarUrl()));
    return transformedUser;
  }

  private User getUnknownUser() {
    return new User(UNKNOWN, UNKNOWN, UNKNOWN, new ArrayList<>(),
        URI.create(""), URI.create(""));
  }

  @Override
  public List<Issue> transform(Collection<RepositoryIssue> originalIssues) {
    List<Issue> transformedIssues = new ArrayList<>();
    for (RepositoryIssue originalIssue : originalIssues) {
      transformedIssues.add(transform(originalIssue));
    }
    return transformedIssues;
  }
}
