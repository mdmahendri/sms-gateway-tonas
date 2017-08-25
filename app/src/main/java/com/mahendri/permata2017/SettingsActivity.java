package com.mahendri.permata2017;

import android.content.SharedPreferences;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class SettingsActivity extends AppCompatActivity {
    public static String PREF_FILE = "PrefFile";
    public static String SIM_SET = "Spinner_SIM";
    public static String PAYLINK_SET = "Pembayaran";
    public static String TICKETLINK_SET = "Tiket";
    public static String AWAL_NO = "No_Daftar";

    EditText mSimEditText;
    EditText mPayEditText;
    EditText mTicketEditText;
    EditText mAwalDbEditText;

    int mSimVal;
    int mAwalVal;
    String mPayVal;
    String mTicketVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        SubscriptionManager subscriptionManager = SubscriptionManager.from(getApplicationContext());
        List<SubscriptionInfo> subscriptionInfoList = subscriptionManager.getActiveSubscriptionInfoList();
        int id1, id2;
        TextView text = (TextView) findViewById(R.id.list_sim);
        if (subscriptionInfoList.size() == 1) {
            id1 = subscriptionInfoList.get(0).getSubscriptionId();
            String hellIsh = " " + id1;
            text.setText(hellIsh);
        }
        if (subscriptionInfoList.size() > 1) {
            id1 = subscriptionInfoList.get(0).getSubscriptionId();
            id2 = subscriptionInfoList.get(1).getSubscriptionId();
            text.setText(id1 + ", " + id2);
        }

        SharedPreferences preferences = getSharedPreferences(PREF_FILE, 0);
        mSimVal = preferences.getInt(SIM_SET, 1);
        mPayVal = preferences.getString(PAYLINK_SET, "http://www.mahendri.com/test.html");
        mTicketVal = preferences.getString(TICKETLINK_SET, "http://www.mahendri.com/test2.html");
        mAwalVal = preferences.getInt(AWAL_NO, 100);

        mSimEditText = (EditText) findViewById(R.id.pilihan_sim);
        mSimEditText.setText(String.valueOf(mSimVal));

        mAwalDbEditText = (EditText) findViewById(R.id.numb_db_edit);
        mAwalDbEditText.setText(String.valueOf(mAwalVal));

        mPayEditText = (EditText) findViewById(R.id.pay_link);
        mPayEditText.setText(mPayVal);

        mTicketEditText = (EditText) findViewById(R.id.ticket_link);
        mTicketEditText.setText(mTicketVal);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.save_setting:
                mSimVal = Integer.parseInt(mSimEditText.getText().toString().trim());
                mAwalVal = Integer.parseInt(mAwalDbEditText.getText().toString().trim());
                mPayVal = mPayEditText.getText().toString().trim();
                mTicketVal = mTicketEditText.getText().toString().trim();
                SharedPreferences setting = getSharedPreferences(PREF_FILE, 0);
                SharedPreferences.Editor editor = setting.edit();
                if (mSimVal > -1) editor.putInt(SIM_SET, mSimVal);
                if (mAwalVal > 99) editor.putInt(AWAL_NO, mAwalVal);
                editor.putString(PAYLINK_SET, mPayVal);
                editor.putString(TICKETLINK_SET, mTicketVal);
                editor.apply();
                finish();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
