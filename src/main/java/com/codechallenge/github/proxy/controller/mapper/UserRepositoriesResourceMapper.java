package com.codechallenge.github.proxy.controller.mapper;

import com.codechallenge.github.proxy.controller.dto.RepositoryInfoDTO;
import com.codechallenge.github.proxy.controller.dto.RepositoryInfoDTO.BranchInfoDTO;
import com.codechallenge.github.proxy.model.BranchInfo;
import com.codechallenge.github.proxy.model.RepositoryInfo;
import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserRepositoriesResourceMapper {

    private ModelMapper modelMapper = new ModelMapper();

    public Collection<RepositoryInfoDTO> mapToRepositoryInfoDTOs(Collection<RepositoryInfo> repositoriesInfo) {
        createRepositoryToRepositoryDTOTypeMapping();

        return repositoriesInfo.stream()
                .map(repo -> this.modelMapper.map(repo, RepositoryInfoDTO.class))
                .collect(Collectors.toList());
    }

    private void createRepositoryToRepositoryDTOTypeMapping() {
        TypeMap<RepositoryInfo, RepositoryInfoDTO> repositoryMapper =
                this.modelMapper.createTypeMap(RepositoryInfo.class, RepositoryInfoDTO.class);
        repositoryMapper.addMapping(repo -> repo.getBasicInfo().getName(), RepositoryInfoDTO::setRepositoryName);
        repositoryMapper.addMapping(repo -> repo.getBasicInfo().getOwner().getLogin(), RepositoryInfoDTO::setOwnerLogin);
        repositoryMapper.addMappings(mapper -> mapper.using(new BranchesListConverter())
                .map(RepositoryInfo::getBranches, RepositoryInfoDTO::setBranches));
    }

    private static class BranchesListConverter extends AbstractConverter<List<BranchInfo>, List<BranchInfoDTO>> {
        @Override
        protected List<BranchInfoDTO> convert(List<BranchInfo> branches) {

            return branches.stream()
                    .map(branch -> new BranchInfoDTO(branch.getName(), branch.getCommit().getSha()))
                    .collect(Collectors.toList());
        }
    }
}
