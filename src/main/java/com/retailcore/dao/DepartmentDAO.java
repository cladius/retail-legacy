package com.retailcore.dao;

import com.retailcore.entity.Department;
import com.retailcore.orm.BaseDAO;
import com.retailcore.orm.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

public class DepartmentDAO extends BaseDAO<Department> {

    private static DepartmentDAO instance;

    private DepartmentDAO() {
        super(Department.class);
    }

    public static synchronized DepartmentDAO getInstance() {
        if (instance == null) {
            instance = new DepartmentDAO();
        }
        return instance;
    }

    public Department findByDepartmentCode(String departmentCode) throws SQLException {
        return findOneByColumn("DepartmentCode", departmentCode);
    }

    public List<Department> findRootDepartments() throws SQLException {
        QueryBuilder qb = QueryBuilder.select()
                .from(metadata.getFullTableName())
                .whereIsNull("ParentDepartmentID")
                .whereEquals("IsActive", true)
                .orderBy("SortOrder", QueryBuilder.SortDirection.ASC);
        return findByQuery(qb);
    }

    public List<Department> findSubDepartments(int parentDepartmentId) throws SQLException {
        QueryBuilder qb = QueryBuilder.select()
                .from(metadata.getFullTableName())
                .whereEquals("ParentDepartmentID", parentDepartmentId)
                .whereEquals("IsActive", true)
                .orderBy("SortOrder", QueryBuilder.SortDirection.ASC);
        return findByQuery(qb);
    }
}
