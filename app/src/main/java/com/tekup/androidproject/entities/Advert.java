package com.tekup.androidproject.entities;

import java.io.Serializable;
import java.util.Objects;

public class Advert implements Serializable {
     Long id;

     String description;

     String adType;

     String estateType;

     float surfaceArea;

     int nbRooms;

     String location;

    float price;

    String imageURL;

    public Advert() {
    }

    public Advert(Long id, String description, String adType, String estateType, float surfaceArea, int nbRooms, String location, float price, String imageURL) {
        this.id = id;
        this.description = description;
        this.adType = adType;
        this.estateType = estateType;
        this.surfaceArea = surfaceArea;
        this.nbRooms = nbRooms;
        this.location = location;
        this.price = price;
        this.imageURL = imageURL;
    }

    public Long getId() {
        return id;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAdType() {
        return adType;
    }

    public void setAdType(String adType) {
        this.adType = adType;
    }

    public String getEstateType() {
        return estateType;
    }

    public void setEstateType(String estateType) {
        this.estateType = estateType;
    }

    public float getSurfaceArea() {
        return surfaceArea;
    }

    public void setSurfaceArea(float surfaceArea) {
        this.surfaceArea = surfaceArea;
    }

    public int getNbRooms() {
        return nbRooms;
    }

    public void setNbRooms(int nbRooms) {
        this.nbRooms = nbRooms;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

}
