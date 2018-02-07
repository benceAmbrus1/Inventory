package com.codecool;

public class Main {

    public static void main(String[] args) {
        StoreManager storeManager = new StoreManager();
        PersistentStore persistentStore = new PersistentStore();
        storeManager.addStorage(persistentStore);

        storeManager.addCDProduct("Twenty one pilot",4500,10);
        storeManager.addCDProduct("Avanged Sevenfold",5000,12);
        storeManager.addBookProduct("Game of Thrones", 6000, 1000);
        storeManager.addBookProduct("It", 5000, 1000);
        System.out.println(storeManager.listProducts());
        System.out.println(storeManager.getTotalProductPrice());

        System.out.println("-------------------save-then-load-------------------------");

        persistentStore.saveToXml("Products.xml");
        persistentStore.loadProducts("Products.xml");
        System.out.println(storeManager.listProducts());
        System.out.println(storeManager.getTotalProductPrice());
    }
}
