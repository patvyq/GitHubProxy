package com.codechallenge.github.proxy.controller;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

class UserRepositoriesResourceWireMock {

    @Test
    @DisplayName("Return error message and status 406 for endpoint call with accept header other than application/json")
    public void test1() throws Exception {
        WireMockServer wireMockServer = new WireMockServer();
        wireMockServer.start();

        configureFor("localhost", 8080);
        stubFor(get(urlEqualTo("/user/user/repositories/info"))
                .willReturn(aResponse().withBody("{\n" +
                                "    \"status\": 406,\n" +
                                "    \"message\": \"This endpoint only accept application/json format\"\n" +
                                "}")
                        .withHeader("Accept", "")
                        .withStatus(406)));

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet("http://localhost:8080/user/user/repositories/info");
        request.addHeader("Accept", "application/xml");
        HttpResponse httpResponse = httpClient.execute(request);

        Assertions.assertEquals(406, httpResponse.getStatusLine().getStatusCode());
        Assertions.assertEquals("{\n" +
                "    \"status\": 406,\n" +
                "    \"message\": \"This endpoint only accept application/json format\"\n" +
                "}", convertResponseToString(httpResponse));

        verify(getRequestedFor(urlEqualTo("/user/user/repositories/info")));

        wireMockServer.stop();
    }

    @Test
    @DisplayName("Return error message and status 404 for endpoint call with non existing user")
    public void test2() throws Exception {
        WireMockServer wireMockServer = new WireMockServer();
        wireMockServer.start();

        configureFor("localhost", 8080);
        stubFor(get(urlEqualTo("/user/user/repositories/info"))
                .willReturn(aResponse().withBody("{\n" +
                        "    \"status\": 404,\n" +
                        "    \"message\": \"User user do not exist or have no repositories\"\n" +
                        "}").withStatus(404)));

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet("http://localhost:8080/user/user/repositories/info");
		request.addHeader("Accept", "application/json");
        HttpResponse httpResponse = httpClient.execute(request);

        Assertions.assertEquals(404, httpResponse.getStatusLine().getStatusCode());
        Assertions.assertEquals("{\n" +
                "    \"status\": 404,\n" +
                "    \"message\": \"User user do not exist or have no repositories\"\n" +
                "}", convertResponseToString(httpResponse));

        verify(getRequestedFor(urlEqualTo("/user/user/repositories/info")));

        wireMockServer.stop();
    }

    @Test
    @DisplayName("Return correct entity and status 200 for correct endpoint call")
    public void test3() throws Exception {
        WireMockServer wireMockServer = new WireMockServer();
        wireMockServer.start();

        configureFor("localhost", 8080);
        stubFor(get(urlEqualTo("/user/owner/repositories/info"))
                .willReturn(aResponse().withBody("{\n" +
						"\"repositoryName\": \"Repo\",\n" +
						"\"ownerLogin\": \"owner\",\n" +
						"\"branches\": [\n" +
						"{\n" +
						"\"name\": \"develop\",\n" +
						"\"sha\": \"3d6b2b5731e176f24d29fb35ac54be1d580d383d\"\n" +
						"},\n" +
						"{\n" +
						"\"name\": \"master\",\n" +
						"\"sha\": \"2516b38eca7a9a433bd09ddc299898429d461cbc\"\n" +
						"}\n" +
						"]\n" +
						"}").withStatus(200)));

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet("http://localhost:8080/user/owner/repositories/info");
		request.addHeader("Accept", "application/json");
        HttpResponse httpResponse = httpClient.execute(request);

        Assertions.assertEquals(200, httpResponse.getStatusLine().getStatusCode());
        Assertions.assertEquals("{\n" +
				"\"repositoryName\": \"Repo\",\n" +
				"\"ownerLogin\": \"owner\",\n" +
				"\"branches\": [\n" +
				"{\n" +
				"\"name\": \"develop\",\n" +
				"\"sha\": \"3d6b2b5731e176f24d29fb35ac54be1d580d383d\"\n" +
				"},\n" +
				"{\n" +
				"\"name\": \"master\",\n" +
				"\"sha\": \"2516b38eca7a9a433bd09ddc299898429d461cbc\"\n" +
				"}\n" +
				"]\n" +
				"}", convertResponseToString(httpResponse));

        verify(getRequestedFor(urlEqualTo("/user/owner/repositories/info")));

        wireMockServer.stop();
    }

    private String convertResponseToString(HttpResponse response) throws IOException {
        InputStream responseStream = response.getEntity().getContent();
        Scanner scanner = new Scanner(responseStream, "UTF-8");
        String responseString = scanner.useDelimiter("\\Z").next();
        scanner.close();
        return responseString;
    }
}
