package com.codecool;

import java.util.List;

public class StoreManager {

    StorageCapable storage;

    public void addStorage(StorageCapable storage){
        this.storage = storage;
    }

    public void addCDProduct(String name, int price, int tracks){
        storage.storeCDProduct(name, price, tracks);
    }

    public void addBookProduct(String name, int price, int pages){
        storage.storeBookProduct(name, price, pages);
    }

    public String listProducts(){
        List<Product> listOfProducts = storage.getAllProduct();
        String products = "";
        for (int i = 0; i < listOfProducts.size(); i++) {
            if (i == listOfProducts.size() - 1) {
                products += listOfProducts.get(i).getName();
            } else {
                products += listOfProducts.get(i).getName() + ", ";
            }
        }
        return products;
    }

    public int getTotalProductPrice(){
        int sumOfPrices = 0;
        List<Product> listOfProducts = storage.getAllProduct();
        for (int i = 0; i < listOfProducts.size() ; i++) {
            sumOfPrices += listOfProducts.get(i).getPrice();
        }
        return sumOfPrices;
    }
}
