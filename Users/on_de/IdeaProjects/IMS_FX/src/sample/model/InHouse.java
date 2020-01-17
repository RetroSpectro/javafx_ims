package sample.model;

import javafx.beans.property.SimpleIntegerProperty;
import sample.extended.DB_Manager;

public class InHouse extends Part {

    private SimpleIntegerProperty machineId;

    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = new SimpleIntegerProperty(machineId);
    }

    public int getMachineId() {
        return machineId.get();
    }

    public void setMachineId(int machineId) {
        this.machineId.set(machineId);
    }
}
