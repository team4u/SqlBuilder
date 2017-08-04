package org.team4u.sql.builder.dialect;

import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 * @author Jay Wu
 */
public class GenericDialect implements Dialect {

    private static final Log log = LogFactory.get();

    @Override
    public boolean match(DatabaseMetaData metaData) {
        return true;
    }

    protected boolean matchProductName(DatabaseMetaData metaData, String productName) {
        try {
            return metaData.getDatabaseProductName().toLowerCase().startsWith(productName);
        } catch (SQLException e) {
            log.debug("getDatabaseProductName failed(metaData={})", metaData);
            return false;
        }
    }

    @Override
    public String createCountSelect(String sql) {
        return "select count(1) from (" + sql + ") count_table";
    }

    @Override
    public String createPageSelect(String sql, int limit, int offset) {
        return String.format("%s limit %d offset %d", sql, limit, offset);
    }

    @Override
    public Long getSequence(DataSource dataSource, String sequenceName) {
        return null;
    }
}