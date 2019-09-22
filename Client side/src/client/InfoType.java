package com.example.zvi.basicApp.client;

import android.content.Intent;

/**
 * An object that can hold an integer or string but not both.
 */
public class InfoType {
    private final int number;
    private final String text;

    public InfoType(int number){
        this.number = number;
        text = null;
    }

    public InfoType(String text){
        this.text = text;
        number = 0;
    }

    public boolean isNumber(){
        return text==null;
    }

    public int getNumber() {
        return number;
    }

    public String getText() {
        return text;
    }
}
