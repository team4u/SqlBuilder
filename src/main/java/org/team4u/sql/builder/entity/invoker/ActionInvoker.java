package org.team4u.sql.builder.entity.invoker;

import org.team4u.kit.core.lang.Registry;
import org.team4u.sql.builder.entity.Entity;
import org.team4u.sql.builder.entity.annotation.ActionType;

/**
 * @author Jay Wu
 */
public interface ActionInvoker extends Registry.Applicant<String> {

    /**
     * 执行动作
     *
     * @param entity     实体模型
     * @param context    当前实体对象
     * @param actionType 动作类型
     */
    <T> void invoke(Entity<T> entity, Object context, ActionType actionType);
}