package org.team4u.sql.builder.entity.builder;

import org.team4u.sql.builder.entity.Entity;
import org.team4u.sql.builder.expression.SqlExpression;

/**
 * @author Jay Wu
 */
public class EntitySqlExpressionNameHandler implements SqlExpression.Handler {

    private Entity entity;

    public EntitySqlExpressionNameHandler(Entity entity) {
        this.entity = entity;
    }

    @Override
    public String handleExpression(String expr) {
        return entity.getColumnName(expr);
    }

    @Override
    public String handleOperation(String op) {
        return op;
    }

    @Override
    public <T> T handleValue(T value) {
        return value;
    }
}