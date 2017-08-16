package org.team4u.sql.builder.expression;

import org.team4u.sql.builder.builder.AbstractTableSqlBuilder;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * <p>
 * Most SqlExpressions accept a SQL expression and one or more values to which the
 * SQL is compared. Predicates do not escape this expression. As such, do not
 * accept arbitrary expressions from users or other sources, as it may be a
 * source of SQL injection vulnerabilities. The normal use-case is that these
 * expressions are hard-coded in your application. Values, on the other hand,
 * are substituted as proper prepared statement parameters so they are safe
 * from SQL injection.
 *
 * @author Jay Wu
 */
public final class SqlExpressions {
    /**
     * Joins a series of predicates with AND.
     */
    public static SqlExpression and(SqlExpression... sqlExpressions) {
        return join("and", Arrays.asList(sqlExpressions));
    }


    /**
     * Joins a series of predicates with AND.
     */
    public static SqlExpression and(List<SqlExpression> sqlExpressions) {
        return join("and", sqlExpressions);
    }

    public static SqlExpression and(final String expr, final String op, final Object value) {
        return and(exps(expr, op, value));
    }

    /**
     * Adds an equals clause to a creator.
     *
     * @param expr  SQL expression to be compared for equality.
     * @param value Value to which the SQL expression is compared.
     */
    public static SqlExpression exps(final String expr, final String op, final Object value) {
        return new AbstractSqlExpression() {
            private String param;

            public void init(AbstractTableSqlBuilder creator) {
                param = creator.allocateParameterName();
                if (value == null) {
                    creator.setPlaceHolder(param, null);
                } else {
                    creator.setParameter(param, handler.handleValue(value));
                }
            }

            public String toSql() {
                return String.format("%s %s %s%s",
                        handler.handleExpression(expr),
                        handler.handleOperation(op),
                        value == null ? "$" : ":",
                        param);
            }
        };
    }

    /**
     * Adds an IN clause to a creator.
     *
     * @param expr   SQL expression to be tested for inclusion.
     * @param values Values for the IN clause.
     */
    public static SqlExpression in(final String expr, final List<?> values) {

        return new AbstractSqlExpression() {

            private String sql;

            @Override
            public void init(AbstractTableSqlBuilder creator) {

                StringBuilder sb = new StringBuilder();
                sb.append(handler.handleExpression(expr)).append(" in (");

                boolean first = true;
                for (Object value : handler.handleValue(values)) {
                    String param = creator.allocateParameterName();
                    creator.setParameter(param, value);
                    if (!first) {
                        sb.append(", ");
                    }
                    sb.append(":").append(param);
                    first = false;
                }

                sb.append(")");

                sql = sb.toString();

            }

            @Override
            public String toSql() {
                return sql;
            }
        };
    }

    /**
     * Adds an IN clause to a creator.
     *
     * @param expr   SQL expression to be tested for inclusion.
     * @param values Values for the IN clause.
     */
    public static SqlExpression in(final String expr, final Object... values) {
        return in(expr, Arrays.asList(values));
    }

    /**
     * Returns a predicate that takes no parameters. The given SQL expression is
     * used directly.
     *
     * @param sql SQL text of the expression
     */
    public static SqlExpression sql(final String sql) {
        return new AbstractSqlExpression() {
            public String toSql() {
                return sql;
            }

            public void init(AbstractTableSqlBuilder creator) {
            }
        };
    }

    /**
     * Inverts the sense of the given child predicate. In SQL terms, this
     * surrounds the given predicate with "not (...)".
     *
     * @param childSqlExpression Predicate whose sense is to be inverted.
     */
    public static SqlExpression not(final SqlExpression childSqlExpression) {
        return new AbstractSqlExpression() {
            public void init(AbstractTableSqlBuilder creator) {
                childSqlExpression.setHandler(handler);
                childSqlExpression.init(creator);
            }

            public String toSql() {
                return "not (" + childSqlExpression.toSql() + ")";
            }
        };
    }

    /**
     * Joins a series of predicates with OR.
     */
    public static SqlExpression or(SqlExpression... sqlExpressions) {
        return join("or", Arrays.asList(sqlExpressions));
    }

    /**
     * Joins a series of predicates with OR.
     */
    public static SqlExpression or(List<SqlExpression> sqlExpressions) {
        return join("or", sqlExpressions);
    }

    public static SqlExpression or(final String expr, final String op, final Object value) {
        return or(exps(expr, op, value));
    }

    public static SqlExpression and(SqlExpression sqlExpression) {
        return wrap(sqlExpression, "and ", null);
    }

    public static SqlExpression or(SqlExpression sqlExpression) {
        return wrap(sqlExpression, "or ", null);
    }

    public static SqlExpression wrap(final SqlExpression sqlExpression, final String start, final String end) {
        return new AbstractSqlExpression() {
            public void init(AbstractTableSqlBuilder creator) {
                sqlExpression.init(creator);
            }

            public String toSql() {
                sqlExpression.setHandler(handler);
                String sql = sqlExpression.toSql();
                if (start != null) {
                    sql = start + sql;
                }

                if (end != null) {
                    sql = sql + end;
                }

                return sql;
            }
        };
    }

    /**
     * Factory for 'and' and 'or' predicates.
     */
    private static SqlExpression join(final String joinWord, final List<SqlExpression> preds) {
        return new AbstractSqlExpression() {
            public void init(AbstractTableSqlBuilder creator) {
                for (SqlExpression p : preds) {
                    p.setHandler(handler);
                    p.init(creator);
                }
            }

            public String toSql() {
                StringBuilder sb = new StringBuilder()
                        .append("(");
                boolean first = true;
                for (SqlExpression p : preds) {
                    if (!first) {
                        sb.append(" ").append(joinWord).append(" ");
                    }
                    sb.append(p.toSql());
                    first = false;
                }
                return sb.append(")").toString();
            }
        };
    }
}