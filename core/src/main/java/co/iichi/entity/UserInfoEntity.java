package co.iichi.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "user_info")
@NamedNativeQueries(
    value = {
            @NamedNativeQuery(
                    name = "UserInfo.ByGoogleUserId",
                    query = "  SELECT" +
                            "      u.user_id" +
                            "    , u.nickname" +
                            "  FROM" +
                            "      user_info u" +
                            "    , google_oauth2 go" +
                            "  WHERE" +
                            "      u.user_id = go.user_id" +
                            "  AND go.google_user_id = :googleUserId",
                    resultClass = UserInfoEntity.class
            )
    }
)
public class UserInfoEntity {
    @Id
    @Column(name = "user_id")
    protected String userId;

    @Column(name = "nickname")
    protected String nickname;
}
