package com.example.koreantime.DTO;

import java.util.List;

public class DTO_group {
    String name;
    int num;
    List<String> participation;

    public DTO_group(){}
    public DTO_group(String name, int num, List<String> participation){
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

    public List<String> getParticipation() {
        return participation;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setParticipation(List<String> participation) {
        this.participation = participation;
    }
}