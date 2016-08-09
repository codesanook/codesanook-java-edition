package com.codesanook.dto;

public class MetaTag {

    private String ogUrl;
    private String ogType;
    private String ogTitle;
    private String ogDescription;
    private String ogImage;

    private final long fbAppId = 161236587286870L;

    public MetaTag() {
        ogUrl = "http://codesanook.com";
        ogType = "website";
        ogTitle = "codesanook.com : Let's code and have fun";
        ogDescription = "codesanook.com Thai software engineers community, Java, C#, PHP, JavaScript";
        ogImage = "https://s3-ap-southeast-1.amazonaws.com/codesanook-static/resources/codesanook-logo.png";
    }

    public static MetaTag getInstance(){
        return new MetaTag();
    }

    public String getOgUrl() {
        return ogUrl;
    }

    public void setOgUrl(String ogUrl) {
        this.ogUrl = ogUrl;
    }

    public String getOgType() {
        return ogType;
    }

    public void setOgType(String ogType) {
        this.ogType = ogType;
    }

    public String getOgTitle() {
        return ogTitle;
    }

    public void setOgTitle(String ogTitle) {
        this.ogTitle = ogTitle;
    }

    public String getOgDescription() {
        return ogDescription;
    }

    public void setOgDescription(String ogDescription) {
        this.ogDescription = ogDescription;
    }

    public String getOgImage() {
        return ogImage;
    }

    public void setOgImage(String ogImage) {
        this.ogImage = ogImage;
    }

    public long getFbAppId() {
        return fbAppId;
    }
}
