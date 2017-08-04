# SqlBuilder - SQL Builder Utilities

[TOC]

快速简单生成标准SQL字符串的工具类

## Maven

Add the following dependency to your POM:

```xml
<dependency>
    <groupId>org.team4u.sql</groupId>
    <artifactId>sql-builder</artifactId>
    <version>1.0.0</version>
</dependency>

<dependency>
    <groupId>org.team4u</groupId>
    <artifactId>team-kit-core</artifactId>
    <version>1.0.0</version>
</dependency>
<dependency>
    <groupId>com.xiaoleilu</groupId>
    <artifactId>hutool-core</artifactId>
    <version>3.0.9</version>
</dependency>
<dependency>
    <groupId>com.xiaoleilu</groupId>
    <artifactId>hutool-log</artifactId>
    <version>3.0.9</version>
</dependency>
```

## System Requirements

* JDK 1.7+

## Features

* 简单快速构建的SQL字符串
* 轻量级ORM
* 方便与其他持久化框架集成

## SqlBuilders

通过SqlBuilders提供的各种API,可以方便地构建各种复杂的SQL字符串.

因为SqlBuilder仅专注于SQL字符串生成,所以可以非常方便与现有的持久化框架集成.

[SimpleDao](https://github.com/team4u/SimpleDao)基于SqlBuilder实现了一套轻量级的持久化框架.

### Select

```java
Sql sql = SqlBuilders.select("test")
        .from("test2")
        .column("z1")
        .distinct()
        .leftJoin("test3 test3.id = test2.id")
        .where("a", "=", 1)
        .and(SqlExpressions.in("b2", CollectionUtil.arrayListOf(1, 2)))
        .or("c", "=", 3)
        .orderBy("d")
        .orderBy("e", false)
        .create();
```

以上代码将生成:

```sql
select distinct z1
from test, test2
left join test3 test3.id = test2.id
where a = ? and b = ? and b2 in (?, ?) or c = ?
order by d, e desc
```

```
params=[1, 2, 1, 2, 3]
```

你也可以嵌套表达式

```java
import static com.asiainfo.sql.builder.expression.SqlExpressions.*;

Sql sql = SqlBuilders.select("test")
        .where(or(
                and(
                        exps("a", "=", 1),
                        exps("b", "=", 2)
                ),
                and(
                        exps("c", "=", 3),
                        exps("d", "=", 4))
                )
        )
        .create();
```

以上代码将生成:

```sql
select * from test where ((a = ? and b = ?) or (c = ? and d = ?))
```

```
params=[1, 2, 3, 4]
```

### Update

```java
Sql sql = SqlBuilders.update("test")
        .set("a", 1)
        .set("b", 2)
        .where("c", "=", 3)
        .and("d", "=", 4)
        .or("e", "=", 5)
        .create();
```

以上代码将生成:

```sql
update test set a = ?, b = ? where c = ? and d = ? or e = ?
```

```
params=[1, 2, 3, 4, 5]
```

### Delete

```java
Sql sql = SqlBuilders.delete("test")
        .where("a", "=", 1)
        .and("b", "=", 2)
        .or("c", "=", 3)
        .create();
```

以上代码将生成:

```sql
delete from test where a = ? and b = ? or c = ?
```

```
params=[1, 2, 3]
```

### Insert

```java
Sql sql = SqlBuilders.insert("test")
        .set("a", 1)
        .set("b", 2)
        .create();
```

以上代码将生成:

```sql
insert into test (a, b) values (?, ?)
```

```
params=[1, 2]
```

### 自定义SQL

如果存在更复杂的SQL语句, 无法用以上的SqlBuilder完成,可以直接自定义SQL进行处理:

```java
Sql sql = SqlBuilders.sql("select * from $table where a = :a and b = :b")
        .setPlaceHolder("table", "test")
        .setParameter("a", "1")
        .setParameter("b", "2")
        .create();

```

其中:

* setPlaceHolder中的$key将被value完全替换
* setParameter中的:key将会被替换成?，value将保存于sql的params中

以上代码将生成:

```sql
select * from test a = ? and b = ?
```

```
params=[1, 2]
```

### SQL文件

除了直接在代码中定义SQL外,还可以从外部文件加载SQL.

例如,在test.sql文件定义以下内容:

```sql
/* 你可以随便写任何的注释文字，只有距离 SQL 语句最近的那一行注释，才会被认为是键值 */
/* test1 */
SELECT *
FROM client
WHERE name = :name;

/* test2 */
SELECT *
FROM client2
WHERE name = :name;
```

在使用前,先加载SQL文件,并进行注册

注意，以下代码只需执行一次:

```java
SqlContents.getInstance().register(new FileSqlContentManager("test.sql"));
```

FileSqlContentManager支持多个文件或者多个目录，同时支持相对路径（相对于classes）和绝对路径。

SQL文件加载完成后,可以在任意地方获取文件内定义的SQL:

```java
Sql sql = SqlBuilders.sqlKey("test1").setParameter("name", "x").create();
```

以上代码将生成:

```sql
SELECT *
FROM client
WHERE name = ?;
```

```
params=[x]
```

##### SQL修改自动加载

FileSqlContentManager支持SQL文件修改后自动刷新，执行watch可设置监控周期（秒）方法。

```java
SqlContents.getInstance().register(new FileSqlContentManager("test.sql").watch(5));
```

##### 自定义SQL内容管理器

你可以定制自己的SQL内容管理器，如从数据库中加载SQL源，只需要实现SqlContentManager接口即可.

```java
/**
 * 自定 SQL 的管理接口。 <br>
 * <p>
 * 你可以通过这个接口的实现类，将你的自定义 SQL 存放在文件或者数据库中
 *
 * @author Jay Wu
 */
public interface SqlContentManager extends Registrar.Applicant<String> {

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
```

然后进行一次初始化以及注册

例如：

```java
SqlContents.getInstance().register(new DbSqlContentManager());
```

### Dialect

针对不同数据库中的非标准SQL部分,通过Dialect进行处理.

#### 获取Dialect

SqlBuidler可以根据dataSource自动获取对应数据库的Dialect

```java
Dialect dialect = DialectManager.INSTANCE.getDialect(dataSource)
```

#### count

```java
dialect.createCountSelect("select * from test where id = ?");
```

以上代码将生成:

```sql
select count(1) from (select * from test where id = ?) count_table
```

#### 分页


```java
dialect.createPageSelect("select * from test where id = ?", 1, 0);
```

若数据库为MySQL，以上代码将生成:

```sql
select * from test where id = ? limit 1 offset 0
```

## ORM

SqlBuilder实现了轻量级的ORM功能

### 实体类定义

SqlBuilder定义了三种基本注解,分别为：

* @Table 表

   name：表名

* @Id 主键

   auto：默认值为FALSE,若设置为TRUE,则表示数据库主键为自增长策略,SqlBuilder不做处理,仅为后续开发预留

* @Column 字段

   name：字段名

若不指定@Table或者@Column的name,则将采用默认的规则对数据库的表名或者字段名称进行映射.

例如:

```java
// 对应表名为test_entity
@Table
public class TestEntity{}


// 对应字段名为name_and_age
@Column
private String nameAndAge;
```

完整代码示例:

```java
// 对应表名为test
@Table(name = "test1")
public class SingleIdEntity {

    // 对应字段名为id
    @Id
    @Column
    private String id;

    // 对应字段名为name_and_age
    @Column
    private String nameAndAge;

    // 对应字段名为name1
    @Column(name = "name1")
    private String name;

    // 省略getter/setter
}
```

其中@Id支持多主键:

```java
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

    // 省略getter/setter
}
```

### Select

实体类的查询与普通查询基本一致,将根据注解自动映射为数据库对应的表名以及字段名

```java
Sql sql = SqlBuilders.select(SingleIdEntity.class)
        .where("name", "=", 1)
        .and(SqlExpressions.or(
                SqlExpressions.exps("nameAndAge", "=", 1),
                SqlExpressions.exps("nameAndAge", "=", 3)
        )).create();
```

以上代码将生成:

```sql
select * from test where name1 = ? and (name_and_age = ? or name_and_age = ?)
```

```
params=[1, 1, 3]
```

支持主键快速查询:

```java
SqlBuilders.select(SingleIdEntity.class).withPK("1").create();
```

以上代码将生成:

```sql
select * from test where id = ?
```

```
params=[1]
```

支持多主键快速查询,将按照实体类中@Id定义顺序依次生成对应的值:

```java
SqlBuilders.select(TwoIdEntity.class).withPK("1", "2").create();
```

以上代码将生成:

```sql
select * from test2 where id1 = ? and id2 = ?
```

```
params=[1, 2]
```

### Update

实体类的更新与普通更新基本一致.

```java
Sql sql = SqlBuilders.update(SingleIdEntity.class)
        .set("name", 1)
        .where("nameAndAge", "=", 2)
        .create();
```

以上代码将生成:

```sql
update test set name1 = ? where name_and_age = ?
```

```
params=[1, 2]
```

支持快速更新:

```java
SingleIdEntity test = new SingleIdEntity().setId("1").setName("a").setNameAndAge("b")
SqlBuilders.update(test).create();
```
以上代码将生成:

```sql
update test set name1 = ?, name_and_age = ? where id = ?
```

```
params=[a, b, 1]
```

支持指定字段快速更新

```java
SingleIdEntity test = new SingleIdEntity().setId("1").setName("a").setNameAndAge("b")
SqlBuilders.update(test).column("name").create();
```

以上代码将生成:

```sql
update test set name1 = ? where id = ?
```

```
params=[a, 1]
```

### Insert

实体类的插入与普通插入基本一致.

```java
SqlBuilders.insert(SingleIdEntity.class).set("name", 1).set("id",2).create()
```

以上代码将生成:

```sql
insert into test (name1, id) values (?, ?)
```

```
params=[1, 2]
```

支持快速插入:

```java
SingleIdEntity test = new SingleIdEntity().setId("1").setName("a").setNameAndAge("b")
SqlBuilders.insert(test).create()
```

以上代码将生成:

```sql
insert into test (id, name_and_age, name1) values (?, ?, ?)
```

```
params=[1, b, a]
```

### Delete

实体类的删除与普通删除基本一致.

```java
SqlBuilders.delete(SingleIdEntity.class).where("nameAndAge", "=", 2).create()
```

以上代码将生成:

```sql
delete from test where name_and_age = ?
```

```
params=[2]
```

支持快速删除:

```java
SingleIdEntity test = new SingleIdEntity().setId("1").setName("a").setNameAndAge("b")
SqlBuilders.delete(test).create()
```

```sql
delete from test where id = ?
```

```
params=[1]
```

### Action

对于实体类的操作时,可能存在一些预处理,比如插入操作可能需要采取某些主键生成策略。

为此，SqlBuilder定义了6种动作类型:

* BEFORE_SELECT 查询前
* BEFORE_INSERT 插入前
* BEFORE_UPDATE 更新前
* BEFORE_DELETE 删除前
* AFTER_SELECT 查询后
* AFTER_INSERT 插入后
* AFTER_UPDATE 更新后
* AFTER_DELETE 删除后

其中AFTER系列动作仅为预留定义,SqlBuilder不做处理,SqlBuilder仅处理BEFORE系列动作

#### UUID主键生成策略

SqlBuilder已实现UUID主键生成策略,可以直接使用:

```java
@Actions({
        @Action(key = UUIDForPkInvoker.KEY, actionType = ActionType.BEFORE_INSERT)
})
public class SingleIdEntity {

    // 省略字段定义
}
```

以上代码将在插入前执行主键生成策略,生成UUID,并且为@id所在字段赋值

例如：

```java
SingleIdEntity test = new SingleIdEntity().setName("a").setNameAndAge("b")
SqlBuilders.insert(test).create()
```

以上代码将生成:

```sql
insert into test (id, name_and_age, name1) values (?, ?, ?)
```

```
params=[FA90F2A45BC94F01B28362864ADCED62, b, a]
```

注意，id字段已自动赋值为FA90F2A45BC94F01B28362864ADCED62

#### 自定义动作执行者

需要实现ActionInvoker接口

```java
public interface ActionInvoker extends Registrar.Applicant<String> {

    /**
     * 执行动作
     *
     * @param entity 实体模型
     * @param context 当前实体对象
     * @param actionType 动作类型
     */
    void invoke(Entity entity, Object context, ActionType actionType);
}
```

具体实现请参考UUIDForPkInvoker

### 第三方注解支持

SqlBuilder默认支持自带的三种注解,同时也支持标准JPA注解以及Nutz实体类注解,只需要将默认的SqlBuilders换成对应的专属SqlBuilders即可

* Nutz

  NutzSqlBuilders


* JPA

  JpaSqlBuilders

若你使用了其他框架的注解,可以扩展实体注解解析器进行处理,需要实现EntityProcessor接口:


```java
public interface EntityProcessor {

    /**
     * 是否实体类
     */
    boolean isEntity(Class<?> clazz);

    /**
     * 将普通类解析成对应的实体模型
     */
    Entity toEntity(Class<?> clazz);
}
```

具体实现可以参考已有的DefaultEntityProcessor，

同时需要自定义专属SqlBuilders，具体实现请参考JpaSqlBuilders。

**End**