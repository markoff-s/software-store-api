package com.da.softwarestore.controller;

import com.da.softwarestore.common.web.EndPoints;
import com.da.softwarestore.configuration.WebSecurityConfig;
import com.da.softwarestore.model.software.Category;
import com.da.softwarestore.service.CategoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = CategoryController.class, secure = false) // have to turn off security even for unsecured endpoints https://github.com/spring-projects/spring-boot/issues/6514
public class CategoryControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Test
    public void shouldReturnCategories() throws Exception {
        List<Category> categories = asList(new Category("cat1"), new Category("cat2"));
        when(categoryService.getAll()).thenReturn(categories);

        mockMvc.perform(get(EndPoints.API + EndPoints.CATEGORIES))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$.[0].name", is("cat1")))
                .andExpect(jsonPath("$.[1].name", is("cat2")));
    }
}
