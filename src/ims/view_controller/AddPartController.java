package ims.view_controller;

import ims.model.InHousePart;
import ims.model.Inventory;
import ims.model.OutsourcedPart;
import ims.model.Part;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddPartController implements Initializable {

    @FXML
    private RadioButton radioAddPartInHouse;
    @FXML
    private RadioButton radioAddPartOutsourced;
    @FXML
    private Label lblAddPartIDNumber;
    @FXML
    private TextField txtAddPartName;
    @FXML
    private TextField txtAddPartInv;
    @FXML
    private TextField txtAddPartPrice;
    @FXML
    private TextField txtAddPartMin;
    @FXML
    private TextField txtAddPartMax;
    @FXML
    private Label lblAddPartDyn;
    @FXML
    private TextField txtAddPartDyn;

    private boolean isOutsourced;
    private String exceptionMessage = new String();
    private int partID;

    @FXML
    void addPartInHouseRadio(ActionEvent event) {
        isOutsourced = false;
        lblAddPartDyn.setText("Machine ID");
        txtAddPartDyn.setPromptText("Machine ID");
        radioAddPartOutsourced.setSelected(false);
    }

    @FXML
    void addPartOutsourcedRadio(ActionEvent event) {
        isOutsourced = true;
        lblAddPartDyn.setText("Company Name");
        txtAddPartDyn.setPromptText("Company Name");
        radioAddPartInHouse.setSelected(false);
    }

    @FXML
    void handleAddPartSave(ActionEvent event) throws IOException {
        String partName = txtAddPartName.getText();
        String partInv = txtAddPartInv.getText();
        String partPrice = txtAddPartPrice.getText();
        String partMin = txtAddPartMin.getText();
        String partMax = txtAddPartMax.getText();
        String partDyn = txtAddPartDyn.getText();

        try {
            exceptionMessage = Part.isPartValid(partName, Integer.parseInt(partMin), Integer.parseInt(partMax), Integer.parseInt(partInv), Double.parseDouble(partPrice), exceptionMessage);
            if (exceptionMessage.length() > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Error Adding Part");
                alert.setContentText(exceptionMessage);
                alert.showAndWait();
                exceptionMessage = "";
            }
            else {
                if (isOutsourced == false) {
                    System.out.println("Part name: " + partName);
                    InHousePart inPart = new InHousePart();
                    inPart.setPartID(partID);
                    inPart.setPartName(partName);
                    inPart.setPartPrice(Double.parseDouble(partPrice));
                    inPart.setPartInStock(Integer.parseInt(partInv));
                    inPart.setPartMin(Integer.parseInt(partMin));
                    inPart.setPartMax(Integer.parseInt(partMax));
                    inPart.setPartMachineID(Integer.parseInt(partDyn));
                    Inventory.addPart(inPart);
                } else {
                    System.out.println("Part name: " + partName);
                    OutsourcedPart outPart = new OutsourcedPart();
                    outPart.setPartID(partID);
                    outPart.setPartName(partName);
                    outPart.setPartPrice(Double.parseDouble(partPrice));
                    outPart.setPartInStock(Integer.parseInt(partInv));
                    outPart.setPartMin(Integer.parseInt(partMin));
                    outPart.setPartMax(Integer.parseInt(partMax));
                    outPart.setPartCompanyName(partDyn);
                    Inventory.addPart(outPart);
                }

                Parent addPartSave = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                Scene scene = new Scene(addPartSave);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
        }
        catch(NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Error Adding Part");
            alert.setContentText("Form contains blank fields.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleAddPartCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm Cancel");
        alert.setHeaderText("Confirm Cancel");
        alert.setContentText("Are you sure you want to cancel adding a new part?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            Parent addPartCancel = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            Scene scene = new Scene(addPartCancel);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
        else {
            System.out.println("You clicked cancel.");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        partID = Inventory.getPartIDCount();
        lblAddPartIDNumber.setText("Auto-Gen: " + partID);
    }
}
