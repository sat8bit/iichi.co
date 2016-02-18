package co.iichi.board.entity;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Created by sat8bit on 2016/02/18.
 */
public class BoardEntityManager {
    public static final EntityManagerFactory INSTANCE = Persistence.createEntityManagerFactory("boardUnit");

    private BoardEntityManager() {}

}
