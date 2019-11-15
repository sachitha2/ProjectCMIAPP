package com.example.chata.projectcmi;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class PaymentHistory extends AppCompatActivity {
    SQLiteDatabase sqLiteDatabase;
    int total;
    ListViewPaymentHistory myAdapter;
    ArrayList<SingleRowForPaymentHistory> myList;
    ListView customerList;
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


        customerList = findViewById(R.id.listInstallments);

        SingleRowForPaymentHistory singleRow;

        for(int i = 1; i <= 2;i++){
            singleRow = new SingleRowForPaymentHistory(1,"200","2019");
            myList.add(singleRow);
        }

        myAdapter = new ListViewPaymentHistory(this,myList);

        customerList.setAdapter(myAdapter);
    }
}
