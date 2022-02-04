package com.example.mye_commerceuserapp.models;

public class UserProductModel extends ProductsModel{
    private boolean isInCart;
    private boolean isFavourite;

    public boolean isInCart() {
        return isInCart;
    }

    public void setInCart(boolean inCart) {
        isInCart = inCart;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }
}
