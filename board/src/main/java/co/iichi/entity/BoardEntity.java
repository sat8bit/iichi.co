package co.iichi.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "board")
public class BoardEntity {
    @Id
    @Column(name = "board_id")
    protected String boardId;

    @Column(name = "board_subject")
    protected String boardSubject;

    @Column(name = "comment_count")
    protected Integer commentCount;

    @Column(name = "created_by_user_id")
    protected String createdByUserId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_posted_at")
    protected Date lastPostedAt;
}
