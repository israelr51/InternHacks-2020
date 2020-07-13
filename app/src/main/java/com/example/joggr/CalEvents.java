package com.example.joggr;

public class CalEvents {
    String name;
    String start_time;
    String end_time;
    String date_of_event;
    public CalEvents(){
        name = null;
        start_time = null;
        end_time = null;
        date_of_event = null;
    }
    public CalEvents(String n, String st, String et, String date){
        name = n;
        start_time = st;
        end_time = et;
        date_of_event = date;
    }
}
