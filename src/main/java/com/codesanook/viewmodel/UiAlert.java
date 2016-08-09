package com.codesanook.viewmodel;

import java.io.Serializable;

public class UiAlert implements Serializable {
    private UiAlertType type;
    private String message;

    public static final String KEY = UiAlert.class.getSimpleName();

    public  UiAlert(UiAlertType type, String message){
        this.type = type;
        this.message = message;
    }

    public UiAlertType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

}
