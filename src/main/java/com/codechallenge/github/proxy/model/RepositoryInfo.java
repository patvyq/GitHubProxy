package com.codechallenge.github.proxy.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collection;

@Getter
@AllArgsConstructor
public class RepositoryInfo {

    private RepositoryBasicInfo basicInfo;
    private Collection<BranchInfo> branches;
}
