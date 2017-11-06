package br.uff.ic.analzr.testclasses;

import java.util.List;

public class NormalClass {
    private int field;

    private NormalClass normalDependency;



    private List<PureDataClass> collectionDependency;

    private DataClassWithPropertyChangeListener dcwpclDependency;

    private PureDataClass dcDependency;

    private DataClassWithToString dcwtsDependency;

    private DataClassWithEquals dcweDependency;

    public NormalClass getNormalDependency() {
        return normalDependency;
    }

    public PureDataClass getDcDependency() {
        return dcDependency;
    }

    public void setNormalDependency(NormalClass normalDependency) {
        this.normalDependency = normalDependency;
    }

    public void setDcDependency(PureDataClass dcDependency) {
        this.dcDependency = dcDependency;
    }

    public DataClassWithEquals getDcweDependency() {
        return dcweDependency;
    }

    public void setDcweDependency(DataClassWithEquals dcweDependency) {
        this.dcweDependency = dcweDependency;
    }

    public DataClassWithToString getDcwtsDependency() {
        return dcwtsDependency;
    }

    public void setDcwtsDependency(DataClassWithToString dcwtsDependency) {
        this.dcwtsDependency = dcwtsDependency;
    }

    public List<PureDataClass> getCollectionDependency() {
        return collectionDependency;
    }

    public void setCollectionDependency(List<PureDataClass> collectionDependency) {
        this.collectionDependency = collectionDependency;
    }

    public DataClassWithPropertyChangeListener getDcwpclDependency() {
        return dcwpclDependency;
    }

    public void setDcwpclDependency(DataClassWithPropertyChangeListener dcwpclDependency) {
        this.dcwpclDependency = dcwpclDependency;
    }

    public NormalClass(int field){

        this.field = field;

    }

    public int getField() {
        return field;
    }

    public void setField(int field) {
        this.field = field;
    }

    public long doSomething(int value){
        return value + field;
    }
}
