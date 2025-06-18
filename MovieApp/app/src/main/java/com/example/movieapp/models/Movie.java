package com.example.movieapp.models;

import java.io.Serializable;

public class Movie implements Serializable {
    private String id;
    private String title;
    private String year;
    private String posterUrl;

    public Movie() {}

    public Movie(String id, String title, String year, String posterUrl) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.posterUrl = posterUrl;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getYear() { return year; }
    public String getPosterUrl() { return posterUrl; }

    public void setId(String id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setYear(String year) { this.year = year; }
    public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }
}
