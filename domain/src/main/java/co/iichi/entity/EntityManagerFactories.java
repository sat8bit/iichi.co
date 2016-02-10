package co.iichi.entity;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Created by sat8bit on 2016/02/01.
 */
public class EntityManagerFactories {

    public static final EntityManagerFactory IICHICO_MYSQL_UNIT = Persistence.createEntityManagerFactory("iichicoMysqlUnit");

    private EntityManagerFactories() {}
}
