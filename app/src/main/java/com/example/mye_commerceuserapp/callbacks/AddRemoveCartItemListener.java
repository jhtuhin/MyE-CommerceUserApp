package com.example.mye_commerceuserapp.callbacks;

import com.example.mye_commerceuserapp.models.CartModel;

public interface AddRemoveCartItemListener {
    void onCartItemAdd(CartModel cartModel,int position);
    void onCartItemRemove(String productId,int position);
}
