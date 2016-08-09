package com.codesanook.viewmodel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public enum UiAlertType implements Serializable {

    INFO(1),
    WARNING(2),
    ERROR(3);
    private int typeId;

    private static final Map<Integer, String> cssClassLookup = new HashMap<>();

    static {
        cssClassLookup.put(UiAlertType.INFO.getTypeId(), "alert-info");
        cssClassLookup.put(UiAlertType.WARNING.getTypeId(), "alert-warning");
        cssClassLookup.put(UiAlertType.ERROR.getTypeId(), "alert-danger");
    }


    UiAlertType(int typeId) {
        this.typeId = typeId;
    }

    public int getTypeId() {
        return this.typeId;
    }

    public String getCssClass() {
        return cssClassLookup.get(this.getTypeId());
    }
}
