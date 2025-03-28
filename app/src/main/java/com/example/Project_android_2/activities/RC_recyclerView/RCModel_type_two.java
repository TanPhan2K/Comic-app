package com.example.Project_android_2.activities.RC_recyclerView;

public class RCModel_type_two {
    private String name_comic;
    private String name_author;
    public RCModel_type_two (String name_comic,String name_author){
        this.name_comic = name_comic;
        this.name_author = name_author;
    }

    public String getName_comic() {
        return name_comic;
    }

    public void setName_comic(String name_comic) {
        this.name_comic = name_comic;
    }

    public String getName_author() {
        return name_author;
    }

    public void setName_author(String name_author) {
        this.name_author = name_author;
    }
}