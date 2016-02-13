package co.iichi.domain;

import co.iichi.common.util.DateTimeUtils;
import co.iichi.common.util.ShortenUtils;
import lombok.Value;

import java.util.Date;

@Value
public class Comment {
    protected String boardId;
    protected Integer commentSeq;
    protected String postedByUserId;
    protected String postedByUserNickname;
    protected String body;
    protected Date postedAt;

    public String getPostedByUserNicknameShorted() {
        return ShortenUtils.nickname(postedByUserNickname);
    }

    public String getPostedAtFormatted() {
        return DateTimeUtils.toString(DateTimeUtils.toZonedDateTime(postedAt));
    }
}
