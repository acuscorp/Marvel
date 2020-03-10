
package com.acuscorp.marvel.Models; ;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Events implements Serializable {

    @SerializedName("available")
    @Expose
    private Integer available;
    @SerializedName("collectionURI")
    @Expose
    private String collectionURI;

    @Expose
    private Integer returned;

    /**
     * No args constructor for use in serialization
     *
     */
    public Events() {
    }

    /**
     *
     * @param collectionURI
     * @param available
     * @param returned
     * @param items
     */
    public Events(Integer available, String collectionURI, Integer returned) {
        super();
        this.available = available;
        this.collectionURI = collectionURI;

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



    public Integer getReturned() {
        return returned;
    }

    public void setReturned(Integer returned) {
        this.returned = returned;
    }

}