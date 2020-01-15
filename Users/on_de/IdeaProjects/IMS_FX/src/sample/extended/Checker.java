package sample.extended;

import javafx.scene.control.Alert;
import javafx.stage.Modality;
import sample.controller.*;
import sample.model.*;

public class Checker {
    private int err = 0;

    public int getErrors() {
        return err;
    }

    public void makeAlert(String str) {
        err++;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Warning");
        alert.setHeaderText(str);
        alert.showAndWait();
    }

    public void checkInt(String str, String errStr) {
        try {
            int val = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            makeAlert(errStr + " field should be a number!");
        }
    }


    public void checkDouble(String price) {

        try {
            double pr = Double.parseDouble(price);
        } catch (NumberFormatException e) {
            makeAlert("Price field should be a number!");
        }
    }

    public void checkMaxMin(int max, int min) {
        if (max < 0 || min < 0) {
            makeAlert("Max and min should be greater than 0!");
        }
        if (max < min)
        {
            makeAlert("Max should be greater than min!");
        }
    }

}
