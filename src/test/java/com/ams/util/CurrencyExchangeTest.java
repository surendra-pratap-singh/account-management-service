package com.ams.util;

import com.ams.exception.InternalServerErrorException;
import com.ams.exception.InvalidCurrencyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@PropertySource("classpath:application.properties")
class CurrencyExchangeTest {

    @Mock
    private RestTemplate restTemplateMock;

    @InjectMocks
    private CurrencyExchange currencyExchange;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        currencyExchange = new CurrencyExchange(restTemplateMock);
        ReflectionTestUtils.setField(currencyExchange, "EXCHANGE_API_KEY", "62205db3096c091233d56eac");
        ReflectionTestUtils.setField(currencyExchange, "EXCHANGE_API_URL", "https://v6.exchangerate-api.com/v6/");
    }

    @Test
    void testGetExchangedCurrencySuccess() {
        String fromCurrency = "USD";
        String toCurrency = "EUR";
        BigDecimal amount = BigDecimal.TEN;
        String url = "https://v6.exchangerate-api.com/v6/62205db3096c091233d56eac/pair/USD/EUR/10";
        String responseString = "{\"conversion_result\": 8.5}";

        lenient().when(restTemplateMock.getForObject(url, String.class)).thenReturn(responseString);

        BigDecimal exchangedCurrency = currencyExchange.getExchangedCurrency(fromCurrency, toCurrency, amount);

        assertEquals(new BigDecimal("8.5"), exchangedCurrency);
    }

    @Test
    void testGetExchangedCurrencyError() {
        String fromCurrency = "USD";
        String toCurrency = "EUR";
        BigDecimal amount = BigDecimal.TEN;
        String url = "https://v6.exchangerate-api.com/v6/62205db3096c091233d56eac/pair/USD/EUR/10";

        when(restTemplateMock.getForObject(url, String.class)).thenThrow(new RuntimeException("Test exception"));

        InternalServerErrorException thrown = assertThrows(
                InternalServerErrorException.class,
                () -> currencyExchange.getExchangedCurrency(fromCurrency, toCurrency, amount),
                "Exchange service not available, Please try after some time"
        );
        assertTrue(thrown.getMessage().contains("Exchange service not available, Please try after some time"));
    }
}
