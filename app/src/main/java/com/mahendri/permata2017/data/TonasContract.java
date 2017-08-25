package com.mahendri.permata2017.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by mahendri on 1/1/2017.
 * Define Contract for Database
 */

public class TonasContract {
    static final String CONTENT_AUTHORITY = "com.mahendri.permata2017";
    static final String PATH_DAFTAR = "daftar";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final class PendaftarEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATH_DAFTAR);
        static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/"
                + CONTENT_AUTHORITY + "/" + PATH_DAFTAR;
        static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/"
                + CONTENT_AUTHORITY + "/" + PATH_DAFTAR;

        static final String TABLE_NAME = "daftar";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_DAFTAR_PAKET = "paket";
        public static final String COLUMN_DAFTAR_HP = "hp";
        public static final String COLUMN_DAFTAR_NAMA = "nama";
        public static final String COLUMN_DAFTAR_SEKOLAH = "sekolah";
        public static final String COLUMN_DAFTAR_BAYAR = "bayar";

        public static final int BELUM_BAYAR = 0;
        public static final int SUDAH_BAYAR = 1;
    }
}
