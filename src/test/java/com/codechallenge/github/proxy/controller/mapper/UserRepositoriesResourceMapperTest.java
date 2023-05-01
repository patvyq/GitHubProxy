package com.codechallenge.github.proxy.controller.mapper;

import com.codechallenge.github.proxy.controller.dto.RepositoryInfoDTO;
import com.codechallenge.github.proxy.controller.dto.RepositoryInfoDTO.BranchInfoDTO;
import com.codechallenge.github.proxy.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class UserRepositoriesResourceMapperTest {

    private static final String NAME = "repo";
    private static final String LOGIN = "login";
    private static final String BRANCH_1_NAME = "master";
    private static final String BRANCH_2_NAME = "develop";
    private static final String SHA_1 = "aasdkisghdkajhsdjkahlsdfhskdfb";
    private static final String SHA_2 = "aasdkisghdkajhsfjkghlidfhskdad";

    @Test
    @DisplayName("Return correctly mapped RepositoryInfoDTO from RepositoryInfo")
    public void test() {
        RepositoryInfo repositoryInfo = mockRepositoryInfo();

        Collection<RepositoryInfoDTO> result =
                new UserRepositoriesResourceMapper().mapToRepositoryInfoDTOs(Collections.singleton(repositoryInfo));

        Assertions.assertEquals(createExpectedRepositoryInfoDTO(), result.stream().findFirst().get());
    }

    private RepositoryInfo mockRepositoryInfo() {
        OwnerBasicInfo ownerBasicInfo = new OwnerBasicInfo(LOGIN);
        RepositoryBasicInfo repositoryBasicInfo = new RepositoryBasicInfo(NAME, false, ownerBasicInfo);
        BranchInfo branch1 = new BranchInfo(BRANCH_1_NAME, new CommitInfo(SHA_1));
        BranchInfo branch2 = new BranchInfo(BRANCH_2_NAME, new CommitInfo(SHA_2));
        return new RepositoryInfo(repositoryBasicInfo, Arrays.asList(branch1, branch2));
    }

    private RepositoryInfoDTO createExpectedRepositoryInfoDTO() {
        BranchInfoDTO branch1 = new BranchInfoDTO(BRANCH_1_NAME, SHA_1);
        BranchInfoDTO branch2 = new BranchInfoDTO(BRANCH_2_NAME, SHA_2);
        return new RepositoryInfoDTO(NAME, LOGIN, Arrays.asList(branch1, branch2));
    }
}
