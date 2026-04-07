package com.retailcore.orm;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultMapper {

    public static <T> T mapRow(ResultSet rs, Class<T> clazz) throws SQLException {
        try {
            T entity = clazz.getDeclaredConstructor().newInstance();
            EntityMetadata metadata = EntityMetadata.forClass(clazz);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                String columnName = rsmd.getColumnLabel(i);
                if (columnName == null || columnName.isEmpty()) {
                    columnName = rsmd.getColumnName(i);
                }

                ColumnMetadata colMeta = metadata.getColumnByDbName(columnName);
                if (colMeta == null) {
                    continue;
                }

                Object value = extractValue(rs, i, colMeta.getFieldType(), rsmd.getColumnType(i));
                if (value != null || colMeta.isNullable()) {
                    colMeta.setValue(entity, value);
                }
            }

            return entity;
        } catch (ReflectiveOperationException e) {
            throw new SQLException("Failed to instantiate entity: " + clazz.getName(), e);
        }
    }

    public static <T> List<T> mapRows(ResultSet rs, Class<T> clazz) throws SQLException {
        List<T> results = new ArrayList<>();
        while (rs.next()) {
            results.add(mapRow(rs, clazz));
        }
        return results;
    }

    public static Map<String, Object> mapRowToMap(ResultSet rs) throws SQLException {
        Map<String, Object> row = new HashMap<>();
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();

        for (int i = 1; i <= columnCount; i++) {
            String columnName = rsmd.getColumnLabel(i);
            if (columnName == null || columnName.isEmpty()) {
                columnName = rsmd.getColumnName(i);
            }
            row.put(columnName, rs.getObject(i));
        }

        return row;
    }

    public static List<Map<String, Object>> mapRowsToMap(ResultSet rs) throws SQLException {
        List<Map<String, Object>> results = new ArrayList<>();
        while (rs.next()) {
            results.add(mapRowToMap(rs));
        }
        return results;
    }

    private static Object extractValue(ResultSet rs, int index, Class<?> targetType, int sqlType) throws SQLException {
        Object value = rs.getObject(index);
        if (rs.wasNull() || value == null) {
            return null;
        }

        if (targetType == Integer.class || targetType == int.class) {
            return rs.getInt(index);
        } else if (targetType == Long.class || targetType == long.class) {
            return rs.getLong(index);
        } else if (targetType == Double.class || targetType == double.class) {
            return rs.getDouble(index);
        } else if (targetType == Float.class || targetType == float.class) {
            return rs.getFloat(index);
        } else if (targetType == Boolean.class || targetType == boolean.class) {
            return rs.getBoolean(index);
        } else if (targetType == String.class) {
            return rs.getString(index);
        } else if (targetType == BigDecimal.class) {
            return rs.getBigDecimal(index);
        } else if (targetType == Date.class) {
            Timestamp ts = rs.getTimestamp(index);
            return ts != null ? new Date(ts.getTime()) : null;
        } else if (targetType == java.sql.Date.class) {
            return rs.getDate(index);
        } else if (targetType == Timestamp.class) {
            return rs.getTimestamp(index);
        } else if (targetType == Short.class || targetType == short.class) {
            return rs.getShort(index);
        } else if (targetType == Byte.class || targetType == byte.class) {
            return rs.getByte(index);
        } else if (targetType == byte[].class) {
            return rs.getBytes(index);
        }

        return value;
    }

    public static <T> T mapScalar(ResultSet rs, Class<T> type) throws SQLException {
        if (!rs.next()) {
            return null;
        }
        Object value = rs.getObject(1);
        if (value == null) {
            return null;
        }
        return convertScalar(value, type);
    }

    @SuppressWarnings("unchecked")
    private static <T> T convertScalar(Object value, Class<T> type) {
        if (type.isInstance(value)) {
            return (T) value;
        }

        if (type == Integer.class || type == int.class) {
            return (T) Integer.valueOf(((Number) value).intValue());
        } else if (type == Long.class || type == long.class) {
            return (T) Long.valueOf(((Number) value).longValue());
        } else if (type == Double.class || type == double.class) {
            return (T) Double.valueOf(((Number) value).doubleValue());
        } else if (type == Float.class || type == float.class) {
            return (T) Float.valueOf(((Number) value).floatValue());
        } else if (type == Boolean.class || type == boolean.class) {
            if (value instanceof Number) {
                return (T) Boolean.valueOf(((Number) value).intValue() != 0);
            }
            return (T) Boolean.valueOf(value.toString());
        } else if (type == String.class) {
            return (T) value.toString();
        } else if (type == BigDecimal.class) {
            if (value instanceof Number) {
                return (T) new BigDecimal(value.toString());
            }
        }

        return (T) value;
    }

    public static int mapCount(ResultSet rs) throws SQLException {
        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    public static boolean mapExists(ResultSet rs) throws SQLException {
        if (rs.next()) {
            return rs.getInt(1) == 1;
        }
        return false;
    }

    public static <T> List<T> mapColumn(ResultSet rs, int columnIndex, Class<T> type) throws SQLException {
        List<T> results = new ArrayList<>();
        while (rs.next()) {
            Object value = rs.getObject(columnIndex);
            if (value != null) {
                results.add(convertScalar(value, type));
            }
        }
        return results;
    }

    public static <T> List<T> mapColumn(ResultSet rs, String columnName, Class<T> type) throws SQLException {
        List<T> results = new ArrayList<>();
        while (rs.next()) {
            Object value = rs.getObject(columnName);
            if (value != null) {
                results.add(convertScalar(value, type));
            }
        }
        return results;
    }
}
