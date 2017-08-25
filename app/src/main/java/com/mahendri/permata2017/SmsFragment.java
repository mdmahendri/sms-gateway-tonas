package com.mahendri.permata2017;


import android.content.ContentUris;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mahendri.permata2017.data.TonasContract.PendaftarEntry;


/**
 * A simple {@link Fragment} subclass.
 */
public class SmsFragment extends Fragment {

    EditText mCariEditText;
    EditText mNamaEditText;
    EditText mKotakEditText;
    TextView mPeringatanText;
    Button manualSmsButton;
    String mName;
    String mNo;

    public SmsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sms, container, false);

        mCariEditText = (EditText) view.findViewById(R.id.edit_cari_no);
        mNamaEditText = (EditText) view.findViewById(R.id.nama_konfirmasi);
        mPeringatanText = (TextView) view.findViewById(R.id.peringatan);
        manualSmsButton = (Button) view.findViewById(R.id.kirim_manual_button);
        mKotakEditText = (EditText) view.findViewById(R.id.kotak_sms);

        Button button = (Button) view.findViewById(R.id.button_cari);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCariEditText.getText().toString().trim().length() > 0){
                    long id = Long.parseLong(mCariEditText.getText().toString());
                    Uri uri = ContentUris.withAppendedId(PendaftarEntry.CONTENT_URI,id);
                    Cursor cursor = getActivity().getContentResolver().query(uri,null,null,null,null);

                    if (cursor != null && cursor.getCount() > 0){
                        mPeringatanText.setVisibility(TextView.INVISIBLE);
                        cursor.moveToFirst();
                        mNo = cursor.getString(cursor.getColumnIndex(PendaftarEntry.COLUMN_DAFTAR_HP));
                        String nama = cursor.getString(cursor.getColumnIndex(PendaftarEntry.COLUMN_DAFTAR_NAMA));
                        mNamaEditText.setText(nama);
                        mName = new SmsReceiver().nametoSend(nama);
                        SharedPreferences preferences = getContext().getSharedPreferences(SettingsActivity.PREF_FILE, 0);
                        String payVal = preferences.getString(SettingsActivity.PAYLINK_SET, "http://www.mahendri.com/test.html");
                        id = id + preferences.getInt(SettingsActivity.AWAL_NO, 100);
                        String isiSmsDefault = "Terimakasih " + mName + " sudah mendaftar TONAS USM STIS Permata, no pendaftaran anda: "
                                + id + ". Informasi pembayaran klik " + payVal;
                        mKotakEditText.setText(isiSmsDefault);
                    } else {
                        mPeringatanText.setVisibility(TextView.VISIBLE);
                        mNo = null;
                        mNamaEditText.setText("");
                    }
                    if (cursor != null) cursor.close();
                }
            }
        });

        manualSmsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNamaEditText.getText().length() != 0){
                    SharedPreferences preferences = getContext().getSharedPreferences(SettingsActivity.PREF_FILE, 0);
                    int simVal = preferences.getInt(SettingsActivity.SIM_SET, 1);
                    SmsManager.getSmsManagerForSubscriptionId(simVal).sendTextMessage(mNo, null, mKotakEditText.getText().toString().trim(), null, null);
                } else {
                    Toast.makeText(getContext(), "Isi dulu no pendaftar", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //Inflate the layout for this fragment
        return view;
    }
}