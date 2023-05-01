package com.codechallenge.github.proxy.controller.client;

import com.codechallenge.github.proxy.model.BranchInfo;
import com.codechallenge.github.proxy.model.RepositoryBasicInfo;
import com.google.common.collect.ImmutableMap;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.Collection;

@Component
public class GitHubApiClient {

    private static final String USER_NAME_PARAM = "userName";

    private static final String GIT_HUB_API_USER_URL_TEMPLATE =
            String.format("https://api.github.com/users/{%s}", USER_NAME_PARAM);
    private static final String GIT_HUB_API_REPOS_URL_TEMPLATE =
            String.format("https://api.github.com/users/{%s}/repos", USER_NAME_PARAM);

    private static final String REPO_NAME_PARAM = "repoName";
    private static final String GIT_HUB_API_BRANCHES_URL_TEMPLATE =
            String.format("https://api.github.com/repos/{%s}/{%s}/branches", USER_NAME_PARAM, REPO_NAME_PARAM);

    public ResponseEntity<Object> getGitHubUser(String userName) {
        RestTemplate restTemplate = new RestTemplate();
        URI gitHubApiReposUrl = UriComponentsBuilder.fromHttpUrl(GIT_HUB_API_USER_URL_TEMPLATE)
                .buildAndExpand(ImmutableMap.of(USER_NAME_PARAM, userName)).toUri();
        return restTemplate.getForEntity(gitHubApiReposUrl, Object.class);
    }

    public Collection<RepositoryBasicInfo> getGitHubRepos(String userName) {
        RestTemplate restTemplate = new RestTemplate();
        URI gitHubApiReposUrl = UriComponentsBuilder.fromHttpUrl(GIT_HUB_API_REPOS_URL_TEMPLATE)
                .buildAndExpand(ImmutableMap.of(USER_NAME_PARAM, userName)).toUri();
        return Arrays.asList(restTemplate.getForEntity(gitHubApiReposUrl, RepositoryBasicInfo[].class).getBody());
    }

    public Collection<BranchInfo> getGitHubRepoBranches(String userName, String repoName) {
        RestTemplate restTemplate = new RestTemplate();
        URI gitHubApiBranchesUrl = UriComponentsBuilder.fromHttpUrl(GIT_HUB_API_BRANCHES_URL_TEMPLATE)
                .buildAndExpand(ImmutableMap.of(USER_NAME_PARAM, userName, REPO_NAME_PARAM, repoName)).toUri();
        return Arrays.asList(restTemplate.getForEntity(gitHubApiBranchesUrl, BranchInfo[].class).getBody());
    }
}
