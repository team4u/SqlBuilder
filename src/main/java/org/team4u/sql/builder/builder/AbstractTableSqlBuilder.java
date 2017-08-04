package org.team4u.sql.builder.builder;

import org.team4u.sql.builder.Sql;

import java.util.List;

/**
 * Abstract base class of SQL creator classes.
 *
 * @author Jay Wu
 */
public abstract class AbstractTableSqlBuilder implements TableSqlBuilder {

    protected String table;

    private ParameterizedSqlBuilder parameterizedSqlBuilder = new ParameterizedSqlBuilder();
    private int paramIndex;

    public AbstractTableSqlBuilder() {
    }

    public AbstractTableSqlBuilder(String table) {
        this.table = table;
    }

    /**
     * Copy constructor. Used by cloneable creators.
     *
     * @param other AbstractSqlCreator being cloned.
     */
    public AbstractTableSqlBuilder(AbstractTableSqlBuilder other) {
        this.table = other.table;
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

    @Override
    public String getTable() {
        return table;
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