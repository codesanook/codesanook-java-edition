package com.codesanook.dto;

public class UrlDto {

    private String domain;

    public UrlDto() { }

    public UrlDto(String domain, String relativeUrl) {
        this.domain = domain;
        this.relativeUrl = relativeUrl;
        this.absoluteUrl = String.format("http://%s%s", domain, relativeUrl);
    }

    private String absoluteUrl;
    private String relativeUrl;

    public String getAbsoluteUrl() {
        return absoluteUrl;
    }

    public void setAbsoluteUrl(String absoluteUrl) {
        this.absoluteUrl = absoluteUrl;
    }

    public String getRelativeUrl() {
        return relativeUrl;
    }

    public void setRelativeUrl(String relativeUrl) {
        this.relativeUrl = relativeUrl;
    }
}
