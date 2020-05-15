package com.example.dm.util;

public class PartiesOutstandingObject {

    private String name;
    private String amt;
    private String date;

    public PartiesOutstandingObject() {
    }

    public PartiesOutstandingObject( String name, String amt, String date) {
        this.name = name;
        this.amt = amt;
        this.date = date;

    }




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
