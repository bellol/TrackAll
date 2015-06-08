package com.example.bellng.trackall.enums;

/**
 * Created by bellng on 10/05/2015.
 */
public enum PackageType {
    AusPost("Australia Post","australia-post"),
    USPS("United States Postal Service","usps"),
    StarTrack("StarTrack","star-track"),
    FedEx("FedEx","fedex"),
    DHL("DHL","dhl"),
    TNTAU("TNT","tnt-au"),
    UPS("UPS","ups"),
    TollPriority("Toll Priority","toll-priority"),
    TollIPEC("Toll IPEC","toll-ipec")
    ;

    private String displayName,slugName;

    private PackageType(String displayName, String slugName){
        this.displayName = displayName;
        this.slugName = slugName;
    }

    public String toString(){
        return displayName;
    }
    public String getSlugName() {return slugName; }
}
