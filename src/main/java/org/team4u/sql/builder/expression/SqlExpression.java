package org.team4u.sql.builder.expression;

import org.team4u.sql.builder.builder.AbstractTableSqlBuilder;

/**
 * Component in the where clause of a {@link AbstractTableSqlBuilder}.
 *
 * @author Jay Wu
 */
public interface SqlExpression {

    /**
     * Initializes the predicate. For example, this may allocate one or more
     * parameters from the creator and set values for the parameters. This is
     * called by the creator when the predicate is added to it.
     */
    void init(AbstractTableSqlBuilder creator);

    void setHandler(Handler handler);

    /**
     * Returns an SQL expression representing the predicate. Parameters may be
     * included preceded by a colon.
     */
    String toSql();

    interface Handler {

        String handleExpression(String expr);

        String handleOperation(String op);

        <T> T handleValue(T value);
    }
}