package com.example.project_prm.Entities;

import androidx.room.Embedded;
import androidx.room.Ignore;

public class CartWithProduct {
    @Embedded
    private Cart cart;

    private String productName;
    private double productPrice;

    public CartWithProduct() {
    }

    @Ignore
    public CartWithProduct(Cart cart, String productName, double productPrice) {
        this.cart = cart;
        this.productName = productName;
        this.productPrice = productPrice;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }
}