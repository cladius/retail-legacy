package com.retailcore.service;

import com.retailcore.dao.*;
import com.retailcore.entity.*;
import com.retailcore.orm.TransactionManager;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class InventoryService {

    private static InventoryService instance;
    private final InventoryDAO inventoryDAO;
    private final ProductDAO productDAO;

    private InventoryService() {
        inventoryDAO = InventoryDAO.getInstance();
        productDAO = ProductDAO.getInstance();
    }

    public static synchronized InventoryService getInstance() {
        if (instance == null) {
            instance = new InventoryService();
        }
        return instance;
    }

    public void adjustInventory(int productId, int storeId, int newQuantity, int employeeId, String reason) throws SQLException {
        try {
            TransactionManager.begin();

            Inventory inv = inventoryDAO.findByProductAndStore(productId, storeId);
            if (inv == null) {
                throw new SQLException("Inventory record not found for product " + productId + " at store " + storeId);
            }

            int oldQuantity = inv.getQuantityOnHand();
            int difference = newQuantity - oldQuantity;

            inventoryDAO.adjustQuantity(productId, storeId, difference);

            AuditLogDAO.getInstance().logAction("tbl_Inventory", inv.getInventoryId(),
                    "ADJUST", "QuantityOnHand=" + oldQuantity, "QuantityOnHand=" + newQuantity, employeeId);

            TransactionManager.commit();
        } catch (SQLException e) {
            TransactionManager.rollback();
            throw e;
        }
    }

    public void receiveShipment(int purchaseOrderId, int employeeId) throws SQLException {
        try {
            TransactionManager.begin();

            PurchaseOrderDAO poDAO = PurchaseOrderDAO.getInstance();
            PurchaseOrder po = poDAO.findById(purchaseOrderId);
            if (po == null) {
                throw new SQLException("Purchase order not found: " + purchaseOrderId);
            }

            String sql = "SELECT * FROM tbl_PurchaseOrderItem WHERE PurchaseOrderID = ?";
            List<Map<String, Object>> items = poDAO.executeQuery(sql, purchaseOrderId);

            for (Map<String, Object> item : items) {
                int productId = (int) item.get("ProductID");
                int qtyOrdered = (int) item.get("QuantityOrdered");

                Inventory inv = inventoryDAO.findByProductAndStore(productId, po.getStoreId());
                if (inv == null) {
                    Inventory newInv = new Inventory();
                    newInv.setProductId(productId);
                    newInv.setStoreId(po.getStoreId());
                    newInv.setQuantityOnHand(qtyOrdered);
                    inventoryDAO.insert(newInv);
                } else {
                    inventoryDAO.receiveStock(productId, po.getStoreId(), qtyOrdered);
                }
            }

            poDAO.receiveFullOrder(purchaseOrderId);

            TransactionManager.commit();
        } catch (SQLException e) {
            TransactionManager.rollback();
            throw e;
        }
    }

    public List<Inventory> getLowStockItems(int storeId) throws SQLException {
        return inventoryDAO.findLowStock(storeId);
    }

    public List<Inventory> getOutOfStockItems(int storeId) throws SQLException {
        return inventoryDAO.findOutOfStock(storeId);
    }

    public List<Map<String, Object>> getInventoryValuation(int storeId) throws SQLException {
        return inventoryDAO.getInventoryValueByStore(storeId);
    }

    public int getAvailableStock(int productId, int storeId) throws SQLException {
        return inventoryDAO.getAvailableQuantity(productId, storeId);
    }

    public int getTotalStockAcrossStores(int productId) throws SQLException {
        return inventoryDAO.getTotalQuantityAcrossStores(productId);
    }
}
