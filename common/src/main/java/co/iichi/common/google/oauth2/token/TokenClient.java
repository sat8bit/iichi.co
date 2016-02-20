package co.iichi.common.google.oauth2.token;

import co.iichi.common.google.oauth2.token.response.Response;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
public class TokenClient {

    Logger logger = LoggerFactory.getLogger(TokenClient.class);

    @Value("${google.oauth2.token_uri}")
    protected String tokenUri;

    @Value("${google.oauth2.client_id}")
    protected String clientId;

    @Value("${google.oauth2.client_secret}")
    protected String clientSecret;


    protected RestTemplate restTemplate = new RestTemplate();

    public Optional<String> getAccessTokenByCode(String code, String redirectUri) {
        String params = new StringBuilder()
                .append("grant_type=authorization_code")
                .append("&code=").append(code)
                .append("&client_id=").append(clientId)
                .append("&client_secret=").append(clientSecret)
                .append("&redirect_uri=").append(redirectUri)
                .toString();

        logger.info("request url is {}", tokenUri);
        logger.info("request body is {}", params);

        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<String> httpEntity = new HttpEntity<>(params, httpHeaders);

            Response response = restTemplate.postForObject(tokenUri, httpEntity, Response.class);
            logger.info("response is {}", response);

            return Optional.of(response.getAccessToken());
        } catch (HttpClientErrorException e) {
            logger.info("Got HttpClientErrorException. response body is {}", e.getResponseBodyAsString());
            return Optional.empty();
        }
    }
}
