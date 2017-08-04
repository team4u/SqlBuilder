package org.team4u.sql.builder.content;


import org.team4u.kit.core.lang.Registry;

import java.util.Set;

/**
 * 自定 SQL 的管理接口。 <br>
 * <p>
 * 你可以通过这个接口的实现类，将你的自定义 SQL 存放在文件或者数据库中
 *
 * @author Jay Wu
 */
public interface SqlContentManager extends Registry.Applicant<String> {

    /**
     * 获取一段 Sql 的原始文本
     *
     * @param key Sql 的键值
     * @return Sql 的原始字符串
     */
    String get(String key);

    /**
     * @return 本接口下共管理了多少条 Sql 语句
     */
    int size();

    /**
     * @return 一个包括所有 Sql 语句键值的数组
     */
    Set<String> keys();

    /**
     * 刷新缓存
     */
    void refresh();

    /**
     * 增加一条 Sql
     *
     * @param key   键值
     * @param value Sql 原始字符串
     */
    void create(String key, String value);

    /**
     * 移除一条 Sql
     *
     * @param key 键值
     */
    void remove(String key);
}