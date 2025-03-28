package com.example.Project_android_2.activities.RC_recyclerView;

public class author_comic_model {
    private String ID_comic;
    private String Name_comic;
    private String ID_author;
    private String Name_author;
    private String Image_comic;
    public author_comic_model() {
    }

    public author_comic_model(String ID_comic, String name_comic, String ID_author, String name_author,String Image_comic) {
        this.ID_comic = ID_comic;
        this.Name_comic = name_comic;
        this.ID_author = ID_author;
        this.Name_author = name_author;
        this.Image_comic = Image_comic;
    }

    public String getID_comic() {
        return ID_comic;
    }

    public void setID_comic(String ID_comic) {
        this.ID_comic = ID_comic;
    }

    public String getName_comic() {
        return Name_comic;
    }

    public void setName_comic(String name_comic) {
        Name_comic = name_comic;
    }

    public String getID_author() {
        return ID_author;
    }

    public void setID_author(String ID_author) {
        this.ID_author = ID_author;
    }

    public String getName_author() {
        return Name_author;
    }

    public void setName_author(String name_author) {
        Name_author = name_author;
    }

    public String getImage_comic() {
        return Image_comic;
    }

    public void setImage_comic(String image_comic) {
        Image_comic = image_comic;
    }
}