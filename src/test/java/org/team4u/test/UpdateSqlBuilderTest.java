package org.team4u.test;

import org.junit.Assert;
import org.junit.Test;
import org.team4u.sql.builder.Sql;
import org.team4u.sql.builder.builder.UpdateSqlBuilder;

import java.util.Arrays;

/**
 * @author Jay Wu
 */
public class UpdateSqlBuilderTest {

    @Test
    public void create() {
        Sql sql = new UpdateSqlBuilder("test")
                .setValue("a", 1)
                .setValue("b", 2)
                .where("c", "=", 3)
                .and("d", "=", 4)
                .or("e", "=", 5)
                .create();

        Assert.assertEquals("update test set a = ?, b = ? where c = ? and d = ? or e = ?", sql.getContent());
        Assert.assertEquals("[1, 2, 3, 4, 5]", Arrays.toString(sql.getParams()));
    }
}