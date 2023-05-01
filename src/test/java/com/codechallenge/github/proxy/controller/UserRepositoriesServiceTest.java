package com.codechallenge.github.proxy.controller;

import com.codechallenge.github.proxy.controller.client.GitHubApiClient;
import com.codechallenge.github.proxy.model.*;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class UserRepositoriesServiceTest {

    private static final String NAME = "repo";
    private static final String LOGIN = "login";
    private static final String BRANCH_1_NAME = "master";
    private static final String BRANCH_2_NAME = "develop";
    private static final String SHA_1 = "aasdkisghdkajhsdjkahlsdfhskdfb";
    private static final String SHA_2 = "aasdkisghdkajhsfjkghlidfhskdad";

    @Mock
    private GitHubApiClient client;

    @InjectMocks
    private UserRepositoriesService service;

    private AutoCloseable closeable;

    @BeforeEach
    void initService() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }

    @Test
    @DisplayName("Throw ResponseStatusException when user do not exist")
    public void test1() {
        Mockito.when(client.getGitHubUser(Mockito.anyString()))
                .thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        ResponseStatusException thrown = Assertions.assertThrows(ResponseStatusException.class, () -> {
            service.checkUserExistence("test");
        });

        Assertions.assertEquals("User test do not exist or have no repositories", thrown.getReason());
    }

    @Test
    @DisplayName("Return repository info for non forked user repos")
    public void test2() {
        Mockito.when(client.getGitHubRepoBranches(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(mockBranchesInfo());
        Mockito.when(client.getGitHubRepos(Mockito.anyString()))
                .thenReturn(Collections.singleton(mockRepositoryBasicInfo(false)));

        Collection<RepositoryInfo> result = service.getUserRepositoriesInfo("login");

        RepositoryInfo expected = createExpectedRepositoryInfo();
        assertRepositoryInfo(expected, result.stream().findFirst().get());
    }

    @Test
    @DisplayName("Omit user forked repos (empty result)")
    public void test3() {
        Mockito.when(client.getGitHubRepoBranches(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(mockBranchesInfo());
        Mockito.when(client.getGitHubRepos(Mockito.anyString()))
                .thenReturn(Collections.singleton(mockRepositoryBasicInfo(true)));

        Collection<RepositoryInfo> result = service.getUserRepositoriesInfo("login");

        Assertions.assertEquals(0, result.size());
    }

    private RepositoryInfo createExpectedRepositoryInfo() {
        return new RepositoryInfo(mockRepositoryBasicInfo(false), mockBranchesInfo());
    }

    private Collection<BranchInfo> mockBranchesInfo() {
        BranchInfo branch1 = new BranchInfo(BRANCH_1_NAME, new CommitInfo(SHA_1));
        BranchInfo branch2 = new BranchInfo(BRANCH_2_NAME, new CommitInfo(SHA_2));
        return Arrays.asList(branch1, branch2);
    }

    private RepositoryBasicInfo mockRepositoryBasicInfo(boolean fork) {
        OwnerBasicInfo ownerBasicInfo = new OwnerBasicInfo(LOGIN);
        return new RepositoryBasicInfo(NAME, fork, ownerBasicInfo);
    }

    private void assertRepositoryInfo(RepositoryInfo expected, RepositoryInfo actual) {
        Assertions.assertEquals(expected.getBasicInfo().getName(), actual.getBasicInfo().getName());
        Assertions.assertEquals(expected.getBasicInfo().getFork(), actual.getBasicInfo().getFork());
        Assertions.assertEquals(
                expected.getBasicInfo().getOwner().getLogin(), actual.getBasicInfo().getOwner().getLogin());

        expected.getBranches().forEach(branch -> assertBranchInfo(branch, actual.getBranches()));
    }

    private void assertBranchInfo(BranchInfo expected, Collection<BranchInfo> actualBranches) {
        BranchInfo actual = actualBranches.stream()
                .filter(branch -> expected.getName().equals(branch.getName()))
                .findFirst().get();

        Assertions.assertEquals(expected.getName(), actual.getName());
        Assertions.assertEquals(expected.getCommit().getSha(), actual.getCommit().getSha());
    }
}
