package com.example.Project_android_2.activities.RC_recyclerView;

public class comic_model {
    private String AUTHOR;
    private String COUNTRY;
    private String CREATE_AT;
    private String ID;
    private String PUBLISH_BY;
    private String SUB_TITLE;
    private String SYNOPSIS;
    private String THUMBNAIL;
    private String TITLE;

    public comic_model() {
    }

    public comic_model(String AUTHOR, String COUNTRY, String CREATE_AT, String ID, String PUBLISH_BY, String SUB_TITLE, String SYNOPSIS, String THUMBNAIL, String TITLE) {
        this.AUTHOR = AUTHOR;
        this.COUNTRY = COUNTRY;
        this.CREATE_AT = CREATE_AT;
        this.ID = ID;
        this.PUBLISH_BY = PUBLISH_BY;
        this.SUB_TITLE = SUB_TITLE;
        this.SYNOPSIS = SYNOPSIS;
        this.THUMBNAIL = THUMBNAIL;
        this.TITLE = TITLE;
    }

    public String getAUTHOR() {
        return AUTHOR;
    }

    public void setAUTHOR(String AUTHOR) {
        this.AUTHOR = AUTHOR;
    }

    public String getCOUNTRY() {
        return COUNTRY;
    }

    public void setCOUNTRY(String COUNTRY) {
        this.COUNTRY = COUNTRY;
    }

    public String getCREATE_AT() {
        return CREATE_AT;
    }

    public void setCREATE_AT(String CREATE_AT) {
        this.CREATE_AT = CREATE_AT;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPUBLISH_BY() {
        return PUBLISH_BY;
    }

    public void setPUBLISH_BY(String PUBLISH_BY) {
        this.PUBLISH_BY = PUBLISH_BY;
    }

    public String getSUB_TITLE() {
        return SUB_TITLE;
    }

    public void setSUB_TITLE(String SUB_TITLE) {
        this.SUB_TITLE = SUB_TITLE;
    }

    public String getSYNOPSIS() {
        return SYNOPSIS;
    }

    public void setSYNOPSIS(String SYNOPSIS) {
        this.SYNOPSIS = SYNOPSIS;
    }

    public String getTHUMBNAIL() {
        return THUMBNAIL;
    }

    public void setTHUMBNAIL(String THUMBNAIL) {
        this.THUMBNAIL = THUMBNAIL;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }
}