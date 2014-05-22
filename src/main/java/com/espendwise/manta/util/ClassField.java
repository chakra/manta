package com.espendwise.manta.util;


import java.util.List;

public class ClassField {

    private String name;
    private Class type;
    private List<ClassField> innerFields;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public List<ClassField> getInnerFields() {
        return innerFields;
    }

    public void setInnerFields(List<ClassField> innerFields) {
        this.innerFields = innerFields;
    }

    @Override
    public String toString() {
        return "ClassField{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", innerFields=" + innerFields +
                '}';
    }
}
