package ims.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class InHousePart extends Part {

    private final IntegerProperty partMachineID;

    public InHousePart() {
        super();
        partMachineID = new SimpleIntegerProperty();
    }

    public int getPartMachineID() {
        return this.partMachineID.get();
    }

    public void setPartMachineID(int partMachineID) {
        this.partMachineID.set(partMachineID);
    }
}
