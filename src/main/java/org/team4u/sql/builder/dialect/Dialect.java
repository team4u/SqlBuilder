package org.team4u.sql.builder.dialect;

import javax.sql.DataSource;
import java.sql.DatabaseMetaData;

/**
 * Interface representing a SQL dialect. Dialects can modify SQL queries in
 * database server-specific ways.
 *
 * @author Jay Wu
 */
public interface Dialect {

    boolean match(DatabaseMetaData metaData);

    String createCountSelect(String sql);

    /**
     * Returns a SQL statement that returns a limited number of rows from an
     * inner query. Note that the inner select should include an ORDER BY clause
     * that strictly orders the result set; otherwise, some database servers may
     * return pages inconsistently.
     *
     * @param sql    Inner query that would return the full result set.
     * @param limit  Maximum number of rows to return.
     * @param offset Index into the result set of the first row returned.
     */
    String createPageSelect(String sql, int limit, int offset);

    /**
     * Returns an integer supplier representing a database sequence.
     *
     * @param dataSource   DataSource where the sequence exists.
     * @param sequenceName Name of the sequence.
     */
    Long getSequence(DataSource dataSource, String sequenceName);
}