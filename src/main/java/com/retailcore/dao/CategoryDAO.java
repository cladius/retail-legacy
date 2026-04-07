package com.retailcore.dao;

import com.retailcore.entity.Category;
import com.retailcore.orm.BaseDAO;
import com.retailcore.orm.QueryBuilder;
import com.retailcore.orm.ResultMapper;
import com.retailcore.orm.TransactionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CategoryDAO extends BaseDAO<Category> {

    private static CategoryDAO instance;

    private CategoryDAO() {
        super(Category.class);
    }

    public static synchronized CategoryDAO getInstance() {
        if (instance == null) {
            instance = new CategoryDAO();
        }
        return instance;
    }

    public Category findByCategoryCode(String categoryCode) throws SQLException {
        return findOneByColumn("CategoryCode", categoryCode);
    }

    public List<Category> findByDepartment(int departmentId) throws SQLException {
        QueryBuilder qb = QueryBuilder.select()
                .from(metadata.getFullTableName())
                .whereEquals("DepartmentID", departmentId)
                .whereEquals("IsActive", true)
                .orderBy("SortOrder", QueryBuilder.SortDirection.ASC);
        return findByQuery(qb);
    }

    public List<Category> findRootCategories() throws SQLException {
        QueryBuilder qb = QueryBuilder.select()
                .from(metadata.getFullTableName())
                .whereIsNull("ParentCategoryID")
                .whereEquals("IsActive", true)
                .orderBy("SortOrder", QueryBuilder.SortDirection.ASC);
        return findByQuery(qb);
    }

    public List<Category> findSubCategories(int parentCategoryId) throws SQLException {
        QueryBuilder qb = QueryBuilder.select()
                .from(metadata.getFullTableName())
                .whereEquals("ParentCategoryID", parentCategoryId)
                .whereEquals("IsActive", true)
                .orderBy("SortOrder", QueryBuilder.SortDirection.ASC);
        return findByQuery(qb);
    }

    public int getProductCount(int categoryId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM [dbo].[tbl_Product] WHERE CategoryID = ? AND Status = 1";
        return executeScalar(sql, Integer.class, categoryId);
    }
}
