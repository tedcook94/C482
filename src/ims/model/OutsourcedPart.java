package ims.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class OutsourcedPart extends Part {

    private final StringProperty partCompanyName;

    public OutsourcedPart() {
        super();
        partCompanyName = new SimpleStringProperty();
    }

    public String getPartCompanyName() {
        return this.partCompanyName.get();
    }

    public void setPartCompanyName(String partCompanyName) {
        this.partCompanyName.set(partCompanyName);
    }
}
