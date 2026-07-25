package com.retailcore;

import com.retailcore.config.DatabaseConfig;
import com.retailcore.dao.*;
import com.retailcore.entity.*;
import com.retailcore.service.*;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class RetailCoreApplication {

    private DatabaseConfig dbConfig;
    private TransactionService transactionService;
    private InventoryService inventoryService;
    private ReportService reportService;

    public void startup() throws SQLException {
        dbConfig = DatabaseConfig.getInstance();
        dbConfig.initialize();
        transactionService = TransactionService.getInstance();
        inventoryService = InventoryService.getInstance();
        reportService = ReportService.getInstance();
    }

    public void shutdown() {
        if (dbConfig != null) {
            dbConfig.shutdown();
        }
    }

    public Product lookupProduct(String skuOrUpc) throws SQLException {
        ProductDAO dao = ProductDAO.getInstance();
        Product product = dao.findBySku(skuOrUpc);
        if (product == null) {
            product = dao.findByUpc(skuOrUpc);
        }
        return product;
    }

    public Customer lookupCustomer(String identifier) throws SQLException {
        CustomerDAO dao = CustomerDAO.getInstance();
        Customer customer = dao.findByCustomerNumber(identifier);
        if (customer == null) {
            customer = dao.findByEmail(identifier);
        }
        return customer;
    }

    public Employee authenticateEmployee(int storeId, String pinCode) throws SQLException {
        return EmployeeDAO.getInstance().authenticateByPin(storeId, pinCode);
    }

    public int processNewTransaction(int storeId, int employeeId, Integer customerId,
                                      int registerNumber,
                                      List<TransactionService.TransactionItemRequest> items,
                                      List<TransactionService.PaymentRequest> payments,
                                      String promoCode) throws SQLException {
        return transactionService.processTransaction(storeId, employeeId, customerId,
                registerNumber, items, payments, promoCode);
    }

    public void voidExistingTransaction(int transactionId, int employeeId, String reason) throws SQLException {
        transactionService.voidTransaction(transactionId, employeeId, reason);
    }

    public Map<String, Object> getStoreDashboard(int storeId) throws SQLException {
        return reportService.getStoreDashboard(storeId, new Date());
    }

    public List<Product> getLowStockProducts(int storeId) throws SQLException {
        return ProductDAO.getInstance().findLowStock(storeId);
    }

    public BigDecimal getStoreRevenue(int storeId, Date startDate, Date endDate) throws SQLException {
        return reportService.getStoreRevenue(storeId, startDate, endDate);
    }

    public static void main(String[] args) {
        RetailCoreApplication app = new RetailCoreApplication();
        try {
            app.startup();
            System.out.println("RetailCore Application Started");
            System.out.println("Database connection pool initialized");
            System.out.println("Ready to process transactions");

            // Test database connection
            System.out.println("Testing database connection...");
            Product product = app.lookupProduct("SKU-001");
            System.out.println("Product found: " + product.getProductName());
        } catch (SQLException e) {
            System.err.println("Failed to start application: " + e.getMessage());
            e.printStackTrace();
        } finally {
            Runtime.getRuntime().addShutdownHook(new Thread(app::shutdown));
        }
    }
}
