package com.laberit.Modu.rest.adapter;

import com.laberit.Modu.domain.model.Cart;
import com.laberit.Modu.domain.model.CartItem;
import com.laberit.Modu.domain.model.response.CartWithAllChecks;
import com.laberit.Modu.ports.driving.CartItemServicePort;
import com.laberit.Modu.ports.driving.CartServicePort;
import com.laberit.Modu.ports.driving.command.AddCartItemCommand;
import com.laberit.Modu.rest.generated.model.CartResponse;
import com.laberit.Modu.rest.generated.model.CartUpdatedAtResponse;
import com.laberit.Modu.rest.generated.model.ValidatedCartResponse;
import com.laberit.Modu.rest.mapper.CartItemRestMapper;
import com.laberit.Modu.rest.mapper.CartRestMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartRestAdapter.class)
@ContextConfiguration(classes = {CartRestAdapter.class})
class CartRestAdapterTest {

    private static final String DEVICE_ID = "testdevice12345678";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CartServicePort cartServicePort;

    @MockitoBean
    private CartItemServicePort cartItemServicePort;

    @MockitoBean
    private CartRestMapper cartMapper;

    @MockitoBean
    private CartItemRestMapper cartItemMapper;

    @BeforeEach
    void setUpAuth() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(DEVICE_ID, null, List.of()));
    }

    @AfterEach
    void clearAuth() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void initializeCart_shouldReturn201WithCartResponse() throws Exception {
        Cart cart = Cart.builder().deviceId(DEVICE_ID).cartItems(List.of()).build();
        CartResponse response = new CartResponse();
        response.setDeviceId(DEVICE_ID);

        when(cartServicePort.initializeCart(DEVICE_ID)).thenReturn(cart);
        when(cartMapper.toCartResponse(cart)).thenReturn(response);

        mockMvc.perform(put("/cart"))
                .andExpect(status().isCreated());
    }

    @Test
    void getValidatedCart_shouldReturn200WithValidatedCartResponse() throws Exception {
        Cart cart = Cart.builder().deviceId(DEVICE_ID).cartItems(List.of()).build();
        CartWithAllChecks checks = new CartWithAllChecks(cart, List.of(), List.of(), List.of());
        ValidatedCartResponse response = new ValidatedCartResponse();

        when(cartServicePort.getCartWithAllChecks(DEVICE_ID)).thenReturn(checks);
        when(cartMapper.toValidatedCartResponse(checks)).thenReturn(response);

        mockMvc.perform(get("/cart"))
                .andExpect(status().isOk());
    }

    @Test
    void addCartItem_shouldReturn200WithCartResponse() throws Exception {
        Cart cart = Cart.builder().deviceId(DEVICE_ID).cartItems(List.of()).build();
        AddCartItemCommand command = new AddCartItemCommand(DEVICE_ID, 1L, 2);
        CartResponse response = new CartResponse();

        when(cartMapper.toAddCartItemCommand(eq(DEVICE_ID), any())).thenReturn(command);
        when(cartItemServicePort.addCartItemToCart(command)).thenReturn(cart);
        when(cartMapper.toCartResponse(cart)).thenReturn(response);

        mockMvc.perform(post("/cart/addItem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"variant_id\": 1, \"quantity\": 2}"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteCartItem_shouldReturn200WithCartResponse() throws Exception {
        Cart cart = Cart.builder().deviceId(DEVICE_ID).cartItems(List.of()).build();
        CartResponse response = new CartResponse();

        doNothing().when(cartItemServicePort).deleteCartItemById(DEVICE_ID, 5L);
        when(cartServicePort.findCartByDeviceId(DEVICE_ID)).thenReturn(cart);
        when(cartMapper.toCartResponse(cart)).thenReturn(response);

        mockMvc.perform(delete("/cart/items/5"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteCartItems_shouldReturn200WithCartResponse() throws Exception {
        Cart cart = Cart.builder().deviceId(DEVICE_ID).cartItems(List.of()).build();
        CartResponse response = new CartResponse();

        doNothing().when(cartItemServicePort).deleteAllCartItems(DEVICE_ID);
        when(cartServicePort.findCartByDeviceId(DEVICE_ID)).thenReturn(cart);
        when(cartMapper.toCartResponse(cart)).thenReturn(response);

        mockMvc.perform(delete("/cart/items"))
                .andExpect(status().isOk());
    }

    @Test
    void getCartUpdatedAt_shouldReturn200() throws Exception {
        Instant now = Instant.now();
        CartUpdatedAtResponse response = new CartUpdatedAtResponse();

        when(cartServicePort.getCartUpdatedAt(DEVICE_ID)).thenReturn(now);
        when(cartMapper.toCartUpdatedAtResponse(now)).thenReturn(response);

        mockMvc.perform(get("/cart/cartUpdatedAt"))
                .andExpect(status().isOk());
    }

    @Test
    void updateCart_shouldReturn200WithValidatedCartResponse() throws Exception {
        Cart cart = Cart.builder().deviceId(DEVICE_ID).cartItems(List.of()).build();
        CartWithAllChecks checks = new CartWithAllChecks(cart, List.of(), List.of(), List.of());
        ValidatedCartResponse response = new ValidatedCartResponse();

        when(cartMapper.toCartFromCartUpdateRequest(any())).thenReturn(cart);
        when(cartServicePort.updateCart(any())).thenReturn(checks);
        when(cartMapper.toValidatedCartResponse(checks)).thenReturn(response);

        mockMvc.perform(patch("/cart/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"shippingCosts\": 0.0, \"cartItems\": []}"))
                .andExpect(status().isOk());
    }

    @Test
    void updateCartItemsQuantities_shouldReturn200WithCartResponse() throws Exception {
        Cart cart = Cart.builder().deviceId(DEVICE_ID).cartItems(List.of()).build();
        CartResponse response = new CartResponse();

        when(cartItemMapper.toUpdateCartItemQuantityCommandList(any())).thenReturn(List.of());
        when(cartServicePort.updateCartItemsQuantities(eq(DEVICE_ID), any())).thenReturn(cart);
        when(cartMapper.toCartResponse(cart)).thenReturn(response);

        mockMvc.perform(patch("/cart/updateCartItemsQuantities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cartItems\": []}"))
                .andExpect(status().isOk());
    }
}
