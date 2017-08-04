package org.team4u.sql.builder.entity.processor;

import org.team4u.sql.builder.entity.Entity;

/**
 * @author Jay Wu
 */
public interface EntityProcessor {

    /**
     * 是否实体类
     */
    <T> boolean isEntity(Class<T> clazz);

    /**
     * 将普通类解析成对应的实体模型
     */
    <T> Entity<T> toEntity(Class<T> clazz);
}
