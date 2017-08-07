package org.team4u.sql.builder.entity.builder;


import org.team4u.sql.builder.Sql;
import org.team4u.sql.builder.builder.DeleteSqlBuilder;
import org.team4u.sql.builder.builder.WhereSqlBuilder;
import org.team4u.sql.builder.entity.EntityManager;
import org.team4u.sql.builder.entity.annotation.ActionType;
import org.team4u.sql.builder.entity.invoker.ActionInvokerManager;

/**
 * @author Jay Wu
 */
public class EntityDeleteSqlBuilder<T> extends EntityWhereSqlBuilder<T> {

    protected DeleteSqlBuilder sqlBuilder = new DeleteSqlBuilder(entity.getTable());

    public EntityDeleteSqlBuilder() {
        super();
    }

    public EntityDeleteSqlBuilder(Class<T> entityClass) {
        super(entityClass);
    }

    public EntityDeleteSqlBuilder(EntityManager entityManager, Class<T> entityClass) {
        super(entityManager, entityClass);
    }

    @SuppressWarnings("unchecked")
    public EntityDeleteSqlBuilder(EntityManager entityManager, T entityObj) {
        this(entityManager, (Class<T>) entityObj.getClass());
        this.entityObj = entityObj;
    }

    public EntityDeleteSqlBuilder(T entityObj) {
        this(DEFAULT_ENTITY_MANAGER, entityObj);
    }

    public EntityDeleteSqlBuilder(EntityDeleteSqlBuilder<T> other) {
        super(other);
        this.sqlBuilder = new DeleteSqlBuilder(other.sqlBuilder);
    }

    public EntityDeleteSqlBuilder<T> setEntityClass(Class<T> entityClass) {
        return this.setEntityClass(DEFAULT_ENTITY_MANAGER, entityClass);
    }

    @Override
    public EntityDeleteSqlBuilder<T> setEntityClass(EntityManager entityManager, Class<T> entityClass) {
        super.setEntityClass(entityManager, entityClass);
        sqlBuilder = new DeleteSqlBuilder(entity.getTable());
        return this;
    }

    @Override
    public Sql create() {
        if (entityObj != null) {
            ActionInvokerManager.getInstance().execute(entity, entityObj, ActionType.BEFORE_DELETE);

            appendPkSqlExpression();
        }

        return sqlBuilder.create();
    }

    @Override
    protected WhereSqlBuilder getSqlBuilder() {
        return sqlBuilder;
    }
}