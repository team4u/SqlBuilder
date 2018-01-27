package org.team4u.sql.builder.builder;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import org.team4u.sql.builder.Sql;

import java.util.Arrays;
import java.util.List;

/**
 * Abstract base class of SQL creator classes.
 *
 * @author Jay Wu
 */
public abstract class AbstractTableSqlBuilder implements TableSqlBuilder {

    protected List<String> tables = CollectionUtil.newArrayList();

    private ParameterizedSqlBuilder parameterizedSqlBuilder = new ParameterizedSqlBuilder();
    private int paramIndex;

    public AbstractTableSqlBuilder() {
    }

    public AbstractTableSqlBuilder(String... tables) {
        if (tables != null) {
            this.tables.addAll(Arrays.asList(tables));
        }
    }

    /**
     * Copy constructor. Used by cloneable creators.
     *
     * @param other AbstractSqlCreator being cloned.
     */
    public AbstractTableSqlBuilder(AbstractTableSqlBuilder other) {
        this.tables = other.tables;
        this.paramIndex = other.paramIndex;
        this.parameterizedSqlBuilder = new ParameterizedSqlBuilder((other.getParameterizedSqlBuilder()));
    }

    /**
     * Allocate and return a new parameter that is unique within this
     * SelectCreator. The parameter is of the form "paramN", where N is an
     * integer that is incremented each time this method is called.
     */
    public String allocateParameterName() {
        return "param" + paramIndex++;
    }

    @Override
    public Sql create() {
        parameterizedSqlBuilder.setSqlContent(toString());
        return parameterizedSqlBuilder.create();
    }

    public void setParameter(String name, Object value) {
        parameterizedSqlBuilder.setParameter(name, value);
    }

    public void setPlaceHolder(String name, Object value) {
        parameterizedSqlBuilder.setPlaceHolder(name, value);
    }

    @Override
    public String getTable() {
        return StrUtil.join(",", tables);
    }

    /**
     * Constructs a list of items with given separators.
     *
     * @param sql  StringBuilder to which the constructed string will be
     *             appended.
     * @param list List of objects (usually strings) to join.
     * @param init String to be added to the start of the list, before any of the
     *             items.
     * @param sep  Separator string to be added between items in the list.
     */
    protected void appendList(StringBuilder sql, List<?> list, String init, String sep) {
        boolean first = true;

        for (Object s : list) {
            if (first) {
                sql.append(init);
            } else {
                sql.append(sep);
            }
            sql.append(s);
            first = false;
        }
    }

    public ParameterizedSqlBuilder getParameterizedSqlBuilder() {
        return parameterizedSqlBuilder;
    }

    public AbstractTableSqlBuilder setParameterizedSqlBuilder(ParameterizedSqlBuilder parameterizedSqlBuilder) {
        this.parameterizedSqlBuilder = parameterizedSqlBuilder;
        return this;
    }

    public int getParamIndex() {
        return paramIndex;
    }

    public AbstractTableSqlBuilder setParamIndex(int paramIndex) {
        this.paramIndex = paramIndex;
        return this;
    }
}