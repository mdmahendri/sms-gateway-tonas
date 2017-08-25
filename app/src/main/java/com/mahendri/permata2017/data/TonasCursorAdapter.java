package com.mahendri.permata2017.data;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.mahendri.permata2017.R;
import com.mahendri.permata2017.data.TonasContract.PendaftarEntry;

/**
 * Created by mahendri on 1/2/2017.
 */

public class TonasCursorAdapter extends CursorAdapter {

    public TonasCursorAdapter(Context context, Cursor cursor){
        super(context,cursor,0);
    }

    @Override
    public View newView (Context context, Cursor cursor, ViewGroup parent){
        return LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor){
        TextView namaView = (TextView) view.findViewById(R.id.nama_text_view);
        TextView sekolahView = (TextView) view.findViewById(R.id.sekolah_text_view);
        TextView idView = (TextView) view.findViewById(R.id.id_text_view);

        String nama = cursor.getString(cursor.getColumnIndexOrThrow(PendaftarEntry.COLUMN_DAFTAR_NAMA));
        String sekolah = cursor.getString(cursor.getColumnIndexOrThrow(PendaftarEntry.COLUMN_DAFTAR_SEKOLAH));
        String id = String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(PendaftarEntry._ID)));

        namaView.setText(nama);
        sekolahView.setText(sekolah);
        idView.setText(id);
    }
}
