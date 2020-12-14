package com.example.quickdate.model;

public class LookingFor {

    private int looking;
    private int min_age;
    private int max_age;
    private int min_height;
    private int max_height;
    private int min_weight;
    private int max_weight;

    public LookingFor(){
        this.looking = 1;
        this.min_age = 18;
        this.max_age = 60;
        this.min_height = 100;
        this.max_height = 250;
        this.min_weight = 40;
        this.max_weight = 100;
    }

    public LookingFor(int looking, int min_age, int max_age, int min_height, int max_height, int min_weight, int max_weight){
        this.looking = looking;
        this.min_age = min_age;
        this.max_age = max_age;
        this.min_height = min_height;
        this.max_height = max_height;
        this.min_weight = min_weight;
        this.max_weight = max_weight;
    }

    public int getLooking() {
        return looking;
    }

    public void setLooking(int looking) {
        this.looking = looking;
    }

    public int getMin_age() {
        return min_age;
    }

    public void setMin_age(int min_age) {
        this.min_age = min_age;
    }

    public int getMax_age() {
        return max_age;
    }

    public void setMax_age(int max_age) {
        this.max_age = max_age;
    }

    public int getMin_height() {
        return min_height;
    }

    public void setMin_height(int min_height) {
        this.min_height = min_height;
    }

    public int getMax_height() {
        return max_height;
    }

    public void setMax_height(int max_height) {
        this.max_height = max_height;
    }

    public int getMin_weight() {
        return min_weight;
    }

    public void setMin_weight(int min_weight) {
        this.min_weight = min_weight;
    }

    public int getMax_weight() {
        return max_weight;
    }

    public void setMax_weight(int max_weight) {
        this.max_weight = max_weight;
    }
}
