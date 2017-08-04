package org.team4u.sql.builder.util;

import org.team4u.sql.builder.builder.*;
import org.team4u.sql.builder.content.SqlContents;
import org.team4u.sql.builder.entity.builder.EntityDeleteSqlBuilder;
import org.team4u.sql.builder.entity.builder.EntityInsertSqlBuilder;
import org.team4u.sql.builder.entity.builder.EntitySelectSqlBuilder;
import org.team4u.sql.builder.entity.builder.EntityUpdateSqlBuilder;

/**
 * @author Jay Wu
 */
public class SqlBuilders {

    public static <T> EntitySelectSqlBuilder<T> select(Class<T> entityClass) {
        return new EntitySelectSqlBuilder<T>(entityClass);
    }

    public static SelectSqlBuilder select(String table) {
        return new SelectSqlBuilder(table);
    }

    public static <T> EntityUpdateSqlBuilder update(T entity) {
        return new EntityUpdateSqlBuilder<T>(entity);
    }

    public static <T> EntityUpdateSqlBuilder<T> update(Class<T> entityClass) {
        return new EntityUpdateSqlBuilder<T>(entityClass);
    }

    public static UpdateSqlBuilder update(String table) {
        return new UpdateSqlBuilder(table);
    }

    public static <T> EntityInsertSqlBuilder<T> insert(Class<T> entityClass) {
        return new EntityInsertSqlBuilder<T>(entityClass);
    }

    public static <T> EntityInsertSqlBuilder<T> insert(T entity) {
        return new EntityInsertSqlBuilder<T>(entity);
    }

    public static InsertSqlBuilder insert(String table) {
        return new InsertSqlBuilder(table);
    }

    public static <T> EntityDeleteSqlBuilder<T> delete(Class<T> entityClass) {
        return new EntityDeleteSqlBuilder<T>(entityClass);
    }

    public static <T> EntityDeleteSqlBuilder<T> delete(T entity) {
        return new EntityDeleteSqlBuilder<T>(entity);
    }

    public static DeleteSqlBuilder delete(String table) {
        return new DeleteSqlBuilder(table);
    }

    public static ParameterizedSqlBuilder sql(String content) {
        return new ParameterizedSqlBuilder().setSqlContent(content);
    }

    public static ParameterizedSqlBuilder sqlKey(String key) {
        return sql(SqlContents.getInstance().getSqlContent(key));
    }
}