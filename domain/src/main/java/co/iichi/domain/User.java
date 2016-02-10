package co.iichi.domain;

import lombok.Data;
import lombok.Setter;
import lombok.Value;

@Data
public class User {

    public User(
            String userId,
            String nickname
    ) {
        this.userId = userId;
        this.nickname = nickname;
    }

    protected String userId;
    protected String nickname;
}
