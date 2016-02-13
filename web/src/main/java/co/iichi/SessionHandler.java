package co.iichi;

import co.iichi.domain.User;
import co.iichi.domain.UserManager;
import co.iichi.entity.EntityManagerFactories;
import co.iichi.entity.SessionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
public class SessionHandler {

    private static final String COOKIE_NAME_SESSION_ID = "b";

    private static final String ATTRIBUTE_NAME_USER = "userId";

    @Autowired
    protected UserManager userManager;

    public Boolean handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {

        Optional<User> user = getUser(httpServletRequest);
        if (!user.isPresent()) {
            httpServletResponse.sendRedirect("/login");
            return false;
        }

        httpServletRequest.setAttribute(ATTRIBUTE_NAME_USER, user.get());
        return true;
    }

    public Optional<User> getUser(HttpServletRequest httpServletRequest) {
        User user = (User)httpServletRequest.getAttribute(ATTRIBUTE_NAME_USER);

        // すでにSessionHandlerを通して設定している可能性があるので先にチェック
        if (user != null) {
            return Optional.of(user);
        }

        Optional<String> sessionId = getSessionId(httpServletRequest);
        if (!sessionId.isPresent()) {
            return Optional.empty();
        }

        Optional<String> userId = getUserId(sessionId.get());
        if (!userId.isPresent()) {
            return Optional.empty();
        }

        return userManager.getUserByUserId(userId.get());
    }

    protected Optional<String> getSessionId(HttpServletRequest httpServletRequest) {
        Cookie[] cookies = httpServletRequest.getCookies();

        if (cookies == null) {
            return Optional.empty();
        }

        for(Cookie cookie : cookies) {
            if (COOKIE_NAME_SESSION_ID.equals(cookie.getName())) {
                return Optional.of(cookie.getValue());
            }
        }
        return Optional.empty();
    }

    protected Optional<String> getUserId(String sessionId) {
        EntityManager entityManager = EntityManagerFactories.IICHICO_MYSQL_UNIT.createEntityManager();

        try {
            SessionEntity sessionEntity = entityManager.find(SessionEntity.class, sessionId);

            if (sessionEntity == null) {
                return Optional.empty();
            }

            return Optional.of(sessionEntity.getUserId());
        } finally {
            entityManager.close();
        }
    }

    public void write(HttpServletResponse httpServletResponse, User user) {

        EntityManager entityManager = EntityManagerFactories.IICHICO_MYSQL_UNIT.createEntityManager();
        try {
            EntityTransaction entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();
            SessionEntity sessionEntity = new SessionEntity();
            sessionEntity.setSessionId(UUID.randomUUID().toString());
            sessionEntity.setUserId(user.getUserId());
            entityManager.persist(sessionEntity);
            entityTransaction.commit();

            Cookie cookie = new Cookie(COOKIE_NAME_SESSION_ID, sessionEntity.getSessionId());
            cookie.setPath("/");
            httpServletResponse.addCookie(cookie);
        } finally {
            entityManager.close();
        }
    }

    public void remove(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse
    ) {
        Cookie cookies[] = httpServletRequest.getCookies();

        if (cookies == null) {
            return;
        }

        for (Cookie cookie : cookies) {
            if (COOKIE_NAME_SESSION_ID.equals(cookie.getName())) {
                cookie.setMaxAge(0);
                cookie.setPath("/");
                httpServletResponse.addCookie(cookie);
                return;
            }
        }
    }
}
