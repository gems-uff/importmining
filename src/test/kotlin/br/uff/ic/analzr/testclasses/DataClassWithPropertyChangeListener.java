package br.uff.ic.analzr.testclasses;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class DataClassWithPropertyChangeListener {

    private int field;

    public DataClassWithPropertyChangeListener(int field){

        this.field = field;

    }

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }

    private String value;

    public String getValue() {
        return this.value;
    }

    public void setValue(String newValue) {
        String oldValue = this.value;
        this.value = newValue;
        this.pcs.firePropertyChange("value", oldValue, newValue);
    }
}
