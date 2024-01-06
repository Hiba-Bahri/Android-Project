package com.tekup.androidproject.entities;

public class Advert {
    private Long id;

    private String description;

    private String adType;

    private String estateType;

    private float surfaceArea;

    private String nbRooms;

    private String location;

    private float price;

    private String image;

    public Advert() {
    }

    public Advert(Long id, String description, String adType, String estateType, float surfaceArea, String nbRooms, String location, float price, String image) {
        this.id = id;
        this.description = description;
        this.adType = adType;
        this.estateType = estateType;
        this.surfaceArea = surfaceArea;
        this.nbRooms = nbRooms;
        this.location = location;
        this.price = price;
        this.image = image;
    }

    public Long getId() {
        return id;
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

    public String getNbRooms() {
        return nbRooms;
    }

    public void setNbRooms(String nbRooms) {
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
