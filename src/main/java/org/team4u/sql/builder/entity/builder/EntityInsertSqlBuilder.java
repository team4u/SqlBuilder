package org.team4u.sql.builder.entity.builder;


import com.xiaoleilu.hutool.util.ReUtil;
import org.team4u.kit.core.util.AssertUtil;
import org.team4u.sql.builder.Sql;
import org.team4u.sql.builder.builder.InsertSqlBuilder;
import org.team4u.sql.builder.entity.Entity;
import org.team4u.sql.builder.entity.EntityManager;
import org.team4u.sql.builder.entity.annotation.ActionType;
import org.team4u.sql.builder.entity.invoker.ActionInvokerManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jay Wu
 */
public class EntityInsertSqlBuilder<T> extends EntitySqlBuilder<T> {

    protected InsertSqlBuilder sqlBuilder;

    protected Object entityObj;

    protected List<Entity.Column> setColumns = new ArrayList<>();

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

    public EntityInsertSqlBuilder<T> column(String name) {
        setColumns.add(entity.getColumnWithFieldName(name));
        return this;
    }

    public EntityInsertSqlBuilder<T> columns(String regularNames) {
        if (regularNames == null) {
            return this;
        }

        for (Entity.Column column : entity.getColumns()) {
            if (column.isId() && !column.isAutoId()) {
                column(column.getProperty().getName());
                continue;
            }

            if (ReUtil.isMatch(regularNames, column.getProperty().getName())) {
                column(column.getProperty().getName());
            }
        }

        AssertUtil.notEmpty(setColumns, "Not column match, regularNames=" + regularNames);
        return this;
    }

    public EntityInsertSqlBuilder<T> setValue(String name, Object value) {
        Entity.Column column = entity.getColumnWithFieldName(name);
        entity.checkColumn(column, name);

        if (column.isInsertable()) {
            sqlBuilder.setValue(column.getColumnName(), value);
        }
        return this;
    }

    public EntityInsertSqlBuilder<T> setValueIf(String name, Object value, boolean cnd) {
        if (!cnd) {
            return this;
        }

        setValue(name, value);
        return this;
    }

    public EntityInsertSqlBuilder<T> setValueIfNotNull(String name, Object value) {
        return setValueIf(name, value, value != null);
    }

    @Override
    public Sql create() {
        if (entityObj != null) {
            ActionInvokerManager.getInstance().execute(entity, entityObj, ActionType.BEFORE_INSERT);

            if (setColumns.isEmpty()) {
                setColumns.addAll(entity.getColumns());
            }

            for (Entity.Column column : setColumns) {
                String name = column.getProperty().getName();
                Object value = column.getPropertyValue(entityObj);

                if (column.isId()) {
                    if (value == null) {
                        continue;
                    }
                }

                if (isInsertIgnoreNull()) {
                    setValueIfNotNull(name, value);
                } else {
                    setValue(name, value);
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