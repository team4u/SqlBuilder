package org.team4u.sql.builder.entity.invoker;

import cn.hutool.core.lang.Assert;
import org.team4u.kit.core.lang.Registry;
import org.team4u.sql.builder.entity.Entity;
import org.team4u.sql.builder.entity.annotation.Action;
import org.team4u.sql.builder.entity.annotation.ActionType;

/**
 * @author Jay Wu
 */
public class ActionInvokerManager extends Registry<String, ActionInvoker> {

    private static final ActionInvokerManager instance = new ActionInvokerManager();

    protected ActionInvokerManager() {
        register(new UUIDForPkInvoker());
    }

    public static ActionInvokerManager getInstance() {
        return instance;
    }

    public void execute(Entity entity, Action action, Object context) {
        Assert.notNull(context, "context is null(entity=%s)", entity.getClassRef());
        Assert.notNull(entity, "context is not a table(context=%s)", context);

        ActionInvoker invoker = get(action.key());
        Assert.notNull(invoker, "Can't find ActionInvoker(key=%s,entity=%s)", action.key(), entity.getClassRef());

        invoker.invoke(entity, context, action.actionType());
    }

    public void execute(Entity entity, Object context, ActionType actionType) {
        for (Action action : entity.getActions()) {
            if (action.actionType().equals(actionType)) {
                execute(entity, action, context);
                break;
            }
        }
    }
}