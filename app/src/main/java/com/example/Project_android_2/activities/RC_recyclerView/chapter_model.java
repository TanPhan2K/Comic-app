package com.example.Project_android_2.activities.RC_recyclerView;

public class chapter_model {
    private String ID;
    private int CHAPTER_INDEX;
    private String CONTENT;
    private String ID_COMIC;
    private String TITLE;
    private String create_At;
    private int has_html;

    public chapter_model() {
    }

    public chapter_model(String ID, int CHAPTER_INDEX, String CONTENT, String ID_COMIC, String TITLE, String create_At, int has_html) {
        this.ID = ID;
        this.CHAPTER_INDEX = CHAPTER_INDEX;
        this.CONTENT = CONTENT;
        this.ID_COMIC = ID_COMIC;
        this.TITLE = TITLE;
        this.create_At = create_At;
        this.has_html = has_html;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public int getCHAPTER_INDEX() {
        return CHAPTER_INDEX;
    }

    public void setCHAPTER_INDEX(int CHAPTER_INDEX) {
        this.CHAPTER_INDEX = CHAPTER_INDEX;
    }

    public String getCONTENT() {
        return CONTENT;
    }

    public void setCONTENT(String CONTENT) {
        this.CONTENT = CONTENT;
    }

    public String getID_COMIC() {
        return ID_COMIC;
    }

    public void setID_COMIC(String ID_COMIC) {
        this.ID_COMIC = ID_COMIC;
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

    public int getHas_html() {
        return has_html;
    }

    public void setHas_html(int has_html) {
        this.has_html = has_html;
    }
}