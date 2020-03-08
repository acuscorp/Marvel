package com.acuscorp.marvel.Models;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Comics {

    @SerializedName("available")
    private Integer available;
    @SerializedName("collectionURI")
    private String collectionURI;
    @SerializedName("items")
    private List<Item> items = null;
    @SerializedName("returned")

    private Integer returned;

    public Comics(Integer available, String collectionURI, List<Item> items, Integer returned) {
        this.available = available;
        this.collectionURI = collectionURI;
        this.items = items;
        this.returned = returned;
    }

    public Integer getAvailable() {
        return available;
    }

    public void setAvailable(Integer available) {
        this.available = available;
    }

    public String getCollectionURI() {
        return collectionURI;
    }

    public void setCollectionURI(String collectionURI) {
        this.collectionURI = collectionURI;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Integer getReturned() {
        return returned;
    }

    public void setReturned(Integer returned) {
        this.returned = returned;
    }
}

