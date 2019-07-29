package com.example.chata.projectcmi;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class NewPendingOrder extends AppCompatActivity {
    Spinner sPinerItems;
    SQLiteDatabase sqLiteDatabase;
    private String[] id, name;
    private TextView txtInvoiceId;

    long time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pending_order);

        sPinerItems = findViewById(R.id.spinerItems);
        txtInvoiceId = (TextView) findViewById(R.id.txtInvoiceId);

        time = System.currentTimeMillis();

        txtInvoiceId.setText( time+"");

        sqLiteDatabase = openOrCreateDatabase("cmi", Customers.MODE_PRIVATE,null);

        Cursor cForPack =sqLiteDatabase.rawQuery("SELECT * FROM area ;",null);

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
        sPinerItems.setAdapter(packAdaptor);
    }
}
