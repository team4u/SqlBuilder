package org.team4u.sql.builder.content;


import org.team4u.kit.core.lang.Registry;

/**
 * @author Jay Wu
 */
public class SqlContents extends Registry<String, SqlContentManager> {

    private static final SqlContents instance = new SqlContents();

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
}