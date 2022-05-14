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
    List<String> groups_id;

    public DTO_user(){}
    public DTO_user(String email,String nickname,String addr1, String addr2, String addr3,List<String> groups_id){
        this.email=email;
        this.nickname=nickname;
        this.addr1=addr1;
        this.addr2=addr2;
        this.addr3=addr3;
        this.groups_id=groups_id;
    }
    public DTO_user(String email,String nickname,String addr1, String addr2, String addr3){
        this.email=email;
        this.nickname=nickname;
        this.addr1=addr1;
        this.addr2=addr2;
        this.addr3=addr3;
        this.groups_id=new ArrayList<>(1);
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
    public List<String> getGroups_id(){
        return groups_id;
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
    public void setGroups_id(List<String> groups_id){
        this.groups_id=groups_id;
    }
}
