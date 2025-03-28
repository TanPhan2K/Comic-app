package com.example.Project_android_2.activities.RC_recyclerView;

public class comic_category {
    private String CREATE_AT;
    private String ID_CATEGORY;
    private String ID_COMIC;
    private String TITLE_CATEGORY;

    public comic_category() {
    }

    public comic_category(String CREATE_AT, String ID_CATEGORY, String ID_COMIC, String TITLE_CATEGORY) {
        this.CREATE_AT = CREATE_AT;
        this.ID_CATEGORY = ID_CATEGORY;
        this.ID_COMIC = ID_COMIC;
        this.TITLE_CATEGORY = TITLE_CATEGORY;
    }

    public String getCREATE_AT() {
        return CREATE_AT;
    }

    public void setCREATE_AT(String CREATE_AT) {
        this.CREATE_AT = CREATE_AT;
    }

    public String getID_CATEGORY() {
        return ID_CATEGORY;
    }

    public void setID_CATEGORY(String ID_CATEGORY) {
        this.ID_CATEGORY = ID_CATEGORY;
    }

    public String getID_COMIC() {
        return ID_COMIC;
    }

    public void setID_COMIC(String ID_COMIC) {
        this.ID_COMIC = ID_COMIC;
    }

    public String getTITLE_CATEGORY() {
        return TITLE_CATEGORY;
    }

    public void setTITLE_CATEGORY(String TITLE_CATEGORY) {
        this.TITLE_CATEGORY = TITLE_CATEGORY;
    }
}