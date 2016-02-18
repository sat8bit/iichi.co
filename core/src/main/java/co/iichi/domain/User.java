package co.iichi.domain;

import co.iichi.common.util.ShortenUtils;
import lombok.Data;
import lombok.Setter;
import lombok.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

@Data
public class User {

    private static final Logger logger = LoggerFactory.getLogger(User.class);

    public User(
            String userId,
            String nickname
    ) {
        this.userId = userId;
        this.nickname = nickname;
    }

    protected String userId;
    protected String nickname;

    public String getNicknameShort() {
        return ShortenUtils.nickname(nickname);
    }
}
