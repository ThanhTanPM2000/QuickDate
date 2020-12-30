package com.example.quickdate.model;

import android.net.Uri;
import android.provider.ContactsContract;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Info implements Serializable {

    private ArrayList<String> Images;
    private String ImgAvt;
    private String Nickname;
    private String AboutMe;
    private int Age;
    private String Gender;
    private int Height;
    private int Weight;
    private String provincial;

    public Info(){

    }

    public Info(String provincial){
        this.setImages(new ArrayList<>());
        this.setImgAvt("https://www.bing.com/images/search?view=detailV2&ccid=tHfOfsDi&id=10A4E08E5E5EBD796D7E989E4D11F7DA1E98DEFE&thid=OIP.tHfOfsDimeTq-ZWT2lYdnAHaHa&mediaurl=http%3a%2f%2f1.bp.blogspot.com%2f-f3vkR7Bo9I0%2fVHvNPu6SQRI%2fAAAAAAAAQmE%2fbno1q_sNkLg%2fs1600%2fAvatar-Facebook-an-danh-trang-14.png&exph=500&expw=500&q=h%c3%acnh+v%c3%b4+danh+facebook&simid=608014430146593327&ck=729EA94D3E79740E153F12B77793A19B&selectedIndex=42&FORM=IRPRST&ajaxhist=0");
        this.setNickname("Guest");
        this.setGender("Female");
        this.setAge(18);
        this.setAboutMe("");
        this.setHeight(100);
        this.setWeight(40);
        this.provincial = provincial;
    }

    public ArrayList<String> getImages() {
        return Images;
    }

    public void setImages(ArrayList<String> images) {
        Images = images;
    }

    public String getImgAvt() {
        return ImgAvt;
    }

    public void setImgAvt(String imgAvt) {
        ImgAvt = imgAvt;
    }

    public String getNickname() {
        return Nickname;
    }

    public void setNickname(String nickname) {
        Nickname = nickname;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public String getAboutMe() {
        return AboutMe;
    }

    public void setAboutMe(String aboutMe) {
        AboutMe = aboutMe;
    }

    public int getHeight() {
        return Height;
    }

    public void setHeight(int height) {
        Height = height;
    }

    public int getWeight() {
        return Weight;
    }

    public void setWeight(int weight) {
        Weight = weight;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getProvincial() {
        return provincial;
    }

    public void setProvincial(String provincial) {
        this.provincial = provincial;
    }
}
