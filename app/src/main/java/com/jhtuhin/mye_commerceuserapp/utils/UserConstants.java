package com.jhtuhin.mye_commerceuserapp.utils;

public class UserConstants {
    public static final String REQUEST_KEY="date_key";
    public static final String DATE_KEY="date";
    public static final String YEAR="year";
    public static final String MONTH="month";
    public static final String DAY="day";
    public static final String CURRENCY = "৳";

    public static class PaymentMethod {
        public static final String COD = "Cash on Delivery";
        public static final String ONLINE = "Online Payment";
    }

    public static class OrderStatus{
        public static final String PENDING = "Pending";
        public static final String DELIVERED = "Delivered";
        public static final String CANCELLED = "Cancelled";
    }


    public static class dbCollections{
        public static final String COLLECTION_PRODUCT="Products";
        public static final String COLLECTION_PURCHASE="Purchases";
        public static final String COLLECTION_CATEGORY="Categories";
        public static final String COLLECTION_ORDER="Orders";
        public static final String COLLECTION_USER="Users";
        public static final String COLLECTION_CART="Cart";
        public static final String COLLECTION_ORDER_DETAILS = "OrderDetails";
        public static final String COLLECTION_ORDER_SETTINGS = "OrderSettings";
        public static final String DOCUMENT_ORDER_SETTING = "Settings";

    }
}
