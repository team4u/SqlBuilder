package org.team4u.sql.builder.entity.invoker;

import com.xiaoleilu.hutool.util.RandomUtil;
import com.xiaoleilu.hutool.util.StrUtil;
import org.team4u.sql.builder.entity.Entity;
import org.team4u.sql.builder.entity.annotation.ActionType;

/**
 * @author Jay Wu
 */
public class UUIDForPkInvoker implements ActionInvoker {

    public static final String KEY = "UUIDForPkInvoker";

    @Override
    public <T> void invoke(Entity<T> entity, Object context, ActionType actionType) {
        for (Entity.Column column : entity.getIdColumns()) {
            if (StrUtil.isEmpty((String) column.getPropertyValue(context))) {
                column.setPropertyValue(context, RandomUtil.randomUUID().replace("-", ""));
            }
        }
    }

    @Override
    public String getKey() {
        return KEY;
    }
}