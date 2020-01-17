package sample.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.extended.DB_Manager;

public class Product {
    private ObservableList<Part> associatedParts;
    private SimpleIntegerProperty id;
    private SimpleStringProperty name;
    private SimpleDoubleProperty price;
    private SimpleIntegerProperty stock;
    private SimpleIntegerProperty min;
    private SimpleIntegerProperty max;

    public Product(int id, String name, double price, int stock, int max, int min) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.price =  new SimpleDoubleProperty(price);
        this.stock = new SimpleIntegerProperty(stock);
        this.max = new SimpleIntegerProperty(max);
        this.min = new SimpleIntegerProperty(min);
        associatedParts = FXCollections.observableArrayList();
    }


    public void addAssociatedPart(Part part)
    {
        associatedParts.add(part);
        DB_Manager.getInstance().addAssociatedPart(part,this.getId());
    }
    public void deleteAssociatedPart(Part associatedPart)
    {
        associatedParts.remove(associatedPart);
        DB_Manager.getInstance().deleteAssociatedPart(associatedPart.getId(), this.getId());
    }
    public ObservableList<Part> getAllAssociatedParts()
    {
        return associatedParts;
    }

    public void setAssociatedParts(ObservableList<Part> associatedParts) {
        this.associatedParts = associatedParts;
        System.out.println(this.getId());
        DB_Manager.getInstance().addAssociatedParts(associatedParts,this.getId());
    }

    public int getId() {
        return id.get();
    }


    public void setId(int id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }


    public void setName(String name) {
        this.name.set(name);
    }

    public double getPrice() {
        return price.get();
    }


    public void setPrice(double price) {
        this.price.set(price);
    }

    public int getStock() {
        return stock.get();
    }


    public void setStock(int stock) {
        this.stock.set(stock);
    }

    public int getMin() {
        return min.get();
    }


    public void setMin(int min) {
        this.min.set(min);
    }

    public int getMax() {
        return max.get();
    }


    public void setMax(int max) {
        this.max.set(max);
    }
}
