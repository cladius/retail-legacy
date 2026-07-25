package com.retailcore.orm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QueryBuilder {

    private StringBuilder selectClause;
    private StringBuilder fromClause;
    private StringBuilder joinClause;
    private StringBuilder whereClause;
    private StringBuilder groupByClause;
    private StringBuilder havingClause;
    private StringBuilder orderByClause;
    private List<Object> parameters;
    private int topCount;
    private int offsetCount;
    private int fetchCount;
    private boolean distinct;
    private QueryType queryType;
    private String tableName;
    private StringBuilder setClause;
    private StringBuilder insertColumns;
    private StringBuilder insertValues;
    private List<Object> whereParameters;
    private List<Object> setParameters;
    private List<Object> insertParameters;

    public enum QueryType {
        SELECT, INSERT, UPDATE, DELETE, COUNT, EXISTS
    }

    public enum JoinType {
        INNER, LEFT, RIGHT, FULL, CROSS
    }

    public enum SortDirection {
        ASC, DESC
    }

    public QueryBuilder() {
        selectClause = new StringBuilder();
        fromClause = new StringBuilder();
        joinClause = new StringBuilder();
        whereClause = new StringBuilder();
        groupByClause = new StringBuilder();
        havingClause = new StringBuilder();
        orderByClause = new StringBuilder();
        setClause = new StringBuilder();
        insertColumns = new StringBuilder();
        insertValues = new StringBuilder();
        parameters = new ArrayList<>();
        whereParameters = new ArrayList<>();
        setParameters = new ArrayList<>();
        insertParameters = new ArrayList<>();
        topCount = -1;
        offsetCount = -1;
        fetchCount = -1;
        distinct = false;
        queryType = QueryType.SELECT;
    }

    public static QueryBuilder select() {
        QueryBuilder qb = new QueryBuilder();
        qb.queryType = QueryType.SELECT;
        return qb;
    }

    public static QueryBuilder select(String... columns) {
        QueryBuilder qb = new QueryBuilder();
        qb.queryType = QueryType.SELECT;
        qb.selectClause.append(String.join(", ", columns));
        return qb;
    }

    public static QueryBuilder insert(String tableName) {
        QueryBuilder qb = new QueryBuilder();
        qb.queryType = QueryType.INSERT;
        qb.tableName = tableName;
        return qb;
    }

    public static QueryBuilder update(String tableName) {
        QueryBuilder qb = new QueryBuilder();
        qb.queryType = QueryType.UPDATE;
        qb.tableName = tableName;
        return qb;
    }

    public static QueryBuilder delete(String tableName) {
        QueryBuilder qb = new QueryBuilder();
        qb.queryType = QueryType.DELETE;
        qb.tableName = tableName;
        return qb;
    }

    public static QueryBuilder count() {
        QueryBuilder qb = new QueryBuilder();
        qb.queryType = QueryType.COUNT;
        return qb;
    }

    public static QueryBuilder exists() {
        QueryBuilder qb = new QueryBuilder();
        qb.queryType = QueryType.EXISTS;
        return qb;
    }

    public QueryBuilder columns(String... columns) {
        if (selectClause.length() > 0) {
            selectClause.append(", ");
        }
        selectClause.append(String.join(", ", columns));
        return this;
    }

    public QueryBuilder column(String column) {
        if (selectClause.length() > 0) {
            selectClause.append(", ");
        }
        selectClause.append(column);
        return this;
    }

    public QueryBuilder column(String column, String alias) {
        if (selectClause.length() > 0) {
            selectClause.append(", ");
        }
        selectClause.append(column).append(" AS ").append(alias);
        return this;
    }

    public QueryBuilder distinct() {
        this.distinct = true;
        return this;
    }

    public QueryBuilder top(int count) {
        this.topCount = count;
        return this;
    }

    public QueryBuilder from(String table) {
        fromClause.setLength(0);
        fromClause.append(table);
        return this;
    }

    public QueryBuilder from(String table, String alias) {
        fromClause.setLength(0);
        fromClause.append(table).append(" ").append(alias);
        return this;
    }

    public QueryBuilder join(String table, String condition) {
        joinClause.append(" INNER JOIN ").append(table).append(" ON ").append(condition);
        return this;
    }

    public QueryBuilder join(JoinType type, String table, String condition) {
        switch (type) {
            case INNER:
                joinClause.append(" INNER JOIN ");
                break;
            case LEFT:
                joinClause.append(" LEFT JOIN ");
                break;
            case RIGHT:
                joinClause.append(" RIGHT JOIN ");
                break;
            case FULL:
                joinClause.append(" FULL OUTER JOIN ");
                break;
            case CROSS:
                joinClause.append(" CROSS JOIN ");
                break;
        }
        joinClause.append(table).append(" ON ").append(condition);
        return this;
    }

    public QueryBuilder join(JoinType type, String table, String alias, String condition) {
        switch (type) {
            case INNER:
                joinClause.append(" INNER JOIN ");
                break;
            case LEFT:
                joinClause.append(" LEFT JOIN ");
                break;
            case RIGHT:
                joinClause.append(" RIGHT JOIN ");
                break;
            case FULL:
                joinClause.append(" FULL OUTER JOIN ");
                break;
            case CROSS:
                joinClause.append(" CROSS JOIN ");
                break;
        }
        joinClause.append(table).append(" ").append(alias).append(" ON ").append(condition);
        return this;
    }

    public QueryBuilder leftJoin(String table, String condition) {
        return join(JoinType.LEFT, table, condition);
    }

    public QueryBuilder rightJoin(String table, String condition) {
        return join(JoinType.RIGHT, table, condition);
    }

    public QueryBuilder where(String condition) {
        if (whereClause.length() > 0) {
            whereClause.append(" AND ");
        }
        whereClause.append(condition);
        return this;
    }

    public QueryBuilder where(String condition, Object value) {
        if (whereClause.length() > 0) {
            whereClause.append(" AND ");
        }
        whereClause.append(condition);
        whereParameters.add(value);
        return this;
    }

    public QueryBuilder where(String column, String operator, Object value) {
        if (whereClause.length() > 0) {
            whereClause.append(" AND ");
        }
        whereClause.append(column).append(" ").append(operator).append(" ?");
        whereParameters.add(value);
        return this;
    }

    public QueryBuilder whereEquals(String column, Object value) {
        return where(column, "=", value);
    }

    public QueryBuilder whereNotEquals(String column, Object value) {
        return where(column, "<>", value);
    }

    public QueryBuilder whereGreaterThan(String column, Object value) {
        return where(column, ">", value);
    }

    public QueryBuilder whereLessThan(String column, Object value) {
        return where(column, "<", value);
    }

    public QueryBuilder whereGreaterOrEqual(String column, Object value) {
        return where(column, ">=", value);
    }

    public QueryBuilder whereLessOrEqual(String column, Object value) {
        return where(column, "<=", value);
    }

    public QueryBuilder whereLike(String column, String pattern) {
        if (whereClause.length() > 0) {
            whereClause.append(" AND ");
        }
        whereClause.append(column).append(" LIKE ?");
        whereParameters.add(pattern);
        return this;
    }

    public QueryBuilder whereNotLike(String column, String pattern) {
        if (whereClause.length() > 0) {
            whereClause.append(" AND ");
        }
        whereClause.append(column).append(" NOT LIKE ?");
        whereParameters.add(pattern);
        return this;
    }

    public QueryBuilder whereIn(String column, Object... values) {
        if (whereClause.length() > 0) {
            whereClause.append(" AND ");
        }
        StringBuilder placeholders = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            if (i > 0) placeholders.append(", ");
            placeholders.append("?");
            whereParameters.add(values[i]);
        }
        whereClause.append(column).append(" IN (").append(placeholders).append(")");
        return this;
    }

    public QueryBuilder whereNotIn(String column, Object... values) {
        if (whereClause.length() > 0) {
            whereClause.append(" AND ");
        }
        StringBuilder placeholders = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            if (i > 0) placeholders.append(", ");
            placeholders.append("?");
            whereParameters.add(values[i]);
        }
        whereClause.append(column).append(" NOT IN (").append(placeholders).append(")");
        return this;
    }

    public QueryBuilder whereBetween(String column, Object lower, Object upper) {
        if (whereClause.length() > 0) {
            whereClause.append(" AND ");
        }
        whereClause.append(column).append(" BETWEEN ? AND ?");
        whereParameters.add(lower);
        whereParameters.add(upper);
        return this;
    }

    public QueryBuilder whereIsNull(String column) {
        if (whereClause.length() > 0) {
            whereClause.append(" AND ");
        }
        whereClause.append(column).append(" IS NULL");
        return this;
    }

    public QueryBuilder whereIsNotNull(String column) {
        if (whereClause.length() > 0) {
            whereClause.append(" AND ");
        }
        whereClause.append(column).append(" IS NOT NULL");
        return this;
    }

    public QueryBuilder or(String condition) {
        if (whereClause.length() > 0) {
            whereClause.append(" OR ");
        }
        whereClause.append(condition);
        return this;
    }

    public QueryBuilder or(String condition, Object value) {
        if (whereClause.length() > 0) {
            whereClause.append(" OR ");
        }
        whereClause.append(condition);
        whereParameters.add(value);
        return this;
    }

    public QueryBuilder openParen() {
        if (whereClause.length() > 0) {
            whereClause.append(" AND ");
        }
        whereClause.append("(");
        return this;
    }

    public QueryBuilder closeParen() {
        whereClause.append(")");
        return this;
    }

    public QueryBuilder groupBy(String... columns) {
        if (groupByClause.length() > 0) {
            groupByClause.append(", ");
        }
        groupByClause.append(String.join(", ", columns));
        return this;
    }

    public QueryBuilder having(String condition) {
        if (havingClause.length() > 0) {
            havingClause.append(" AND ");
        }
        havingClause.append(condition);
        return this;
    }

    public QueryBuilder having(String condition, Object value) {
        if (havingClause.length() > 0) {
            havingClause.append(" AND ");
        }
        havingClause.append(condition);
        whereParameters.add(value);
        return this;
    }

    public QueryBuilder orderBy(String column) {
        if (orderByClause.length() > 0) {
            orderByClause.append(", ");
        }
        orderByClause.append(column);
        return this;
    }

    public QueryBuilder orderBy(String column, SortDirection direction) {
        if (orderByClause.length() > 0) {
            orderByClause.append(", ");
        }
        orderByClause.append(column).append(" ").append(direction.name());
        return this;
    }

    public QueryBuilder offset(int offset) {
        this.offsetCount = offset;
        return this;
    }

    public QueryBuilder fetch(int count) {
        this.fetchCount = count;
        return this;
    }

    public QueryBuilder paginate(int page, int pageSize) {
        this.offsetCount = (page - 1) * pageSize;
        this.fetchCount = pageSize;
        return this;
    }

    public QueryBuilder set(String column, Object value) {
        if (setClause.length() > 0) {
            setClause.append(", ");
        }
        setClause.append(column).append(" = ?");
        setParameters.add(value);
        return this;
    }

    public QueryBuilder setNull(String column) {
        if (setClause.length() > 0) {
            setClause.append(", ");
        }
        setClause.append(column).append(" = NULL");
        return this;
    }

    public QueryBuilder setExpression(String column, String expression) {
        if (setClause.length() > 0) {
            setClause.append(", ");
        }
        setClause.append(column).append(" = ").append(expression);
        return this;
    }

    public QueryBuilder values(String column, Object value) {
        if (insertColumns.length() > 0) {
            insertColumns.append(", ");
            insertValues.append(", ");
        }
        insertColumns.append(column);
        insertValues.append("?");
        insertParameters.add(value);
        return this;
    }

    public String build() {
        StringBuilder sql = new StringBuilder();

        switch (queryType) {
            case SELECT:
                sql.append("SELECT ");
                if (distinct) sql.append("DISTINCT ");
                if (topCount > 0) sql.append("LIMIT ").append(topCount).append(" ");
                if (selectClause.length() > 0) {
                    sql.append(selectClause);
                } else {
                    sql.append("*");
                }
                sql.append(" FROM ").append(fromClause);
                if (joinClause.length() > 0) sql.append(joinClause);
                if (whereClause.length() > 0) sql.append(" WHERE ").append(whereClause);
                if (groupByClause.length() > 0) sql.append(" GROUP BY ").append(groupByClause);
                if (havingClause.length() > 0) sql.append(" HAVING ").append(havingClause);
                if (orderByClause.length() > 0) sql.append(" ORDER BY ").append(orderByClause);
                if (offsetCount >= 0 && fetchCount > 0) {
                    if (orderByClause.length() == 0) {
                        sql.append(" ORDER BY (SELECT NULL)");
                    }
                    sql.append(" LIMIT ").append(fetchCount).append(" OFFSET ").append(offsetCount);
                } else if (offsetCount >= 0) {
                    if (orderByClause.length() == 0) {
                        sql.append(" ORDER BY (SELECT NULL)");
                    }
                    sql.append(" OFFSET ").append(offsetCount);
                }
                break;

            case INSERT:
                sql.append("INSERT INTO ").append(tableName);
                sql.append(" (").append(insertColumns).append(")");
                sql.append(" VALUES (").append(insertValues).append(")");
                break;

            case UPDATE:
                sql.append("UPDATE ").append(tableName);
                sql.append(" SET ").append(setClause);
                if (whereClause.length() > 0) sql.append(" WHERE ").append(whereClause);
                break;

            case DELETE:
                sql.append("DELETE FROM ").append(tableName);
                if (whereClause.length() > 0) sql.append(" WHERE ").append(whereClause);
                break;

            case COUNT:
                sql.append("SELECT COUNT(*) FROM ").append(fromClause);
                if (joinClause.length() > 0) sql.append(joinClause);
                if (whereClause.length() > 0) sql.append(" WHERE ").append(whereClause);
                break;

            case EXISTS:
                sql.append("SELECT CASE WHEN EXISTS (SELECT 1 FROM ").append(fromClause);
                if (joinClause.length() > 0) sql.append(joinClause);
                if (whereClause.length() > 0) sql.append(" WHERE ").append(whereClause);
                sql.append(") THEN 1 ELSE 0 END");
                break;
        }

        return sql.toString();
    }

    public List<Object> getParameters() {
        List<Object> allParams = new ArrayList<>();
        switch (queryType) {
            case INSERT:
                allParams.addAll(insertParameters);
                break;
            case UPDATE:
                allParams.addAll(setParameters);
                allParams.addAll(whereParameters);
                break;
            default:
                allParams.addAll(whereParameters);
                break;
        }
        return allParams;
    }

    public QueryBuilder reset() {
        selectClause.setLength(0);
        fromClause.setLength(0);
        joinClause.setLength(0);
        whereClause.setLength(0);
        groupByClause.setLength(0);
        havingClause.setLength(0);
        orderByClause.setLength(0);
        setClause.setLength(0);
        insertColumns.setLength(0);
        insertValues.setLength(0);
        parameters.clear();
        whereParameters.clear();
        setParameters.clear();
        insertParameters.clear();
        topCount = -1;
        offsetCount = -1;
        fetchCount = -1;
        distinct = false;
        return this;
    }
}
