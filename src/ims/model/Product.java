package ims.model;

import com.sun.javafx.collections.ObservableSequentialListWrapper;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Product {

    private static ObservableList<Part> parts = FXCollections.observableArrayList();
    private final IntegerProperty productID;
    private final StringProperty name;
    private final DoubleProperty price;
    private final IntegerProperty inStock;
    private final IntegerProperty min;
    private final IntegerProperty max;


    //// Constructor
    public Product() {
        productID = new SimpleIntegerProperty();
        name = new SimpleStringProperty();
        price = new SimpleDoubleProperty();
        inStock = new SimpleIntegerProperty();
        min = new SimpleIntegerProperty();
        max = new SimpleIntegerProperty();
    }


    //// Getters

    public IntegerProperty productIDProperty() {
        return productID;
    }

    public StringProperty productNameProperty() {
        return name;
    }

    public DoubleProperty productPriceProperty() {
        return price;
    }

    public IntegerProperty productInvProperty() {
        return inStock;
    }

    public IntegerProperty productMinProperty() {
        return min;
    }

    public IntegerProperty productMaxProperty() {
        return max;
    }

    public int getProductID() {
        return this.productID.get();
    }

    public String getProductName() {
        return this.name.get();
    }

    public double getProductPrice() {
        return this.price.get();
    }

    public int getProductInStock() {
        return this.inStock.get();
    }

    public int getProductMin() {
        return this.min.get();
    }

    public int getProductMax() {
        return this.max.get();
    }

    public ObservableList getProductParts() {
        return parts;
    }


    //// Setters
    public void setProductID(int productID) {
        this.productID.set(productID);
    }

    public void setProductName(String name) {
        this.name.set(name);
    }

    public void setProductPrice(double price) {
        this.price.set(price);
    }

    public void setProductInStock(int inStock) {
        this.inStock.set(inStock);
    }

    public void setProductMin(int min) {
        this.min.set(min);
    }

    public void setProductMax(int max) {
        this.max.set(max);
    }

    public void setProductParts(ObservableList<Part> parts) {
        this.parts = parts;
    }


    //// Validation
    public static String isProductValid(String name, int min, int max, int inv, double price, ObservableList<Part> parts, String errorMessage) {
        double sumOfParts = 0.00;
         for (int i = 0; i < parts.size(); i++) {
             sumOfParts = sumOfParts + parts.get(i).getPartPrice();
         }

        if (name == null) {
            errorMessage = errorMessage + "The name field is required. ";
        }
        if (inv < 1) {
            errorMessage = errorMessage + "The inventory count cannot be less than 1. ";
        }
        if (price <= 0) {
            errorMessage = errorMessage + "The price must be greater than $0. ";
        }
        if (max < min) {
            errorMessage = errorMessage + "The Max must be greater than or equal to the Min. ";
        }
        if (inv < min || inv > max) {
            errorMessage = errorMessage + "The inventory must be between the Min and Max values. ";
        }
        if (sumOfParts > price) {
            errorMessage = errorMessage + "Price must be greater than the sum of all part costs. ";
        }
        return errorMessage;
    }
}
