package com.ams.util;

import com.ams.exception.InternalServerErrorException;
import com.ams.exception.ResourceNotFoundException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.swing.text.Utilities;
import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Slf4j
public class CurrencyExchange {

    @Value("${currency.exchange.key}")
    private String EXCHANGE_API_KEY;

    @Value("${currency.exchange.url}")
    private String EXCHANGE_API_URL;

    @Lazy
    private final RestTemplate restTemplate;

    public BigDecimal getExchangedCurrency(String fromCurrency, String toCurrency, BigDecimal amount) {
        log.trace("Request for currency exchange");
        String url = buildUrl(fromCurrency, toCurrency, amount);

        try{
            String responseString = restTemplate.getForObject(url, String.class);
            if (responseString != null) {
                JsonObject jsonObject = JsonParser.parseString(responseString).getAsJsonObject();
                return jsonObject.get("conversion_result").getAsBigDecimal();
            } else {
                log.error("Exchange service not available");
                throw new InternalServerErrorException("Exchange service not available, Please try after some time");
            }
        }catch (Exception e){
            log.error(e.getMessage());
            throw new InternalServerErrorException("Exchange service not available, Please try after some time");
        }

    }

    private String buildUrl(String fromCurrency, String toCurrency, BigDecimal amount){
        return EXCHANGE_API_URL + EXCHANGE_API_KEY + "/pair/" + fromCurrency + "/" + toCurrency + "/" + amount;
    }

}
