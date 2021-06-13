package com.example.bookforme.models;

public class Booking {
    String startTime, endTime, numberOfPeople, tableNumber;
    public Booking(){}
    public Booking(String startTime, String endTime, String numberOfPeople, String tableNumber){
        this.startTime = startTime;
        this.endTime = endTime;
        this.numberOfPeople = numberOfPeople;
        this.tableNumber = tableNumber;
    }
    public String getStartTime(){return startTime;}
    public String getEndTime() { return  endTime;}
    public String getNumberOfPeople(){return numberOfPeople;}
    public String getTableNumber(){return  tableNumber;}

    public void setStartTime(String startTime){ this.startTime=startTime;}
    public void setEndTime(String endTime){ this.endTime = endTime;}
    public void setNumberOfPeople(String numberOfPeople){this.numberOfPeople = numberOfPeople;}
    public void setTableNumber(String tableNumber){this.tableNumber=tableNumber;}
}
