package org.team4u.sql.builder.entity;

import org.team4u.kit.core.lang.Registry;
import org.team4u.sql.builder.entity.processor.DefaultEntityProcessor;
import org.team4u.sql.builder.entity.processor.EntityProcessor;

/**
 * @author Jay Wu
 */
public class EntityManager extends Registry<Class<?>, Entity<?>> {

    public static final EntityProcessor DEFAULT_ENTITY_PROCESSOR = new DefaultEntityProcessor();

    private EntityProcessor entityProcessor;

    public EntityManager() {
        this(DEFAULT_ENTITY_PROCESSOR);
    }

    public EntityManager(EntityProcessor entityProcessor) {
        this.entityProcessor = entityProcessor;
    }

    @SuppressWarnings("unchecked")
    public <T> Entity<T> createIfNotExist(Class<T> key) {
        Entity<T> entity = (Entity<T>) get(key);

        if (entity != null) {
            return entity;
        }

        synchronized (this) {
            if (get(key) == null) {
                register(entityProcessor.toEntity(key));
            }
        }

        return (Entity<T>) get(key);
    }

    public Entity findByTable(String table) {
        for (Entity entity : applicants.values()) {
            if (entity.getTable().equalsIgnoreCase(table)) {
                return entity;
            }
        }

        return null;
    }

    public boolean isEntity(Class<?> entityClass) {
        return entityProcessor.isEntity(entityClass);
    }
}