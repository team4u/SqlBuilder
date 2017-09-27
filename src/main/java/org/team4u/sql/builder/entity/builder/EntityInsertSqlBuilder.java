package org.team4u.sql.builder.entity.builder;


import org.team4u.sql.builder.Sql;
import org.team4u.sql.builder.builder.InsertSqlBuilder;
import org.team4u.sql.builder.entity.Entity;
import org.team4u.sql.builder.entity.EntityManager;
import org.team4u.sql.builder.entity.annotation.ActionType;
import org.team4u.sql.builder.entity.invoker.ActionInvokerManager;

/**
 * @author Jay Wu
 */
public class EntityInsertSqlBuilder<T> extends EntitySqlBuilder<T> {

    protected InsertSqlBuilder sqlBuilder;

    protected Object entityObj;

    protected boolean insertIgnoreNull;

    public EntityInsertSqlBuilder(Class<T> entityClass) {
        this(DEFAULT_ENTITY_MANAGER, entityClass);
    }

    public EntityInsertSqlBuilder(EntityManager entityManager, Class<T> entityClass) {
        super(entityManager, entityClass);
        sqlBuilder = new InsertSqlBuilder(entity.getTable());
    }

    @SuppressWarnings("unchecked")
    public EntityInsertSqlBuilder(EntityManager entityManager, T entityObj) {
        this(entityManager, (Class<T>) entityObj.getClass());
        this.entityObj = entityObj;
    }

    public EntityInsertSqlBuilder(T entityObj) {
        this(DEFAULT_ENTITY_MANAGER, entityObj);
    }

    public EntityInsertSqlBuilder(EntityInsertSqlBuilder<T> other) {
        super(other);
        entityObj = other.entityObj;
        sqlBuilder = new InsertSqlBuilder(other.sqlBuilder);
    }

    public EntityInsertSqlBuilder<T> setValue(String name, Object value) {
        Entity.Column column = entity.getColumnWithFieldName(name);
        entity.checkColumn(column, name);

        if (column.isInsertable()) {
            sqlBuilder.setValue(column.getColumnName(), value);
        }
        return this;
    }

    public EntityInsertSqlBuilder<T> setValueIfNotNull(String name, Object value) {
        if (value == null) {
            return this;
        }

        setValue(name, value);
        return this;
    }

    @Override
    public Sql create() {
        if (entityObj != null) {
            ActionInvokerManager.getInstance().execute(entity, entityObj, ActionType.BEFORE_INSERT);
            for (Entity.Column column : entity.getColumns()) {
                if (isInsertIgnoreNull() && !column.isId()) {
                    setValueIfNotNull(column.getProperty().getName(), column.getPropertyValue(entityObj));
                } else {
                    setValue(column.getProperty().getName(), column.getPropertyValue(entityObj));
                }
            }
        }

        return sqlBuilder.create();
    }

    public boolean isInsertIgnoreNull() {
        return insertIgnoreNull;
    }

    public EntityInsertSqlBuilder setInsertIgnoreNull(boolean insertIgnoreNull) {
        this.insertIgnoreNull = insertIgnoreNull;
        return this;
    }
}