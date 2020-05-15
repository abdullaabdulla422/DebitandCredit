package com.example.dm.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.dm.util.Note;
import com.example.dm.util.PartyBillRec;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "notes_db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create notes table
        db.execSQL(Note.CREATE_TABLE);
//        db.execSQL(PartyBillRec.CREATE_TABLE1);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Note.TABLE_NAME);
//        db.execSQL("DROP TABLE IF EXISTS " + PartyBillRec.TABLE_NAME1);

        try
        {
            db.execSQL(Note.TABLE_NAME);
//            db.execSQL(PartyBillRec.TABLE_NAME1);
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.e("Exception", e.getMessage());
        }
        // Create tables again
        onCreate(db);
    }

    public long insertNote(String note) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Note.COLUMN_NOTE, note);

        // insert row
        long id = db.insert(Note.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }
//    public long insertbillrec(String name,String amt,String date,String mop) {
//        // get writable database as we want to write data
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        // `id` and `timestamp` will be inserted automatically.
//        // no need to add them
//        values.put(PartyBillRec.COLUMN_NAME, name);
//        values.put(PartyBillRec.COLUMN_AMT, amt);
//        values.put(PartyBillRec.COLUMN_DATE, date);
//        values.put(PartyBillRec.COLUMN_MOP, mop);
//
//        // insert row
//        long id = db.insert(PartyBillRec.TABLE_NAME1, null, values);
//
//        // close db connection
//        db.close();
//
//        // return newly inserted row id
//        return id;
//    }

    public Note getNote(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Note.TABLE_NAME,
                new String[]{Note.COLUMN_ID, Note.COLUMN_NOTE, Note.COLUMN_TIMESTAMP},
                Note.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        Note note = new Note(
                cursor.getInt(cursor.getColumnIndex(Note.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Note.COLUMN_NOTE)),
                cursor.getString(cursor.getColumnIndex(Note.COLUMN_TIMESTAMP)));

        // close the db connection
        cursor.close();

        return note;
    }
//    public PartyBillRec getBill(long id) {
//        // get readable database as we are not inserting anything
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        Cursor cursor = db.query(PartyBillRec.TABLE_NAME1,
//                new String[]{PartyBillRec.COLUMN_ID, PartyBillRec.COLUMN_NAME, PartyBillRec.COLUMN_AMT,
//                PartyBillRec.COLUMN_DATE,PartyBillRec.COLUMN_MOP},
//                PartyBillRec.COLUMN_ID + "=?",
//                new String[]{String.valueOf(id)}, null, null, null, null);
//
//        if (cursor != null)
//            cursor.moveToFirst();
//
//        // prepare note object
//        PartyBillRec partyBillRec = new PartyBillRec(
//                cursor.getString(cursor.getColumnIndex(PartyBillRec.COLUMN_NAME)),
//                cursor.getString(cursor.getColumnIndex(PartyBillRec.COLUMN_AMT)),
//                cursor.getString(cursor.getColumnIndex(PartyBillRec.COLUMN_DATE)),
//                cursor.getString(cursor.getColumnIndex(PartyBillRec.COLUMN_MOP)));
//
//        // close the db connection
//        cursor.close();
//
//        return partyBillRec;
//    }

    public List<Note> getAllNotes() {
        List<Note> notes = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Note.TABLE_NAME + " ORDER BY " +
                Note.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setId(cursor.getInt(cursor.getColumnIndex(Note.COLUMN_ID)));
                note.setNote(cursor.getString(cursor.getColumnIndex(Note.COLUMN_NOTE)));
                note.setTimestamp(cursor.getString(cursor.getColumnIndex(Note.COLUMN_TIMESTAMP)));

                notes.add(note);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return notes;
    }
//    public List<PartyBillRec> getAllBills() {
//        List<PartyBillRec> partiesbills1 = new ArrayList<>();
//
//        // Select All Query
//        String selectQuery2 = "SELECT * FROM " + PartyBillRec.TABLE_NAME1 + " ORDER BY " +
//                PartyBillRec.COLUMN_ID + " DESC" ;
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery2, null);
//
//        // looping through all rows and adding to list
//        if (cursor.moveToFirst()) {
//            do {
//                PartyBillRec partiesbill = new PartyBillRec();
//                partiesbill.setName(cursor.getString(cursor.getColumnIndex(PartyBillRec.COLUMN_NAME)));
//                partiesbill.setAmt(cursor.getString(cursor.getColumnIndex(PartyBillRec.COLUMN_AMT)));
//                partiesbill.setDate(cursor.getString(cursor.getColumnIndex(PartyBillRec.COLUMN_DATE)));
//                partiesbill.setMop(cursor.getString(cursor.getColumnIndex(PartyBillRec.COLUMN_MOP)));
//
//                partiesbills1.add(partiesbill);
//            } while (cursor.moveToNext());
//        }
//
//        // close db connection
//        db.close();
//
//        // return notes list
//        return partiesbills1;
//    }

    public int getNotesCount() {
        String countQuery = "SELECT  * FROM " + Note.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }
//    public int getBiillCount() {
//        String countQuery = "SELECT  * FROM " + PartyBillRec.TABLE_NAME1;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(countQuery, null);
//
//        int count = cursor.getCount();
//        cursor.close();
//
//
//        // return count
//        return count;
//    }

    public int updateNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Note.COLUMN_NOTE, note.getNote());

        // updating row
        return db.update(Note.TABLE_NAME, values, Note.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
    }

    public void deleteNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Note.TABLE_NAME, Note.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
        db.close();
    }
}
