package com.codechallenge.github.proxy.controller;

import com.codechallenge.github.proxy.controller.client.GitHubApiClient;
import com.codechallenge.github.proxy.model.BranchInfo;
import com.codechallenge.github.proxy.model.RepositoryBasicInfo;
import com.codechallenge.github.proxy.model.RepositoryInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class UserRepositoriesService {

    @Autowired
    private GitHubApiClient gitHubApiClient;

    public void checkUserExistence(String userName) {
        try {
            gitHubApiClient.getGitHubUser(userName);
        } catch (HttpClientErrorException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("User %s do not exist or have no repositories", userName), ex);
        }
    }

    public Collection<RepositoryInfo> getUserRepositoriesInfo(String userName) {
        Collection<RepositoryBasicInfo> repositoriesBasicInfo = gitHubApiClient.getGitHubRepos(userName);
        Collection<RepositoryBasicInfo> onlyNonForkRepos = findOnlyNonForkRepos(repositoriesBasicInfo);
        return getRepositoriesInfo(userName, onlyNonForkRepos);
    }

    private Collection<RepositoryBasicInfo> findOnlyNonForkRepos(
            Collection<RepositoryBasicInfo> repositoriesBasicInfo) {

        return repositoriesBasicInfo.stream()
                .filter(repo -> !repo.getFork())
                .collect(Collectors.toList());
    }

    private Collection<RepositoryInfo> getRepositoriesInfo(String userName,
                                                           Collection<RepositoryBasicInfo> repositoriesBasicInfo) {

        return repositoriesBasicInfo.stream().parallel()
                .map(basicInfo -> getRepositoryInfo(userName, basicInfo))
                .collect(Collectors.toList());
    }

    private RepositoryInfo getRepositoryInfo(String userName, RepositoryBasicInfo repositoryBasicInfo) {
        Collection<BranchInfo> branchesInfo =
                gitHubApiClient.getGitHubRepoBranches(userName, repositoryBasicInfo.getName());
        return new RepositoryInfo(repositoryBasicInfo, branchesInfo);
    }
}
