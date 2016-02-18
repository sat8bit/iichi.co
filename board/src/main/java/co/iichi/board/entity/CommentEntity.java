package co.iichi.board.entity;

import co.iichi.core.entity.UserInfoEntity;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "comment")
public class CommentEntity {

    @EmbeddedId
    protected PrimaryKey primaryKey;

    @Column(name = "posted_by_user_id")
    protected String postedByUserId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "posted_at")
    protected Date postedAt;

    @Column(name = "body")
    protected String body;

    @JoinColumn(name = "posted_by_user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    protected UserInfoEntity userInfoEntity;

    @Data
    public static class PrimaryKey implements Serializable {
        @Column(name = "board_id")
        protected String boardId;
        @Column(name = "comment_seq")
        protected Integer commentSeq;

        public PrimaryKey() {
        }

        public PrimaryKey(String boardId, Integer commentSeq) {
            this.boardId = boardId;
            this.commentSeq = commentSeq;
        }
    }
}
