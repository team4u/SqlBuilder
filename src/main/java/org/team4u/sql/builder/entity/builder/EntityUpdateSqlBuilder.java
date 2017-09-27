package org.team4u.sql.builder.entity.builder;


import org.team4u.sql.builder.Sql;
import org.team4u.sql.builder.builder.UpdateSqlBuilder;
import org.team4u.sql.builder.builder.WhereSqlBuilder;
import org.team4u.sql.builder.entity.Entity;
import org.team4u.sql.builder.entity.EntityManager;
import org.team4u.sql.builder.entity.annotation.ActionType;
import org.team4u.sql.builder.entity.invoker.ActionInvokerManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jay Wu
 */
public class EntityUpdateSqlBuilder<T> extends EntityWhereSqlBuilder<T> {

    protected UpdateSqlBuilder sqlBuilder = new UpdateSqlBuilder(entity.getTable());

    protected List<String> setColumns = new ArrayList<String>();

    protected boolean updateIgnoreNull;

    public EntityUpdateSqlBuilder(Class<T> entityClass) {
        super(entityClass);
    }

    public EntityUpdateSqlBuilder(EntityManager entityManager, Class<T> entityClass) {
        super(entityManager, entityClass);
    }

    @SuppressWarnings("unchecked")
    public EntityUpdateSqlBuilder(EntityManager entityManager, T entityObj) {
        this(entityManager, (Class<T>) entityObj.getClass());
        this.entityObj = entityObj;
    }

    public EntityUpdateSqlBuilder(T entityObj) {
        this(DEFAULT_ENTITY_MANAGER, entityObj);
    }

    public EntityUpdateSqlBuilder(EntityUpdateSqlBuilder<T> other) {
        super(other);
        sqlBuilder = other.sqlBuilder;
        setColumns.addAll(other.setColumns);
    }

    public EntityUpdateSqlBuilder<T> setEntityClass(Class<T> entityClass) {
        return this.setEntityClass(DEFAULT_ENTITY_MANAGER, entityClass);
    }

    @Override
    public EntityUpdateSqlBuilder<T> setEntityClass(EntityManager entityManager, Class<T> entityClass) {
        super.setEntityClass(entityManager, entityClass);
        sqlBuilder = new UpdateSqlBuilder(entity.getTable());
        return this;
    }

    public EntityUpdateSqlBuilder<T> where(String expr) {
        sqlBuilder.where(expr);
        return this;
    }

    public EntityUpdateSqlBuilder<T> setValue(String expr) {
        sqlBuilder.setValue(expr);
        return this;
    }

    public EntityUpdateSqlBuilder<T> setValue(String name, Object value) {
        Entity.Column column = entity.getColumnWithFieldName(name);
        entity.checkColumn(column, name);

        if (column.isUpdatable()) {
            sqlBuilder.setValue(column.getColumnName(), value);
        }

        return this;
    }

    public EntityUpdateSqlBuilder<T> setValueIfNotNull(String name, Object value) {
        if (value == null) {
            return this;
        }

        setValue(name, value);
        return this;
    }

    public EntityUpdateSqlBuilder<T> column(String name) {
        setColumns.add(entity.getColumnName(name));
        return this;
    }

    @Override
    public Sql create() {
        if (entityObj != null) {
            ActionInvokerManager.getInstance().execute(entity, entityObj, ActionType.BEFORE_UPDATE);

            if (setColumns.isEmpty()) {
                for (Entity.Column column : entity.getColumns()) {
                    if (column.isId()) {
                        continue;
                    }

                    column(column.getProperty().getName());
                }
            }

            for (String columnName : setColumns) {
                Entity.Column column = entity.getColumnWithColumnName(columnName);

                if (isUpdateIgnoreNull()) {
                    setValueIfNotNull(column.getProperty().getName(), column.getPropertyValue(entityObj));
                } else {
                    setValue(column.getProperty().getName(), column.getPropertyValue(entityObj));
                }
            }

            appendPkSqlExpression();
        }

        return sqlBuilder.create();
    }

    public boolean isUpdateIgnoreNull() {
        return updateIgnoreNull;
    }

    public EntityUpdateSqlBuilder setUpdateIgnoreNull(boolean updateIgnoreNull) {
        this.updateIgnoreNull = updateIgnoreNull;
        return this;
    }

    @Override
    protected WhereSqlBuilder getSqlBuilder() {
        return sqlBuilder;
    }
}