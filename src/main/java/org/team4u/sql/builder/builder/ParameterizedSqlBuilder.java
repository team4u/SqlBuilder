package org.team4u.sql.builder.builder;

import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;
import org.team4u.sql.builder.Sql;

import java.io.Serializable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Jay Wu
 */
public class ParameterizedSqlBuilder implements SqlBuilder, Cloneable, Serializable {

    protected static final String NAME_REGEX = "[a-z][_a-z0-9]*";
    protected static final String PARAM_REGEX = ":(" + NAME_REGEX + ")";
    protected static final String PLACE_HOLDER_REGEX = "\\$(" + NAME_REGEX + ")";

    protected static final Pattern NAME_PATTERN = Pattern.compile(NAME_REGEX, Pattern.CASE_INSENSITIVE);
    protected static final Pattern PARAM_PATTERN = Pattern.compile(PARAM_REGEX, Pattern.CASE_INSENSITIVE);
    protected static final Pattern PLACE_HOLDER_PATTERN = Pattern.compile(PLACE_HOLDER_REGEX, Pattern.CASE_INSENSITIVE);
    private static final Log log = LogFactory.get();
    protected String sqlContent;
    protected Map<String, Object> parameterMap = new HashMap<String, Object>();
    protected Map<String, Object> placeHolderMap = new HashMap<String, Object>();

    public ParameterizedSqlBuilder() {
    }

    protected ParameterizedSqlBuilder(ParameterizedSqlBuilder other) {
        this.sqlContent = other.sqlContent;
        for (String key : other.parameterMap.keySet()) {
            this.parameterMap.put(key, other.parameterMap.get(key));
        }
    }

    @SuppressWarnings("CloneDoesntCallSuperClone")
    public ParameterizedSqlBuilder clone() {
        return new ParameterizedSqlBuilder(this);
    }

    @Override
    public Sql create() {
        Sql sql = parseForParameter(sqlContent);
        parseForPlaceHolder(sql);

        if (log.isDebugEnabled()) {
            log.debug("create sql(sql={},params={})", sql.getContent(), Arrays.toString(sql.getParams()));
        }

        return sql;
    }

    protected Sql parseForParameter(String sqlContent) {
        StringBuilder psSql = new StringBuilder();
        List<Object> paramValues = new ArrayList<Object>();

        Matcher m = PARAM_PATTERN.matcher(sqlContent);
        int index = 0;
        while (m.find(index)) {
            psSql.append(sqlContent.substring(index, m.start()));
            String name = m.group(1);
            index = m.end();
            if (parameterMap.containsKey(name)) {
                psSql.append("?");
                paramValues.add(parameterMap.get(name));
            } else {
                throw new IllegalArgumentException("Unknown parameter '" + name + "' at position " + m.start());
            }
        }

        psSql.append(this.sqlContent.substring(index));

        return new Sql(psSql.toString(), paramValues);
    }

    protected Sql parseForPlaceHolder(Sql sql) {
        StringBuilder psSql = new StringBuilder();

        Matcher m = PLACE_HOLDER_PATTERN.matcher(sql.getContent());
        int index = 0;
        while (m.find(index)) {
            psSql.append(sql.getContent().substring(index, m.start()));
            String name = m.group(1);
            index = m.end();
            if (placeHolderMap.containsKey(name)) {
                psSql.append(placeHolderMap.get(name));
            }
        }

        psSql.append(sql.getContent().substring(index));

        return sql.setContent(psSql.toString());
    }

    public Map<String, Object> getParameterMap() {
        return Collections.unmodifiableMap(parameterMap);
    }

    public String getSqlContent() {
        return sqlContent;
    }

    public ParameterizedSqlBuilder setSqlContent(String sqlContent) {
        this.sqlContent = sqlContent;
        return this;
    }

    public ParameterizedSqlBuilder setParameter(String name, Object value) {
        if (NAME_PATTERN.matcher(name).matches()) {
            parameterMap.put(name, value);
        } else {
            throw new IllegalArgumentException(
                    "'"
                            + name
                            + "' is not a valid parameter name. Names must start with a letter, and contain only letters, numbers, and underscores.");
        }

        return this;
    }

    public ParameterizedSqlBuilder setPlaceHolder(String name, Object value) {
        if (NAME_PATTERN.matcher(name).matches()) {
            placeHolderMap.put(name, value);
        } else {
            throw new IllegalArgumentException(
                    "'"
                            + name
                            + "' is not a valid placeHolder name. Names must start with a letter, and contain only letters, numbers, and underscores.");
        }

        return this;
    }
}