package com.example.chata.projectcmi;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class PaymentHistory extends AppCompatActivity {
    SQLiteDatabase sqLiteDatabase;
    int total;
    TextView txtTotal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_history);
        String InvoiceId = getIntent().getStringExtra("InvoiceId");

        sqLiteDatabase = openOrCreateDatabase("cmi", Customers.MODE_PRIVATE,null);
        Cursor cForCollection =sqLiteDatabase.rawQuery("SELECT * FROM collection;",null);

        total = cForCollection.getCount();

        setTitle("Payment History - "+InvoiceId);

        txtTotal = findViewById(R.id.titleData);

        Cursor deal = sqLiteDatabase.rawQuery("SELECT * FROM deals WHERE id = "+InvoiceId+";",null);
        deal.moveToNext();


        txtTotal.setText(" "+(deal.getFloat(5)-deal.getFloat(6)));
    }
}
