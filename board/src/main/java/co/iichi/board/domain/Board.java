package co.iichi.board.domain;

import co.iichi.common.util.DateTimeUtils;
import lombok.Value;

import java.util.Date;

@Value
public class Board {
    protected String boardId;
    protected String boardSubject;
    protected Integer commentCount;
    protected String createdByUserId;
    protected Date lastPostedAt;

    public String getLastPostedAtFormatted() {
        return DateTimeUtils.toString(DateTimeUtils.toZonedDateTime(lastPostedAt));
    }
}
