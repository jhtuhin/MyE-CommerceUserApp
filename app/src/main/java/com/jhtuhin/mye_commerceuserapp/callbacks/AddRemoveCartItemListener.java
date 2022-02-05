package com.jhtuhin.mye_commerceuserapp.callbacks;

import com.jhtuhin.mye_commerceuserapp.models.CartModel;

public interface AddRemoveCartItemListener {
    void onCartItemAdd(CartModel cartModel,int position);
    void onCartItemRemove(String productId,int position);
}
