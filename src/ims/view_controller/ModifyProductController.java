package ims.view_controller;

import ims.model.Inventory;
import ims.model.Part;
import ims.model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

import static ims.view_controller.MainScreenController.productToModifyIndex;
import static ims.model.Inventory.getPartInventory;
import static ims.model.Inventory.getProductInventory;

public class ModifyProductController implements Initializable {

    private ObservableList<Part> currentParts = FXCollections.observableArrayList();
    private int productIndex = productToModifyIndex();
    private String exceptionMessage = new String();
    private int productID;

    @FXML
    private Label lblModifyProductIDNumber;
    @FXML
    private TextField txtModifyProductName;
    @FXML
    private TextField txtModifyProductInv;
    @FXML
    private TextField txtModifyProductPrice;
    @FXML
    private TextField txtModifyProductMin;
    @FXML
    private TextField txtModifyProductMax;
    @FXML
    private TextField txtModifyProductSearch;
    @FXML
    private TableView<Part> tvModifyProductAdd;
    @FXML
    private TableColumn<Part, Integer> tvModifyProductAddIDColumn;
    @FXML
    private TableColumn<Part, String> tvModifyProductAddNameColumn;
    @FXML
    private TableColumn<Part, Integer> tvModifyProductAddInvColumn;
    @FXML
    private TableColumn<Part, Double> tvModifyProductAddPriceColumn;
    @FXML
    private TableView<Part> tvModifyProductDelete;
    @FXML
    private TableColumn<Part, Integer> tvModifyProductDeleteIDColumn;
    @FXML
    private TableColumn<Part, String> tvModifyProductDeleteNameColumn;
    @FXML
    private TableColumn<Part, Integer> tvModifyProductDeleteInvColumn;
    @FXML
    private TableColumn<Part, Double> tvModifyProductDeletePriceColumn;

    @FXML
    void handleClearSearch(ActionEvent event) {
        updateAddPartsTableView();
        txtModifyProductSearch.setText("");
    }

    @FXML
    void handleSearch(ActionEvent event) {
        String searchPart = txtModifyProductSearch.getText();
        int partIndex = -1;
        if (Inventory.lookupPart(searchPart) == -1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Search Error");
            alert.setHeaderText("Part not found");
            alert.setContentText("The search term entered does not match any known parts.");
            alert.showAndWait();
        }
        else {
            partIndex = Inventory.lookupPart(searchPart);
            Part tempPart = Inventory.getPartInventory().get(partIndex);
            ObservableList<Part> tempPartList = FXCollections.observableArrayList();
            tempPartList.add(tempPart);
            tvModifyProductAdd.setItems(tempPartList);
        }
    }

    @FXML
    void handleAdd(ActionEvent event) {
        Part part = tvModifyProductAdd.getSelectionModel().getSelectedItem();
        currentParts.add(part);
        updateDeletePartsTableView();
    }

    @FXML
    void handleDelete(ActionEvent event) {
        Part part = tvModifyProductDelete.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Part Deletion");
        alert.setHeaderText("Confirm");
        alert.setContentText("Are you sure you want to delete " + part.getPartName() + " from parts?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            currentParts.remove(part);
        }
        else {
            System.out.println("You clicked cancel.");
        }
    }

    @FXML
    private void handleModifyProductSave(ActionEvent event) throws IOException {
        String productName = txtModifyProductName.getText();
        String productInv = txtModifyProductInv.getText();
        String productPrice = txtModifyProductPrice.getText();
        String productMin = txtModifyProductMin.getText();
        String productMax = txtModifyProductMax.getText();

        try {
            exceptionMessage = Product.isProductValid(productName, Integer.parseInt(productMin), Integer.parseInt(productMax), Integer.parseInt(productInv), Double.parseDouble(productPrice), currentParts, exceptionMessage);
            if (exceptionMessage.length() > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Error Modifying Product");
                alert.setContentText(exceptionMessage);
                alert.showAndWait();
                exceptionMessage = "";
            }
            else {
                System.out.println("Product name: " + productName);
                Product newProduct = new Product();
                newProduct.setProductID(productID);
                newProduct.setProductName(productName);
                newProduct.setProductInStock(Integer.parseInt(productInv));
                newProduct.setProductPrice(Double.parseDouble(productPrice));
                newProduct.setProductMin(Integer.parseInt(productMin));
                newProduct.setProductMax(Integer.parseInt(productMax));
                newProduct.setProductParts(currentParts);
                Inventory.updateProduct(productIndex, newProduct);

                Parent modifyProductSaveParent = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                Scene scene = new Scene(modifyProductSaveParent);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
        }
        catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Error Modifying Product");
            alert.setContentText("Form contains blank fields.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleModifyProductCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm Cancel");
        alert.setHeaderText("Confirm Cancel");
        alert.setContentText("Are you sure you want to cancel modifying the product?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            Parent modifyProductCancelParent = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            Scene scene = new Scene(modifyProductCancelParent);
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
        Product product = getProductInventory().get(productIndex);
        productID = getProductInventory().get(productIndex).getProductID();
        lblModifyProductIDNumber.setText("Auto-Gen: " + productID);
        txtModifyProductName.setText(product.getProductName());
        txtModifyProductInv.setText(Integer.toString(product.getProductInStock()));
        txtModifyProductPrice.setText(Double.toString(product.getProductPrice()));
        txtModifyProductMin.setText(Integer.toString(product.getProductMin()));
        txtModifyProductMax.setText(Integer.toString(product.getProductMax()));
        currentParts = product.getProductParts();
        tvModifyProductAddIDColumn.setCellValueFactory(cellData -> cellData.getValue().partIDproperty().asObject());
        tvModifyProductAddNameColumn.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        tvModifyProductAddInvColumn.setCellValueFactory(cellData -> cellData.getValue().partInvProperty().asObject());
        tvModifyProductAddPriceColumn.setCellValueFactory(cellData -> cellData.getValue().partPriceProperty().asObject());
        tvModifyProductDeleteIDColumn.setCellValueFactory(cellData -> cellData.getValue().partIDproperty().asObject());
        tvModifyProductDeleteNameColumn.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        tvModifyProductDeleteInvColumn.setCellValueFactory(cellData -> cellData.getValue().partInvProperty().asObject());
        tvModifyProductDeletePriceColumn.setCellValueFactory(cellData -> cellData.getValue().partPriceProperty().asObject());
        updateAddPartsTableView();
        updateDeletePartsTableView();
    }

    public void updateAddPartsTableView() {
        tvModifyProductAdd.setItems(getPartInventory());
    }

    public void updateDeletePartsTableView() {
        tvModifyProductDelete.setItems(currentParts);
    }
}
