package sample.model;

import javafx.beans.property.SimpleStringProperty;
import sample.extended.DB_Manager;

public class Outsourced extends Part {
    private SimpleStringProperty companyName;

    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = new SimpleStringProperty(companyName);
    }

    public void setCompanyName(String companyName) {
        this.companyName.set(companyName);
    }

    public String getCompanyName() {
        return companyName.get();
    }
}
