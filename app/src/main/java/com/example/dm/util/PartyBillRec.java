package com.example.dm.util;

public class PartyBillRec {
//    public static final String TABLE_NAME1 = "partyrec";
//
//    public static final String COLUMN_ID = "id";
//    public static final String COLUMN_NAME = "name";
//    public static final String COLUMN_AMT = "amt";
//    public static final String COLUMN_DATE = "date";
//    public static final String COLUMN_MOP = "mop";
//
    private String name;
    private String amt;
    private String date;
    private String mop;
//
//    public static final String CREATE_TABLE1 = "CREATE TABLE " + TABLE_NAME1 + "("
//                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
//                    + COLUMN_NAME + " TEXT,"
//                    + COLUMN_AMT + " TEXT,"
//                    + COLUMN_DATE + " TEXT,"
//                    + COLUMN_MOP + " TEXT"
//                    + ")";
    public PartyBillRec(String name, String amt, String date, String mop) {

        this.name = name;
        this.amt = amt;
        this.date = date;
        this.mop = mop;
    }

    public PartyBillRec() {

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

    public String getMop() {
        return mop;
    }

    public void setMop(String mop) {
        this.mop = mop;
    }
}
