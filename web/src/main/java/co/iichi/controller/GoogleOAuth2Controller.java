package co.iichi.controller;


import co.iichi.SessionHandler;
import co.iichi.common.exception.IichicoException;
import co.iichi.common.exception.InternalServerErrorException;
import co.iichi.common.google.oauth2.TokenClient;
import co.iichi.common.google.oauth2.UserinfoClient;
import co.iichi.domain.User;
import co.iichi.domain.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.UUID;

@RequestMapping(path = "google")
@Controller
public class GoogleOAuth2Controller {

    Logger logger = LoggerFactory.getLogger(GoogleOAuth2Controller.class);

    @Value("${google.oauth2.endpoint}")
    protected String endpoint;

    @Value("${google.oauth2.redirect_path}")
    protected String redirectPath;

    @Value("${google.oauth2.client_id}")
    protected String clientId;

    @Value("${google.oauth2.scope}")
    protected String scope;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected SessionHandler sessionHandler;

    @Autowired
    TokenClient tokenClient;

    @Autowired
    UserinfoClient userinfoClient;


    @RequestMapping("oauth2")
    public View oauth(HttpServletRequest httpServletRequest) {
        return new RedirectView(
                new StringBuilder()
                        .append(endpoint)
                        .append("?response_type=code")
                        .append("&client_id=").append(clientId)
                        .append("&redirect_uri=").append(getRedirectUri(httpServletRequest))
                        .append("&scope=").append(scope)
                        .append("&state=").append(UUID.randomUUID().toString())
                        .toString());
    }

    @RequestMapping("oauth2callback")
    public View oauth2callback(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            @RequestParam("state") String state,
            @RequestParam("code") String code
    ) throws IichicoException {
        logger.info("oauth2callback state is {}, code is {}", state, code);

        // Access Token の取得
        Optional<String> accessToken = tokenClient.getAccessTokenByCode(code, getRedirectUri(httpServletRequest));
        if (!accessToken.isPresent()) {
            logger.info("can not get access token.");
            throw new InternalServerErrorException();
        }

        // UserInfoの取得
        Optional<UserinfoClient.Response> googleUserInfo = userinfoClient.getUserInfoByAccessToken(accessToken.get());
        if (!googleUserInfo.isPresent()) {
            logger.info("can not get user info.");
            throw new InternalServerErrorException();
        }
        logger.info("userInfo is {}", googleUserInfo.get());

        // Userの取得
        User user = userRepository.getUserByGoogleUserId(googleUserInfo.get().getId());
        logger.info("login user is {}", user);
        sessionHandler.write(httpServletResponse, user);

        return new RedirectView("/");
    }

    protected String getRedirectUri(HttpServletRequest httpServletRequest) {
        StringBuilder redirectUri = new StringBuilder()
                .append(httpServletRequest.getScheme())
                .append("://")
                .append(httpServletRequest.getServerName());

        if (httpServletRequest.getServerPort() != 80) {
            redirectUri.append(":").append(httpServletRequest.getServerPort());
        }

        return redirectUri.append(redirectPath).toString();
    }
}
