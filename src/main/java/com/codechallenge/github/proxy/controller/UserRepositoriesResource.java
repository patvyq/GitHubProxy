package com.codechallenge.github.proxy.controller;

import com.codechallenge.github.proxy.controller.dto.RepositoryInfoDTO;
import com.codechallenge.github.proxy.controller.mapper.UserRepositoriesResourceMapper;
import com.codechallenge.github.proxy.model.RepositoryInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;

@RestController
@RequestMapping("/user")
@Tag(name = "Users repo")
public class UserRepositoriesResource {

    @Autowired
    private UserRepositoriesService service;

    @GetMapping("/{name}/repositories/info")
    @Operation(summary = "Gets all non fork repos for given user.")
    public Collection<RepositoryInfoDTO> getUserRepositoriesInfo(@PathVariable String name,
                                                                 @RequestHeader(HttpHeaders.ACCEPT) String accept) {

        checkAcceptHeaderCorrectness(accept);
        service.checkUserExistence(name);

        Collection<RepositoryInfo> repositoriesInfo = service.getUserRepositoriesInfo(name);
        return new UserRepositoriesResourceMapper().mapToRepositoryInfoDTOs(repositoriesInfo);
    }

    private void checkAcceptHeaderCorrectness(String accept) {
        if (!MediaType.APPLICATION_JSON.toString().equals(accept)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE, "This endpoint only accept application/json format");
        }
    }
}
