package org.team4u.sql.builder.builder;


import org.team4u.sql.builder.expression.SqlExpression;
import org.team4u.sql.builder.expression.SqlExpressions;
import org.team4u.sql.builder.util.WhereUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Jay Wu
 */
public abstract class WhereSqlBuilder extends AbstractTableSqlBuilder {

    protected List<String> wheres = new ArrayList<String>();

    public WhereSqlBuilder() {
    }

    public WhereSqlBuilder(String... tables) {
        super(tables);
    }

    public WhereSqlBuilder(WhereSqlBuilder other) {
        super(other);
        wheres.addAll(other.wheres);
    }

    public WhereSqlBuilder where(String expr) {
        wheres.add(expr);
        return this;
    }

    public WhereSqlBuilder where(SqlExpression sqlExpression) {
        sqlExpression.init(this);
        where(sqlExpression.toSql());
        return this;
    }

    public WhereSqlBuilder and(SqlExpression sqlExpression) {
        where(SqlExpressions.and(sqlExpression));
        return this;
    }

    public WhereSqlBuilder or(SqlExpression sqlExpression) {
        where(SqlExpressions.or(sqlExpression));
        return this;
    }

    public WhereSqlBuilder where(String column, String op, Object value) {
        where(SqlExpressions.exps(column, op, value));
        return this;
    }

    public WhereSqlBuilder whereIfNotNull(String column, String op, Object value) {
        if (value == null) {
            return this;
        }

        where(column, op, value);
        return this;
    }

    public WhereSqlBuilder and(String column, String op, Object value) {
        where(SqlExpressions.and(column, op, value));
        return this;
    }

    public WhereSqlBuilder andIfNotNull(String column, String op, Object value) {
        if (value == null) {
            return this;
        }

        and(column, op, value);
        return this;
    }

    public WhereSqlBuilder or(String column, String op, Object value) {
        where(SqlExpressions.or(column, op, value));
        return this;
    }

    public WhereSqlBuilder orIfNotNull(String column, String op, Object value) {
        if (value == null) {
            return this;
        }

        or(column, op, value);
        return this;
    }

    public WhereSqlBuilder and(Map<String, ?> params) {
        for (SqlExpression sqlExpression : WhereUtil.toSqlExpressions(params)) {
            and(sqlExpression);
        }

        return this;
    }

    public WhereSqlBuilder or(Map<String, ?> params) {
        for (SqlExpression sqlExpression : WhereUtil.toSqlExpressions(params)) {
            or(sqlExpression);
        }

        return this;
    }

    public List<String> getWheres() {
        return wheres;
    }
}