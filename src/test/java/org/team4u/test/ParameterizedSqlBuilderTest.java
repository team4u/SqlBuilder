package org.team4u.test;

import org.junit.Assert;
import org.junit.Test;
import org.team4u.sql.builder.Sql;
import org.team4u.sql.builder.util.SqlBuilders;

import java.util.Arrays;

/**
 * @author Jay Wu
 */
public class ParameterizedSqlBuilderTest {

    @Test
    public void createWithContent() {
        Sql sql = SqlBuilders.sql("select * from a = :a and b = :b $c")
                .setParameter("a", "1")
                .setParameter("b", "2")
                .setPlaceHolder("c", "and c = 1")
                .create();

        Assert.assertEquals("select * from a = ? and b = ? and c = 1", sql.getContent());
        Assert.assertEquals("[1, 2]", Arrays.toString(sql.getParams()));
    }
}
