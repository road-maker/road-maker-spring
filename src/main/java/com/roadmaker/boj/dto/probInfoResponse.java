package com.roadmaker.boj.dto;

public class probInfoResponse {
    String probNum;
    String probTitle;
    String probDescription;

    public probInfoResponse(String probNum, String probTitle, String probDescription) {
        this.probNum = probNum;
        this.probTitle = probTitle;
        this.probDescription = probDescription;
    }

    public String getProbNum() {
        return probNum;
    }

    public void setProbNum(String probNum) {
        this.probNum = probNum;
    }

    public String getProbTitle() {
        return probTitle;
    }

    public void setProbTitle(String probTitle) {
        this.probTitle = probTitle;
    }

    public String getProbDescription() {
        return probDescription;
    }

    public void setProbDescription(String probDescription) {
        this.probDescription = probDescription;
    }
}
