package com.retailcore.service;

import com.retailcore.dao.*;
import com.retailcore.entity.*;
import com.retailcore.orm.TransactionManager;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class TransactionService {

    private static TransactionService instance;
    private final TransactionDAO transactionDAO;
    private final ProductDAO productDAO;
    private final InventoryDAO inventoryDAO;
    private final CustomerDAO customerDAO;
    private final PromotionDAO promotionDAO;
    private final GiftCardDAO giftCardDAO;

    private TransactionService() {
        transactionDAO = TransactionDAO.getInstance();
        productDAO = ProductDAO.getInstance();
        inventoryDAO = InventoryDAO.getInstance();
        customerDAO = CustomerDAO.getInstance();
        promotionDAO = PromotionDAO.getInstance();
        giftCardDAO = GiftCardDAO.getInstance();
    }

    public static synchronized TransactionService getInstance() {
        if (instance == null) {
            instance = new TransactionService();
        }
        return instance;
    }

    public int processTransaction(int storeId, int employeeId, Integer customerId,
                                   int registerNumber, List<TransactionItemRequest> itemRequests,
                                   List<PaymentRequest> paymentRequests, String promotionCode) throws SQLException {

        BigDecimal subTotal = BigDecimal.ZERO;
        BigDecimal discountTotal = BigDecimal.ZERO;
        BigDecimal taxTotal = BigDecimal.ZERO;

        List<TransactionItem> items = new ArrayList<>();
        Promotion promo = null;

        if (promotionCode != null && !promotionCode.isEmpty()) {
            promo = promotionDAO.findByPromotionCode(promotionCode);
            if (promo != null && customerId != null) {
                if (!promotionDAO.isPromotionValid(promo.getPromotionId(), customerId)) {
                    promo = null;
                }
            }
        }

        for (TransactionItemRequest req : itemRequests) {
            Product product = productDAO.findById(req.getProductId());
            if (product == null) {
                throw new SQLException("Product not found: " + req.getProductId());
            }

            int available = inventoryDAO.getAvailableQuantity(req.getProductId(), storeId);
            if (available < req.getQuantity()) {
                throw new SQLException("Insufficient stock for product: " + product.getSku());
            }

            BigDecimal unitPrice = product.getRetailPrice();
            if (product.getSalePrice() != null && product.getSaleStartDate() != null
                    && product.getSaleEndDate() != null) {
                Date now = new Date();
                if (now.after(product.getSaleStartDate()) && now.before(product.getSaleEndDate())) {
                    unitPrice = product.getSalePrice();
                }
            }

            BigDecimal lineSubtotal = unitPrice.multiply(new BigDecimal(req.getQuantity()));
            BigDecimal lineDiscount = BigDecimal.ZERO;

            if (promo != null && product.getIsDiscountable()) {
                boolean applicable = false;
                if (promo.getApplicableProductId() != null && promo.getApplicableProductId().equals(product.getProductId())) {
                    applicable = true;
                } else if (promo.getApplicableCategoryId() != null && promo.getApplicableCategoryId().equals(product.getCategoryId())) {
                    applicable = true;
                } else if (promo.getApplicableProductId() == null && promo.getApplicableCategoryId() == null) {
                    applicable = true;
                }

                if (applicable) {
                    if (promo.getDiscountType() == 1) {
                        lineDiscount = lineSubtotal.multiply(promo.getDiscountValue()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
                    } else if (promo.getDiscountType() == 2) {
                        lineDiscount = promo.getDiscountValue().min(lineSubtotal);
                    }

                    if (promo.getMaxDiscountAmount() != null && lineDiscount.compareTo(promo.getMaxDiscountAmount()) > 0) {
                        lineDiscount = promo.getMaxDiscountAmount();
                    }
                }
            }

            BigDecimal taxableAmount = lineSubtotal.subtract(lineDiscount);
            BigDecimal lineTax = BigDecimal.ZERO;
            if (product.getIsTaxable()) {
                lineTax = taxableAmount.multiply(new BigDecimal("0.0825")).setScale(2, RoundingMode.HALF_UP);
            }

            BigDecimal lineTotal = taxableAmount.add(lineTax);

            TransactionItem item = new TransactionItem();
            item.setProductId(product.getProductId());
            item.setQuantity(req.getQuantity());
            item.setUnitPrice(unitPrice);
            item.setDiscountAmount(lineDiscount);
            item.setTaxAmount(lineTax);
            item.setLineTotal(lineTotal);
            item.setSerialNumber(req.getSerialNumber());
            items.add(item);

            subTotal = subTotal.add(lineSubtotal);
            discountTotal = discountTotal.add(lineDiscount);
            taxTotal = taxTotal.add(lineTax);
        }

        BigDecimal grandTotal = subTotal.subtract(discountTotal).add(taxTotal);

        BigDecimal totalPayment = BigDecimal.ZERO;
        List<Payment> payments = new ArrayList<>();
        for (PaymentRequest payReq : paymentRequests) {
            Payment payment = new Payment();
            payment.setPaymentMethod(payReq.getPaymentMethod());
            payment.setAmount(payReq.getAmount());
            payment.setReferenceNumber(payReq.getReferenceNumber());
            payment.setCardType(payReq.getCardType());
            payment.setCardLastFour(payReq.getCardLastFour());
            payment.setAuthorizationCode(payReq.getAuthorizationCode());
            payment.setCheckNumber(payReq.getCheckNumber());
            payment.setGiftCardNumber(payReq.getGiftCardNumber());
            payment.setStatus((byte) 1);
            payments.add(payment);
            totalPayment = totalPayment.add(payReq.getAmount());
        }

        if (totalPayment.compareTo(grandTotal) < 0) {
            throw new SQLException("Insufficient payment. Required: " + grandTotal + ", Received: " + totalPayment);
        }

        BigDecimal changeAmount = totalPayment.subtract(grandTotal);
        String txnNumber = transactionDAO.generateTransactionNumber(storeId, registerNumber);

        Transaction transaction = new Transaction();
        transaction.setTransactionNumber(txnNumber);
        transaction.setStoreId(storeId);
        transaction.setRegisterNumber(registerNumber);
        transaction.setEmployeeId(employeeId);
        transaction.setCustomerId(customerId);
        transaction.setTransactionDate(new Date());
        transaction.setTransactionType((byte) 1);
        transaction.setSubTotal(subTotal);
        transaction.setDiscountTotal(discountTotal);
        transaction.setTaxTotal(taxTotal);
        transaction.setGrandTotal(grandTotal);
        transaction.setTenderAmount(totalPayment);
        transaction.setChangeAmount(changeAmount);
        transaction.setPromotionId(promo != null ? promo.getPromotionId() : null);
        transaction.setStatus((byte) 1);

        int loyaltyPointsEarned = 0;
        if (customerId != null) {
            loyaltyPointsEarned = grandTotal.intValue() * 10;
            transaction.setLoyaltyPointsEarned(loyaltyPointsEarned);
        }

        int transactionId = transactionDAO.createFullTransaction(transaction, items, payments);

        if (promo != null) {
            promotionDAO.incrementUsage(promo.getPromotionId());
        }

        if (customerId != null) {
            customerDAO.updateLoyaltyPoints(customerId, loyaltyPointsEarned);
            customerDAO.recordVisit(customerId, grandTotal);
        }

        for (PaymentRequest payReq : paymentRequests) {
            if (payReq.getGiftCardNumber() != null && !payReq.getGiftCardNumber().isEmpty()) {
                GiftCard gc = giftCardDAO.findByCardNumber(payReq.getGiftCardNumber());
                if (gc != null) {
                    giftCardDAO.deductBalance(gc.getGiftCardId(), payReq.getAmount());
                }
            }
        }

        return transactionId;
    }

    public void voidTransaction(int transactionId, int voidEmployeeId, String reason) throws SQLException {
        Transaction txn = transactionDAO.findById(transactionId);
        if (txn == null) {
            throw new SQLException("Transaction not found: " + transactionId);
        }
        if (txn.getStatus() != 1) {
            throw new SQLException("Transaction cannot be voided. Current status: " + txn.getStatus());
        }

        transactionDAO.voidTransaction(transactionId, voidEmployeeId, reason);

        if (txn.getCustomerId() != null) {
            customerDAO.updateLoyaltyPoints(txn.getCustomerId(), -txn.getLoyaltyPointsEarned());
        }
    }

    public List<Map<String, Object>> getDailySalesReport(int storeId, Date date) throws SQLException {
        return transactionDAO.getSalesByHour(storeId, date);
    }

    public List<Map<String, Object>> getEmployeeSalesReport(int storeId, Date startDate, Date endDate) throws SQLException {
        return transactionDAO.getSalesByEmployee(storeId, startDate, endDate);
    }

    public static class TransactionItemRequest {
        private int productId;
        private int quantity;
        private String serialNumber;

        public int getProductId() { return productId; }
        public void setProductId(int productId) { this.productId = productId; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
        public String getSerialNumber() { return serialNumber; }
        public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }
    }

    public static class PaymentRequest {
        private byte paymentMethod;
        private BigDecimal amount;
        private String referenceNumber;
        private String cardType;
        private String cardLastFour;
        private String authorizationCode;
        private String checkNumber;
        private String giftCardNumber;

        public byte getPaymentMethod() { return paymentMethod; }
        public void setPaymentMethod(byte paymentMethod) { this.paymentMethod = paymentMethod; }
        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }
        public String getReferenceNumber() { return referenceNumber; }
        public void setReferenceNumber(String referenceNumber) { this.referenceNumber = referenceNumber; }
        public String getCardType() { return cardType; }
        public void setCardType(String cardType) { this.cardType = cardType; }
        public String getCardLastFour() { return cardLastFour; }
        public void setCardLastFour(String cardLastFour) { this.cardLastFour = cardLastFour; }
        public String getAuthorizationCode() { return authorizationCode; }
        public void setAuthorizationCode(String authorizationCode) { this.authorizationCode = authorizationCode; }
        public String getCheckNumber() { return checkNumber; }
        public void setCheckNumber(String checkNumber) { this.checkNumber = checkNumber; }
        public String getGiftCardNumber() { return giftCardNumber; }
        public void setGiftCardNumber(String giftCardNumber) { this.giftCardNumber = giftCardNumber; }
    }
}
