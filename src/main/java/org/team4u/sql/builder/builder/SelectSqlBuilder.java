package org.team4u.sql.builder.builder;


import org.team4u.sql.builder.expression.SqlExpression;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A SelectSqlCreator that you can use like a SelectSqlCreator.
 *
 * @author Jay Wu
 */
public class SelectSqlBuilder extends WhereSqlBuilder {

    protected boolean distinct;

    protected List<Object> columns = new ArrayList<Object>();

    protected List<String> joins = new ArrayList<String>();

    protected List<String> leftJoins = new ArrayList<String>();

    protected List<String> groupBys = new ArrayList<String>();

    protected List<String> havings = new ArrayList<String>();

    protected List<SelectSqlBuilder> unions = new ArrayList<SelectSqlBuilder>();

    protected List<String> orderBys = new ArrayList<String>();

    protected boolean forUpdate;

    protected boolean noWait;

    public SelectSqlBuilder(String... tables) {
        super(tables);
    }

    public SelectSqlBuilder(SelectSqlBuilder other) {
        super(other);

        this.distinct = other.distinct;
        this.forUpdate = other.forUpdate;
        this.noWait = other.noWait;

        for (Object column : other.columns) {
            if (column instanceof SelectSqlBuilder) {
                this.columns.add(new SelectSqlBuilder((SelectSqlBuilder) column));
            } else {
                this.columns.add(column);
            }
        }

        this.tables.addAll(other.tables);
        this.joins.addAll(other.joins);
        this.leftJoins.addAll(other.leftJoins);
        this.wheres.addAll(other.wheres);
        this.groupBys.addAll(other.groupBys);
        this.havings.addAll(other.havings);

        for (SelectSqlBuilder creator : other.unions) {
            this.unions.add(new SelectSqlBuilder(creator));
        }

        this.orderBys.addAll(other.orderBys);
    }

    @Override
    public SelectSqlBuilder where(SqlExpression sqlExpression) {
        super.where(sqlExpression);
        return this;
    }

    @Override
    public SelectSqlBuilder and(SqlExpression sqlExpression) {
        super.and(sqlExpression);
        return this;
    }

    @Override
    public SelectSqlBuilder or(SqlExpression sqlExpression) {
        super.or(sqlExpression);
        return this;
    }

    @Override
    public SelectSqlBuilder where(String column, String op, Object value) {
        super.where(column, op, value);
        return this;
    }

    @Override
    public SelectSqlBuilder and(String column, String op, Object value) {
        super.and(column, op, value);
        return this;
    }

    @Override
    public SelectSqlBuilder andIfNotNull(String column, String op, Object value) {
        super.andIfNotNull(column, op, value);
        return this;
    }

    @Override
    public SelectSqlBuilder or(String column, String op, Object value) {
        super.or(column, op, value);
        return this;
    }

    @Override
    public SelectSqlBuilder orIfNotNull(String column, String op, Object value) {
        super.orIfNotNull(column, op, value);
        return this;
    }

    @Override
    public SelectSqlBuilder andIf(String column, String op, Object value, boolean cnd) {
        super.andIf(column, op, value, cnd);
        return this;
    }

    @Override
    public SelectSqlBuilder orIf(String column, String op, Object value, boolean cnd) {
        super.orIf(column, op, value, cnd);
        return this;
    }

    public SelectSqlBuilder and(Map<String, ?> params) {
        super.and(params);
        return this;
    }

    public SelectSqlBuilder or(Map<String, ?> params) {
        super.or(params);
        return this;
    }

    public SelectSqlBuilder and(String expr) {
        return where(expr);
    }

    public SelectSqlBuilder column(String name) {
        columns.add(name);
        return this;
    }

    public SelectSqlBuilder column(SelectSqlBuilder subSelect) {
        columns.add(subSelect);
        return this;
    }

    public SelectSqlBuilder column(String name, boolean groupBy) {
        columns.add(name);
        if (groupBy) {
            groupBys.add(name);
        }
        return this;
    }

    public SelectSqlBuilder distinct() {
        this.distinct = true;
        return this;
    }

    public SelectSqlBuilder forUpdate() {
        forUpdate = true;
        return this;
    }

    public SelectSqlBuilder from(String table) {
        tables.add(table);
        return this;
    }

    public List<SelectSqlBuilder> getUnions() {
        return unions;
    }

    public SelectSqlBuilder groupBy(String expr) {
        groupBys.add(expr);
        return this;
    }

    public SelectSqlBuilder having(String expr) {
        havings.add(expr);
        return this;
    }

    public SelectSqlBuilder join(String join) {
        joins.add(join);
        return this;
    }

    public SelectSqlBuilder leftJoin(String join) {
        leftJoins.add(join);
        return this;
    }

    public SelectSqlBuilder noWait() {
        if (!forUpdate) {
            throw new RuntimeException("noWait without forUpdate cannot be called");
        }
        noWait = true;
        return this;
    }

    public SelectSqlBuilder orderBy(String name) {
        orderBys.add(name);
        return this;
    }

    /**
     * Adds an ORDER BY item with a direction indicator.
     *
     * @param name      Name of the column by which to sort.
     * @param ascending If true, specifies the direction "asc", otherwise, specifies
     *                  the direction "desc".
     */
    public SelectSqlBuilder orderBy(String name, boolean ascending) {
        if (ascending) {
            orderBys.add(name + " asc");
        } else {
            orderBys.add(name + " desc");
        }
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sql = new StringBuilder("select ");

        if (distinct) {
            sql.append("distinct ");
        }

        if (columns.size() == 0) {
            sql.append("*");
        } else {
            appendList(sql, columns, "", ", ");
        }

        appendList(sql, tables, " from ", ", ");
        appendList(sql, joins, " join ", " join ");
        appendList(sql, leftJoins, " left join ", " left join ");
        appendList(sql, wheres, " where ", " ");
        appendList(sql, groupBys, " group by ", ", ");
        appendList(sql, havings, " having ", " and ");
        appendList(sql, unions, " union ", " union ");
        appendList(sql, orderBys, " order by ", ", ");

        if (forUpdate) {
            sql.append(" for update");
            if (noWait) {
                sql.append(" nowait");
            }
        }

        return sql.toString();
    }

    /**
     * Adds a "union" select builder. The generated SQL will union this query
     * with the result of the main query. The provided builder must have the
     * same columns as the parent select builder and must not use "order by" or
     * "for update".
     */
    public SelectSqlBuilder union(SelectSqlBuilder unionBuilder) {
        unions.add(unionBuilder);
        return this;
    }

    public SelectSqlBuilder where(String expr) {
        wheres.add(expr);
        return this;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Object> getColumns() {
        return columns;
    }

    public List<String> getTables() {
        return tables;
    }

    public List<String> getJoins() {
        return joins;
    }

    public List<String> getLeftJoins() {
        return leftJoins;
    }

    public List<String> getGroupBys() {
        return groupBys;
    }

    public List<String> getHavings() {
        return havings;
    }

    public List<String> getOrderBys() {
        return orderBys;
    }

    public boolean isForUpdate() {
        return forUpdate;
    }

    public boolean isNoWait() {
        return noWait;
    }
}