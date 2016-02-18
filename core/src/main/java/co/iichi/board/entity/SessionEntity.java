package co.iichi.board.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "session")
public class SessionEntity {
    @Id
    @Column(name = "session_id")
    protected String sessionId;

    @Column(name = "user_id")
    protected String userId;
}
