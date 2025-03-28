package com.example.Project_android_2.models;

public class commic {
    private int id;
    private String title;
    private String sub_title;
    private String thumbnail;
    private String synopsis;
    private String country;
    private String author;
    private String publish_by;

    public commic() {

    }

    public commic(int id, String title, String sub_title, String thumbnail, String synopsis, String country,
                 String author, String publish_by) {
        this.id = id;
        this.title = title;
        this.sub_title = sub_title;
        this.thumbnail = thumbnail;
        this.synopsis = synopsis;
        this.country = country;
        this.author = author;
        this.publish_by = publish_by;
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

    public String getSub_title() {
        return sub_title;
    }

    public void setSub_title(String sub_title) {
        this.sub_title = sub_title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublish_by() {
        return publish_by;
    }

    public void setPublish_by(String publish_by) {
        this.publish_by = publish_by;
    }
}