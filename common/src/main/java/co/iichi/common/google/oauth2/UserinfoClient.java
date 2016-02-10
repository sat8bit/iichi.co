package co.iichi.common.google.oauth2;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
public class UserinfoClient {
    Logger logger = LoggerFactory.getLogger(UserinfoClient.class);

    @Value("${google.oauth2.userinfo_uri}")
    protected String userinfoUri;

    protected RestTemplate restTemplate = new RestTemplate();

    public Optional<Response> getUserInfoByAccessToken(String accessToken) {
        String url = new StringBuilder()
                .append(userinfoUri)
                .append("?access_token=").append(accessToken)
                .toString();

        logger.info("request url is {}", url);

        try {
            Response response = restTemplate.getForObject(url, Response.class);
            logger.info("response is {}", response);

            return Optional.of(response);
        } catch (HttpClientErrorException e) {
            logger.info("Got HttpClientErrorException. response body is {}", e.getResponseBodyAsString());
            return Optional.empty();
        }
    }

    @Data
    public static class Response {

        @JsonProperty(value = "id")
        private String id;

        @JsonProperty(value = "email")
        private String email;

        @JsonProperty(value = "verified_email")
        private Boolean verifiedEmail;

        @JsonProperty(value = "name")
        private String name;

        @JsonProperty(value = "given_name")
        private String givenName;

        @JsonProperty(value = "family_name")
        private String familyName;

        @JsonProperty(value = "link")
        private String link;

        @JsonProperty(value = "gender")
        private String gender;

        @JsonProperty(value = "locale")
        private String locale;
    }
}
