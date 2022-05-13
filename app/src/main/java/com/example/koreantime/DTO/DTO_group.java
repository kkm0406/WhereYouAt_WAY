package com.example.koreantime.DTO;

public class DTO_group {
    String name;
    int num;
    String participation;

    public DTO_group(){
        this.name="";
        this.num=0;
        this.participation="";
    }
    public DTO_group(String name, int num, String participation){
        this.name=name;
        this.num=num;
        this.participation=participation;
    }

    public int getNum() {
        return num;
    }

    public String getName() {
        return name;
    }

    public String getParticipation() {
        return participation;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setParticipation(String participation) {
        this.participation = participation;
    }
}