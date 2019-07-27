package com.example.chata.projectcmi;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class AddACustomer extends AppCompatActivity {
    Button saveCustomer;
    EditText name,nic;
    private Spinner area;
    private String[] Id, ItemName;
    SQLiteDatabase sqLiteDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_acustomer);

        sqLiteDatabase  = openOrCreateDatabase("cmi", Context.MODE_PRIVATE,null);
        setTitle("Add A Customer");
        area = (Spinner) findViewById(R.id.area);
        saveCustomer = findViewById(R.id.btnSaveCustomer);



        Cursor cForItems =sqLiteDatabase.rawQuery("SELECT * FROM area ;",null);

        int nRow = cForItems.getCount();

        Id = new String[nRow];
        ItemName = new String[nRow];

        int i=0;
        while (cForItems.moveToNext()){

            Id[i] = cForItems.getString(0);
            ItemName[i] = cForItems.getString(1);

            i++;
        }

        ArrayAdapter<String> ItemAdaptor = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, ItemName);
        area.setAdapter(ItemAdaptor);


        saveCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = findViewById(R.id.txtName);
                nic = findViewById(R.id.txtNic);
                //find last id Start
                Cursor cForCustomers =sqLiteDatabase.rawQuery("SELECT * FROM customer ORDER BY customer.id DESC;",null);
                cForCustomers.moveToNext();

                //find last id End
                sqLiteDatabase.execSQL("INSERT INTO customer (id, name,nic,areaId) VALUES ('"+(cForCustomers.getInt(0) + 1)+"', '"+name.getText().toString()+"', '"+nic.getText().toString()+"','"+Id[area.getSelectedItemPosition()]+"');");

            }
        });
    }
}
