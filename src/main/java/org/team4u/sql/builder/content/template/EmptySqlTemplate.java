package org.team4u.sql.builder.content.template;

import java.util.Map;

/**
 * @author Jay Wu
 */
public class EmptySqlTemplate implements SqlTemplate {
    @Override
    public String render(String sqlContent, Map<String, Object> parameterMap) {
        return sqlContent;
    }
}