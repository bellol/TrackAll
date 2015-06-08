package com.example.bellng.trackall.enums;

/**
 * Created by bellng on 11/05/2015.
 */
public enum AccountType {
    Myki("Myki");

    private String displayName;

    private AccountType(String displayName){
        this.displayName = displayName;
    }

    public String toString(){
        return displayName;
    }
}
