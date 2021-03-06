package org.egov.workflow.persistence.repository;

import org.egov.workflow.web.contract.*;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import static org.junit.Assert.*;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class UserRepositoryTest {

    private static final String HOST = "http://host";
    private static final String USER_BY_USERID = "/pgr/_search";

    private MockRestServiceServer server;
    private UserRepository userRepository;

    @Before
    public void before() {
        final RestTemplate restTemplate = new RestTemplate();
        userRepository = new UserRepository(restTemplate, HOST, USER_BY_USERID);
        server = MockRestServiceServer.bindTo(restTemplate).build();
    }

    @Test
    public void findUserById() throws Exception {

        final RequestInfo requestInfo = RequestInfo.builder()
                .apiId("org.egov.pgr")
                .ver("1.0")
                .msgId("654654")
                .authToken("cdaaa28a-f88a-429e-a710-b401097a165c")
                .requesterId("67")
                .build();

        final User user = User.builder()
                .id(67l)
                .build();

        server.expect(once(), requestTo("http://host/pgr/_search"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().string(new Resources().getFileContents("searchUserByIdRequest.json")))
                .andRespond(withSuccess(new Resources().getFileContents("searchUserById.json"),
                        MediaType.APPLICATION_JSON_UTF8));

       final GetUserByIdResponse userResponse =  userRepository.findUserById(67L);
       server.verify();
       assertEquals(1,userResponse.getUser().size());

    }

}