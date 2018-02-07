package com.codecool;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Store implements StorageCapable {

    private List<Product> products = new ArrayList<>();

    @Override
    public List<Product> getAllProduct(){
        return products;
    }

    @Override
    public void storeCDProduct(String name, int price, int tracks){
        storeProduct(createProduct("cd", name, price, tracks));
    }

    @Override
    public void storeBookProduct(String name, int price, int pages){
        storeProduct(createProduct("book", name, price, pages));
    }

    public void saveToXml(String fileName){
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("Products");
            doc.appendChild(rootElement);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(fileName));

            for (int i = 0; i < getAllProduct().size() ; i++) {

                Element product1 = doc.createElement("Product");
                rootElement.appendChild(product1);

                if (getAllProduct().get(i) instanceof CDProduct) {

                    product1.setAttribute("type", "cd");
                    product1.setAttribute("name", getAllProduct().get(i).getName());
                    product1.setAttribute("price", Integer.toString(getAllProduct().get(i).getPrice()));
                    product1.setAttribute("tracks", Integer.toString(((CDProduct) getAllProduct().get(i)).getNumOfTracks()));
                    transformer.transform(source, result);

                } else if (getAllProduct().get(i) instanceof BookProduct) {

                    product1.setAttribute("type", "book" );
                    product1.setAttribute("name", getAllProduct().get(i).getName());
                    product1.setAttribute("price", Integer.toString(getAllProduct().get(i).getPrice()));
                    product1.setAttribute("pages", Integer.toString(((BookProduct) getAllProduct().get(i)).getNumOfPages()));
                    transformer.transform(source, result);
                }
            }
        } catch (ParserConfigurationException pcex) {
            pcex.printStackTrace();
        } catch (TransformerException tfex) {
            tfex.printStackTrace();
        }
    }

    protected void storeProduct(Product product){

    }

    protected Product createProduct(String type, String name, int price, int size){
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

    public List<Product> loadProducts(String filename) {
        products.removeAll(products);
        try {
            File fXmlFile = new File(filename);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("Product");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    if (eElement.getAttribute("type").equals("cd")) {
                        Product product = new CDProduct(eElement.getAttribute("name"),
                            Integer.parseInt(eElement.getAttribute("price")),
                            Integer.parseInt(eElement.getAttribute("tracks")));
                        storeProduct(product);
                    } else if (eElement.getAttribute("type").equals("book")) {
                        Product product = new BookProduct(eElement.getAttribute("name"),
                            Integer.parseInt(eElement.getAttribute("price")),
                            Integer.parseInt(eElement.getAttribute("pages")));
                        storeProduct(product);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }
}
