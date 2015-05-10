package com.example.bellng.trackall;

/**
 * Created by bellng on 10/05/2015.
 */
public enum PackageType {
    AusPost("Australia Post");

    private String displayName;

    private PackageType(String displayName){
        this.displayName = displayName;
    }

    public String toString(){
        return displayName;
    }
}
