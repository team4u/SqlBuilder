package org.team4u.sql.builder.builder;


import java.util.ArrayList;
import java.util.List;

/**
 * A UpdateCreator that you can use like an UpdateBuilder.
 *
 * @author Jay Wu
 */
public class UpdateSqlBuilder extends WhereSqlBuilder {

    protected List<String> sets = new ArrayList<String>();

    public UpdateSqlBuilder(String table) {
        super(table);
    }

    public UpdateSqlBuilder(UpdateSqlBuilder other) {
        super(other);
        sets.addAll(other.sets);
    }

    public UpdateSqlBuilder setValue(String expr) {
        sets.add(expr);
        return this;
    }

    public UpdateSqlBuilder setValue(String column, Object value) {
        setValue(String.format("%s = :%s", column, column));
        setParameter(column, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sql = new StringBuilder("update ").append(table);
        appendList(sql, sets, " set ", ", ");
        appendList(sql, wheres, " where ", " ");
        return sql.toString();
    }

    public List<String> getSets() {
        return sets;
    }
}