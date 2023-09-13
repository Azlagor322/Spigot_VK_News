package com.azlagor.minelandnews.db;

import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.*;

import java.sql.Timestamp;

public class NewsTable  extends TableImpl<Record>{
    public static final NewsTable NEWS = new NewsTable();
    public final TableField<Record, Integer> ID = createField(DSL.name("id"), SQLDataType.INTEGER.nullable(false).identity(true));
    public final TableField<Record, Integer> POST_ID = createField(DSL.name("postId"), SQLDataType.INTEGER.nullable(false));
    public final TableField<Record, Timestamp> DATE = createField(DSL.name("date"), SQLDataType.TIMESTAMP);

    public NewsTable() {
        super(DSL.name("news"));
    }
}
