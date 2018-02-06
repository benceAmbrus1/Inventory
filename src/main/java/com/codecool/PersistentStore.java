package com.codecool;

public class PersistentStore extends Store {

    public void storeProduct(Product product){
        getAllProduct().add(product);
    }
}
