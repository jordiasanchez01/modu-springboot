package com.laberit.Modu.rest.adapter;

import com.laberit.Modu.TestApplication;
import com.laberit.Modu.domain.model.Cart;
import com.laberit.Modu.domain.model.CartItem;
import com.laberit.Modu.domain.model.Order;
import com.laberit.Modu.domain.model.response.CartWithAllChecks;
import com.laberit.Modu.domain.model.response.CheckoutResult;
import com.laberit.Modu.domain.model.response.InsufficientStockResult;
import com.laberit.Modu.ports.driving.OrderServicePort;
import com.laberit.Modu.rest.generated.model.CheckoutResponse;
import com.laberit.Modu.rest.generated.model.OrderResponse;
import com.laberit.Modu.rest.generated.model.ValidatedCartResponse;
import com.laberit.Modu.rest.mapper.CartRestMapper;
import com.laberit.Modu.rest.mapper.OrderRestMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrderRestAdapter.class)
@ContextConfiguration(classes = TestApplication.class)
@WithMockUser(username = "testdevice12345678")
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

    private static final String CHECKOUT_BODY =
            "{\"isPaid\":true,\"specialInstructions\":\"Leave at door\",\"shippingCosts\":0.0,\"cartToOrder\":{\"cart_items\":[]}}";

    @Test
    void checkoutCart_shouldReturn201_whenOrderIsPlaced() throws Exception {
        Cart cart = Cart.builder().deviceId("testdevice12345678").cartItems(List.of()).build();
        CartWithAllChecks validCart = new CartWithAllChecks(cart, List.of(), List.of(), List.of());
        Order order = Order.builder().id(1L).build();
        CheckoutResult placedResult = new CheckoutResult(order, validCart);
        CheckoutResponse response = new CheckoutResponse().orderPlaced(true);

        when(orderRestMapper.toAddOrderCommand(any())).thenReturn(null);
        when(orderServicePort.addOrder(any(), any())).thenReturn(placedResult);
        when(orderRestMapper.toOrderResponse(any())).thenReturn(new OrderResponse());

        mockMvc.perform(post("/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CHECKOUT_BODY))
                .andExpect(status().isCreated());
    }

    @Test
    void checkoutCart_shouldReturn409_whenCartHasConflicts() throws Exception {
        CartItem item = CartItem.builder().id(1L).productVariantId(5L).build();
        Cart cart = Cart.builder().deviceId("testdevice12345678")
                .cartItems(List.of(item)).build();
        InsufficientStockResult alert = new InsufficientStockResult(5L, 5, 3);
        CartWithAllChecks invalidCart = new CartWithAllChecks(cart, List.of(), List.of(alert), List.of());
        Order order = Order.builder().id(1L).build();
        CheckoutResult conflictResult = new CheckoutResult(order, invalidCart);

        when(orderRestMapper.toAddOrderCommand(any())).thenReturn(null);
        when(orderServicePort.addOrder(any(), any())).thenReturn(conflictResult);
        when(cartRestMapper.toValidatedCartResponse(any())).thenReturn(new ValidatedCartResponse());

        mockMvc.perform(post("/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CHECKOUT_BODY))
                .andExpect(status().isConflict());
    }

    @Test
    void getOrder_shouldReturn200WithOrderResponse() throws Exception {
        Order order = Order.builder().id(1L).build();

        when(orderServicePort.findOrderById(1L)).thenReturn(order);
        when(orderRestMapper.toOrderResponse(order)).thenReturn(new OrderResponse());

        mockMvc.perform(get("/get_order/1"))
                .andExpect(status().isOk());
    }
}
