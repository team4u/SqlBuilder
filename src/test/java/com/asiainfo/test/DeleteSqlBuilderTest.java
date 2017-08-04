package com.asiainfo.test;

import org.junit.Assert;
import org.junit.Test;
import org.team4u.sql.builder.Sql;
import org.team4u.sql.builder.builder.DeleteSqlBuilder;

import java.util.Arrays;

/**
 * @author Jay Wu
 */
public class DeleteSqlBuilderTest {

    @Test
    public void create() {
        Sql sql = new DeleteSqlBuilder("test")
                .where("a", "=", 1)
                .and("b", "=", 2)
                .or("c", "=", 3)
                .create();

        Assert.assertEquals("delete from test where a = ? and b = ? or c = ?", sql.getContent());
        Assert.assertEquals("[1, 2, 3]", Arrays.toString(sql.getParams()));
    }

    @Test
    public void createWithSqlExpression() {
        Sql sql = new DeleteSqlBuilder("test")
                .where(or(
                        and(
                                exps("a", "=", 1),
                                exps("b", "=", 2)
                        ),
                        and(
                                exps("c", "=", 3),
                                exps("d", "=", 4))
                        )
                )
                .create();

        Assert.assertEquals("delete from test where ((a = ? and b = ?) or (c = ? and d = ?))", sql.getContent());
        Assert.assertEquals("[1, 2, 3, 4]", Arrays.toString(sql.getParams()));
    }
}
