package com.example.foodgram.Model;

public class Bookmark {
    private String postid;

    public Bookmark(String postid) {
        this.postid = postid;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }
}
