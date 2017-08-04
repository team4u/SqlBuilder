package org.team4u.sql.builder.entity.processor;

import org.team4u.sql.builder.entity.Entity;
import org.team4u.sql.builder.entity.annotation.Column;
import org.team4u.sql.builder.entity.annotation.Id;
import org.team4u.sql.builder.entity.annotation.Table;

import java.lang.reflect.Field;

/**
 * @author Jay Wu
 */
public class DefaultEntityProcessor extends AbstractEntityProcessor {

    @Override
    public <T> boolean isEntity(Class<T> clazz) {
        return clazz.isAnnotationPresent(Table.class);
    }

    @Override
    protected <T> String getTableName(Class<T> clazz) {
        Table tableAnnotation = clazz.getAnnotation(Table.class);

        if (tableAnnotation == null) {
            return null;
        }

        return defaultIfEmpty(tableAnnotation.name(), clazz.getSimpleName());
    }

    @Override
    protected Entity.Column toColumn(Field field) {
        Column columnAnnotation = field.getAnnotation(Column.class);

        if (columnAnnotation == null) {
            return null;
        }

        Entity.Column column = new Entity.Column();

        Id idAnnotation = field.getAnnotation(Id.class);
        if (idAnnotation != null) {
            column.setId(true);
            column.setAutoId(idAnnotation.auto());
        }

        column.setColumnName(defaultIfEmpty(columnAnnotation.name(), field.getName()));
        column.setInsertable(columnAnnotation.insertable());
        column.setUpdatable(columnAnnotation.updatable());
        return column;
    }
}