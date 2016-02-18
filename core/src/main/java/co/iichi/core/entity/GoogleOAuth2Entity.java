package co.iichi.core.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "google_oauth2")
public class GoogleOAuth2Entity {
    @Id
    @Column(name = "google_user_id")
    protected String googleUserId;

    @Column(name = "user_id")
    protected String userId;
}
