package org.team4u.sql.builder;

import com.xiaoleilu.hutool.util.CollectionUtil;
import org.team4u.kit.core.lang.EmptyValue;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * @author Jay Wu
 */
public class Sql implements Serializable {

    private String content;
    private Object[] params;

    public Sql() {
    }

    public Sql(String content, List<Object> params) {
        this.content = content;
        setParams(params);
    }

    public Sql(String content, Object[] params) {
        this.content = content;
        this.params = params;
    }

    public String getContent() {
        return content;
    }

    public Sql setContent(String content) {
        this.content = content;
        return this;
    }

    public Object[] getParams() {
        return params;
    }

    public Sql setParams(List<Object> params) {
        this.params = toParamArray(params);
        return this;
    }

    public Sql setParams(Object[] params) {
        this.params = params;
        return this;
    }

    protected Object[] toParamArray(List<Object> paramList) {
        if (CollectionUtil.isEmpty(paramList)) {
            return EmptyValue.EMPTY_OBJECT_ARRAY;
        }

        int i = 0;
        Object[] paramArray = new Object[paramList.size()];

        for (Object object : paramList) {
            paramArray[i++] = object;
        }

        return paramArray;
    }

    @Override
    public String toString() {
        return "Sql{" +
                "content='" + content + '\'' +
                ", params=" + Arrays.toString(params) +
                '}';
    }
}