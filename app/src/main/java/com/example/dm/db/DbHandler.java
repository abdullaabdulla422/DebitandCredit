package com.example.dm.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.dm.util.PartiesOutstandingObject;
import com.example.dm.util.PartyBillRec;

import java.util.ArrayList;
import java.util.List;

public class DbHandler extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "usersdb";
    private static final String TABLE_USERS = "userdetails";
    private static final String TABLE_NAME1 = "partyrecs";


    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_AMT = "amount";
    private static final String KEY_DATE = "date";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_AMT = "amt";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_MOP = "mop";



   private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_USERS + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT,"
            + KEY_AMT + " TEXT,"
            + KEY_DATE + " TEXT"+ ")";
    private static final String CREATE_TABLE1 = "CREATE TABLE " + TABLE_NAME1 + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_NAME + " TEXT,"
            + COLUMN_AMT + " TEXT,"
            + COLUMN_DATE + " TEXT,"
            + COLUMN_MOP + " TEXT"
            + ")";
    public DbHandler(Context context){
        super(context,DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db){


        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_TABLE1);



    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        // Drop older table if exist
        db.execSQL(TABLE_USERS);
        db.execSQL( TABLE_NAME1);
        // Create tables again
        onCreate(db);
    }
    // **** CRUD (Create, Read, Update, Delete) Operations ***** //

    // Adding new User Details
    public void BillDetails(Context context, PartyBillRec partyBillRec
                                ){
        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
//        String query = "SELECT name, amount, date , mop FROM "+ TABLE_NAME1+ " WHERE "
//                + COLUMN_NAME + " = " + partyBillRec;
        Cursor cursor = db.rawQuery("SELECT * FROM "+ TABLE_NAME1 ,null);
        int count=cursor.getCount();
        ContentValues cValues = new ContentValues();
        cValues.put(COLUMN_ID,count+1);
        cValues.put(COLUMN_NAME, partyBillRec.getName());
        cValues.put(COLUMN_AMT, partyBillRec.getAmt());
        cValues.put(COLUMN_DATE, partyBillRec.getDate());
        cValues.put(COLUMN_MOP, partyBillRec.getMop());
        db.insert(TABLE_NAME1,null,cValues);
        Log.d("TAG", "BillDetails: "+cValues);
        db.close();
    }
    // Get User Details
    public ArrayList<PartyBillRec> GetBills(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<PartyBillRec> userList = new ArrayList<>();
        String query = "SELECT * FROM "+ TABLE_NAME1;
        Cursor cursor = db.rawQuery(query,null);
        if (cursor.moveToFirst()){
            do{
                PartyBillRec part=new PartyBillRec();
            part.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
            part.setAmt(cursor.getString(cursor.getColumnIndex(COLUMN_AMT)));
            part.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
            part.setMop(cursor.getString(cursor.getColumnIndex(COLUMN_MOP)));


            userList.add(part);}while (cursor.moveToNext());

                }
        return  userList;
    }

    public List<PartyBillRec> getAll() {
        List<PartyBillRec> partiesbills1 = new ArrayList<>();

        // Select All Query
        String selectQuery2 = "SELECT * FROM " + TABLE_NAME1 + " ORDER BY " +
                COLUMN_NAME + " DESC" ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery2, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PartyBillRec part=new PartyBillRec();
                part.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                part.setAmt(cursor.getString(cursor.getColumnIndex(COLUMN_AMT)));
                part.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
                part.setMop(cursor.getString(cursor.getColumnIndex(COLUMN_MOP)));

                partiesbills1.add( part);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return partiesbills1;
    }


    // Delete User Details
    public void DeleteUser(PartyBillRec userid){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME1, COLUMN_NAME+" = ?",new String[]{
                String.valueOf(userid.getName())});
        db.close();
    }


    public void insertUserDetails(Context context,PartiesOutstandingObject partiesOutstandingObject
    ){
        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT name, amount, date FROM "+ TABLE_USERS;
        Cursor cursor = db.rawQuery(query,null);
        int count=cursor.getCount();
        ContentValues cValues = new ContentValues();
        cValues.put(KEY_ID,count+1);
        cValues.put(KEY_NAME, partiesOutstandingObject.getName());
        cValues.put(KEY_AMT, partiesOutstandingObject.getAmt());
        cValues.put(KEY_DATE, partiesOutstandingObject.getDate());
        db.insert(TABLE_USERS,null,cValues);
        Log.d("TAG", "insertUserDetails: "+cValues);
        db.close();
    }
    // Get User Details
    public ArrayList<PartiesOutstandingObject> GetUsers(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<PartiesOutstandingObject> userList = new ArrayList<>();
        String query = "SELECT name, amount, date FROM "+ TABLE_USERS;
        Cursor cursor = db.rawQuery(query,null);
        if (cursor.moveToFirst()){
            do{
                PartiesOutstandingObject part=new PartiesOutstandingObject();
                part.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
                part.setAmt(cursor.getString(cursor.getColumnIndex(KEY_AMT)));
                part.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));


                userList.add(part);}while (cursor.moveToNext());

        }
        return  userList;
    }
    public List<PartiesOutstandingObject> getAllBills() {
        List<PartiesOutstandingObject> partiesbills1 = new ArrayList<>();

        // Select All Query
        String selectQuery2 = "SELECT * FROM " + TABLE_USERS + " ORDER BY " +
                KEY_NAME + " DESC" ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery2, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PartiesOutstandingObject part=new PartiesOutstandingObject();
                part.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
                part.setAmt(cursor.getString(cursor.getColumnIndex(KEY_AMT)));
                part.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));


                partiesbills1.add( part);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return partiesbills1;
    }




    // Delete User Details
    public void DeleteUser(PartiesOutstandingObject userid){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS, KEY_NAME+" = ?",new String[]{
                String.valueOf(userid.getName())});
        db.close();
    }
    public int updateNote(PartiesOutstandingObject list) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, list.getName());
        values.put(KEY_DATE, list.getDate());
        values.put(KEY_AMT, list.getAmt());

        // updating row
        return db.update(TABLE_USERS, values,
                KEY_NAME + " = ?",
                new String[]{String.valueOf(list.getName())});
    }
    public int updateBill(PartyBillRec list) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, list.getName());
        values.put(COLUMN_DATE, list.getDate());
        values.put(COLUMN_AMT, list.getAmt());
        values.put(COLUMN_MOP, list.getMop());

        // updating row
        return db.update(TABLE_NAME1, values,
                COLUMN_NAME + " = ?",
                new String[]{String.valueOf(list.getName())});
    }
}