package com.example.chira.tugas4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

/**
 * Created by chira on 07/11/16.
 */
public class DBHelper extends SQLiteOpenHelper {
    private  static  final  String DB_NAME = "mahasiswa";
    private static final int DB_VERSION = 1;


    public  static  final String TABLE_NAME = "biodata";
    public  static  final String TABLE_NAME2 = "user";

    public static final String NIM = "_nim";
    public static final String NAMA = "nama";
    public static final String TGL = "tgl";
    public static final String JK = "jk";
    public static final String AlAMAT = "alamat";
    public static final String JURUSAN = "jurusan";
    public static final String ANGKATAN = "angkatan";
    public static final String ID = "id";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    private SQLiteDatabase DBsaya;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " +TABLE_NAME + "( " +
                        NIM+ " Varchar(10) PRIMARY KEY, " +
                        NAMA+ " Varchar(25), " +
                        TGL+ " Date, " +
                        JK+ " Varchar(20), " +
                        AlAMAT+ " Varchar(25), " +
                        JURUSAN+ " Varchar(25), " +
                        ANGKATAN+ " Varchar(25))"
                        ;
        db.execSQL(query);

        String query2 ="CREATE TABLE " +TABLE_NAME2 + " ( "+
                        ID+ " Varchar(10) PRIMARY KEY, " +
                        USERNAME+ " Varchar(20), " +
                        PASSWORD+ " Varchar(20))"
                ;
        db.execSQL(query2);

        String queryInsertLogin = "INSERT INTO " + TABLE_NAME2 + " VALUES ('1','admin','admin') ";
        db.execSQL(queryInsertLogin);

    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void openDB(){
        DBsaya = getWritableDatabase();
    }

    public void closeDB(){
        if (DBsaya !=null && DBsaya.isOpen()){
           DBsaya.close();
        }
    }

    public void insertData(String Nim, String Nama, String Tgl,
                           String Jk, String Alamat, String Jurusan,
                           String Angkatan){
        String query = "INSERT INTO " +TABLE_NAME + "VALUES ('"+Nim+"' , '"+Nama+"','"+Alamat+"', " +
                "'"+Tgl+"', '"+Jk+"', '"+Jurusan+"', '"+Angkatan+"')";
    DBsaya.execSQL(query);
    }

    public long updateData(String Nim, String Nama, String Tgl,
                           String Jk, String Alamat, String Jurusan,
                           String Angkatan){
        ContentValues values = new ContentValues();
        values.put(NAMA, Nama);
        values.put(TGL, Tgl);
        values.put(JK, Jk);
        values.put(AlAMAT, Alamat);
        values.put(JURUSAN, Jurusan);
        values.put(ANGKATAN, Angkatan);

        String where = NIM+ " = '"+Nim+"' ";
        return DBsaya.update(TABLE_NAME, values, where, null);
    }

    public long deleteData(String nim){
        String where = NIM+ " = '"+nim+"' ";
        return DBsaya.delete(TABLE_NAME, where, null);
    }

    public Cursor getAllRecords(){
        String query = "SELECT _nim AS _id, nama, tgl, jk, alamat, jurusan, angkatan FROM " +TABLE_NAME;
        return  DBsaya.rawQuery(query, null);
    }

    public Integer cekNim(String nim){
        String query = "SELECT _nim FROM" +TABLE_NAME+ "WHERE nim =  '"+nim+"' ";
        Cursor cursor = DBsaya.rawQuery(query,null);
        return cursor.getCount();
    }


    public Integer cekLogin(String username, String password){
        String countQuery = "SELECT * FROM " + TABLE_NAME2 + " WHERE username = '"+username+"' AND password = '" + password + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();
        return rowCount;
    }

    public void insertLogin(String username, String password){

        String query = "INSERT INTO " + TABLE_NAME2 + "VALUES ('"+username+"', '"+password+"')";

        DBsaya.execSQL(query);
    }

    public HashMap<String, String> getBioDetail(String nim){
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + NIM + " = " + nim;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            user.put("nim", cursor.getString(0));
            user.put("nama", cursor.getString(1));
            user.put("tgl", cursor.getString(2));
            user.put("jk", cursor.getString(3));
            user.put("alamat", cursor.getString(4));
            user.put("jurusan", cursor.getString(5));
            user.put("angkatan", cursor.getString(6));
        }
        cursor.close();
        db.close();

        return user;
    }
}
