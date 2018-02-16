package com.codecool;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvStore implements StorageCapable {

    private List<Product> products = new ArrayList<>();
    FileWriter fileWriter;
    BufferedReader fileReader;

    @Override
    public List<Product> getAllProduct() {
        return products;
    }

    @Override
    public void storeCDProduct(String name, int price, int tracks) {
        storeProduct(createProduct("cd", name, price, tracks));
    }

    @Override
    public void storeBookProduct(String name, int price, int pages) {
        storeProduct(createProduct("book", name, price, pages));
    }

    public Product createProduct(String type, String name, int price, int size){
        Product product = null;
        try {
            if (type.toLowerCase().equals("book")) {
                product = new BookProduct(name, price, size);
            } else if (type.toLowerCase().equals("cd")) {
                product = new CDProduct(name, price, size);
            }

        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
            System.out.println("Not valid type! (Try cd or book!)");
        }
        return product;
    }

    public void storeProduct(Product product){
        getAllProduct().add(product);
    }

    public void saveToCsv(String fileName){
        try{
            fileWriter = new FileWriter(fileName);

            for(Product prod:getAllProduct()){
                if(prod instanceof CDProduct) {
                    fileWriter.append("Cd");
                    fileWriter.append(String.valueOf(","));
                    fileWriter.append(String.valueOf(prod.getName()));
                    fileWriter.append(String.valueOf(","));
                    fileWriter.append(String.valueOf(prod.getPrice()));
                    fileWriter.append(String.valueOf(","));
                    fileWriter.append(String.valueOf(((CDProduct) prod).getNumOfTracks()));
                    fileWriter.append(String.valueOf("\n"));
                }
                else if(prod instanceof BookProduct){
                    fileWriter.append("Book");
                    fileWriter.append(String.valueOf(","));
                    fileWriter.append(String.valueOf(prod.getName()));
                    fileWriter.append(String.valueOf(","));
                    fileWriter.append(String.valueOf(prod.getPrice()));
                    fileWriter.append(String.valueOf(","));
                    fileWriter.append(String.valueOf(((BookProduct) prod).getNumOfPages()));
                    fileWriter.append(String.valueOf("\n"));
                }
            }
        }catch(IOException e){
            System.out.println("Error in CsvFileWriter");
        }finally {
            try{
                fileWriter.flush();
                fileWriter.close();
            }catch (IOException e){
                System.out.println("Error while flushing/closing");
            }
        }
    }

    public List<Product> loadFromCsv(String fileName){
        try{
            products.removeAll(products);
            fileReader = new BufferedReader(new FileReader(fileName));
            String line = "";
            while((line = fileReader.readLine()) != null){
                String[] list = line.split(",");
                if(list.length > 0){
                    if(list[0].equals("Cd")){
                        Product prod = new CDProduct(list[1],
                            Integer.parseInt(list[2]),
                            Integer.parseInt(list[3]));
                        products.add(prod);
                    }else if(list[0].equals("Book")){
                        Product prod = new BookProduct(list[1],
                            Integer.parseInt(list[2]),
                            Integer.parseInt(list[3]));
                        products.add(prod);
                    }
                }
            }

        }catch(Exception e){
            System.out.println("Error in CsvFileReader");
        }finally {
            try{
                fileReader.close();
            }catch (IOException e){
                System.out.println("Error while closing fileReader");
            }
        }
        return products;
    }
}


