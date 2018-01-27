package org.team4u.sql.builder.entity;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ReflectUtil;
import org.team4u.kit.core.error.ExceptionUtil;
import org.team4u.kit.core.lang.Registry;
import org.team4u.sql.builder.entity.annotation.Action;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jay Wu
 */
public class Entity<T> implements Registry.Applicant<Class<?>> {

    public static final Action[] EMPTY_ACTIONS = new Action[]{};

    private Class<T> classRef;

    private String table;

    private Action[] actions;

    private List<Column> columns = new ArrayList<Column>();

    public Entity<T> addColumn(Column column) {
        columns.add(column);
        return this;
    }

    public Column getColumnWithFieldName(String fieldName) {
        for (Column column : columns) {
            if (column.getProperty().getName().equalsIgnoreCase(fieldName)) {
                return column;
            }
        }

        return null;
    }

    public Column getColumnWithColumnName(String columnName) {
        for (Column column : columns) {
            if (column.columnName.equalsIgnoreCase(columnName)) {
                return column;
            }
        }

        return null;
    }

    public String getColumnName(String fieldName) {
        Entity.Column column = getColumnWithFieldName(fieldName);
        checkColumn(column, fieldName);
        return column.getColumnName();
    }

    public void checkColumn(Column column, String name) {
        Assert.notNull(column,
                String.format("Column is not exist(table=%s,name=%s)", table, name));
    }

    public List<Column> getIdColumns() {
        List<Column> columnList = new ArrayList<Column>();
        for (Column column : columns) {
            if (column.isId()) {
                columnList.add(column);
            }
        }

        return columnList;
    }

    @Override
    public Class<T> getKey() {
        return classRef;
    }

    public String getTable() {
        return table;
    }

    public Entity<T> setTable(String table) {
        this.table = table;
        return this;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public Entity<T> setColumns(List<Column> columns) {
        this.columns = columns;
        return this;
    }

    public Class<T> getClassRef() {
        return classRef;
    }

    public Entity<T> setClassRef(Class<T> classRef) {
        this.classRef = classRef;
        return this;
    }

    public Action[] getActions() {
        return actions;
    }

    public Entity<T> setActions(Action[] actions) {
        this.actions = actions;
        return this;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "classRef=" + classRef +
                ", table='" + table + '\'' +
                ", columns=" + columns +
                '}';
    }

    public static class Column {

        private boolean id = false;
        private boolean autoId = false;

        private boolean updatable = true;
        private boolean insertable = true;

        private String columnName;
        private Field property;

        public String getColumnName() {
            return columnName;
        }

        public Column setColumnName(String columnName) {
            this.columnName = columnName;
            return this;
        }

        public boolean isId() {
            return id;
        }

        public Column setId(boolean id) {
            this.id = id;
            return this;
        }

        public boolean isAutoId() {
            return autoId;
        }

        public Column setAutoId(boolean autoId) {
            this.autoId = autoId;
            return this;
        }

        public Field getProperty() {
            return property;
        }

        public Column setProperty(Field property) {
            this.property = property;
            return this;
        }

        public <T> T getPropertyValue(Object entityObj) {
            try {
                //noinspection unchecked
                return (T) ReflectUtil.getFieldValue(entityObj, property);
            } catch (Exception e) {
                ExceptionUtil.makeRuntime(
                        "Cannot get property(class=%s,name=%s) ", e,
                        property.getDeclaringClass(), property.getName());
            }

            return null;
        }

        public Column setPropertyValue(Object entityObj, Object value) {
            try {
                ReflectUtil.setFieldValue(entityObj, property, value);
            } catch (Exception e) {
                ExceptionUtil.makeRuntime(
                        "Cannot set property(class=%s,name=%s,value=%s) ", e,
                        property.getDeclaringClass(), property.getName(), value);
            }


            return this;
        }

        public boolean isUpdatable() {
            return updatable;
        }

        public Column setUpdatable(boolean updatable) {
            this.updatable = updatable;
            return this;
        }

        public boolean isInsertable() {
            return insertable;
        }

        public Column setInsertable(boolean insertable) {
            this.insertable = insertable;
            return this;
        }

        @Override
        public String toString() {
            return "Column{" +
                    "columnName='" + columnName + '\'' +
                    '}';
        }
    }
}