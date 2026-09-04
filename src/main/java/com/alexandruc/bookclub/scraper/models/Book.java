package com.alexandruc.bookclub.scraper.models;

public class Book {
    private String coverUrl;
    private String title;
    private String author;
    private double rating;
    private String link;

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }





    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }






    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }






    public String getLink() {
        return link;
    }


    public void setLink(String link) {
        this.link = link;
    }






}
