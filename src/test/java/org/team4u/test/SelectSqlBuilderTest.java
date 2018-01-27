package org.team4u.test;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Dict;
import org.junit.Assert;
import org.junit.Test;
import org.team4u.sql.builder.Sql;
import org.team4u.sql.builder.util.SqlBuilders;

import java.util.Arrays;

import static org.team4u.sql.builder.expression.SqlExpressions.*;

/**
 * @author Jay Wu
 */
public class SelectSqlBuilderTest {

    @Test
    public void create() {
        Sql sql = SqlBuilders.select("test t1")
                .from("test2 t2")
                .column("t1.z1")
                .distinct()
                .leftJoin("test3 t3 on t3.id = t2.id")
                .where("a", "=", 1)
                .and("b", "=", 2)
                .and(in("b2", CollectionUtil.newArrayList(1, 2)))
                .and(Dict.create().set("(>)x", "x1").set("y", "y2"))
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
                .or("z", "=", null)
                .orderBy("d")
                .orderBy("e", false)
                .create();

        Assert.assertEquals("select distinct t1.z1 from test t1, test2 t2 " +
                "left join test3 t3 on t3.id = t2.id " +
                "where a = ? and b = ? and b2 in (?, ?) and x > ? and y = ? " +
                "and ((a = ? and b = ?) or (c = ? and d = ?)) " +
                "or c = ? or z = null " +
                "order by d, e desc", sql.getContent());
        Assert.assertEquals("[1, 2, 1, 2, x1, y2, 1, 2, 3, 4, 3]", Arrays.toString(sql.getParams()));
    }
}