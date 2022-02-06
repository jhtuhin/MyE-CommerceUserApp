package com.jhtuhin.mye_commerceuserapp.callbacks;


import com.jhtuhin.mye_commerceuserapp.models.CartModel;

import java.util.List;

public interface OnCartItemQuantityChangeListener {
    void onCartItemQuantityChange(List<CartModel> models);
}
