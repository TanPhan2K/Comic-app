package com.example.Project_android_2.models;

public class category {
    private int id;
    private String title;
    private String thumbnail;
    private String display_color;

    public category() {

    }

    public category(int id, String title, String thumbnail, String display_color) {
        this.id = id;
        this.title = title;
        this.thumbnail = thumbnail;
        this.display_color = display_color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDisplay_color() {
        return display_color;
    }

    public void setDisplay_color(String display_color) {
        this.display_color = display_color;
    }
}
