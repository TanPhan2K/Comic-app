package com.example.Project_android_2.activities.RC_recyclerView;

public class author_model {
    private String ID_author;
    private String INFO;
    private String NAME;
    private String create_At;

    public author_model() {
    }

    public author_model(String ID_author, String INFO, String NAME, String create_At) {
        this.ID_author = ID_author;
        this.INFO = INFO;
        this.NAME = NAME;
        this.create_At = create_At;
    }

    public String getID_author() {
        return ID_author;
    }

    public void setID_author(String ID_author) {
        this.ID_author = ID_author;
    }

    public String getINFO() {
        return INFO;
    }

    public void setINFO(String INFO) {
        this.INFO = INFO;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getCreate_At() {
        return create_At;
    }

    public void setCreate_At(String create_At) {
        this.create_At = create_At;
    }
}