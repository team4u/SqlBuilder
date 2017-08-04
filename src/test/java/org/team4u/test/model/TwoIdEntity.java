package org.team4u.test.model;

import org.team4u.sql.builder.entity.annotation.Column;
import org.team4u.sql.builder.entity.annotation.Id;
import org.team4u.sql.builder.entity.annotation.Table;

/**
 * @author Jay Wu
 */
@Table(name = "test2")
public class TwoIdEntity {

    @Id
    @Column
    private String id1;

    @Id
    @Column
    private String id2;

    @Column
    private String name;

    public String getId1() {
        return id1;
    }

    public TwoIdEntity setId1(String id1) {
        this.id1 = id1;
        return this;
    }

    public String getName() {
        return name;
    }

    public TwoIdEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getId2() {
        return id2;
    }

    public TwoIdEntity setId2(String id2) {
        this.id2 = id2;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TwoIdEntity that = (TwoIdEntity) o;

        if (!id1.equals(that.id1)) return false;
        if (!id2.equals(that.id2)) return false;
        return name != null ? name.equals(that.name) : that.name == null;

    }

    @Override
    public int hashCode() {
        int result = id1.hashCode();
        result = 31 * result + id2.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}