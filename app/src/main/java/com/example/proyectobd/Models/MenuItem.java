package com.example.proyectobd.Models;

public class MenuItem {
    private String title;
    private int iconResource;

    public MenuItem(String title, int iconResource) {
        this.title = title;
        this.iconResource = iconResource;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIconResource() {
        return iconResource;
    }

    public void setIconResource(int iconResource) {
        this.iconResource = iconResource;
    }
}
