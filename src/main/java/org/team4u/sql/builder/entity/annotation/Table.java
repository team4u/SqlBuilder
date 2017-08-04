package org.team4u.sql.builder.entity.annotation;

/**
 * @author Jay Wu
 */

import javax.persistence.SecondaryTable;
import javax.persistence.SecondaryTables;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Specifies the primary table for the annotated entity. Additional
 * tables may be specified using {@link SecondaryTable} or {@link
 * SecondaryTables} annotation.
 * <p/>
 * If no <code>Table</code> annotation is specified for an entity
 * class, the default values apply.
 *
 * @author Jay Wu
 */
@Target(TYPE)
@Retention(RUNTIME)
@Documented
public @interface Table {
    /**
     * (Optional) The name of the table.
     * <p/>
     * Defaults to the entity name.
     */
    String name() default "";
}