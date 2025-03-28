package com.example.Project_android_2.models;

public class chapter {
    private String id;
    private String title;
    private String index;
    private String content;
    private String id_comic;
    private String create_At;

    public chapter() {

    }

    public chapter(String id, String title, String index, String content, String id_comic, String create_At) {
        this.id = id;
        this.title = title;
        this.index = index;
        this.content = content;
        this.id_comic = id_comic;
        this.create_At = create_At;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public String getCreate_At() {
        return create_At;
    }

    public void setCreate_At(String create_At) {
        this.create_At = create_At;
    }
    public String getId_comic() {
        return id_comic;
    }

    public void setId_comic(String id_comic) {
        this.id_comic = id_comic;
    }
}