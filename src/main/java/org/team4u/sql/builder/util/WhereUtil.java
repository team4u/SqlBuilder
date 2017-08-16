package org.team4u.sql.builder.util;

import com.xiaoleilu.hutool.lang.PatternPool;
import com.xiaoleilu.hutool.util.CollectionUtil;
import org.team4u.sql.builder.expression.SqlExpression;
import org.team4u.sql.builder.expression.SqlExpressions;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Jay Wu
 */
public class WhereUtil {

    public static List<SqlExpression> toSqlExpressions(Map<String, ?> params) {
        List<SqlExpression> sqlExpressions = CollectionUtil.newArrayList();

        if (params == null) {
            return sqlExpressions;
        }

        Pattern whereSqlExp = PatternPool.get("\\((.+?)\\)(.+)", Pattern.CASE_INSENSITIVE);

        for (Map.Entry<String, ?> entry : params.entrySet()) {
            Matcher m = whereSqlExp.matcher(entry.getKey());

            String name = entry.getKey();
            String op = "=";
            if (m.matches()) {
                op = m.group(1).trim();
                name = m.group(2).trim();
            }

            sqlExpressions.add(SqlExpressions.exps(name, op, entry.getValue()));
        }

        return sqlExpressions;
    }
}