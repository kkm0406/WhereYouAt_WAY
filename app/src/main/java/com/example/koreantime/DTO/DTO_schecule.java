package com.example.koreantime.DTO;

public class DTO_schecule {
    String host;
    String location;
    String punishment;
    String time;
    String date;
    String name;

    public DTO_schecule(){}
    public DTO_schecule(String host, String location,String punishment,String time,String date,String name){
        this.host=host;
        this.location=location;
        this.punishment=punishment;
        this.time=time;
        this.date=date;
        this.name=name;
    }

    public String getHost() {
        return host;
    }

    public String getLocation() {
        return location;
    }

    public String getPunishment() {
        return punishment;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setPunishment(String punishment) {
        this.punishment = punishment;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setName(String name) {
        this.name = name;
    }
}
