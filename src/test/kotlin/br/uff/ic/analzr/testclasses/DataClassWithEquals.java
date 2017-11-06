package br.uff.ic.analzr.testclasses;

public class DataClassWithEquals {

    private int field;

    public DataClassWithEquals(int field){

        this.field = field;

    }

    public int getField() {
        return field;
    }

    public void setField(int field) {
        this.field = field;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
