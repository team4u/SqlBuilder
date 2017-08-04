package org.team4u.sql.builder.entity.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 对象插入前的自动执行
 *
 * @author Jay Wu
 */
@Target(TYPE)
@Retention(RUNTIME)
@Documented
public @interface Actions {

    Action[] value();
}