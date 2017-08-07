package org.team4u.sql.builder.entity.builder;


import org.team4u.sql.builder.Sql;
import org.team4u.sql.builder.builder.SelectSqlBuilder;
import org.team4u.sql.builder.builder.WhereSqlBuilder;
import org.team4u.sql.builder.entity.EntityManager;
import org.team4u.sql.builder.entity.annotation.ActionType;
import org.team4u.sql.builder.entity.invoker.ActionInvokerManager;
import org.team4u.sql.builder.expression.SqlExpression;

/**
 * A SelectSqlCreator that you can use like a SqlBuilder.
 *
 * @author Jay Wu
 */
public class EntitySelectSqlBuilder<T> extends EntityWhereSqlBuilder<T> {

    private SelectSqlBuilder sqlBuilder;

    public EntitySelectSqlBuilder() {
        super();
    }

    public EntitySelectSqlBuilder(Class<T> entityClass) {
        super(entityClass);
    }

    public EntitySelectSqlBuilder(EntityManager entityManager, Class<T> entityClass) {
        super(entityManager, entityClass);
    }

    public EntitySelectSqlBuilder(EntitySelectSqlBuilder<T> other) {
        super(other);
    }

    public EntitySelectSqlBuilder<T> setEntityClass(Class<T> entityClass) {
        return this.setEntityClass(DEFAULT_ENTITY_MANAGER, entityClass);
    }

    @Override
    public EntitySelectSqlBuilder<T> setEntityClass(EntityManager entityManager, Class<T> entityClass) {
        super.setEntityClass(entityManager, entityClass);
        sqlBuilder = new SelectSqlBuilder(entity.getTable());
        return this;
    }

    @Override
    public EntitySelectSqlBuilder<T> where(String expr) {
        super.where(expr);
        return this;
    }

    @Override
    public EntitySelectSqlBuilder<T> where(SqlExpression sqlExpression) {
        super.where(sqlExpression);
        return this;
    }

    @Override
    public EntitySelectSqlBuilder<T> where(String column, String op, Object value) {
        super.where(column, op, value);
        return this;
    }

    @Override
    public EntitySelectSqlBuilder<T> whereIfNotNull(String column, String op, Object value) {
        super.whereIfNotNull(column, op, value);
        return this;
    }

    @Override
    public EntitySelectSqlBuilder<T> and(String column, String op, Object value) {
        super.and(column, op, value);
        return this;
    }

    @Override
    public EntitySelectSqlBuilder<T> andIfNotNull(String column, String op, Object value) {
        super.andIfNotNull(column, op, value);
        return this;
    }

    @Override
    public EntitySelectSqlBuilder<T> and(SqlExpression sqlExpression) {
        super.and(sqlExpression);
        return this;
    }

    @Override
    public EntitySelectSqlBuilder<T> or(String column, String op, Object value) {
        super.or(column, op, value);
        return this;
    }

    @Override
    public EntitySelectSqlBuilder<T> orIfNotNull(String column, String op, Object value) {
        super.orIfNotNull(column, op, value);
        return this;
    }

    @Override
    public EntitySelectSqlBuilder<T> or(SqlExpression sqlExpression) {
        super.or(sqlExpression);
        return this;
    }

    public EntitySelectSqlBuilder<T> column(String name) {
        sqlBuilder.column(entity.getColumnName(name));
        return this;
    }

    public EntitySelectSqlBuilder<T> column(String name, boolean groupBy) {
        sqlBuilder.column(entity.getColumnName(name), groupBy);
        return this;
    }

    public EntitySelectSqlBuilder<T> distinct() {
        sqlBuilder.distinct();
        return this;
    }

    public EntitySelectSqlBuilder<T> forUpdate() {
        sqlBuilder.forUpdate();
        return this;
    }

    public EntitySelectSqlBuilder<T> from(String table) {
        sqlBuilder.from(table);
        return this;
    }

    public EntitySelectSqlBuilder<T> groupBy(String expr) {
        sqlBuilder.groupBy(expr);
        return this;
    }

    public EntitySelectSqlBuilder<T> having(String expr) {
        sqlBuilder.having(expr);
        return this;
    }

    public EntitySelectSqlBuilder<T> noWait() {
        sqlBuilder.noWait();
        return this;
    }

    public EntitySelectSqlBuilder<T> orderBy(String name) {
        sqlBuilder.orderBy(entity.getColumnName(name));
        return this;
    }

    public EntitySelectSqlBuilder<T> orderBy(String name, boolean ascending) {
        sqlBuilder.orderBy(entity.getColumnName(name), ascending);
        return this;
    }

    @Override
    protected WhereSqlBuilder getSqlBuilder() {
        return sqlBuilder;
    }

    @Override
    public Sql create() {
        ActionInvokerManager.getInstance().execute(entity, entityObj, ActionType.BEFORE_SELECT);
        return sqlBuilder.create();
    }
}