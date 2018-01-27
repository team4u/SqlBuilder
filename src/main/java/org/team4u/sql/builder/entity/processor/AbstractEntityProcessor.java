package org.team4u.sql.builder.entity.processor;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import org.team4u.kit.core.util.ValueUtil;
import org.team4u.sql.builder.entity.Entity;
import org.team4u.sql.builder.entity.annotation.Action;
import org.team4u.sql.builder.entity.annotation.Actions;

import java.lang.reflect.Field;

/**
 * @author Jay Wu
 */
public abstract class AbstractEntityProcessor implements EntityProcessor {

    protected boolean camelCaseForName = true;
    protected boolean lowerCaseForName = true;

    @Override
    public <T> Entity<T> toEntity(Class<T> clazz) {
        String tableName = getTableName(clazz);
        Assert.notNull(tableName, String.format("Class is not entity(class=%s)", clazz));

        Entity<T> entity = new Entity<T>();
        entity.setClassRef(clazz);
        entity.setTable(tableName);
        entity.setActions(getActions(clazz));

        for (Field field : ReflectUtil.getFields(clazz)) {
            Entity.Column column = toColumn(field);

            if (column == null) {
                continue;
            }

            column.setProperty(field);
            entity.addColumn(column);
        }

        return entity;
    }

    protected abstract <T> String getTableName(Class<T> clazz);

    protected abstract Entity.Column toColumn(Field field);

    @SuppressWarnings("unchecked")
    protected <T> Action[] getActions(Class<T> clazz) {
        Actions actions = clazz.getAnnotation(Actions.class);
        if (actions == null) {
            return Entity.EMPTY_ACTIONS;
        }

        return actions.value();
    }

    protected String defaultIfEmpty(String value, String defaultValue) {
        if (StrUtil.isNotBlank(defaultValue)) {
            if (camelCaseForName) {
                defaultValue = StrUtil.toUnderlineCase(defaultValue);
            }

            if (!lowerCaseForName) {
                defaultValue = defaultValue.toUpperCase();
            }
        }

        return ValueUtil.defaultIfEmpty(value, defaultValue);
    }

    public boolean isLowerCaseForName() {
        return lowerCaseForName;
    }

    public AbstractEntityProcessor setLowerCaseForName(boolean lowerCaseForName) {
        this.lowerCaseForName = lowerCaseForName;
        return this;
    }
}