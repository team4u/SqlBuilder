package org.team4u.sql.builder.util;

import org.team4u.sql.builder.entity.EntityManager;
import org.team4u.sql.builder.entity.builder.EntityDeleteSqlBuilder;
import org.team4u.sql.builder.entity.builder.EntityInsertSqlBuilder;
import org.team4u.sql.builder.entity.builder.EntitySelectSqlBuilder;
import org.team4u.sql.builder.entity.builder.EntityUpdateSqlBuilder;
import org.team4u.sql.builder.entity.processor.NutzEntityProcessor;

/**
 * @author Jay Wu
 */
public class NutzSqlBuilders extends SqlBuilders {

    private static EntityManager entityManager = new EntityManager(new NutzEntityProcessor());

    public static <T> EntitySelectSqlBuilder<T> select(Class<T> entityClass) {
        return new EntitySelectSqlBuilder<T>(entityManager, entityClass);
    }

    public static <T> EntityUpdateSqlBuilder<T> update(T entity) {
        return new EntityUpdateSqlBuilder<T>(entityManager, entity);
    }

    public static <T> EntityUpdateSqlBuilder<T> update(Class<T> entityClass) {
        return new EntityUpdateSqlBuilder<T>(entityManager, entityClass);
    }

    public static <T> EntityInsertSqlBuilder<T> insert(Class<T> entityClass) {
        return new EntityInsertSqlBuilder<T>(entityManager, entityClass);
    }

    public static <T> EntityInsertSqlBuilder<T> insert(T entity) {
        return new EntityInsertSqlBuilder<T>(entityManager, entity);
    }

    public static <T> EntityDeleteSqlBuilder<T> delete(Class<T> entityClass) {
        return new EntityDeleteSqlBuilder<T>(entityManager, entityClass);
    }

    public static <T> EntityDeleteSqlBuilder<T> delete(T entity) {
        return new EntityDeleteSqlBuilder<T>(entityManager, entity);
    }
}