package sample.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.extended.DB_Manager;

import java.util.stream.Collectors;

public class Inventory {
    ObservableList<Part> allParts;
    ObservableList<Product> allProducts;
    private int partID ;
    private int productID;

    public Inventory() {
        allProducts = FXCollections.observableArrayList();
        allParts = FXCollections.observableArrayList();

        partID = DB_Manager.getInstance().getLastPartID()+1;
        productID = DB_Manager.getInstance().getLastProductID()+1;

        if (DB_Manager.getInstance().getProducts()!=null && DB_Manager.getInstance().getParts()!=null) {
            allProducts = DB_Manager.getInstance().getProducts();
            allParts = DB_Manager.getInstance().getParts();
        }


    }

    public int getPartID() {
        return partID;
    }

    public void setPartID(int partID) {
        this.partID = partID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public void addPart(Part part) {

        allParts.add(part);

        DB_Manager.getInstance().addPart(part);
        setPartID(getPartID()+1);
    }

    public void addProduct(Product product) {
        allProducts.add(product);
        DB_Manager.getInstance().addProduct(product);

        setProductID(getProductID()+1);
    }

    public Part lookupPart(int partId) {
        return allParts.stream().filter(t -> t.getId() == partId).findFirst().get();
    }

    public Product lookupProduct(int productId) {
        return allProducts.stream().filter(t -> t.getId() == productId).findFirst().get();
    }

    public ObservableList<Part> lookupPart(String str) {
        return allParts.stream().filter(t->t.getName().contains(str)).collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    public ObservableList<Product> lookupProduct(String str) {
        return allProducts.stream().filter(t->t.getName().contains(str)).collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    public void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
        DB_Manager.getInstance().updatePart(selectedPart,index);
    }

    public void updateProduct(int index, Product selectedProduct) {
        allProducts.set(index, selectedProduct);
        DB_Manager.getInstance().updateProduct(selectedProduct, index);
    }

    public void deletePart(Part selectedPart, int id) {
        DB_Manager.getInstance().deletePart(id);
        allParts.remove(selectedPart);

    }

    public void deleteProduct(Product selectedProduct, int id) {
        DB_Manager.getInstance().deleteProduct(id);
        allProducts.remove(selectedProduct);

    }

    public ObservableList<Part> getAllParts() {
        return allParts;
    }

    public ObservableList<Product> getAllProducts() {
        return allProducts;
    }
}

