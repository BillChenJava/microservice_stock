package com.antra.microservices_stock;

import com.antra.microservices_stock.controller.StockController;
import com.antra.microservices_stock.domain.Quote;
import com.netflix.discovery.converters.Auto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
class MicroservicesStockApplicationTests {

    @Autowired
    private MockMvc stockMockMvc;

    @Test
    void contextLoads() {
        System.out.println("test");
    }

    @Test
    public void getStock() throws Exception{
        stockMockMvc.perform(get("/stock/get/Bin"));
    }

}
