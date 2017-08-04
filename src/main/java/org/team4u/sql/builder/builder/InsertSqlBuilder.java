package org.team4u.sql.builder.builder;


import java.util.ArrayList;
import java.util.List;

/**
 * A InsertCreator that you can use like a InsertCreator.
 *
 * @author Jay Wu
 */
public class InsertSqlBuilder extends AbstractTableSqlBuilder {

    protected List<String> columns = new ArrayList<String>();
    protected List<String> values = new ArrayList<String>();

    public InsertSqlBuilder(String table) {
        super(table);
    }

    public InsertSqlBuilder(InsertSqlBuilder other) {
        super(other);
        columns.addAll(other.columns);
        values.addAll(other.values);
    }

    public InsertSqlBuilder setValue(String column, Object value) {
        columns.add(column);
        values.add(":" + column);
        setParameter(column, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sql = new StringBuilder("insert into ").append(table).append(" (");
        appendList(sql, columns, "", ", ");
        sql.append(") values (");
        appendList(sql, values, "", ", ");
        sql.append(")");
        return sql.toString();
    }

    public List<String> getColumns() {
        return columns;
    }

    public List<String> getValues() {
        return values;
    }
}