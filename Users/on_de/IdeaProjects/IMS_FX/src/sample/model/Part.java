package sample.model;

import javafx.beans.property.*;

import java.sql.Connection;

public class Part {

    private SimpleIntegerProperty id;
    private SimpleStringProperty name;
    private SimpleDoubleProperty price;
    private SimpleIntegerProperty stock;
    private SimpleIntegerProperty min;
    private SimpleIntegerProperty max;

    public Part(int id, String name, double price, int stock, int min, int max) {
        this.id = new SimpleIntegerProperty(id);
        this.name= new SimpleStringProperty(name);
        this.price= new SimpleDoubleProperty(price);
        this.stock= new SimpleIntegerProperty(stock);
        this.min= new SimpleIntegerProperty(min);
        this.max= new SimpleIntegerProperty(max);
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
