package com.codechallenge.github.proxy.controller.dto;

import lombok.*;

import java.util.Collection;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class RepositoryInfoDTO {

    private String repositoryName;
    private String ownerLogin;
    private Collection<BranchInfoDTO> branches;

    @Getter
    @Setter
    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BranchInfoDTO {
        private String name;
        private String sha;
    }
}
