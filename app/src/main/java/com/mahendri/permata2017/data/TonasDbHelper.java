package com.mahendri.permata2017.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mahendri.permata2017.data.TonasContract.PendaftarEntry;

/**
 * Created by mahendri on 1/1/2017.
 */

public class TonasDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "tonas.db";

    public TonasDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String CREATE_PENDAFTAR_TABLE = "CREATE TABLE " + PendaftarEntry.TABLE_NAME
                + " (" + PendaftarEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PendaftarEntry.COLUMN_DAFTAR_HP + " TEXT UNIQUE NOT NULL, "
                + PendaftarEntry.COLUMN_DAFTAR_NAMA + " TEXT NOT NULL, "
                + PendaftarEntry.COLUMN_DAFTAR_SEKOLAH + " TEXT NOT NULL, "
                + PendaftarEntry.COLUMN_DAFTAR_PAKET + " INTEGER NOT NULL, "
                + PendaftarEntry.COLUMN_DAFTAR_BAYAR + " INTEGER NOT NULL DEFAULT 0);";
        db.execSQL(CREATE_PENDAFTAR_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i1, int i2){

    }

}
