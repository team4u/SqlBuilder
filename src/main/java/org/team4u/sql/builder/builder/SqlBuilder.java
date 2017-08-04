package org.team4u.sql.builder.builder;

import org.team4u.sql.builder.Sql;

import java.io.Serializable;

/**
 * @author Jay Wu
 */
public interface SqlBuilder extends Serializable {

    Sql create();
}
