package com.alexandruc.bookclub.scraper.models;

import java.util.List;

public class Profile {
    private String profileUrl;
    private String name;
    private String avatarUrl;
    private int reviews;
    private List<Book> currentlyReading;
    private List<Book> toRead;
    private List<Book> read;
    private List<Book> didNotFinish;


    public Profile() {}

    public String getProfileUrl() {
        return profileUrl;
    }
    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarUrl() {return avatarUrl;}
    public void setAvatarUrl(String avatarUrl) {this.avatarUrl = avatarUrl;}

    public void setCurrentlyReading(List<Book> currentlyReading) { this.currentlyReading = currentlyReading; }
    public List<Book> getCurrentlyReading() { return this.currentlyReading; }

    public void setToRead(List<Book> toRead) { this.toRead = toRead; }
    public List<Book> getToRead() { return this.toRead; }

    public void setRead(List<Book> read) { this.read = read; }
    public List<Book> getRead() { return this.read; }

    public void setDidNotFinish(List<Book> didNotFinish) {  this.didNotFinish = didNotFinish; }
    public List<Book> getDidNotFinish() { return this.didNotFinish; }


    public String toString() {
        return "Profile{" +
                "name='" + name + '\'' +
                ", profileUrl='" + profileUrl + '\'' +
                ", reviews=" + reviews +
                ", currentlyReading=" + (currentlyReading != null ? currentlyReading.size() : 0) + " cărți" +
                ", toRead=" + (toRead != null ? toRead.size() : 0) + " cărți" +
                ", read=" + (read != null ? read.size() : 0) + " cărți" +
                ", didNotFinish=" + (didNotFinish != null ? didNotFinish.size() : 0) + " cărți" +
                '}';
    }







}
