package org.team4u.test;

import org.junit.Assert;
import org.junit.Test;
import org.team4u.sql.builder.Sql;
import org.team4u.sql.builder.content.FileSqlContentManager;
import org.team4u.sql.builder.content.SqlContentManager;
import org.team4u.sql.builder.content.SqlContents;
import org.team4u.sql.builder.content.template.JetbrickSqlTemplate;
import org.team4u.sql.builder.util.SqlBuilders;

import java.util.Arrays;

/**
 * @author Jay Wu
 */
public class SqlContentManagerTest {

    @Test
    public void loadAndRemove() {
        SqlContentManager manager = new FileSqlContentManager("test.sql");

        String sqlContent = manager.get("t1");
        Assert.assertEquals(2, manager.size());
        Assert.assertEquals("[jet, t1]", manager.keys().toString());
        Assert.assertEquals("SELECT *\n" +
                "FROM client\n" +
                "WHERE name = :name;", sqlContent);

        manager.remove("t1");
        Assert.assertEquals(1, manager.size());
    }

    @Test
    public void sqlKey() {
        SqlContents.getInstance().register(new FileSqlContentManager("test.sql").watch(5));

        Sql sql = SqlBuilders.sqlKey("t1").setParameter("name", "x").create();

        Assert.assertEquals("SELECT *\n" +
                "FROM client\n" +
                "WHERE name = ?", sql.getContent());
        Assert.assertEquals("[x]", Arrays.toString(sql.getParams()));
    }

    @Test
    public void sqlWithJetbrick() {
        SqlContents.getInstance().setSqlTemplate(new JetbrickSqlTemplate());
        SqlContents.getInstance().register(new FileSqlContentManager("test.sql").watch(5));

        Sql sql = SqlBuilders.sqlKey("jet")
                .setParameter("x", "x")
                .create();

        Assert.assertEquals("SELECT *\n" +
                "FROM client\n" +
                "WHERE 1 = 1\n" +
                "AND x = ?\n", sql.getContent());
        Assert.assertEquals("[x]", Arrays.toString(sql.getParams()));
    }
}