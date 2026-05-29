package com.laberit.Modu.rest.adapter;

import com.laberit.Modu.TestApplication;
import com.laberit.Modu.ports.driving.OrderServicePort;
import com.laberit.Modu.rest.mapper.CartRestMapper;
import com.laberit.Modu.rest.mapper.OrderRestMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
        controllers = OrderRestAdapter.class
)
@ContextConfiguration(classes = TestApplication.class)
class OrderRestAdapterTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderServicePort orderServicePort;

    @MockitoBean
    private OrderRestMapper orderRestMapper;

    @MockitoBean
    private CartRestMapper cartRestMapper;

    @MockitoBean
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Test
    void checkoutCart() {
    }

    @Test
    void getOrder() {
    }
}