package com.codesanook.dto.comments;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PartialComment {
private String content;

@JsonProperty("isSubstring")
private boolean isSubstring;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isSubstring() {
        return isSubstring;
    }


    public boolean getIsSubstring() {
        return isSubstring;
    }

    public void setIsSubstring(boolean isSubstring) {
        this.isSubstring = isSubstring;
    }
}
