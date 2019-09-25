package com.jason.avengers.infos;

import javax.lang.model.element.TypeElement;

public class VariableInfo {

    private String autoMergeClassFullName;
    private TypeElement typeElement;

    public void setAutoMergeClassFullName(String autoMergeClassFullName) {
        this.autoMergeClassFullName = autoMergeClassFullName;
    }

    public String getAutoMergeClassFullName() {
        return autoMergeClassFullName;
    }

    public TypeElement getTypeElement() {
        return typeElement;
    }

    public void setTypeElement(TypeElement typeElement) {
        this.typeElement = typeElement;
    }
}
