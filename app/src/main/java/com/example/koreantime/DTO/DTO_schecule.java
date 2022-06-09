package com.example.koreantime.DTO;

public class DTO_schecule {
    String host;
    String location;
    String punishment_alarm;
    String punishment_vibrate;
    String time;
    String date;
    String name;

    public DTO_schecule(){}
    public DTO_schecule(String host, String location,String punishment_alarm,String punishment_vibrate,String time,String date,String name){
        this.host=host;
        this.location=location;
        this.punishment_alarm=punishment_alarm;
        this.punishment_vibrate=punishment_vibrate;
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

    public String getPunishment_alarm() {
        return punishment_alarm;
    }

    public String getPunishment_vibrate() {
        return punishment_vibrate;
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

    public void setPunishment_alarm(String punishment_alarm) {
        this.punishment_alarm = punishment_alarm;
    }

    public void setPunishment_vibrate(String punishment_vibrate) {
        this.punishment_vibrate = punishment_vibrate;
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
