package ims.view_controller;

import ims.model.Inventory;
import ims.model.Part;
import ims.model.Product;
import ims.Main;
import static ims.model.Inventory.validatePartDelete;
import static ims.model.Inventory.validateProductDelete;
import static ims.model.Inventory.getPartInventory;
import static ims.model.Inventory.deletePart;
import static ims.model.Inventory.getProductInventory;
import static ims.model.Inventory.removeProduct;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;

import java.io.IOException;
import java.util.Optional;

public class MainScreenController implements Initializable {

    @FXML
    private TableView<Part> tvParts;
    @FXML
    private TableColumn<Part, Integer> tvPartsIDColumn;
    @FXML
    private TableColumn<Part, String> tvPartsNameColumn;
    @FXML
    private TableColumn<Part, Integer> tvPartsInvColumn;
    @FXML
    private TableColumn<Part, Double> tvPartsPriceColumn;
    @FXML
    private TableView<Product> tvProducts;
    @FXML
    private TableColumn<Product, Integer> tvProductsIDColumn;
    @FXML
    private TableColumn<Product, String> tvProductsNameColumn;
    @FXML
    private TableColumn<Product, Integer> tvProductsInvColumn;
    @FXML
    private TableColumn<Product, Double> tvProductsPriceColumn;
    @FXML
    private TextField txtSearchParts;
    @FXML
    private TextField txtSearchProducts;


    private static Part modifyPart;
    private static int modifyPartIndex;
    private static Product modifyProduct;
    private static int modifyProductIndex;

    public static int partToModifyIndex() {
        return modifyPartIndex;
    }

    public static int productToModifyIndex() {
        return modifyProductIndex;
    }


    //// Parts section
    @FXML
    private void clearPartsSearch(ActionEvent event) {
        updatePartsTableView();
        txtSearchParts.setText("");
    }

    @FXML
    private void handlePartsSearch(ActionEvent event) {
        String searchPart = txtSearchParts.getText();
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
            tvParts.setItems(tempPartList);
        }
    }

    @FXML
    private void handlePartsDelete(ActionEvent event) {
        Part part = tvParts.getSelectionModel().getSelectedItem();
        if (validatePartDelete(part)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Deletion Error");
            alert.setHeaderText("Part cannot be deleted!");
            alert.setContentText("Part is being used by one or more products.");
            alert.showAndWait();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Part Deletion");
            alert.setHeaderText("Confirm?");
            alert.setContentText("Are you sure you want to delete " + part.getPartName() + "?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                deletePart(part);
                updatePartsTableView();
                System.out.println("Part " + part.getPartName() + " was removed.");
            }
            else {
                System.out.println("Part " + part.getPartName() + " was not removed.");
            }
        }
    }

    @FXML
    private void openAddPartScreen(ActionEvent event) throws IOException {
        Parent addPartParent = FXMLLoader.load(getClass().getResource("AddPart.fxml"));
        Scene addPartScene = new Scene(addPartParent);
        Stage addPartStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        addPartStage.setScene(addPartScene);
        addPartStage.show();
    }

    @FXML
    private void openModifyPartScreen(ActionEvent event) throws IOException {
        modifyPart = tvParts.getSelectionModel().getSelectedItem();
        modifyPartIndex = getPartInventory().indexOf(modifyPart);
        Parent modifyPartParent = FXMLLoader.load(getClass().getResource("ModifyPart.fxml"));
        Scene modifyPartScene = new Scene(modifyPartParent);
        Stage modifyPartStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        modifyPartStage.setScene(modifyPartScene);
        modifyPartStage.show();
    }


    //// Products section
    @FXML
    private void clearProductsSearch(ActionEvent event) {
        updateProductsTableView();
        txtSearchProducts.setText("");
    }

    @FXML
    private void handleProductsSearch(ActionEvent event) {
        String searchProduct = txtSearchProducts.getText();
        int prodIndex = -1;
        if (Inventory.lookupProduct(searchProduct) == -1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Search Error");
            alert.setHeaderText("Product not found");
            alert.setContentText("The search term entered does not match any known products.");
            alert.showAndWait();
        }
        else {
            prodIndex = Inventory.lookupProduct(searchProduct);
            Product tempProduct = Inventory.getProductInventory().get(prodIndex);
            ObservableList<Product> tempProductList = FXCollections.observableArrayList();
            tempProductList.add(tempProduct);
            tvProducts.setItems(tempProductList);
        }
    }

    @FXML
    private void handleProductsDelete(ActionEvent event) {
        Product product = tvProducts.getSelectionModel().getSelectedItem();
        if (validateProductDelete(product)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Deletion Error");
            alert.setHeaderText("Product cannot be deleted!");
            alert.setContentText("Product contains one or more parts.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Product Deletion");
            alert.setHeaderText("Confirm Delete?");
            alert.setContentText("Are you sure you want to delete " + product.getProductName() + "?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                removeProduct(product);
                updateProductsTableView();
                System.out.println("Product " + product.getProductName() + " was removed.");
            } else {
                System.out.println("Product " + product.getProductName() + " was removed.");
            }
        }
    }

    @FXML
    private void openAddProductScreen(ActionEvent event) throws IOException {
        Parent addProductParent = FXMLLoader.load(getClass().getResource("AddProduct.fxml"));
        Scene addProductScene = new Scene(addProductParent);
        Stage addProductStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        addProductStage.setScene(addProductScene);
        addProductStage.show();
    }

    @FXML
    private void openModifyProductScreen(ActionEvent event) throws IOException {
        modifyProduct = tvProducts.getSelectionModel().getSelectedItem();
        modifyProductIndex = getProductInventory().indexOf(modifyProduct);
        Parent modifyProductParent = FXMLLoader.load(getClass().getResource("ModifyProduct.fxml"));
        Scene modifyProductScene = new Scene(modifyProductParent);
        Stage modifyProductStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        modifyProductStage.setScene(modifyProductScene);
        modifyProductStage.show();
    }


    //// Update table views
    public void updatePartsTableView() {
        tvParts.setItems(getPartInventory());
    }

    public void updateProductsTableView() {
        tvProducts.setItems(getProductInventory());
    }


    //// Confirm exit on Main screen
    @FXML
    private void exitButton(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm Exit");
        alert.setHeaderText("Confirm Exit");
        alert.setContentText("Are you sure you want to exit?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            System.exit(0);
        }
        else {
            System.out.println("You clicked cancel.");
        }
    }


    /* public void setMainApp(Main mainApp) {
        updatePartsTableView();
        updateProductsTableView();
    } */


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tvPartsIDColumn.setCellValueFactory(cellData -> cellData.getValue().partIDproperty().asObject());
        tvPartsNameColumn.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        tvPartsInvColumn.setCellValueFactory(cellData -> cellData.getValue().partInvProperty().asObject());
        tvPartsPriceColumn.setCellValueFactory(cellData -> cellData.getValue().partPriceProperty().asObject());
        tvProductsIDColumn.setCellValueFactory(cellData -> cellData.getValue().productIDProperty().asObject());
        tvProductsNameColumn.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());
        tvProductsInvColumn.setCellValueFactory(cellData -> cellData.getValue().productInvProperty().asObject());
        tvProductsPriceColumn.setCellValueFactory(cellData -> cellData.getValue().productPriceProperty().asObject());
        updatePartsTableView();
        updateProductsTableView();
    }
}
