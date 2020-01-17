package sample.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.extended.Checker;
import sample.model.*;


import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AddProductController implements Initializable {

    public Button addBtn;
    public Button deleteBtn;
    public Button saveBtn;
    public Button searchBtn;
    public TextField searchTxtBx;
    public TextField prdctIDTxtBx;
    public TextField prdctNmTxtBx;
    public TextField prdctInvTxtBx;
    public TextField prdctPriceTxtBx;
    public TextField prdctMaxTxtBx;
    public TextField prdctMinTxtBx;
    public Button cancelBtn;
    public TableView containingPartsTable;
    public TableColumn containsPartIdCol;
    public TableColumn containsPartNameCol;
    public TableColumn containsPartInvCol;
    public TableColumn containsPartPriceCol;
    public TableView availablePartsTable;
    public TableColumn availablePartIdCol;
    public TableColumn availablePartNameCol;
    public TableColumn availablePartInvCol;
    public TableColumn availablePartPriceCol;

    private Inventory inv;
    private ObservableList<Part> containingList;
    private ObservableList<Part> availableList;
    private Product productToModify;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        containingList = FXCollections.observableArrayList();

    }



    public void save(ActionEvent actionEvent) {
        Product prod = addProduct();
        if (prod != null && productToModify == null) {
            inv.addProduct(prod);

        } else if (prod != null && productToModify != null) {
            System.out.println();
            inv.updateProduct(inv.getAllProducts().indexOf(productToModify), prod);
        } else {
            return;
        }

        Stage stage = (Stage) saveBtn.getScene().getWindow();
        stage.close();
    }

    public Product addProduct() {
        Checker checker = new Checker();
        if (containingList.size() == 0) {
            checker.makeAlert("Product should contain at least 1 part!");
            return null;
        }
        if (prdctIDTxtBx.getText().length() != 0 && prdctNmTxtBx.getText().length() != 0 && prdctMaxTxtBx.getText().length() != 0 && prdctMinTxtBx.getText().length() != 0 &&
                prdctPriceTxtBx.getText().length() != 0 && prdctInvTxtBx.getText().length() != 0) {

            int id = Integer.parseInt(prdctIDTxtBx.getText());
            String name = prdctNmTxtBx.getText();

            int min, max, stock;
            double price;
            Product p;//prod to modify
            checker.checkInt(prdctMaxTxtBx.getText(), "Max");
            checker.checkInt(prdctMinTxtBx.getText(), "Min");
            checker.checkDouble(prdctPriceTxtBx.getText());
            checker.checkInt(prdctInvTxtBx.getText(), "Inv");

            if (checker.getErrors() == 0) {
                min = Integer.parseInt(prdctMinTxtBx.getText());
                max = Integer.parseInt(prdctMaxTxtBx.getText());
                stock = Integer.parseInt(prdctInvTxtBx.getText());
                price = Double.parseDouble(prdctPriceTxtBx.getText());
                checker.checkMaxMin(max, min);
                p = new Product(id, name, price, stock, max, min);

                p.setAssociatedParts(containingList);

            } else {
                return null;
            }
            return p;
        } else {
            checker.makeAlert("Not all statements are filled!");
        }
        return null;
    }

    public void initTables() {
        this.availableList = inv.getAllParts();
        prdctIDTxtBx.setText(String.valueOf(inv.getProductID()));

        availablePartsTable.setItems(availableList);
        containingPartsTable.setItems(containingList);

        availablePartIdCol.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        availablePartNameCol.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        availablePartInvCol.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        availablePartPriceCol.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));

        containsPartIdCol.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        containsPartNameCol.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        containsPartInvCol.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        containsPartPriceCol.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));
    }

    public void init(Inventory inventory) {
        inv = inventory;
        initTables();
    }

    public void init(Inventory inventory, Product p) {
        inv = inventory;
        productToModify = p;
        prdctInvTxtBx.setText(String.valueOf(p.getStock()));
        prdctMinTxtBx.setText(String.valueOf(p.getMin()));
        prdctMaxTxtBx.setText(String.valueOf(p.getMax()));
        prdctIDTxtBx.setText(String.valueOf(p.getId()));
        prdctPriceTxtBx.setText(String.valueOf(p.getPrice()));
        prdctNmTxtBx.setText(p.getName());
        containingList = p.getAllAssociatedParts();
        initTables();
    }


    public void addPart(ActionEvent actionEvent) {
        Part p = (Part) availablePartsTable.getSelectionModel().getSelectedItem();
        containingList.add(p);
    }

    public void deletePart(ActionEvent actionEvent) {
        Part p = (Part) containingPartsTable.getSelectionModel().getSelectedItem();
        containingList.remove(containingList.indexOf(p));
    }

    public void cancelButtonAction(ActionEvent actionEvent) {

        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();

    }

    public void searchPart(ActionEvent actionEvent) {
        Checker checker = new Checker();
        if (searchTxtBx.getText().length() != 0) {
            String str = searchTxtBx.getText();
            if (inv.lookupPart(str).size() != 0)
                availablePartsTable.setItems(inv.lookupPart(str));
            else {
                checker.makeAlert("No part found!");
            }
        } else {
            availablePartsTable.setItems(availableList);
        }
        searchTxtBx.setText("");
    }
}
