package org.team4u.test;

import com.xiaoleilu.hutool.lang.Dict;
import org.junit.Assert;
import org.junit.Test;
import org.team4u.sql.builder.Sql;
import org.team4u.sql.builder.dialect.GenericDialect;
import org.team4u.sql.builder.entity.Entity;
import org.team4u.sql.builder.entity.annotation.ActionType;
import org.team4u.sql.builder.entity.invoker.ActionInvoker;
import org.team4u.sql.builder.entity.invoker.ActionInvokerManager;
import org.team4u.sql.builder.expression.SqlExpressions;
import org.team4u.sql.builder.util.SqlBuilders;
import org.team4u.test.model.SingleIdEntity;
import org.team4u.test.model.TwoIdEntity;

import java.util.Arrays;

/**
 * @author Jay Wu
 */
public class EntityManagerTest {

    private SingleIdEntity createA() {
        return new SingleIdEntity().setId("1").setName("a").setNameAndAge("b");
    }

    @Test
    public void select() {
        Sql sql = SqlBuilders.select(SingleIdEntity.class)
                .where("name", "=", 1)
                .andIfNotNull("name", "=", null)
                .and(SqlExpressions.and(
                        SqlExpressions.exps("nameAndAge", ">", 1),
                        SqlExpressions.exps("nameAndAge", "<", 3)
                ))
                .or(Dict.create().set("(=)nameAndAge", "x1"))
                .create();

        String expectContent = "select * from test where name1 = ? " +
                "and (name_and_age > ? and name_and_age < ?) " +
                "or name_and_age = ?";
        Assert.assertEquals(expectContent, sql.getContent());
        Assert.assertEquals("[1, 1, 3, x1]", Arrays.toString(sql.getParams()));

        GenericDialect dialect = new GenericDialect();
        Assert.assertEquals("select count(1) from (" + expectContent + ") count_table",
                dialect.createCountSelect(sql.getContent()));
        Assert.assertEquals(expectContent + " limit 1 offset 0",
                dialect.createPageSelect(sql.getContent(), 1, 0));

        sql = SqlBuilders.select(SingleIdEntity.class).withPK("1").create();
        Assert.assertEquals("select * from test where id = ?", sql.getContent());
        Assert.assertEquals("[1]", Arrays.toString(sql.getParams()));

        sql = SqlBuilders.select(TwoIdEntity.class).withPK("1", "2").create();
        Assert.assertEquals("select * from test2 where id1 = ? and id2 = ?", sql.getContent());
        Assert.assertEquals("[1, 2]", Arrays.toString(sql.getParams()));
    }

    @Test
    public void update() {
        Sql sql = SqlBuilders.update(SingleIdEntity.class)
                .setValue("name", 1)
                .setValueIfNotNull("name", null)
                .where("nameAndAge", "=", 2)
                .create();

        String expectContent = "update test set name1 = ? where name_and_age = ?";
        Assert.assertEquals(expectContent, sql.getContent());
        Assert.assertEquals("[1, 2]", Arrays.toString(sql.getParams()));

        SingleIdEntity a = createA();
        sql = SqlBuilders.update(a).column("name").create();

        Assert.assertEquals("update test set name1 = ? where id = ?", sql.getContent());
        Assert.assertEquals("[a, 1]", Arrays.toString(sql.getParams()));

        a.setNameAndAge(null);
        sql = SqlBuilders.update(a).setUpdateIgnoreNull(true).create();

        Assert.assertEquals("update test set name1 = ? where id = ?", sql.getContent());
        Assert.assertEquals("[a, 1]", Arrays.toString(sql.getParams()));

        sql = SqlBuilders.update(
                new TwoIdEntity()
                        .setId1("1")
                        .setId2("2")
                        .setName("b"))
                .create();

        Assert.assertEquals("update test2 set name = ? where id1 = ? and id2 = ?", sql.getContent());
    }

    @Test
    public void insert() {
        Sql sql = SqlBuilders.insert(SingleIdEntity.class).setValue("name", 1).create();

        Assert.assertEquals("insert into test (name1) values (?)", sql.getContent());
        Assert.assertEquals("[1]", Arrays.toString(sql.getParams()));

        SingleIdEntity a = createA();
        sql = SqlBuilders.insert(a).create();
        Assert.assertEquals("insert into test (id, name_and_age, name1) values (?, ?, ?)", sql.getContent());
        Assert.assertEquals("[1, b, a]", Arrays.toString(sql.getParams()));

        a.setId(null);
        sql = SqlBuilders.insert(a).create();
        Assert.assertEquals("insert into test (id, name_and_age, name1) values (?, ?, ?)", sql.getContent());
        Assert.assertNotNull(a.getId());
        Assert.assertEquals("[" + a.getId() + ", b, a]", Arrays.toString(sql.getParams()));

        a.setNameAndAge(null);
        sql = SqlBuilders.insert(a).setInsertIgnoreNull(true).create();
        Assert.assertEquals("insert into test (id, name1) values (?, ?)", sql.getContent());
        Assert.assertEquals("[" + a.getId() + ", a]", Arrays.toString(sql.getParams()));
    }

    @Test
    public void delete() {
        Sql sql = SqlBuilders.delete(SingleIdEntity.class).where("nameAndAge", "=", 2).create();
        Assert.assertEquals("delete from test where name_and_age = ?", sql.getContent());
        Assert.assertEquals("[2]", Arrays.toString(sql.getParams()));

        final SingleIdEntity a = createA();

        final boolean[] invoke = {false};
        ActionInvokerManager.getInstance().register(new ActionInvoker() {
            @Override
            public void invoke(Entity entity, Object context, ActionType actionType) {
                invoke[0] = true;
                Assert.assertEquals(entity.getClassRef(), SingleIdEntity.class);
                Assert.assertEquals(a, context);
                Assert.assertEquals(ActionType.BEFORE_DELETE, actionType);
            }

            @Override
            public String getKey() {
                return "test";
            }
        });

        sql = SqlBuilders.delete(a).create();
        Assert.assertEquals("delete from test where id = ?", sql.getContent());
        Assert.assertEquals("[1]", Arrays.toString(sql.getParams()));
        Assert.assertTrue(invoke[0]);

        sql = SqlBuilders.delete(
                new TwoIdEntity()
                        .setId1("1")
                        .setId2("2")
                        .setName("b"))
                .create();

        Assert.assertEquals("delete from test2 where id1 = ? and id2 = ?", sql.getContent());
    }
}