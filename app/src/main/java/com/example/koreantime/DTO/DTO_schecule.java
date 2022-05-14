package com.example.koreantime.DTO;

public class DTO_schecule {
    String host;
    String location;
    String punishment;
    String time;

    DTO_schecule(String host, String location,String punishment,String time){
        this.host=host;
        this.location=location;
        this.punishment=punishment;
        this.time=time;
    }
}
