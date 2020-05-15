package com.example.dm.util;

public class Sessiondata {
    private static Sessiondata instance = null;
    private String custName;
    private String custAmt;
    private String custDate;
    private int previousTabSelection=0;
    public static Sessiondata getInstance() {
        if (instance == null){
            instance = new Sessiondata();
        }
        return instance;
    }

    public int getPreviousTabSelection() {
        return previousTabSelection;
    }

    public void setPreviousTabSelection(int previousTabSelection) {
        this.previousTabSelection = previousTabSelection;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustAmt() {
        return custAmt;
    }

    public void setCustAmt(String custAmt) {
        this.custAmt = custAmt;
    }

    public String getCustDate() {
        return custDate;
    }

    public void setCustDate(String custDate) {
        this.custDate = custDate;
    }

}
