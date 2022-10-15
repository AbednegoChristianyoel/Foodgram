package com.example.foodgram.Model;

public class Post {
    private String postid;
    private String postimage;
    private String description;
    private String publisher;
    private String judul;
    private String bahanres;
    private String carares;

    public Post(String postid, String postimage, String description, String publisher, String judul, String bahanres, String carares) {
        this.postid = postid;
        this.postimage = postimage;
        this.description = description;
        this.publisher = publisher;
        this.judul = judul;
        this.bahanres = bahanres;
        this.carares = carares;
    }
    public Post(){
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getPostimage() {
        return postimage;
    }

    public void setPostimage(String postimage) {
        this.postimage = postimage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getBahanres() {
        return bahanres;
    }

    public void setBahanres(String bahanres) {
        this.bahanres = bahanres;
    }

    public String getCarares() {
        return carares;
    }

    public void setCarares(String carares) {
        this.carares = carares;
    }
}
