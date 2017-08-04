package org.team4u.sql.builder.entity.annotation;

import java.lang.annotation.*;

/**
 * 标识当前字段为一个 ID
 *
 * @author Jay Wu
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface Id {

    boolean auto() default false;
}