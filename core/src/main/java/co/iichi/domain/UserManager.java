package co.iichi.domain;

import co.iichi.common.exception.IichicoException;
import co.iichi.common.exception.InternalServerErrorException;
import co.iichi.entity.CoreEntityManager;
import co.iichi.entity.GoogleOAuth2Entity;
import co.iichi.entity.UserInfoEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserManager {

    private static final Logger logger = LoggerFactory.getLogger(UserManager.class);

    public Optional<User> getUserByUserId(String userId) {
        EntityManager entityManager = CoreEntityManager.INSTANCE.createEntityManager();

        try {
            UserInfoEntity userInfoEntity = entityManager.find(UserInfoEntity.class, userId);

            if (userInfoEntity == null) {
                return Optional.empty();
            }

            return Optional.of(new User(userInfoEntity.getUserId(), userInfoEntity.getNickname()));
        } finally {
            entityManager.close();
        }
    }

    public void update(User user) throws InternalServerErrorException {
        EntityManager entityManager = CoreEntityManager.INSTANCE.createEntityManager();

        try {
            EntityTransaction entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();

            UserInfoEntity userInfoEntity = entityManager.find(UserInfoEntity.class, user.getUserId(), LockModeType.PESSIMISTIC_WRITE);
            if (userInfoEntity == null) {
                throw new InternalServerErrorException();
            }

            userInfoEntity.setNickname(user.getNickname());
            entityTransaction.commit();
        } finally {
            entityManager.close();
        }
    }

    public User getUserByGoogleUserId(String googleUserId) throws IichicoException {
        EntityManager entityManager = CoreEntityManager.INSTANCE.createEntityManager();

        try {
            // DBからUserを探してみる
            Query query = entityManager.createNamedQuery("UserInfo.ByGoogleUserId");
            query.setParameter("googleUserId", googleUserId);
            List resultList = query.getResultList();

            // DBにUserがいたら
            if (!resultList.isEmpty()) {
                UserInfoEntity userInfoEntity = (UserInfoEntity) resultList.get(0);
                return new User(userInfoEntity.getUserId(), userInfoEntity.getNickname());
            }

            logger.info("create new user by google oauth2 start.");
            // 新規登録
            EntityTransaction entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();

            UserInfoEntity userInfoEntity = createNewUserInfoEntity();
            entityManager.persist(userInfoEntity);
            GoogleOAuth2Entity googleOAuth2Entity = new GoogleOAuth2Entity();
            googleOAuth2Entity.setGoogleUserId(googleUserId);
            googleOAuth2Entity.setUserId(userInfoEntity.getUserId());
            entityManager.persist(googleOAuth2Entity);

            entityTransaction.commit();

            return new User(userInfoEntity.getUserId(), userInfoEntity.getNickname());
        } finally {
            entityManager.close();
        }
    }

    protected UserInfoEntity createNewUserInfoEntity() {
        UserInfoEntity userInfoEntity = new UserInfoEntity();
        userInfoEntity.setUserId(UUID.randomUUID().toString());
        userInfoEntity.setNickname("Anonymous");
        return userInfoEntity;
    }
}
