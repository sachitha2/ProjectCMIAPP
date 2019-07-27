package com.example.chata.projectcmi;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class Pack extends AppCompatActivity {
    Spinner pack;
    SQLiteDatabase sqLiteDatabase;
    private String[] id, name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pack);
        setTitle("Packs");
        pack = findViewById(R.id.packs);

        sqLiteDatabase = openOrCreateDatabase("cmi", Customers.MODE_PRIVATE,null);

        Cursor cForPack =sqLiteDatabase.rawQuery("SELECT * FROM pack ;",null);

        int nRow = cForPack.getCount();

        id = new String[nRow];
        name = new String[nRow];

        int i=0;
        while (cForPack.moveToNext()){

            id[i] = cForPack.getString(0);
            name[i] = cForPack.getString(1);
            i++;
        }
        ArrayAdapter<String> packAdaptor = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, name);
        pack.setAdapter(packAdaptor);


        pack.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("pack", pack.getSelectedItemPosition()+"");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
