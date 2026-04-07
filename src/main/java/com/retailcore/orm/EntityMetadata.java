package com.retailcore.orm;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EntityMetadata {

    private static final ConcurrentHashMap<Class<?>, EntityMetadata> cache = new ConcurrentHashMap<>();

    private String tableName;
    private String schemaName;
    private String catalogName;
    private Class<?> entityClass;
    private List<ColumnMetadata> columns;
    private ColumnMetadata primaryKeyColumn;
    private Map<String, ColumnMetadata> columnByFieldName;
    private Map<String, ColumnMetadata> columnByDbName;

    private EntityMetadata() {
        columns = new ArrayList<>();
        columnByFieldName = new HashMap<>();
        columnByDbName = new HashMap<>();
    }

    public static EntityMetadata forClass(Class<?> clazz) {
        return cache.computeIfAbsent(clazz, EntityMetadata::resolve);
    }

    private static EntityMetadata resolve(Class<?> clazz) {
        EntityMetadata metadata = new EntityMetadata();
        metadata.entityClass = clazz;

        Table tableAnnotation = clazz.getAnnotation(Table.class);
        if (tableAnnotation != null) {
            metadata.tableName = tableAnnotation.name();
            metadata.schemaName = tableAnnotation.schema();
            metadata.catalogName = tableAnnotation.catalog();
        } else {
            metadata.tableName = clazz.getSimpleName();
            metadata.schemaName = "dbo";
            metadata.catalogName = "";
        }

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Column columnAnnotation = field.getAnnotation(Column.class);
            if (columnAnnotation != null) {
                ColumnMetadata colMeta = new ColumnMetadata();
                colMeta.setField(field);
                colMeta.setFieldName(field.getName());
                colMeta.setFieldType(field.getType());

                String colName = columnAnnotation.name();
                if (colName == null || colName.isEmpty()) {
                    colName = field.getName();
                }
                colMeta.setColumnName(colName);
                colMeta.setPrimaryKey(columnAnnotation.primaryKey());
                colMeta.setAutoIncrement(columnAnnotation.autoIncrement());
                colMeta.setNullable(columnAnnotation.nullable());
                colMeta.setLength(columnAnnotation.length());
                colMeta.setDefaultValue(columnAnnotation.defaultValue());
                colMeta.setInsertable(columnAnnotation.insertable());
                colMeta.setUpdatable(columnAnnotation.updatable());

                field.setAccessible(true);
                metadata.columns.add(colMeta);
                metadata.columnByFieldName.put(field.getName(), colMeta);
                metadata.columnByDbName.put(colName.toUpperCase(), colMeta);

                if (columnAnnotation.primaryKey()) {
                    metadata.primaryKeyColumn = colMeta;
                }
            }
        }

        return metadata;
    }

    public String getTableName() {
        return tableName;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public String getFullTableName() {
        StringBuilder sb = new StringBuilder();
        if (catalogName != null && !catalogName.isEmpty()) {
            sb.append("[").append(catalogName).append("].");
        }
        if (schemaName != null && !schemaName.isEmpty()) {
            sb.append("[").append(schemaName).append("].");
        }
        sb.append("[").append(tableName).append("]");
        return sb.toString();
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }

    public List<ColumnMetadata> getColumns() {
        return columns;
    }

    public ColumnMetadata getPrimaryKeyColumn() {
        return primaryKeyColumn;
    }

    public ColumnMetadata getColumnByFieldName(String fieldName) {
        return columnByFieldName.get(fieldName);
    }

    public ColumnMetadata getColumnByDbName(String dbName) {
        return columnByDbName.get(dbName.toUpperCase());
    }

    public List<ColumnMetadata> getInsertableColumns() {
        List<ColumnMetadata> result = new ArrayList<>();
        for (ColumnMetadata col : columns) {
            if (col.isInsertable() && !col.isAutoIncrement()) {
                result.add(col);
            }
        }
        return result;
    }

    public List<ColumnMetadata> getUpdatableColumns() {
        List<ColumnMetadata> result = new ArrayList<>();
        for (ColumnMetadata col : columns) {
            if (col.isUpdatable() && !col.isPrimaryKey()) {
                result.add(col);
            }
        }
        return result;
    }

    public static void clearCache() {
        cache.clear();
    }
}
