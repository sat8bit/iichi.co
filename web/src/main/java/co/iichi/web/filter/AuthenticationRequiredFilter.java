package co.iichi.web.filter;

import co.iichi.SessionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationRequiredFilter extends OncePerRequestFilter {
    Logger logger = LoggerFactory.getLogger(AuthenticationRequiredFilter.class);

    protected SessionHandler sessionHandler;

    @Autowired
    public AuthenticationRequiredFilter(SessionHandler sessionHandler) {
        this.sessionHandler = sessionHandler;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        /**
         * 認証が通ってなかった場合、sessionHandler内でログインページヘの遷移が設定される
         */
        logger.info("access to AuthenticationRequiredFilter");
        if (sessionHandler.handle(request, response)) {
            filterChain.doFilter(request, response);
        }
    }
}
