package com.mahendri.permata2017;

import android.app.Activity;
import android.app.LoaderManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.mahendri.permata2017.data.TonasContract.PendaftarEntry;

public class EditActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EDIT_LOADER = 1;
    //isian activity edit
    private EditText mNamaEditText;
    private EditText mBayarEditText;
    private EditText mIdEditText;
    private EditText mSekolahEditText;
    private EditText mPaketEditText;
    private EditText mNoHpEditText;
    private int mSmsId;
    private String mTicket;

    //uri data yang diedit
    private Uri mCurrentUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        //bind view to code
        mIdEditText = (EditText) findViewById(R.id.id_edit_text);
        mBayarEditText = (EditText) findViewById(R.id.bayar_edit_text);
        mNamaEditText = (EditText) findViewById(R.id.nama_edit_text);
        mSekolahEditText = (EditText) findViewById(R.id.sekolah_edit_text);
        mPaketEditText = (EditText) findViewById(R.id.paket_edit_text);
        mNoHpEditText = (EditText) findViewById(R.id.hp_edit_text);
        SharedPreferences setting = getSharedPreferences(SettingsActivity.PREF_FILE, 0);
        mSmsId = setting.getInt(SettingsActivity.SIM_SET, 1);
        mTicket = setting.getString(SettingsActivity.TICKETLINK_SET, "http://www.mahendri.com/test2.html");

        //tampilkan menubar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //ambil data dari item yang diklik DaftarFragment
        mCurrentUri = getIntent().getData();
        if (mCurrentUri == null){
            setTitle("Data Baru");
            mNoHpEditText.setInputType(InputType.TYPE_CLASS_PHONE);
        } else {
            setTitle("Edit Data");
            mNoHpEditText.setInputType(InputType.TYPE_NULL);
            mNoHpEditText.setTextIsSelectable(true);
            getLoaderManager().initLoader(EDIT_LOADER, null, this);
        }

    }

    private void showAlert(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this)
                .setMessage("Apakah anda yakin mengirim SMS konfirmasi ini?")
                .setPositiveButton("Kirim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doKirimKonfirmasi(mNoHpEditText.getText().toString(),
                                mNamaEditText.getText().toString(),
                                mIdEditText.getText().toString(),
                                mPaketEditText.getText().toString());
                    }
                })
                .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alert.show();
    }


    private void doKirimKonfirmasi(String noHp, String nama, String id, String paket){
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent("SMS_SENT"), 0);

        //update database jika sms terkirim
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (getResultCode() == Activity.RESULT_OK){
                    ContentValues values = new ContentValues();
                    values.put(PendaftarEntry.COLUMN_DAFTAR_NAMA, mNamaEditText.getText().toString());
                    values.put(PendaftarEntry.COLUMN_DAFTAR_SEKOLAH, mSekolahEditText.getText().toString());
                    values.put(PendaftarEntry.COLUMN_DAFTAR_BAYAR, 1);
                    getContentResolver().update(mCurrentUri, values, null, null);
                    Toast.makeText(EditActivity.this, "SMS berhasil dikirim", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditActivity.this, "SMS gagal dikirim", Toast.LENGTH_SHORT).show();
                }
            }
        }, new IntentFilter("SMS_SENT"));

        SharedPreferences preferences = getSharedPreferences(SettingsActivity.PREF_FILE, 0);
        int noPendaftaran = Integer.parseInt(id) + preferences.getInt(SettingsActivity.AWAL_NO, 100);
        int paketDaftar = Integer.parseInt(paket);
        String textSms = "Pembayaran dengan no " + String.valueOf(noPendaftaran) + " paket "
                + paketDaftar + " atas nama " + nama + " berhasil diverifikasi!\n" +
                "Untuk informasi pengambilan tiket klik " + mTicket;
        SmsManager.getSmsManagerForSubscriptionId(mSmsId).sendTextMessage(noHp, null, textSms, sentPI, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_konfirmasi:
                showAlert();
                return true;
            case R.id.action_save:
                simpanUbahan();
                finish();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void simpanUbahan(){
        ContentValues values = new ContentValues();
        values.put(PendaftarEntry.COLUMN_DAFTAR_NAMA, mNamaEditText.getText().toString().trim());
        values.put(PendaftarEntry.COLUMN_DAFTAR_SEKOLAH, mSekolahEditText.getText().toString().trim());
        values.put(PendaftarEntry.COLUMN_DAFTAR_PAKET,
                Integer.parseInt(mPaketEditText.getText().toString().trim()));
        if (mCurrentUri == null){
            values.put(PendaftarEntry.COLUMN_DAFTAR_HP, mNoHpEditText.getText().toString().trim());
            getContentResolver().insert(PendaftarEntry.CONTENT_URI, values);
        } else {
            getContentResolver().update(mCurrentUri, values, null, null);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {PendaftarEntry._ID,
                PendaftarEntry.COLUMN_DAFTAR_NAMA,
                PendaftarEntry.COLUMN_DAFTAR_SEKOLAH,
                PendaftarEntry.COLUMN_DAFTAR_BAYAR,
                PendaftarEntry.COLUMN_DAFTAR_PAKET,
                PendaftarEntry.COLUMN_DAFTAR_HP};
        return new CursorLoader(this, mCurrentUri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        data.moveToFirst();
        int statusBayar = data.getInt(data.getColumnIndex(PendaftarEntry.COLUMN_DAFTAR_BAYAR));
        switch (statusBayar) {
            case PendaftarEntry.BELUM_BAYAR:
                mBayarEditText.setText(getString(R.string.pay_notyet));
                break;
            case PendaftarEntry.SUDAH_BAYAR:
                mBayarEditText.setText(getString(R.string.pay_done));
                break;
        }

        mIdEditText.setText(String.valueOf(data.getInt(data.getColumnIndex(PendaftarEntry._ID))));
        mNamaEditText.setText(data.getString(data.getColumnIndex(PendaftarEntry.COLUMN_DAFTAR_NAMA)));
        mSekolahEditText.setText(data.getString(data.getColumnIndex(PendaftarEntry.COLUMN_DAFTAR_SEKOLAH)));
        mPaketEditText.setText(String.valueOf(data.getInt(data.getColumnIndex(PendaftarEntry.COLUMN_DAFTAR_PAKET))));
        mNoHpEditText.setText(data.getString(data.getColumnIndex(PendaftarEntry.COLUMN_DAFTAR_HP)));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mBayarEditText.setText("");
        mIdEditText.setText("");
        mNamaEditText.setText("");
        mSekolahEditText.setText("");
        mPaketEditText.setText("");
        mNoHpEditText.setText("");
    }
}
