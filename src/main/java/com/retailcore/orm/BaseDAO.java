package com.retailcore.orm;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public abstract class BaseDAO<T> {

    protected final Class<T> entityClass;
    protected final EntityMetadata metadata;

    protected BaseDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
        this.metadata = EntityMetadata.forClass(entityClass);
    }

    public T findById(Object id) throws SQLException {
        String sql = QueryBuilder.select()
                .from(metadata.getFullTableName())
                .where(metadata.getPrimaryKeyColumn().getColumnName() + " = ?", id)
                .build();

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = TransactionManager.getConnectionForOperation();
            stmt = conn.prepareStatement(sql);
            setParameter(stmt, 1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return ResultMapper.mapRow(rs, entityClass);
            }
            return null;
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
            TransactionManager.releaseConnectionIfNotInTransaction(conn);
        }
    }

    public List<T> findAll() throws SQLException {
        String sql = QueryBuilder.select()
                .from(metadata.getFullTableName())
                .build();

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = TransactionManager.getConnectionForOperation();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            return ResultMapper.mapRows(rs, entityClass);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
            TransactionManager.releaseConnectionIfNotInTransaction(conn);
        }
    }

    public List<T> findAllActive() throws SQLException {
        String sql = QueryBuilder.select()
                .from(metadata.getFullTableName())
                .where("IsActive = ?", true)
                .build();

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = TransactionManager.getConnectionForOperation();
            stmt = conn.prepareStatement(sql);
            setParameter(stmt, 1, true);
            rs = stmt.executeQuery();
            return ResultMapper.mapRows(rs, entityClass);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
            TransactionManager.releaseConnectionIfNotInTransaction(conn);
        }
    }

    public List<T> findByColumn(String column, Object value) throws SQLException {
        String sql = QueryBuilder.select()
                .from(metadata.getFullTableName())
                .where(column + " = ?", value)
                .build();

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = TransactionManager.getConnectionForOperation();
            stmt = conn.prepareStatement(sql);
            setParameter(stmt, 1, value);
            rs = stmt.executeQuery();
            return ResultMapper.mapRows(rs, entityClass);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
            TransactionManager.releaseConnectionIfNotInTransaction(conn);
        }
    }

    public T findOneByColumn(String column, Object value) throws SQLException {
        String sql = QueryBuilder.select()
                .top(1)
                .from(metadata.getFullTableName())
                .where(column + " = ?", value)
                .build();

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = TransactionManager.getConnectionForOperation();
            stmt = conn.prepareStatement(sql);
            setParameter(stmt, 1, value);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return ResultMapper.mapRow(rs, entityClass);
            }
            return null;
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
            TransactionManager.releaseConnectionIfNotInTransaction(conn);
        }
    }

    public PagedResult<T> findPaged(int page, int pageSize) throws SQLException {
        return findPaged(page, pageSize, null, null);
    }

    public PagedResult<T> findPaged(int page, int pageSize, String orderBy, QueryBuilder.SortDirection direction) throws SQLException {
        int totalCount = count();
        int offset = (page - 1) * pageSize;

        QueryBuilder qb = QueryBuilder.select()
                .from(metadata.getFullTableName());

        if (orderBy != null && !orderBy.isEmpty()) {
            qb.orderBy(orderBy, direction != null ? direction : QueryBuilder.SortDirection.ASC);
        } else {
            qb.orderBy(metadata.getPrimaryKeyColumn().getColumnName(), QueryBuilder.SortDirection.ASC);
        }

        qb.offset(offset).fetch(pageSize);

        String sql = qb.build();

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = TransactionManager.getConnectionForOperation();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            List<T> items = ResultMapper.mapRows(rs, entityClass);

            PagedResult<T> result = new PagedResult<>();
            result.setItems(items);
            result.setPage(page);
            result.setPageSize(pageSize);
            result.setTotalCount(totalCount);
            result.setTotalPages((int) Math.ceil((double) totalCount / pageSize));
            return result;
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
            TransactionManager.releaseConnectionIfNotInTransaction(conn);
        }
    }

    public int insert(T entity) throws SQLException {
        List<ColumnMetadata> insertableColumns = metadata.getInsertableColumns();
        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();
        List<Object> params = new ArrayList<>();

        for (int i = 0; i < insertableColumns.size(); i++) {
            ColumnMetadata col = insertableColumns.get(i);
            Object value = col.getValue(entity);

            if (i > 0) {
                columns.append(", ");
                values.append(", ");
            }
            columns.append(col.getColumnName());
            values.append("?");
            params.add(value);
        }

        String sql = "INSERT INTO " + metadata.getFullTableName() +
                " (" + columns + ") VALUES (" + values + ")";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = TransactionManager.getConnectionForOperation();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            for (int i = 0; i < params.size(); i++) {
                setParameter(stmt, i + 1, params.get(i));
            }

            int affected = stmt.executeUpdate();

            if (metadata.getPrimaryKeyColumn() != null && metadata.getPrimaryKeyColumn().isAutoIncrement()) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int generatedId = rs.getInt(1);
                    metadata.getPrimaryKeyColumn().setValue(entity, generatedId);
                    return generatedId;
                }
            }

            return affected;
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
            TransactionManager.releaseConnectionIfNotInTransaction(conn);
        }
    }

    public int update(T entity) throws SQLException {
        List<ColumnMetadata> updatableColumns = metadata.getUpdatableColumns();
        ColumnMetadata pkColumn = metadata.getPrimaryKeyColumn();

        if (pkColumn == null) {
            throw new SQLException("No primary key defined for " + entityClass.getName());
        }

        StringBuilder setClause = new StringBuilder();
        List<Object> params = new ArrayList<>();

        for (int i = 0; i < updatableColumns.size(); i++) {
            ColumnMetadata col = updatableColumns.get(i);
            Object value = col.getValue(entity);

            if (i > 0) {
                setClause.append(", ");
            }
            setClause.append(col.getColumnName()).append(" = ?");
            params.add(value);
        }

        Object pkValue = pkColumn.getValue(entity);
        params.add(pkValue);

        String sql = "UPDATE " + metadata.getFullTableName() +
                " SET " + setClause +
                " WHERE " + pkColumn.getColumnName() + " = ?";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = TransactionManager.getConnectionForOperation();
            stmt = conn.prepareStatement(sql);

            for (int i = 0; i < params.size(); i++) {
                setParameter(stmt, i + 1, params.get(i));
            }

            return stmt.executeUpdate();
        } finally {
            closeStatement(stmt);
            TransactionManager.releaseConnectionIfNotInTransaction(conn);
        }
    }

    public int save(T entity) throws SQLException {
        ColumnMetadata pkColumn = metadata.getPrimaryKeyColumn();
        if (pkColumn == null) {
            return insert(entity);
        }

        Object pkValue = pkColumn.getValue(entity);
        if (pkValue == null || (pkValue instanceof Integer && (Integer) pkValue == 0)) {
            return insert(entity);
        }

        T existing = findById(pkValue);
        if (existing != null) {
            return update(entity);
        } else {
            return insert(entity);
        }
    }

    public int delete(Object id) throws SQLException {
        String sql = "DELETE FROM " + metadata.getFullTableName() +
                " WHERE " + metadata.getPrimaryKeyColumn().getColumnName() + " = ?";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = TransactionManager.getConnectionForOperation();
            stmt = conn.prepareStatement(sql);
            setParameter(stmt, 1, id);
            return stmt.executeUpdate();
        } finally {
            closeStatement(stmt);
            TransactionManager.releaseConnectionIfNotInTransaction(conn);
        }
    }

    public int deleteEntity(T entity) throws SQLException {
        ColumnMetadata pkColumn = metadata.getPrimaryKeyColumn();
        Object pkValue = pkColumn.getValue(entity);
        return delete(pkValue);
    }

    public int softDelete(Object id) throws SQLException {
        String sql = "UPDATE " + metadata.getFullTableName() +
                " SET IsActive = 0, ModifiedDate = CURRENT_TIMESTAMP" +
                " WHERE " + metadata.getPrimaryKeyColumn().getColumnName() + " = ?";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = TransactionManager.getConnectionForOperation();
            stmt = conn.prepareStatement(sql);
            setParameter(stmt, 1, id);
            return stmt.executeUpdate();
        } finally {
            closeStatement(stmt);
            TransactionManager.releaseConnectionIfNotInTransaction(conn);
        }
    }

    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM " + metadata.getFullTableName();

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = TransactionManager.getConnectionForOperation();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            return ResultMapper.mapCount(rs);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
            TransactionManager.releaseConnectionIfNotInTransaction(conn);
        }
    }

    public int countByColumn(String column, Object value) throws SQLException {
        String sql = "SELECT COUNT(*) FROM " + metadata.getFullTableName() +
                " WHERE " + column + " = ?";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = TransactionManager.getConnectionForOperation();
            stmt = conn.prepareStatement(sql);
            setParameter(stmt, 1, value);
            rs = stmt.executeQuery();
            return ResultMapper.mapCount(rs);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
            TransactionManager.releaseConnectionIfNotInTransaction(conn);
        }
    }

    public boolean exists(Object id) throws SQLException {
        String sql = "SELECT CASE WHEN EXISTS (SELECT 1 FROM " + metadata.getFullTableName() +
                " WHERE " + metadata.getPrimaryKeyColumn().getColumnName() + " = ?) THEN 1 ELSE 0 END";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = TransactionManager.getConnectionForOperation();
            stmt = conn.prepareStatement(sql);
            setParameter(stmt, 1, id);
            rs = stmt.executeQuery();
            return ResultMapper.mapExists(rs);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
            TransactionManager.releaseConnectionIfNotInTransaction(conn);
        }
    }

    public int[] batchInsert(List<T> entities) throws SQLException {
        if (entities == null || entities.isEmpty()) {
            return new int[0];
        }

        List<ColumnMetadata> insertableColumns = metadata.getInsertableColumns();
        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();

        for (int i = 0; i < insertableColumns.size(); i++) {
            if (i > 0) {
                columns.append(", ");
                values.append(", ");
            }
            columns.append(insertableColumns.get(i).getColumnName());
            values.append("?");
        }

        String sql = "INSERT INTO " + metadata.getFullTableName() +
                " (" + columns + ") VALUES (" + values + ")";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = TransactionManager.getConnectionForOperation();
            stmt = conn.prepareStatement(sql);

            for (T entity : entities) {
                for (int i = 0; i < insertableColumns.size(); i++) {
                    ColumnMetadata col = insertableColumns.get(i);
                    Object value = col.getValue(entity);
                    setParameter(stmt, i + 1, value);
                }
                stmt.addBatch();
            }

            return stmt.executeBatch();
        } finally {
            closeStatement(stmt);
            TransactionManager.releaseConnectionIfNotInTransaction(conn);
        }
    }

    public int batchDelete(List<Object> ids) throws SQLException {
        if (ids == null || ids.isEmpty()) {
            return 0;
        }

        StringBuilder placeholders = new StringBuilder();
        for (int i = 0; i < ids.size(); i++) {
            if (i > 0) placeholders.append(", ");
            placeholders.append("?");
        }

        String sql = "DELETE FROM " + metadata.getFullTableName() +
                " WHERE " + metadata.getPrimaryKeyColumn().getColumnName() + " IN (" + placeholders + ")";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = TransactionManager.getConnectionForOperation();
            stmt = conn.prepareStatement(sql);

            for (int i = 0; i < ids.size(); i++) {
                setParameter(stmt, i + 1, ids.get(i));
            }

            return stmt.executeUpdate();
        } finally {
            closeStatement(stmt);
            TransactionManager.releaseConnectionIfNotInTransaction(conn);
        }
    }

    public List<T> findByQuery(QueryBuilder queryBuilder) throws SQLException {
        String sql = queryBuilder.build();
        List<Object> params = queryBuilder.getParameters();

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = TransactionManager.getConnectionForOperation();
            stmt = conn.prepareStatement(sql);

            for (int i = 0; i < params.size(); i++) {
                setParameter(stmt, i + 1, params.get(i));
            }

            rs = stmt.executeQuery();
            return ResultMapper.mapRows(rs, entityClass);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
            TransactionManager.releaseConnectionIfNotInTransaction(conn);
        }
    }

    public T findOneByQuery(QueryBuilder queryBuilder) throws SQLException {
        String sql = queryBuilder.build();
        List<Object> params = queryBuilder.getParameters();

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = TransactionManager.getConnectionForOperation();
            stmt = conn.prepareStatement(sql);

            for (int i = 0; i < params.size(); i++) {
                setParameter(stmt, i + 1, params.get(i));
            }

            rs = stmt.executeQuery();
            if (rs.next()) {
                return ResultMapper.mapRow(rs, entityClass);
            }
            return null;
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
            TransactionManager.releaseConnectionIfNotInTransaction(conn);
        }
    }

    public int executeUpdate(String sql, Object... params) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = TransactionManager.getConnectionForOperation();
            stmt = conn.prepareStatement(sql);

            for (int i = 0; i < params.length; i++) {
                setParameter(stmt, i + 1, params[i]);
            }

            return stmt.executeUpdate();
        } finally {
            closeStatement(stmt);
            TransactionManager.releaseConnectionIfNotInTransaction(conn);
        }
    }

    public List<Map<String, Object>> executeQuery(String sql, Object... params) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = TransactionManager.getConnectionForOperation();
            stmt = conn.prepareStatement(sql);

            for (int i = 0; i < params.length; i++) {
                setParameter(stmt, i + 1, params[i]);
            }

            rs = stmt.executeQuery();
            return ResultMapper.mapRowsToMap(rs);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
            TransactionManager.releaseConnectionIfNotInTransaction(conn);
        }
    }

    public <S> S executeScalar(String sql, Class<S> type, Object... params) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = TransactionManager.getConnectionForOperation();
            stmt = conn.prepareStatement(sql);

            for (int i = 0; i < params.length; i++) {
                setParameter(stmt, i + 1, params[i]);
            }

            rs = stmt.executeQuery();
            return ResultMapper.mapScalar(rs, type);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
            TransactionManager.releaseConnectionIfNotInTransaction(conn);
        }
    }

    protected void setParameter(PreparedStatement stmt, int index, Object value) throws SQLException {
        if (value == null) {
            stmt.setNull(index, Types.NULL);
        } else if (value instanceof String) {
            stmt.setString(index, (String) value);
        } else if (value instanceof Integer) {
            stmt.setInt(index, (Integer) value);
        } else if (value instanceof Long) {
            stmt.setLong(index, (Long) value);
        } else if (value instanceof Double) {
            stmt.setDouble(index, (Double) value);
        } else if (value instanceof Float) {
            stmt.setFloat(index, (Float) value);
        } else if (value instanceof Boolean) {
            stmt.setBoolean(index, (Boolean) value);
        } else if (value instanceof BigDecimal) {
            stmt.setBigDecimal(index, (BigDecimal) value);
        } else if (value instanceof Date) {
            stmt.setTimestamp(index, new Timestamp(((Date) value).getTime()));
        } else if (value instanceof java.sql.Date) {
            stmt.setDate(index, (java.sql.Date) value);
        } else if (value instanceof Timestamp) {
            stmt.setTimestamp(index, (Timestamp) value);
        } else if (value instanceof Short) {
            stmt.setShort(index, (Short) value);
        } else if (value instanceof Byte) {
            stmt.setByte(index, (Byte) value);
        } else if (value instanceof byte[]) {
            stmt.setBytes(index, (byte[]) value);
        } else {
            stmt.setObject(index, value);
        }
    }

    protected void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try { rs.close(); } catch (SQLException ignored) {}
        }
    }

    protected void closeStatement(Statement stmt) {
        if (stmt != null) {
            try { stmt.close(); } catch (SQLException ignored) {}
        }
    }

    protected void closeConnection(Connection conn) {
        if (conn != null) {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
    }
}
