package org.team4u.sql.builder.entity.builder;


import cn.hutool.core.lang.Assert;
import org.team4u.sql.builder.builder.WhereSqlBuilder;
import org.team4u.sql.builder.entity.Entity;
import org.team4u.sql.builder.entity.EntityManager;
import org.team4u.sql.builder.expression.SqlExpression;
import org.team4u.sql.builder.expression.SqlExpressions;
import org.team4u.sql.builder.util.WhereUtil;

import java.util.Map;

/**
 * @author Jay Wu
 */
public abstract class EntityWhereSqlBuilder<T> extends EntitySqlBuilder<T> {

    protected Object entityObj;

    public EntityWhereSqlBuilder() {
    }

    public EntityWhereSqlBuilder(Class<T> entityClass) {
        super(entityClass);
    }

    public EntityWhereSqlBuilder(EntityManager entityManager, Class<T> entityClass) {
        super(entityManager, entityClass);
    }

    public EntityWhereSqlBuilder(EntityWhereSqlBuilder<T> other) {
        super(other);
        this.entityObj = other.entityObj;
    }

    public EntityWhereSqlBuilder<T> setEntityClass(EntityManager entityManager, Class<T> entityClass) {
        super.setEntityClass(entityManager, entityClass);
        return this;
    }

    public EntityWhereSqlBuilder<T> where(String expr) {
        getSqlBuilder().where(expr);
        return this;
    }

    public EntityWhereSqlBuilder<T> where(SqlExpression sqlExpression) {
        sqlExpression.setHandler(nameHandler);
        getSqlBuilder().where(sqlExpression);
        return this;
    }

    public EntityWhereSqlBuilder<T> where(String name, String op, Object value) {
        where(SqlExpressions.exps(name, op, value));
        return this;
    }

    public EntityWhereSqlBuilder<T> and(String column, String op, Object value) {
        where(SqlExpressions.and(column, op, value));
        return this;
    }

    public EntityWhereSqlBuilder<T> andIfNotNull(String column, String op, Object value) {
        return andIf(column, op, value, value != null);
    }

    public EntityWhereSqlBuilder<T> andIf(String column, String op, Object value, boolean cnd) {
        if (!cnd) {
            return this;
        }

        and(column, op, value);
        return this;
    }

    public EntityWhereSqlBuilder<T> and(SqlExpression sqlExpression) {
        sqlExpression.setHandler(nameHandler);
        where(SqlExpressions.and(sqlExpression));
        return this;
    }

    public EntityWhereSqlBuilder<T> or(String column, String op, Object value) {
        where(SqlExpressions.or(column, op, value));
        return this;
    }

    public EntityWhereSqlBuilder<T> orIfNotNull(String column, String op, Object value) {
        return orIf(column, op, value, value != null);
    }

    public EntityWhereSqlBuilder<T> orIf(String column, String op, Object value, boolean cnd) {
        if (!cnd) {
            return this;
        }

        or(column, op, value);
        return this;
    }

    public EntityWhereSqlBuilder<T> or(SqlExpression sqlExpression) {
        sqlExpression.setHandler(nameHandler);
        where(SqlExpressions.or(sqlExpression));
        return this;
    }

    public EntityWhereSqlBuilder<T> withPK(Object... pkList) {
        Assert.notEmpty(pkList, "PK list is empty");
        Assert.isTrue(entity.getIdColumns().size() == pkList.length,
                "PK list do not match the entity id columns");

        for (int i = 0; i < entity.getIdColumns().size(); i++) {
            Entity.Column column = entity.getIdColumns().get(i);
            SqlExpression sqlExpression = SqlExpressions.exps(
                    column.getProperty().getName(),
                    "=",
                    pkList[i]
            );

            if (getSqlBuilder().getWheres().size() == 0) {
                where(sqlExpression);
            } else {
                and(sqlExpression);
            }
        }

        return this;
    }

    public EntityWhereSqlBuilder<T> and(Map<String, ?> params) {
        for (SqlExpression sqlExpression : WhereUtil.toSqlExpressions(params)) {
            and(sqlExpression);
        }

        return this;
    }

    public EntityWhereSqlBuilder<T> or(Map<String, ?> params) {
        for (SqlExpression sqlExpression : WhereUtil.toSqlExpressions(params)) {
            or(sqlExpression);
        }

        return this;
    }

    protected abstract WhereSqlBuilder getSqlBuilder();

    protected void appendPkSqlExpression() {
        if (entityObj == null) {
            return;
        }

        for (Entity.Column column : entity.getIdColumns()) {
            SqlExpression sqlExpression = SqlExpressions.exps(
                    column.getProperty().getName(),
                    "=",
                    column.getPropertyValue(entityObj)
            );

            if (getSqlBuilder().getWheres().size() == 0) {
                where(sqlExpression);
            } else {
                and(sqlExpression);
            }
        }
    }
}