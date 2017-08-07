package org.team4u.sql.builder.entity.builder;


import org.team4u.sql.builder.builder.TableSqlBuilder;
import org.team4u.sql.builder.entity.Entity;
import org.team4u.sql.builder.entity.EntityManager;

/**
 * @author Jay Wu
 */
public abstract class EntitySqlBuilder<T> implements TableSqlBuilder {

    public static final EntityManager DEFAULT_ENTITY_MANAGER = new EntityManager();
    private static final long serialVersionUID = 1;
    protected EntityManager entityManager;
    protected EntitySqlExpressionNameHandler nameHandler;
    protected Entity<T> entity;

    public EntitySqlBuilder() {
    }

    public EntitySqlBuilder(Class<T> entityClass) {
        this(DEFAULT_ENTITY_MANAGER, entityClass);
    }

    public EntitySqlBuilder(EntityManager entityManager, Class<T> entityClass) {
        setEntityClass(entityManager, entityClass);
    }

    public EntitySqlBuilder(EntitySqlBuilder<T> other) {
        this.entity = other.entity;
        this.entityManager = other.entityManager;
        this.nameHandler = other.nameHandler;
    }

    public EntityWhereSqlBuilder<T> setEntityClass(EntityManager entityManager, Class<T> entityClass) {
        this.entityManager = entityManager;
        entity = entityManager.createIfNotExist(entityClass);
        nameHandler = new EntitySqlExpressionNameHandler(entity);
        return null;
    }

    public Entity getEntity() {
        return entity;
    }

    @Override
    public String getTable() {
        return entity.getTable();
    }
}