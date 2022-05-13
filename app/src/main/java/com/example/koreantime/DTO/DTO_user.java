package com.example.koreantime.DTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DTO_user implements Serializable {
    String ci;
    String addr1;
    String addr2;
    String addr3;
    List<String> groups_id;

    public DTO_user(){
        this.ci="";
        this.addr1="";
        this.addr2="";
        this.addr3="";
        this.groups_id=new ArrayList<>();
    }
    public DTO_user(String ci,String addr1, String addr2, String addr3,List<String> groups_id){
        this.ci=ci;
        this.addr1=addr1;
        this.addr2=addr2;
        this.addr3=addr3;
        this.groups_id=groups_id;
    }
    public String getCi(){
        return ci;
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
    public void setCi(String ci){
        this.ci=ci;
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
