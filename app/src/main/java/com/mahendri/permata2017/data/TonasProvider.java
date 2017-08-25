package com.mahendri.permata2017.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mahendri.permata2017.data.TonasContract.PendaftarEntry;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * Created by mahendri on 1/2/2017.
 * to provide help on database access
 */

public class TonasProvider extends ContentProvider {

    TonasDbHelper mTonasDbHelper;
    public static final String NAME_KEY = "namaDaftar";
    public static final String NO_KEY = "noDaftar";
    public static final String PAKET_KEY = "paketDaftar";
    public static final String DOUB_KEY = "doubCheck";

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int PENDAFTAR = 100;
    private static final int PENDAFTAR_ID = 101;

    static {
        uriMatcher.addURI(TonasContract.CONTENT_AUTHORITY,TonasContract.PATH_DAFTAR,PENDAFTAR);
        uriMatcher.addURI(TonasContract.CONTENT_AUTHORITY,TonasContract.PATH_DAFTAR + "/#", PENDAFTAR_ID);
    }

    @Override
    public boolean onCreate(){
        mTonasDbHelper = new TonasDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder){
        SQLiteDatabase db = mTonasDbHelper.getReadableDatabase();
        final int match = uriMatcher.match(uri);
        Cursor cursor;

        switch (match) {
            case PENDAFTAR:
                cursor = db.query(PendaftarEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case PENDAFTAR_ID:
                selection = PendaftarEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(PendaftarEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Query tidak dapat dilakukan");
        }
        if(getContext() != null) cursor.setNotificationUri(getContext().getContentResolver(),uri);

        return cursor;
        }
    
    @Override
    public Uri insert (@NonNull Uri uri, ContentValues values){
        final int match = uriMatcher.match(uri);
        switch (match){
            case PENDAFTAR:
                return insertPendaftar(uri, values);
            default:
                throw new IllegalArgumentException("Insert data gagal");
        }
    }
    
    private Uri insertPendaftar(Uri uri, ContentValues values){
        if (values.size() == 0) return null;
        SQLiteDatabase db = mTonasDbHelper.getWritableDatabase();
        long id = db.insertOrThrow(PendaftarEntry.TABLE_NAME,null,values);
        if(getContext() != null) getContext().getContentResolver().notifyChange(uri,null);
        return ContentUris.withAppendedId(uri,id);
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs){
        final int match = uriMatcher.match(uri);
        switch (match){
            case PENDAFTAR:
                return updatePendaftar(uri, values, selection, selectionArgs);
            case PENDAFTAR_ID:
                selection = PendaftarEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updatePendaftar(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("cant do update for that uri");
        }
    }

    private int updatePendaftar(Uri uri, ContentValues values, String selection, String[] selectionArgs){
        boolean checkNama = values.containsKey(PendaftarEntry.COLUMN_DAFTAR_NAMA);
        boolean checkSekolah = values.containsKey(PendaftarEntry.COLUMN_DAFTAR_SEKOLAH);

        int count;
        SQLiteDatabase db = mTonasDbHelper.getWritableDatabase();
        if (checkNama && checkSekolah){
            count = db.update(PendaftarEntry.TABLE_NAME, values, selection, selectionArgs);
            if(getContext() != null) getContext().getContentResolver().notifyChange(uri, null);
            return count;
        } else {
            throw new IllegalArgumentException("Gagal update, uri tidak cocok");
        }
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mTonasDbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);

        switch (match){
            case PENDAFTAR:
                int count = db.delete(PendaftarEntry.TABLE_NAME, selection,selectionArgs);
                if (getContext() != null) getContext().getContentResolver().notifyChange(uri, null);
                return count;
            case PENDAFTAR_ID:
                selection = PendaftarEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                int count2 = db.delete(PendaftarEntry.TABLE_NAME, selection, selectionArgs);
                if (getContext() != null) getContext().getContentResolver().notifyChange(uri, null);
                return count2;
            default:
                throw new IllegalArgumentException("Delete can't be done far that uri");
        }
    }

    @Override
    public String getType(@NonNull Uri uri) {
        final int match = uriMatcher.match(uri);
        switch (match){
            case PENDAFTAR:
                return PendaftarEntry.CONTENT_LIST_TYPE;
            case PENDAFTAR_ID:
                return PendaftarEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown uri");
        }
    }

    @Nullable
    @Override
    public Bundle call(@NonNull String method, String arg, Bundle extras) {
        Bundle bundle = new Bundle();
        if (method.equals("export")){
            exportTabel(arg);
            bundle = null;
        }
        if (method.equals("check")){
            if (bundle != null){
                Cursor cursor = new TonasDbHelper(getContext()).getReadableDatabase().rawQuery("SELECT " + PendaftarEntry._ID
                        + ", " + PendaftarEntry.COLUMN_DAFTAR_NAMA + " FROM "
                                + PendaftarEntry.TABLE_NAME + " WHERE " + PendaftarEntry.COLUMN_DAFTAR_HP + " = ? ",
                        new String[]{arg});
                int count = cursor.getCount();
                if (count > 0) {
                    cursor.moveToFirst();
                    String nameDaftar = cursor.getString(cursor.getColumnIndex(PendaftarEntry.COLUMN_DAFTAR_NAMA));
                    int paketDaftar = cursor.getInt(cursor.getColumnIndex(PendaftarEntry.COLUMN_DAFTAR_PAKET));
                    int noDaftar = cursor.getInt(cursor.getColumnIndex(PendaftarEntry._ID));
                    bundle.putString(NAME_KEY, nameDaftar);
                    bundle.putInt(PAKET_KEY, paketDaftar);
                    bundle.putInt(NO_KEY, noDaftar);
                }
                cursor.close();
                bundle.putInt(DOUB_KEY, count);
            }
        }
        return bundle;
    }

    private void exportTabel(String name){
        SQLiteDatabase db = mTonasDbHelper.getReadableDatabase();
        Cursor c;

        try {
            c = db.rawQuery("select * from " + PendaftarEntry.TABLE_NAME, null);
            int rowcount;
            int colcount;

            File externalDir = Environment.getExternalStorageDirectory();
            String filename = name + ".csv";

            // the name of the file to export with
            File saveFile = new File(externalDir, filename);
            FileWriter fileWriter = new FileWriter(saveFile);

            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            rowcount = c.getCount();
            colcount = c.getColumnCount();
            if (rowcount > 0) {
                c.moveToFirst();

                //Get name
                for (int i = 0; i < colcount; i++) {
                    if (i != colcount - 1) {

                        bufferedWriter.write(c.getColumnName(i) + ",");

                    } else {

                        bufferedWriter.write(c.getColumnName(i));

                    }
                }
                bufferedWriter.newLine();

                //Get content
                for (int i = 0; i < rowcount; i++) {
                    c.moveToPosition(i);

                    for (int j = 0; j < colcount; j++) {
                        if (j != colcount - 1)
                            bufferedWriter.write(c.getString(j) + ",");
                        else
                            bufferedWriter.write(c.getString(j));
                    }
                    bufferedWriter.newLine();
                }
                bufferedWriter.flush();
                c.close();
            }
        } catch (Exception ex) {
            if (db.isOpen()) {
                db.close();
            }

        }

    }


}
