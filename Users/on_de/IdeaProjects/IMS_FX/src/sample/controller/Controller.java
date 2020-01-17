package sample.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.model.*;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class Controller implements Initializable {
    public TableView tableParts;
    public TableView tableProducts;
    public TableColumn partIDCol;
    public TableColumn partNmCol;
    public TableColumn partInvCol;
    public TableColumn partPriceCol;
    public TableColumn prdctIDCol;
    public TableColumn prdctNmCol;
    public TableColumn prdctInvCol;
    public TableColumn prdctPriceCol;
    public TextField searchPartTxtBx;
    public TextField searchProdTxtBx;

    private Inventory inventory;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        inventory = new Inventory();

        tableParts.setItems(inventory.getAllParts());
        tableProducts.setItems(inventory.getAllProducts());

        partIDCol.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        partNmCol.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        partInvCol.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));

        prdctIDCol.setCellValueFactory(new PropertyValueFactory<Product, Integer>("id"));
        prdctNmCol.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        prdctInvCol.setCellValueFactory(new PropertyValueFactory<Product, Integer>("stock"));
        prdctPriceCol.setCellValueFactory(new PropertyValueFactory<Product, Double>("price"));

    }

    public void makeAlert(String str) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Warning");
        alert.setHeaderText(str);
        Optional<ButtonType> result = alert.showAndWait();
    }

    public void AddPart(ActionEvent event) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/AddPartView.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        AddPartController controller = fxmlLoader.<AddPartController>getController();
        controller.init(inventory);

        Stage stage = new Stage();
        stage.setScene(new Scene(root1));
        stage.show();

    }


    public void AddProduct(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/AddProductView.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        AddProductController controller = fxmlLoader.<AddProductController>getController();
        controller.init(inventory);

        Stage stage = new Stage();
        stage.setScene(new Scene(root1));
        stage.show();
    }

    public void ModProduct(ActionEvent event) throws IOException {
        if (tableProducts.getSelectionModel().getSelectedItem() != null) {
            Product p = (Product) tableProducts.getSelectionModel().getSelectedItem();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/AddProductView.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            AddProductController controller = fxmlLoader.<AddProductController>getController();
            controller.init(inventory, p);

            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
        } else {
            makeAlert("Pick the part in the table");
        }
    }

    public void ModPart(ActionEvent event) throws IOException {

        if (tableParts.getSelectionModel().getSelectedItem() != null) {
            Part p = (Part) tableParts.getSelectionModel().getSelectedItem();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/AddPartView.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            AddPartController controller = fxmlLoader.<AddPartController>getController();

            controller.init(inventory, p);

            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
        } else {
            makeAlert("Pick the part in the table");
        }
    }

    public void deletePart(ActionEvent event) {
        if (tableParts.getSelectionModel().getSelectedItem() != null) {
            Part p = (Part) tableParts.getSelectionModel().getSelectedItem();
            inventory.deletePart(p, p.getId());
        } else {

            makeAlert("Pick the part in the table");
        }

    }

    public void deleteProduct(ActionEvent event) {
        if (tableProducts.getSelectionModel().getSelectedItem() != null) {
            Product p = (Product) tableProducts.getSelectionModel().getSelectedItem();
            System.out.println(p.getId());
           inventory.deleteProduct(p, p.getId());
        } else {
            makeAlert("Pick the part in the table");
        }
    }

    public void searchPart(ActionEvent event) {
        if (searchPartTxtBx.getText().length() != 0) {
            String str = searchPartTxtBx.getText();
if (inventory.lookupPart(str).size()!=0)
            tableParts.setItems(inventory.lookupPart(str));
else{makeAlert("No part found!");
}
        } else {
            tableParts.setItems(inventory.getAllParts());
        }
        searchPartTxtBx.setText("");
    }

    public void searchProduct(ActionEvent event) {
        if (searchProdTxtBx.getText().length() != 0) {
            String str = searchProdTxtBx.getText();
            if (inventory.lookupProduct(str).size()!=0)
                tableProducts.setItems(inventory.lookupProduct(str));
            else{makeAlert("No product found!");}

        } else {
            tableProducts.setItems(inventory.getAllProducts());
        }
        searchProdTxtBx.setText("");
    }


}
