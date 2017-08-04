package org.team4u.sql.builder.builder;


/**
 * A DeleteCreator that you can use like a DeleteBuilder.
 *
 * @author Jay Wu
 */
public class DeleteSqlBuilder extends WhereSqlBuilder {

    public DeleteSqlBuilder(String table) {
        super(table);
    }

    public DeleteSqlBuilder(WhereSqlBuilder other) {
        super(other);
    }

    @Override
    public String toString() {
        StringBuilder sql = new StringBuilder("delete from ").append(table);
        appendList(sql, wheres, " where ", " ");
        return sql.toString();
    }
}