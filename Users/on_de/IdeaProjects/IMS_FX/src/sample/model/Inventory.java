package sample.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.stream.Collectors;

public class Inventory {
    ObservableList<Part> allParts;
    ObservableList<Product> allProducts;
    private int partID = 0;
    private int productID = 0;

    public Inventory() {
        allProducts = FXCollections.observableArrayList();
        allParts = FXCollections.observableArrayList();
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
        setPartID(getPartID()+1);
    }

    public void addProduct(Product product) {
        allProducts.add(product);
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
    }

    public void updateProduct(int index, Product selectedProduct) {
        allProducts.set(index, selectedProduct);
    }

    public void deletePart(Part selectedPart) {
        allParts.remove(selectedPart);
    }

    public void deleteProduct(Product selectedProduct) {
        allProducts.remove(selectedProduct);
    }

    public ObservableList<Part> getAllParts() {
        return allParts;
    }

    public ObservableList<Product> getAllProducts() {
        return allProducts;
    }
}

