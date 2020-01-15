package sample.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.extended.Checker;
import sample.model.*;


import javax.persistence.criteria.CriteriaBuilder;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddPartController implements Initializable {
    public RadioButton outSrcRdBtn;
    public RadioButton inHouseRdBtn;
    public Button saveBtn;
    public Button cancelBtn;
    public TextField idTxtBx;
    public TextField nameTxtBx;
    public TextField invTxtBx;
    public TextField priceTxtBx;
    public TextField maxTxtBx;
    public TextField minTxtBx;
    public TextField corpTxtBx;
    public Label cmpLabel;

    private Inventory inv;
    private Part partToModify;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ToggleGroup group = new ToggleGroup();

        outSrcRdBtn.setToggleGroup(group);
        inHouseRdBtn.setToggleGroup(group);

        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

            public void changed(ObservableValue<? extends Toggle> changed, Toggle oldValue, Toggle newValue) {

                RadioButton selectedBtn = (RadioButton) newValue;
                String str = "";
                if (selectedBtn.getText().equals(outSrcRdBtn.getText())) {
                    str = "Company Name";
                } else {
                    str = "Machine ID";
                }
                cmpLabel.setText(str);
                corpTxtBx.setPromptText(str);
            }
        });

    }


    public void save(ActionEvent actionEvent) {
      Part part = addPart();
      if(part != null&&partToModify==null) {
          inv.addPart(part);
      }
      else if(part != null&&partToModify!=null)
      {
          inv.updatePart(inv.getAllParts().indexOf(partToModify), part);
      }
      else
     {
          return;
      }
      Stage stage = (Stage) saveBtn.getScene().getWindow();
        stage.close();
    }

    protected Part addPart()
    {
        Checker checker = new Checker();
        if (idTxtBx.getText().length() != 0 && nameTxtBx.getText().length() != 0 && maxTxtBx.getText().length() != 0 &&
                minTxtBx.getText().length() != 0 && priceTxtBx.getText().length() != 0 && invTxtBx.getText().length() != 0 && corpTxtBx.getText().length() != 0) {

            int id = Integer.parseInt(idTxtBx.getText());
            String name = nameTxtBx.getText();

            int min, max, stock;
            double price;
            Part p;
            checker.checkInt(maxTxtBx.getText(), "Max");
            checker.checkInt(minTxtBx.getText(), "Min");
            checker.checkDouble(priceTxtBx.getText());
            checker.checkInt(invTxtBx.getText(), "Inv");

            if (checker.getErrors() == 0) {
                min = Integer.parseInt(minTxtBx.getText());
                max = Integer.parseInt(maxTxtBx.getText());
                stock = Integer.parseInt(invTxtBx.getText());
                price = Double.parseDouble(priceTxtBx.getText());
                checker.checkMaxMin(max,min);
                if (inHouseRdBtn.isSelected()) {
                    checker.checkInt(corpTxtBx.getText(), "MachineID");
                    if (checker.getErrors() == 0) {
                        int machineId = Integer.parseInt(corpTxtBx.getText());

                        p = new InHouse(id, name, price, stock, min, max, machineId);

                    } else return null;

                } else {
                    String corp = corpTxtBx.getText();
                    p = new Outsourced(id, name, price, stock, min, max, corp);

                }
                return p;

            }
        } else {
            checker.makeAlert("Not all statements are filled!");
        }
        return null;
    }



    public void init(Inventory inventory) {
        inv = inventory;
        idTxtBx.setText(String.valueOf(inv.getPartID()));
    }

    public void init(Inventory inventory, Part p) {
        inv = inventory;
        partToModify = p;
        if (p instanceof InHouse) {
            InHouse i = (InHouse) p;
            inHouseRdBtn.selectedProperty().setValue(true);
            outSrcRdBtn.selectedProperty().setValue(false);
            corpTxtBx.setText(String.valueOf(i.getMachineId()));
        } else {
            Outsourced o = (Outsourced) p;
            corpTxtBx.setText(o.getCompanyName());
        }
        idTxtBx.setText(String.valueOf(p.getId()));
        nameTxtBx.setText(p.getName());
        invTxtBx.setText(String.valueOf(p.getStock()));
        maxTxtBx.setText(String.valueOf(p.getMax()));
        minTxtBx.setText(String.valueOf(p.getMin()));
        priceTxtBx.setText(String.valueOf(p.getPrice()));
    }

    public void cancelButtonAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Warning");
        alert.setHeaderText("Cancel?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            Stage stage = (Stage) cancelBtn.getScene().getWindow();
            stage.close();
        } else {
            return;
        }
    }
}
