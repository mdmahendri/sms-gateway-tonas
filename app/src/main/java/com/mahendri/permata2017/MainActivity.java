package com.mahendri.permata2017;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.mahendri.permata2017.data.TonasContract.PendaftarEntry;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dasar);

        //viewpager untuk slide page
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(new TonasPagerAdapter(getSupportFragmentManager(), this));

        //tablayout untuk viewpager
        TabLayout tab = (TabLayout) findViewById(R.id.sliding_tab);
        tab.setupWithViewPager(viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if(getSupportActionBar() != null) getSupportActionBar().setElevation(0);
        return true;
    }

    private void doSave(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText editText = new EditText(getBaseContext());
        editText.setTextColor(Color.BLACK);
        editText.setHint("Nama file");
        alert.setView(editText);
        alert.setMessage("Simpan tabel ke memori internal? Beri nama file");
        alert.setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String namaFile = editText.getText().toString().trim();
                getContentResolver().call(PendaftarEntry.CONTENT_URI, "export", namaFile, null);
            }
        });
        alert.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        FrameLayout.LayoutParams layout = new FrameLayout.LayoutParams(510, FrameLayout.LayoutParams.WRAP_CONTENT);
        layout.setMargins(50,5,5,5);
        alert.show();
        editText.setLayoutParams(layout);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_export_tabel:
                doSave();
                return true;
            case R.id.settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.about:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
            case R.id.insert_new:
                startActivity(new Intent(this, EditActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
