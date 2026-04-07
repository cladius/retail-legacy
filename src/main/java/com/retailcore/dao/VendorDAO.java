package com.retailcore.dao;

import com.retailcore.entity.Vendor;
import com.retailcore.orm.BaseDAO;
import com.retailcore.orm.QueryBuilder;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class VendorDAO extends BaseDAO<Vendor> {

    private static VendorDAO instance;

    private VendorDAO() {
        super(Vendor.class);
    }

    public static synchronized VendorDAO getInstance() {
        if (instance == null) {
            instance = new VendorDAO();
        }
        return instance;
    }

    public Vendor findByVendorCode(String vendorCode) throws SQLException {
        return findOneByColumn("VendorCode", vendorCode);
    }

    public List<Vendor> searchByName(String searchTerm) throws SQLException {
        QueryBuilder qb = QueryBuilder.select()
                .from(metadata.getFullTableName())
                .whereLike("VendorName", "%" + searchTerm + "%")
                .whereEquals("IsActive", true)
                .orderBy("VendorName", QueryBuilder.SortDirection.ASC);
        return findByQuery(qb);
    }

    public List<Map<String, Object>> getVendorOrderSummary() throws SQLException {
        String sql = "SELECT v.VendorID, v.VendorName, v.VendorCode, " +
                "COUNT(po.PurchaseOrderID) AS OrderCount, " +
                "ISNULL(SUM(po.TotalAmount), 0) AS TotalOrdered " +
                "FROM [dbo].[tbl_Vendor] v " +
                "LEFT JOIN [dbo].[tbl_PurchaseOrder] po ON v.VendorID = po.VendorID " +
                "WHERE v.IsActive = 1 " +
                "GROUP BY v.VendorID, v.VendorName, v.VendorCode " +
                "ORDER BY TotalOrdered DESC";
        return executeQuery(sql);
    }

    public int getProductCount(int vendorId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM [dbo].[tbl_Product] WHERE VendorID = ? AND Status = 1";
        return executeScalar(sql, Integer.class, vendorId);
    }
}
