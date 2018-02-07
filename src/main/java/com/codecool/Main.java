package com.codecool;

public class Main {

    public static void main(String[] args) {
        StoreManager storeManager = new StoreManager();
        PersistentStore persistentStore = new PersistentStore();
        storeManager.addStorage(persistentStore);

        storeManager.addCDProduct("Avanged Sevenfold",5000,12);
        storeManager.addBookProduct("It", 5000, 1000);

        persistentStore.saveToXml("Products.xml");
        /*persistentStore.loadProducts("Products.xml");
        System.out.println(storeManager.listProducts());
        System.out.println(storeManager.getTotalProductPrice());*/
    }
}
