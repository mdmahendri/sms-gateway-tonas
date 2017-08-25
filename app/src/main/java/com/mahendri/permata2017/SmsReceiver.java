package com.mahendri.permata2017;

import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

import com.mahendri.permata2017.data.TonasContract.PendaftarEntry;
import com.mahendri.permata2017.data.TonasProvider;

import java.util.regex.Pattern;

/**
 * Created by mahendri on 12/31/2016.
 * using this as the core for this program
 */

public class SmsReceiver extends BroadcastReceiver {
    @SuppressWarnings("deprecation")
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences preferences = context.getSharedPreferences(SettingsActivity.PREF_FILE, 0);
        int simVal = preferences.getInt(SettingsActivity.SIM_SET, 1);
        int awalVal = preferences.getInt(SettingsActivity.AWAL_NO, 100);
        String payVal = preferences.getString(SettingsActivity.PAYLINK_SET,
                "http://www.mahendri.com/test.html");
        final Bundle bundle = intent.getExtras();

        //Ekstrak SMS
        if (bundle != null) {
            if (bundle.getInt("subscription", 1) == simVal){
                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                if (pdusObj != null) {
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdusObj[0]);
                    String pengirim = smsMessage.getDisplayOriginatingAddress();
                    String isiSms = smsMessage.getDisplayMessageBody();

                    if (Pattern.matches("\\+\\d{7,}", pengirim)){
                        //var isi sms yang akan dikirim
                        String kirimSms;

                        //Cek sesuai format tidak
                        if (Pattern.matches("(?i)TONAS/[A-Za-z\\-\\'\\.\\s]{3,40}/[\\w\\d\\s]{5,30}/[12]"
                                , isiSms)) {
                            //partisi beberapa bagian SMS
                            isiSms = isiSms.substring(isiSms.indexOf("/") + 1);
                            String nama = isiSms.substring(0, isiSms.indexOf("/")).trim();

                            isiSms = isiSms.substring(isiSms.indexOf("/") + 1);
                            String sekolah = isiSms.substring(0, isiSms.indexOf("/")).trim();
                            int paket = Integer.parseInt(isiSms.substring(isiSms.indexOf("/")+1));

                            //Data SMS ke database
                            ContentValues values = new ContentValues();
                            values.put(PendaftarEntry.COLUMN_DAFTAR_NAMA, nama);
                            values.put(PendaftarEntry.COLUMN_DAFTAR_SEKOLAH, sekolah);
                            values.put(PendaftarEntry.COLUMN_DAFTAR_HP, pengirim);
                            values.put(PendaftarEntry.COLUMN_DAFTAR_PAKET, paket);

                            //Ekstrak nama depan dulu bos
                            String sName = nametoSend(nama);

                            int count = 1;
                            //Cek apa sudah pernah mendaftar sebelumnya
                            Bundle doubCheck = context.getContentResolver().call(
                                    PendaftarEntry.CONTENT_URI, "check", pengirim, null);
                            if (doubCheck != null){
                                count = doubCheck.getInt("doubCheck");
                            }

                            //Lanjut insert ke db dan kirim sms
                            if (count == 0) {
                                Uri uri = context.getContentResolver().insert(
                                        PendaftarEntry.CONTENT_URI, values);
                                long id = ContentUris.parseId(uri);
                                id = id + awalVal;
                                kirimSms = "Terimakasih " + sName
                                        + " sudah mendaftar TONAS USM STIS Permata, " +
                                        "no pendaftaran anda: " + id + ". " +
                                        "Informasi pembayaran klik " + payVal;

                                SmsManager.getSmsManagerForSubscriptionId(simVal)
                                        .sendTextMessage(pengirim, null, kirimSms, null, null);
                            } else {
                                //Kirim pesan bahwa sudah pernah daftar
                                if (doubCheck != null){
                                    int noDaftar = doubCheck.getInt(TonasProvider.NO_KEY) + awalVal;
                                    String namaDaftar = doubCheck.getString(TonasProvider.NAME_KEY);
                                    int paketDaftar = doubCheck.getInt(TonasProvider.PAKET_KEY);
                                    kirimSms = "Maaf anda sudah pernah mendaftar sebelumnya "
                                            + "dengan nama " + namaDaftar + ", no pendaftaran "
                                            + noDaftar + ", paket " + paketDaftar;
                                    SmsManager.getSmsManagerForSubscriptionId(simVal)
                                            .sendTextMessage(pengirim, null, kirimSms, null, null);
                                }
                            }
                        } else {
                            kirimSms = "Pendaftaran gagal!\nPastikan format SMS benar. " +
                                    "Ketik: TONAS/Nama lengkap/Asal sekolah/Paket\nContoh: " +
                                    "TONAS/Permata/SMAN 50 Sragen/1";
                            SmsManager.getSmsManagerForSubscriptionId(simVal)
                                    .sendTextMessage(pengirim, null, kirimSms, null, null);
                        }
                    }
                }
            }

        }
    }

    String nametoSend(String nama){
        String name;
        String nDepan;
        if(nama.contains(" ")) {
            String namaRinci = nama.substring(nama.indexOf(" ")+1);
            if (namaRinci.contains(" ")){
                String namaTengah = namaRinci.substring(0,namaRinci.indexOf(" "));
                nDepan = nama.substring(0, nama.indexOf(" "));
                name = nDepan + " " + namaTengah;
            } else {
                name = nama.substring(0, nama.indexOf(" "));
            }
        } else {
            name = nama;
        }

        return name;
    }
}
