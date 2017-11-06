package br.uff.ic.analzr.testclasses;

public class DataClassWithToString {

    private int field;

    public DataClassWithToString(int field){

        this.field = field;

    }

    public int getField() {
        return field;
    }

    public void setField(int field) {
        this.field = field;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
