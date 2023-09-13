package com.azlagor.minelandnews.db;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Timestamp;

public class DataBase {

    static String jdbcUrl = "jdbc:mysql://loscalhost:3306/minecraft";
    static String username = "root";
    static String password = "1337228";
    public static void insert(int post_id, long date)
    {
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            DSLContext create = DSL.using(connection, SQLDialect.MYSQL);
            Timestamp timestamp = new Timestamp(date * 1000);
            create.insertInto(NewsTable.NEWS)
                    .set(NewsTable.NEWS.POST_ID, post_id)
                    .set(NewsTable.NEWS.DATE, timestamp)
                    .execute();
        } catch (Exception e) {

        }
    }
}
