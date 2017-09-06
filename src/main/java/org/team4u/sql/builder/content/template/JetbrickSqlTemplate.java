package org.team4u.sql.builder.content.template;

import jetbrick.template.JetEngine;
import jetbrick.template.JetTemplate;

import java.io.StringWriter;
import java.util.Map;

/**
 * @author Jay Wu
 */
public class JetbrickSqlTemplate implements SqlTemplate {

    protected JetEngine engine = JetEngine.create();

    @Override
    public String render(String sqlContent, Map<String, Object> parameterMap) {
        JetTemplate template = engine.createTemplate(sqlContent);
        StringWriter sw = new StringWriter();
        template.render(parameterMap, sw);
        return sw.toString();
    }
}