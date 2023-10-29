package com.daviddlc.microservices.productservice;

import com.daviddlc.microservices.productservice.dto.ProductRequest;
import com.daviddlc.microservices.productservice.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Testcontainers
@SpringBootTest
class ProductServiceApplicationTests {

    @Container
    static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:5.7");

    private final MockMvc mockMvc;

    private final ProductRepository productRepository;

    private final ObjectMapper objectMapper;

    @Autowired
    ProductServiceApplicationTests(MockMvc mockMvc, ProductRepository productRepository, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.productRepository = productRepository;
        this.objectMapper = objectMapper;
    }

    static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.datasource.username", mySQLContainer::getUsername);
        dynamicPropertyRegistry.add("spring.datasource.password", mySQLContainer::getPassword);
    }

    @Test
    void shouldCreateProduct() throws Exception {
        productRepository.deleteAll();
        ProductRequest productRequest = getProductRequest();

        String productRequestString = objectMapper.writeValueAsString(productRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productRequestString))
                .andExpect(status().isCreated());

        Assertions.assertEquals(1, productRepository.findAll().size());
    }

    private ProductRequest getProductRequest() {
        return ProductRequest.builder()
                .name("iPhone 15")
                .description("iPhone 15")
                .price(BigDecimal.valueOf(1300))
                .build();
    }
}
