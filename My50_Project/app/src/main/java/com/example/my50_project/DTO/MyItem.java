package com.example.my50_project.DTO;

import java.io.Serializable;

public class MyItem implements Serializable {
    private String id, name, hire_date, image_path;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHire_date() {
        return hire_date;
    }

    public void setHire_date(String hire_date) {
        this.hire_date = hire_date;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public MyItem(String id, String name, String hire_date, String image_path) {
        this.id = id;
        this.name = name;
        this.hire_date = hire_date;
        this.image_path = image_path;
    }
}
