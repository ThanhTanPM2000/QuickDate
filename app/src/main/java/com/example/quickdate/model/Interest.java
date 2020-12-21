package com.example.quickdate.model;

public class Interest {
    private String Name;
    private Boolean Status;
    private int Image;

    public Interest(){

    }

    public Interest(String Name, Boolean Status, int Image){
        this.Name = Name;
        this.Status = Status;
        this.Image = Image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Boolean getStatus() {
        return Status;
    }

    public void setStatus(Boolean status) {
        Status = status;
    }

    public int getImage() {
        return Image;
    }

    public void setImage(int image) {
        Image = image;
    }
}
