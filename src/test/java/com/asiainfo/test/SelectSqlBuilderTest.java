package com.asiainfo.test;

import com.xiaoleilu.hutool.util.CollectionUtil;
import org.junit.Assert;
import org.junit.Test;
import org.team4u.sql.builder.Sql;
import org.team4u.sql.builder.util.SqlBuilders;

import java.util.Arrays;

/**
 * @author Jay Wu
 */
public class SelectSqlBuilderTest {

    @Test
    public void create() {
        Sql sql = SqlBuilders.select("test")
                .from("test2")
                .column("z1")
                .distinct()
                .leftJoin("test3 test3.id = test2.id")
                .where("a", "=", 1)
                .and("b", "=", 2)
                .and(in("b2", CollectionUtil.newArrayList(1, 2)))
                .and(or(
                        and(
                                exps("a", "=", 1),
                                exps("b", "=", 2)
                        ),
                        and(
                                exps("c", "=", 3),
                                exps("d", "=", 4))
                        )
                )
                .orIfNotNull("c", "=", 3)
                .orIfNotNull("c", "=", null)
                .orderBy("d")
                .orderBy("e", false)
                .create();

        Assert.assertEquals("select distinct z1 from test, test2 " +
                "left join test3 test3.id = test2.id " +
                "where a = ? and b = ? and b2 in (?, ?) " +
                "and ((a = ? and b = ?) or (c = ? and d = ?)) " +
                "or c = ? " +
                "order by d, e desc", sql.getContent());
        Assert.assertEquals("[1, 2, 1, 2, 1, 2, 3, 4, 3]", Arrays.toString(sql.getParams()));
    }
}