package org.team4u.sql.builder.entity.processor;

import org.team4u.sql.builder.entity.Entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.Field;

/**
 * @author Jay Wu
 */
public class JpaEntityProcessor extends AbstractEntityProcessor {

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
        column.setId(field.getAnnotation(Id.class) != null);
        column.setColumnName(defaultIfEmpty(columnAnnotation.name(), field.getName()));
        column.setInsertable(columnAnnotation.insertable());
        column.setUpdatable(columnAnnotation.updatable());
        return column;
    }
}