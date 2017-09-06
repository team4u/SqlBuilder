package org.team4u.sql.builder.content;


import org.team4u.kit.core.lang.Registry;
import org.team4u.sql.builder.content.template.EmptySqlTemplate;
import org.team4u.sql.builder.content.template.SqlTemplate;

/**
 * @author Jay Wu
 */
public class SqlContents extends Registry<String, SqlContentManager> {

    private static final SqlContents instance = new SqlContents();

    private SqlTemplate sqlTemplate = new EmptySqlTemplate();

    public static SqlContents getInstance() {
        return instance;
    }

    public String getSqlContent(String key) {
        for (SqlContentManager sqlContentManager : applicants.values()) {
            String content = sqlContentManager.get(key);
            if (content != null) {
                return content;
            }
        }

        return null;
    }

    public SqlTemplate getSqlTemplate() {
        return sqlTemplate;
    }

    public SqlContents setSqlTemplate(SqlTemplate sqlTemplate) {
        this.sqlTemplate = sqlTemplate;
        return this;
    }
}