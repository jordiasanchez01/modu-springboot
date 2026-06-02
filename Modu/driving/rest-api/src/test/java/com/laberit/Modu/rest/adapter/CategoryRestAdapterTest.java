package com.laberit.Modu.rest.adapter;


import com.laberit.Modu.domain.model.Category;
import com.laberit.Modu.ports.driving.CategoryServicePort;
import com.laberit.Modu.rest.generated.model.HomeCategoryResponse;
import com.laberit.Modu.rest.mapper.CategoryRestMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryRestAdapter.class)
@ContextConfiguration(classes = {CategoryRestAdapter.class})
public class CategoryRestAdapterTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    CategoryServicePort servicePort;

    @MockitoBean
    CategoryRestMapper mapper;

    @Test
    void getCategories_shouldReturnEmptyListWhenNoCategoriesExist() throws Exception{
        // given
        when(servicePort.findAll()).thenReturn(List.of());
        when(mapper.toHomeCategoryResponseList(List.of())).thenReturn(List.of());

        // when - then
        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getCategories_shouldReturnCategoriesWhenCategoriesExist() throws Exception{
        // given
        Category category1 = Category.builder().id(1).name("Activewear").build();
        Category category2 = Category.builder().id(2).name("Streetwear").build();

        HomeCategoryResponse category1Response = new HomeCategoryResponse("Activewear");
        HomeCategoryResponse category2Response = new HomeCategoryResponse("Streetwear");

        when(servicePort.findAll()).thenReturn(List.of(category1, category2));
        when(mapper.toHomeCategoryResponseList(List.of(category1, category2))).thenReturn(List.of(category1Response, category2Response));

        // when - then
        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Activewear"))
                .andExpect(jsonPath("$[1].name").value("Streetwear"));
    }
}
