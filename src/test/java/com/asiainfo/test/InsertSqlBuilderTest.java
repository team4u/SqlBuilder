package com.asiainfo.test;

import org.junit.Assert;
import org.junit.Test;
import org.team4u.sql.builder.Sql;
import org.team4u.sql.builder.builder.InsertSqlBuilder;

import java.util.Arrays;

/**
 * @author Jay Wu
 */
public class InsertSqlBuilderTest {

    @Test
    public void create() {
        Sql sql = new InsertSqlBuilder("test")
                .setValue("a", 1)
                .setValue("b", 2)
                .create();

        Assert.assertEquals("insert into test (a, b) values (?, ?)", sql.getContent());
        Assert.assertEquals("[1, 2]", Arrays.toString(sql.getParams()));
    }
}
