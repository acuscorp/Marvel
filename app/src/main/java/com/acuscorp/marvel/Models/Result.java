package com.acuscorp.marvel.Models;

import com.google.gson.annotations.SerializedName;


import com.google.gson.annotations.Expose;


public class Result {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("modified")
    @Expose
    private String modified;
    @SerializedName("thumbnail")
    @Expose
    private Thumbnail thumbnail;
    @SerializedName("resourceURI")
    @Expose
    private String resourceURI;
    @SerializedName("comics")
    @Expose
    private Comics comics;

    public Result(Integer id, String name, String description, String modified, Thumbnail thumbnail, String resourceURI, Comics comics) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.modified = modified;
        this.thumbnail = thumbnail;
        this.resourceURI = resourceURI;
        this.comics = comics;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getResourceURI() {
        return resourceURI;
    }

    public void setResourceURI(String resourceURI) {
        this.resourceURI = resourceURI;
    }

    public Comics getComics() {
        return comics;
    }

    public void setComics(Comics comics) {
        this.comics = comics;
    }



}