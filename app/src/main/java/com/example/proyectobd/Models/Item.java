package com.example.proyectobd.Models;

public class Item {
    private String title;
    private String description;
    private int imageResource;
    private boolean isActive;

    public Item(String title, String description, int imageResource, boolean isActive) {
        this.title = title;
        this.description = description;
        this.imageResource = imageResource;
        this.isActive = isActive;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
