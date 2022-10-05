package com.example.foodgram.Model;

public class User {

    private String id;
    private String email;
    private String nama;
    private String imageurl;
    private String bio;
    private String search;

    public User(String id, String email, String nama, String imageurl, String bio, String search) {
        this.id = id;
        this.email = email;
        this.nama = nama;
        this.imageurl = imageurl;
        this.bio = bio;
        this.search = search;
    }

    public User(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
