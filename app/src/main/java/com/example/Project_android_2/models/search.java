package com.example.Project_android_2.models;

public class search {
    private int id;
    private String keyword;
    private String id_comic;
    public search(){

    }

    public search(int id, String keyword, String id_comic) {
        this.id = id;
        this.keyword = keyword;
        this.id_comic = id_comic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getId_comic() {
        return id_comic;
    }

    public void setId_comic(String id_comic) {
        this.id_comic = id_comic;
    }
}
