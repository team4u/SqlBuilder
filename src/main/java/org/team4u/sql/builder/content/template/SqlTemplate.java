package org.team4u.sql.builder.content.template;

import java.util.Map;

/**
 * @author Jay Wu
 */
public interface SqlTemplate {

    String render(String sqlContent, Map<String, Object> parameterMap);
}
