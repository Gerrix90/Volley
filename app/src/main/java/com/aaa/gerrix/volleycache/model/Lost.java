package com.aaa.gerrix.volleycache.model;


public class Lost {

    private String title, image;
    private int id;

    public Lost(String title, String image, int id) {
        this.title = title;
        this.image = image;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public int getId() {
        return id;
    }
}
