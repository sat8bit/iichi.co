package co.iichi.board.entity;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Created by sat8bit on 2016/02/01.
 */
public class CoreEntityManager {
    public static final EntityManagerFactory INSTANCE = Persistence.createEntityManagerFactory("coreUnit");

    private CoreEntityManager() {}
}
