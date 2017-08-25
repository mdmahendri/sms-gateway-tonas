package com.mahendri.permata2017;

import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        if (getSupportActionBar()!= null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ArrayList<About> aboutArray = new ArrayList<>();
        aboutArray.add(new About("Keperluan", "Membantu pelaksanaan TONAS 2017"));
        aboutArray.add(new About("Versi", "Versi awal, baru saja dibuat"));
        aboutArray.add(new About("Kontak", "mahendri.md@gmail.com"));
        aboutArray.add(new About("Pesan", "Program baru, masih banyak kekurangan"));


        AboutAdapter aboutAdapter = new AboutAdapter(this, aboutArray);
        ListView aboutList = (ListView) findViewById(R.id.about_list);
        aboutList.setAdapter(aboutAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
