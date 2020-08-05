package com.antra.microservices_stock.controller;

import com.antra.microservices_stock.domain.Quote;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/stock")
public class StockController {
    @Autowired
    RestTemplate restTemplate;

    private YahooFinance yahooFinance;

    public StockController(){
        this.yahooFinance = new YahooFinance();
    }

    @HystrixCommand(fallbackMethod = "backupCall")
    @RequestMapping(value = "/get/{username}")
    public List<Quote> getStock(@PathVariable("username") final String username){
        /**
         * restTemplate.exchange(
         *         String url,
         *         HttpMethod method,
         *         HttpEntity requestEntity,
         *         Class responseType,
         *         Object uriVariables[]
         *     )
         */
        ResponseEntity<List<String>> quoteResponse = restTemplate.exchange("http://quote/quote/get/"+username, HttpMethod.GET,null,
                new ParameterizedTypeReference<List<String>>(){});

        List<String> quotes = quoteResponse.getBody();

        return quotes.stream().map(quote->{
            Stock stock = getStockPrice(quote);
            return new Quote(quote,stock.getQuote().getPrice());
        }).collect(Collectors.toList());

    }

    private Stock getStockPrice(String quote) {
        try {
            return YahooFinance.get(quote);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  new Stock(quote);
    }


    public List<Quote> backupCall(String abc){
        Quote quote = new Quote("NON",new BigDecimal("0"));
        List list = new ArrayList();
        list.add(quote);
        return list;
    }
}
