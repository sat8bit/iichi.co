package co.iichi.common.google.oauth2.userinfo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Response {

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
