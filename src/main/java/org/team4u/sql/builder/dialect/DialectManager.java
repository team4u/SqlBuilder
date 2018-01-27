package org.team4u.sql.builder.dialect;


import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;


/**
 * @author Jay Wu
 */
public enum DialectManager {

    INSTANCE;

    private final static Log log = LogFactory.get();
    private List<Dialect> dialects = new ArrayList<Dialect>();

    DialectManager() {
        for (Dialect dialect : ServiceLoader.load(Dialect.class)) {
            dialects.add(dialect);
        }
    }

    public DialectManager register(Dialect dialect) {
        dialects.add(dialect);
        return this;
    }

    public Dialect getDialect(final DataSource dataSource) {
        for (Dialect dialect : dialects) {
            Connection connection = null;
            try {
                connection = dataSource.getConnection();
                if (dialect.match(connection.getMetaData())) {
                    return dialect;
                }
            } catch (SQLException e) {
                log.error(e);
                return null;
            } finally {
                safeClose(connection);
            }
        }

        return null;
    }

    private void safeClose(Connection connection) {
        if (connection == null) {
            return;
        }

        try {
            connection.close();
        } catch (SQLException e) {
            // Ignore error
        }
    }
}