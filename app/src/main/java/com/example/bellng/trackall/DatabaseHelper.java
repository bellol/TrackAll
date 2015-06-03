package com.example.bellng.trackall;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.bellng.trackall.listitems.Package;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by Bell on 3/06/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "database";
    public static final int DATABASE_VERSION  = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Package.CREATE_STATEMENT);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Package.TABLE_NAME);
        onCreate(db);
    }


    public void addPackage(Package p){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Package.COLUMN_TITLE, p.getTitle());
        values.put(Package.COLUMN_TRACKING_NUMBER, p.getTrackingNumber());
        values.put(Package.COLUMN_SLUG_NAME, p.getSlugName());

        db.insert(Package.TABLE_NAME, null, values);
        db.close();
    }

    public void removePackage(Package p){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(Package.TABLE_NAME, Package.COLUMN_ID + " = ?", new String[]{String.valueOf(p.getId())});
    }

    public void editPackageName(Package p, String name){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Package.COLUMN_TITLE, name);
        db.update(Package.TABLE_NAME, values, Package.COLUMN_ID + " = ?", new String[]{String.valueOf(p.getId())});
    }

    public HashMap<Long,Package> getAllPackages(){
        HashMap<Long,Package> packages = new LinkedHashMap<Long,Package>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + Package.TABLE_NAME, null);

        if(cursor.moveToFirst()){
            do{
                Package p = new Package(cursor.getLong(0),cursor.getString(1),cursor.getString(2),cursor.getString(3));
                packages.put(p.getId(), p);
            } while(cursor.moveToNext());
        }

        cursor.close();

        return packages;
    }
}
