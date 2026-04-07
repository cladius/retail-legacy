package com.retailcore.service;

import com.retailcore.dao.*;
import com.retailcore.entity.*;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportService {

    private static ReportService instance;
    private final TransactionDAO transactionDAO;
    private final StoreDAO storeDAO;
    private final EmployeeDAO employeeDAO;
    private final ProductDAO productDAO;
    private final CustomerDAO customerDAO;
    private final InventoryDAO inventoryDAO;

    private ReportService() {
        transactionDAO = TransactionDAO.getInstance();
        storeDAO = StoreDAO.getInstance();
        employeeDAO = EmployeeDAO.getInstance();
        productDAO = ProductDAO.getInstance();
        customerDAO = CustomerDAO.getInstance();
        inventoryDAO = InventoryDAO.getInstance();
    }

    public static synchronized ReportService getInstance() {
        if (instance == null) {
            instance = new ReportService();
        }
        return instance;
    }

    public Map<String, Object> getStoreDashboard(int storeId, Date date) throws SQLException {
        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("dailySales", transactionDAO.getDailySales(storeId, date));
        dashboard.put("transactionCount", transactionDAO.getDailyTransactionCount(storeId, date));
        dashboard.put("salesByHour", transactionDAO.getSalesByHour(storeId, date));
        dashboard.put("employeeCount", storeDAO.getStoreEmployeeCount(storeId));
        dashboard.put("productCount", storeDAO.getStoreProductCount(storeId));
        dashboard.put("lowStockCount", inventoryDAO.findLowStock(storeId).size());
        dashboard.put("outOfStockCount", inventoryDAO.findOutOfStock(storeId).size());
        return dashboard;
    }

    public List<Map<String, Object>> getMultiStoreSalesComparison(Date startDate, Date endDate) throws SQLException {
        return storeDAO.getStoreSalesSummary(startDate, endDate);
    }

    public List<Map<String, Object>> getEmployeePerformance(int storeId, Date startDate, Date endDate) throws SQLException {
        return employeeDAO.getEmployeeSalesPerformance(storeId, startDate, endDate);
    }

    public List<Product> getTopSellingProducts(int storeId, int limit) throws SQLException {
        return productDAO.findTopSelling(storeId, limit);
    }

    public List<Customer> getTopCustomers(int limit) throws SQLException {
        return customerDAO.findTopSpenders(limit);
    }

    public BigDecimal getStoreRevenue(int storeId, Date startDate, Date endDate) throws SQLException {
        return storeDAO.getStoreRevenue(storeId, startDate, endDate);
    }

    public BigDecimal getAverageTransactionValue(int storeId, Date startDate, Date endDate) throws SQLException {
        return transactionDAO.getAverageTransactionValue(storeId, startDate, endDate);
    }

    public List<Map<String, Object>> getVendorPerformance() throws SQLException {
        return VendorDAO.getInstance().getVendorOrderSummary();
    }

    public List<Map<String, Object>> getInventoryReport(int storeId) throws SQLException {
        return inventoryDAO.getInventoryValueByStore(storeId);
    }
}
