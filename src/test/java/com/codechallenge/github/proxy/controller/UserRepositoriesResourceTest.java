package com.codechallenge.github.proxy.controller;

import com.codechallenge.github.proxy.controller.client.GitHubApiClient;
import com.codechallenge.github.proxy.controller.dto.RepositoryInfoDTO;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.Collections;

public class UserRepositoriesResourceTest {

    @Mock
    private UserRepositoriesService service;

    @InjectMocks
    private UserRepositoriesResource resource;

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
    @DisplayName("Throw ResponseStatusException when accept media type is other than application/json")
    public void test1() {
        Mockito.when(service.getUserRepositoriesInfo(Mockito.anyString()))
                .thenReturn(Collections.emptyList());

        ResponseStatusException thrown = Assertions.assertThrows(ResponseStatusException.class, () -> {
            resource.getUserRepositoriesInfo("test", "application/xml");
        });

        Assertions.assertEquals("This endpoint only accept application/json format", thrown.getReason());
    }

    @Test
    @DisplayName("Return standard result for media type application/json")
    public void test2() {
        Mockito.when(service.getUserRepositoriesInfo(Mockito.anyString()))
                .thenReturn(Collections.emptyList());

        Collection<RepositoryInfoDTO> result =
                resource.getUserRepositoriesInfo("test", "application/json");

        Assertions.assertEquals(0, result.size());
    }
}
