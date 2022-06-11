package com.example.koreantime.DTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DTO_user implements Serializable {
    String email;
    String nickname;
    String addr1;
    String addr2;
    String addr3;
    String push_token;
    Double latitude;
    Double longitude;

    public DTO_user(){}
    public DTO_user(String email,String nickname,String addr1, String addr2, String addr3,String push_token,Double latitude,Double longitude){
        this.email=email;
        this.nickname=nickname;
        this.addr1=addr1;
        this.addr2=addr2;
        this.addr3=addr3;
        this.latitude=latitude;
        this.longitude=longitude;
        this.push_token=push_token;
    }
    public DTO_user(String email,String nickname,String addr1, String addr2, String addr3,String push_token){
        this.email=email;
        this.nickname=nickname;
        this.addr1=addr1;
        this.addr2=addr2;
        this.addr3=addr3;
        this.latitude=0.0;
        this.longitude=0.0;
        this.push_token=push_token;
    }
    public DTO_user(String email,String nickname,String addr1, String addr2, String addr3){
        this.email=email;
        this.nickname=nickname;
        this.addr1=addr1;
        this.addr2=addr2;
        this.addr3=addr3;
        this.push_token="";
    }
    public String getEmail(){
        return email;
    }

    public String getNickname() {
        return nickname;
    }

    public String getAddr1(){
        return addr1;
    }
    public String getAddr2(){
        return addr2;
    }
    public String getAddr3(){
        return addr3;
    }

    public String getPush_token() {
        return push_token;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setEmail(String email){
        this.email=email;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setAddr1(String addr1){
        this.addr1=addr1;
    }
    public void setAddr2(String addr2){
        this.addr2=addr2;
    }
    public void setAddr3(String addr3){
        this.addr3=addr3;
    }

    public void setPush_token(String push_token) {
        this.push_token = push_token;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}
