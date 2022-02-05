package com.jhtuhin.mye_commerceuserapp.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.jhtuhin.mye_commerceuserapp.callbacks.OnActionCompleteListener;
import com.jhtuhin.mye_commerceuserapp.models.CartModel;
import com.jhtuhin.mye_commerceuserapp.models.ProductsModel;
import com.jhtuhin.mye_commerceuserapp.models.UserProductModel;
import com.jhtuhin.mye_commerceuserapp.utils.UserConstants;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;
import java.util.List;

public class ProductViewModel extends ViewModel {
    public static final String TAG=ProductViewModel.class.getSimpleName();
    private final FirebaseFirestore db=FirebaseFirestore.getInstance();
    public MutableLiveData<List<String>> categoryListLiveData=new MutableLiveData<>();
    public MutableLiveData<List<ProductsModel>> productsListLiveData =new MutableLiveData<>();
    public MutableLiveData<List<UserProductModel>> userProductListLiveData=new MutableLiveData<>();
    public MutableLiveData<List<CartModel>> cartListLiveData = new MutableLiveData<>();

    public ProductViewModel(){
        getAllCategories();
        getAllProducts();
    }
    public void addToCart(CartModel cartModel, String uid, OnActionCompleteListener completeListener) {
        db.collection(UserConstants.dbCollections.COLLECTION_USER)
                .document(uid)
                .collection(UserConstants.dbCollections.COLLECTION_CART)
                .document(cartModel.getProductId())
                .set(cartModel)
                .addOnSuccessListener(unused -> {
                    completeListener.onSuccess();
                }).addOnFailureListener(e -> {
            completeListener.onFailure();
        });
    }

    public void removeFromCart(String uid, String productId, OnActionCompleteListener completeListener) {
        db.collection(UserConstants.dbCollections.COLLECTION_USER)
                .document(uid)
                .collection(UserConstants.dbCollections.COLLECTION_CART)
                .document(productId)
                .delete()
                .addOnSuccessListener(unused -> {
                    completeListener.onSuccess();
                }).addOnFailureListener(e -> {
            completeListener.onFailure();
        });
    }

    private void getAllCategories(){
        db.collection(UserConstants.dbCollections.COLLECTION_CATEGORY)
                .addSnapshotListener((value, error) -> {
                    if(error != null) return;;
                    final List<String> items=new ArrayList<>();
                    for(DocumentSnapshot doc: value.getDocuments()){
                        items.add(doc.get("name",String.class));
                    }
                    categoryListLiveData.postValue(items);
                });
    }

    public void getAllProducts(){
        db.collection(UserConstants.dbCollections.COLLECTION_PRODUCT).whereEqualTo("status",true)
                .addSnapshotListener((value, error) -> {
            if(error != null) return;;

            final List<ProductsModel> items=new ArrayList<>();
            for (DocumentSnapshot doc : value.getDocuments()) {
                items.add(doc.toObject(ProductsModel.class));
            }

            productsListLiveData.postValue(items);
        });
    }

    public void getAllCartItems(String uid) {
        db.collection(UserConstants.dbCollections.COLLECTION_USER)
                .document(uid)
                .collection(UserConstants.dbCollections.COLLECTION_CART)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    final List<CartModel> cartModels = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        cartModels.add(doc.toObject(CartModel.class));
                    }
                    prepareUserProductList(cartModels);
                }).addOnFailureListener(e -> {

        });
    }

    public void getAllCartItemSnapshot(String uid) {
        db.collection(UserConstants.dbCollections.COLLECTION_USER)
                .document(uid)
                .collection(UserConstants.dbCollections.COLLECTION_CART)
                .addSnapshotListener((value, error) -> {
                    if (error != null) return;
                    final List<CartModel> cartModels = new ArrayList<>();
                    for (DocumentSnapshot doc : value.getDocuments()) {
                        cartModels.add(doc.toObject(CartModel.class));
                    }
                    cartListLiveData.postValue(cartModels);
                });
    }

    private void prepareUserProductList(List<CartModel> cartModels) {
        List<UserProductModel> userProductModels = new ArrayList<>();
        for (ProductsModel p : productsListLiveData.getValue()) {
            final UserProductModel upm = new UserProductModel();
            upm.setProductID(p.getProductID());
            upm.setProductName(p.getProductName());
            upm.setCategory(p.getCategory());
            upm.setDescription(p.getDescription());
            upm.setPrice(p.getPrice());
            upm.setProductImageUrl(p.getProductImageUrl());
            upm.setStatus(p.isStatus());
            upm.setInCart(false);
            upm.setFavourite(false);
            userProductModels.add(upm);
        }

        if (!cartModels.isEmpty()){
            for (CartModel c : cartModels) {
                for (UserProductModel up : userProductModels) {
                    if (c.getProductId().equals(up.getProductID())) {
                        up.setInCart(true);
                    }
                }
            }
        }

        userProductListLiveData.postValue(userProductModels);
    }

    public void getAllProductsByCategory(String category) {
        db.collection(UserConstants.dbCollections.COLLECTION_PRODUCT)
                .whereEqualTo("category", category)
                .addSnapshotListener((value, error) -> {
                    if (error != null) return;

                    final List<ProductsModel> items = new ArrayList<>();
                    for (DocumentSnapshot doc : value.getDocuments()) {
                        items.add(doc.toObject(ProductsModel.class));
                    }

                    productsListLiveData.postValue(items);
                });
    }

    public LiveData<ProductsModel> getProductByProductId(String productId) {
        final MutableLiveData<ProductsModel> productLiveData =
                new MutableLiveData<>();
        db.collection(UserConstants.dbCollections.COLLECTION_PRODUCT)
                .document(productId)
                .addSnapshotListener((value, error) -> {
                    if (error != null) return;
                    productLiveData.postValue(
                            value.toObject(ProductsModel.class));
                });
        return productLiveData;
    }

}
