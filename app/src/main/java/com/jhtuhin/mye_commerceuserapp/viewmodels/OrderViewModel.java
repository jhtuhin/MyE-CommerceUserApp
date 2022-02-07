package com.jhtuhin.mye_commerceuserapp.viewmodels;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.WriteBatch;
import com.jhtuhin.mye_commerceuserapp.callbacks.OnActionCompleteListener;
import com.jhtuhin.mye_commerceuserapp.models.CartModel;
import com.jhtuhin.mye_commerceuserapp.models.OrderModel;
import com.jhtuhin.mye_commerceuserapp.models.OrderSettingsModel;
import com.jhtuhin.mye_commerceuserapp.utils.UserConstants;


import java.util.List;

public class OrderViewModel extends ViewModel {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private MutableLiveData<OrderSettingsModel> settingsModelMutableLiveData =
            new MutableLiveData<>();
    private OrderSettingsModel orderSettingsModel;
    public MutableLiveData<OrderSettingsModel> getSettingsModelMutableLiveData() {
        return settingsModelMutableLiveData;
    }

    public void getOrderSettings() {
        db.collection(UserConstants.dbCollections.COLLECTION_ORDER_SETTINGS)
                .document(UserConstants.dbCollections.DOCUMENT_ORDER_SETTING)
                .addSnapshotListener((value, error) -> {
                    if (error != null) return;
                    settingsModelMutableLiveData.postValue(
                            value.toObject(OrderSettingsModel.class));
                });

    }

    public double getDiscountAmount(double price, double discount) {
        return (price * discount) / 100;
    }

    public double getVatAmount(double price, double vat) {
        return (price * vat) / 100;
    }

    public double getGrandTotal(double price, double vat, double discount, double deliveryCharge) {
        return (price - getDiscountAmount(price, discount))
                +getVatAmount(price, vat) + deliveryCharge;
    }

    public OrderSettingsModel getOrderSettingsModel() {
        return orderSettingsModel;
    }

    public void setOrderSettingsModel(OrderSettingsModel orderSettingsModel) {
        this.orderSettingsModel = orderSettingsModel;
    }

    public void addNewOrder(OrderModel orderModel, List<CartModel> cartModelList, OnActionCompleteListener actionCompleteListener) {
        final WriteBatch writeBatch = db.batch();
        final DocumentReference orderDoc =
                db.collection(UserConstants.dbCollections.COLLECTION_ORDER)
                .document();
        orderModel.setOrderId(orderDoc.getId());
        writeBatch.set(orderDoc, orderModel);

        for (CartModel c : cartModelList) {
            final DocumentReference doc =
                    db.collection(UserConstants.dbCollections.COLLECTION_ORDER)
                    .document(orderDoc.getId())
                    .collection(UserConstants.dbCollections.COLLECTION_ORDER_DETAILS)
                    .document();
            writeBatch.set(doc, c);
        }

        writeBatch.commit()
                .addOnSuccessListener(unused -> {
                    actionCompleteListener.onSuccess();
                })
                .addOnFailureListener(unused -> {
                    actionCompleteListener.onFailure();
                });
    }
}
