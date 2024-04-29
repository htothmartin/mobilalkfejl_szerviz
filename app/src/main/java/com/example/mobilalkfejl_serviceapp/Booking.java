package com.example.mobilalkfejl_serviceapp;

import java.util.Date;

public class Booking {

    private String id;
    private String user_id;
    private String service_id;
    private String service_name;
    private Date date;
    private String time;

    public Booking() {
    }

    public Booking(String id, String user_id, String service_id, Date date, String time, String service_name) {
        this.id = id;
        this.user_id = user_id;
        this.service_id = service_id;
        this.date = date;
        this.time = time;
        this.service_name = service_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id='" + id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", service_id='" + service_id + '\'' +
                ", date=" + date +
                ", time='" + time + '\'' +
                '}';
    }
}


