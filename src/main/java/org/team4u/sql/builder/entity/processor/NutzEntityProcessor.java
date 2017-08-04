package org.team4u.sql.builder.entity.processor;

import org.nutz.dao.entity.annotation.*;
import org.team4u.sql.builder.entity.Entity;

import java.lang.reflect.Field;

/**
 * @author Jay Wu
 */
public class NutzEntityProcessor extends AbstractEntityProcessor {

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

        return defaultIfEmpty(tableAnnotation.value(), clazz.getSimpleName());
    }

    @Override
    protected Entity.Column toColumn(Field field) {
        Column columnAnnotation = field.getAnnotation(Column.class);

        if (columnAnnotation == null) {
            return null;
        }

        ColDefine colDefineAnnotation = field.getAnnotation(ColDefine.class);

        Entity.Column column = new Entity.Column();
        Id idAnnotation = field.getAnnotation(Id.class);

        if (idAnnotation != null) {
            column.setId(true);
            column.setAutoId(idAnnotation.auto());
        } else {
            column.setId(field.getAnnotation(Name.class) != null);
        }

        column.setColumnName(defaultIfEmpty(columnAnnotation.value(), field.getName()));
        column.setInsertable(colDefineAnnotation.insert());
        column.setUpdatable(colDefineAnnotation.update());
        return column;
    }
}