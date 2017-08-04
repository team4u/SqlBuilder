package org.team4u.test.model;

import org.team4u.sql.builder.entity.annotation.*;
import org.team4u.sql.builder.entity.invoker.UUIDForPkInvoker;

/**
 * @author Jay Wu
 */
@Table(name = "test")
@Actions({
        @Action(key = UUIDForPkInvoker.KEY, actionType = ActionType.BEFORE_INSERT),
        @Action(key = "test", actionType = ActionType.BEFORE_DELETE)
})
public class SingleIdEntity {

    @Id
    @Column
    private String id;

    @Column
    private String nameAndAge;

    @Column(name = "name1")
    private String name;

    public String getNameAndAge() {
        return nameAndAge;
    }

    public SingleIdEntity setNameAndAge(String nameAndAge) {
        this.nameAndAge = nameAndAge;
        return this;
    }

    public String getName() {
        return name;
    }

    public SingleIdEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getId() {
        return id;
    }

    public SingleIdEntity setId(String id) {
        this.id = id;
        return this;
    }
}
