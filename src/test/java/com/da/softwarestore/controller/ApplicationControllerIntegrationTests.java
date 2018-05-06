package com.da.softwarestore.controller;

import com.da.softwarestore.common.web.EndPoints;
import com.da.softwarestore.model.software.Category;
import com.da.softwarestore.security.jwt.JwtAuthenticationRequest;
import com.da.softwarestore.security.jwt.JwtAuthenticationResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
public class ApplicationControllerIntegrationTests {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void shouldCreateApplicationForValidFullRequest() throws Exception {
        createApplication("valid_full.zip");
    }

    @Test
    public void shouldCreateApplicationForValidNoSmallPicRequest() throws Exception {
        createApplication("valid_no_small_pic.zip");
    }

    @Test
    public void shouldCreateApplicationForValidNoBigPicRequest() throws Exception {
        createApplication("valid_no_big_pic.zip");
    }

    @Test
    public void shouldCreateApplicationForNoPicsRequest() throws Exception {
        createApplication("valid_no_pics.zip");
    }

    private void createApplication(String s) throws Exception {
        Category category = getCategory();
        MockMultipartFile file = getArchive(s);
        String authToken = getAuthenticationToken();

        // create application
        mockMvc.perform(
                multipart(EndPoints.API + EndPoints.APPLICATIONS)
                        .file(file)
                        .param("categoryId", category.getId().toString())
                        .param("description", "description")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header(tokenHeader, "Bearer " + authToken))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrlPattern("/api/applications/[0-9]+"));
    }

    private MockMultipartFile getArchive(String archiveName) throws URISyntaxException, IOException {
        // get archive
        Path path = Paths.get(getClass().getClassLoader().getResource("archives/" + archiveName).toURI());
        return new MockMultipartFile("file", archiveName, "multipart/form-data", Files.readAllBytes(path));
    }

    private Category getCategory() throws Exception {
        // get category
        MvcResult mvcResult = mockMvc.perform(get(EndPoints.API + EndPoints.CATEGORIES))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.length()", greaterThan(0)))
                .andReturn();

        String responseJson = mvcResult.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<Category> categories = mapper.readValue(responseJson, new TypeReference<List<Category>>() {
        });
        assertThat(categories).isNotEmpty();
        return categories.get(0);
    }

    private String getAuthenticationToken() throws Exception {
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

        return jwtAuthenticationResponse.getToken();
    }

    private static ResultMatcher redirectedUrlPattern(final String expectedUrlPattern) {
        return result -> {
            Pattern pattern = Pattern.compile("\\A" + expectedUrlPattern + "\\z");
            assertThat(pattern.matcher(result.getResponse().getRedirectedUrl()).find()).isTrue();
        };
    }
}
