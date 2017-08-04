package org.team4u.sql.builder.expression;

/**
 * @author Jay Wu
 */
public abstract class AbstractSqlExpression implements SqlExpression {

    public static final SqlExpression.Handler EMPTY_HANDLER = new SqlExpression.Handler() {
        @Override
        public String handleExpression(String expr) {
            return expr;
        }

        @Override
        public String handleOperation(String op) {
            return op;
        }

        @Override
        public <T> T handleValue(T value) {
            return value;
        }
    };

    protected SqlExpression.Handler handler = EMPTY_HANDLER;

    @Override
    public void setHandler(SqlExpression.Handler handler) {
        this.handler = handler;
    }
}