package co.iichi.common.google.oauth2.token.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Response {
    @JsonProperty(value = "access_token")
    private String accessToken;

    @JsonProperty(value = "token_type")
    private String tokenType;

    @JsonProperty(value = "expires_in")
    private String expiresIn;

    @JsonProperty(value = "id_token")
    private String idToken;
}
