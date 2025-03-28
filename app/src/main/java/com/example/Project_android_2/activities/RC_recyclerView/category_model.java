package com.example.Project_android_2.activities.RC_recyclerView;

public class category_model {
    private String DISPLAY_COLOR;
    private String ID;
    private String TITLE;
    private String create_At;
    private String info;
    private String thumbnail;

    public category_model() {
    }

    public category_model(String DISPLAY_COLOR, String ID, String TITLE, String create_At, String info, String thumbnail) {
        this.DISPLAY_COLOR = DISPLAY_COLOR;
        this.ID = ID;
        this.TITLE = TITLE;
        this.create_At = create_At;
        this.info = info;
        this.thumbnail = thumbnail;
    }

    public String getDISPLAY_COLOR() {
        return DISPLAY_COLOR;
    }

    public void setDISPLAY_COLOR(String DISPLAY_COLOR) {
        this.DISPLAY_COLOR = DISPLAY_COLOR;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public String getCreate_At() {
        return create_At;
    }

    public void setCreate_At(String create_At) {
        this.create_At = create_At;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}