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
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(fileName));

            Element rootElement = doc.createElement("Store");
            doc.appendChild(rootElement);

            for (int i = 0; i < getAllProduct().size() ; i++) {
                if (getAllProduct().get(i) instanceof CDProduct) {
                    Element product1 = doc.createElement("Product");
                    rootElement.appendChild(product1);
                    Attr attr = doc.createAttribute("type");
                    attr.setValue("cd");
                    product1.setAttributeNode(attr);

                    Element name = doc.createElement("name");
                    name.appendChild(doc.createTextNode(getAllProduct().get(i).getName()));
                    product1.appendChild(name);

                    Element price = doc.createElement("price");
                    price.appendChild(doc.createTextNode(Integer.toString(getAllProduct().get(i).getPrice())));
                    product1.appendChild(price);

                    Element numOfTracks = doc.createElement("tracks");
                    numOfTracks.appendChild(doc.createTextNode(Integer.toString(((CDProduct) getAllProduct().get(i)).getNumOfTracks())));
                    product1.appendChild(numOfTracks);

                    transformer.transform(source, result);

                } else if (getAllProduct().get(i) instanceof BookProduct) {
                    Element product1 = doc.createElement("Product");
                    rootElement.appendChild(product1);
                    Attr attr = doc.createAttribute("type");
                    attr.setValue("book");
                    product1.setAttributeNode(attr);

                    Element name = doc.createElement("name");
                    name.appendChild(doc.createTextNode(getAllProduct().get(i).getName()));
                    product1.appendChild(name);

                    Element price = doc.createElement("price");
                    price.appendChild(doc.createTextNode(Integer.toString(getAllProduct().get(i).getPrice())));
                    product1.appendChild(price);

                    Element numOfTracks = doc.createElement("pages");
                    numOfTracks.appendChild(doc.createTextNode(Integer.toString(((BookProduct) getAllProduct().get(i)).getNumOfPages())));
                    product1.appendChild(numOfTracks);

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
        try {
            File fXmlFile = new File(filename);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();
            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("Product");
            //System.out.println("----------------------------");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                System.out.println("\nCurrent Element :" + nNode.getNodeName());
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    if (eElement.getAttribute("type").equals("cd")) {
                        Product product = new CDProduct(eElement.getElementsByTagName("name").item(0).getTextContent(),
                            Integer.parseInt(eElement.getElementsByTagName("price").item(0).getTextContent()),
                            Integer.parseInt(eElement.getElementsByTagName("tracks").item(0).getTextContent()));
                        products.add(product);
                    } else if (eElement.getAttribute("type").equals("book")) {
                        Product product = new BookProduct(eElement.getElementsByTagName("name").item(0).getTextContent(),
                            Integer.parseInt(eElement.getElementsByTagName("price").item(0).getTextContent()),
                            Integer.parseInt(eElement.getElementsByTagName("pages").item(0).getTextContent()));
                        products.add(product);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }
}
