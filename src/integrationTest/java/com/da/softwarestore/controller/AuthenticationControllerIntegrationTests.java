package com.da.softwarestore.controller;

import com.da.softwarestore.common.web.EndPoints;
import com.da.softwarestore.security.jwt.JwtAuthenticationRequest;
import com.da.softwarestore.security.jwt.JwtAuthenticationResponse;
import com.da.softwarestore.security.jwt.JwtTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
public class AuthenticationControllerIntegrationTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Test
    public void shouldReturnValidAuthenticationTokenForValidCredentials() throws Exception {
        final String userName = "user";
        final String password = "user";
        JwtAuthenticationRequest request = new JwtAuthenticationRequest(userName, password);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(request);

        MvcResult mvcResult = mockMvc.perform(post(EndPoints.API + EndPoints.AUTHENTICATION)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.token", notNullValue()))
                .andReturn();

        String responseJson = mvcResult.getResponse().getContentAsString();
        JwtAuthenticationResponse jwtAuthenticationResponse = mapper.readValue(responseJson, JwtAuthenticationResponse.class);

        String usernameFromToken = jwtTokenUtil.getUsernameFromToken(jwtAuthenticationResponse.getToken());
        assertThat(usernameFromToken).isEqualToIgnoringCase(userName);
    }

    @Test
    public void shouldReturn401ForInvalidCredentials() throws Exception {
        final String userName = "user";
        final String password = "user1";
        JwtAuthenticationRequest request = new JwtAuthenticationRequest(userName, password);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(request);

        mockMvc.perform(post(EndPoints.API + EndPoints.AUTHENTICATION)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
