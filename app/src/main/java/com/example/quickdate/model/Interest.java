package com.example.quickdate.model;

public class Interest {
    private String Name;
    private Boolean Status;
    private int ImageID;

    public Interest(){

    }

    public Interest(String Name, Boolean Status, int ImageID){
        this.Name = Name;
        this.Status = Status;
        this.ImageID = ImageID;
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

    public int getImageID() {
        return ImageID;
    }

    public void setImageID(int imageID) {
        ImageID = imageID;
    }
}
