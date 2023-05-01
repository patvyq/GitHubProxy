package com.codechallenge.github.proxy.controller;

import com.codechallenge.github.proxy.controller.dto.RepositoryInfoDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserRepositoriesResourceIT {

    @Autowired
    private TestRestTemplate template;

    @Test
    public void test() {
        HttpHeaders headers = new HttpHeaders();
        headers.remove("Accept");
        headers.add("Accept", "application/json");
        ResponseEntity<RepositoryInfoDTO[]> response = template.exchange("/user/dsfsdfsdfs/repositories/info",
                HttpMethod.GET, new HttpEntity<>(headers), RepositoryInfoDTO[].class);
        Assertions.assertEquals(0, response.getBody().length);
    }
}
