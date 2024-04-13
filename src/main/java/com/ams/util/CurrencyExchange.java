package com.ams.util;

import com.ams.exception.ResourceNotFoundException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Component
public class CurrencyExchange {

    @Value("${currency.exchange.key:62205db3096c091233d56eac}")
    private String EXCHANGE_API_KEY;

    @Value("${currency.exchange.url:https://v6.exchangerate-api.com/v6/}")
    private String EXCHANGE_API_URL;

    @Autowired
    @Lazy
    private RestTemplate restTemplate;

    private static final Logger log = LoggerFactory.getLogger(CurrencyExchange.class.getSimpleName());

    public BigDecimal getExchangedCurrency(String fromCurrency, String toCurrency, BigDecimal amount) {
        log.info("API KEY: {}", EXCHANGE_API_KEY);
        String url = buildUrl(fromCurrency, toCurrency, amount);

        String responseString = restTemplate.getForObject(url, String.class);

        if (responseString != null) {
            log.info("responseString: " + responseString);
            JsonObject jsonObject = JsonParser.parseString(responseString).getAsJsonObject();
            return jsonObject.get("conversion_result").getAsBigDecimal();
        } else {
            throw new ResourceNotFoundException("Not Found");
        }
    }

    private String buildUrl(String fromCurrency, String toCurrency, BigDecimal amount){
        return EXCHANGE_API_URL + EXCHANGE_API_KEY + "/pair/" + fromCurrency + "/" + toCurrency + "/" + amount;
    }

}
