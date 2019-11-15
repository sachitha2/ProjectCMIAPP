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


        setTitle("Payment History - "+InvoiceId);

        txtTotal = findViewById(R.id.titleData);

        Cursor deal = sqLiteDatabase.rawQuery("SELECT * FROM deals WHERE id = "+InvoiceId+";",null);
        deal.moveToNext();


        txtTotal.setText(" "+(deal.getFloat(5)-deal.getFloat(6)));


        customerList = findViewById(R.id.listInstallments);

        Cursor cForCollection =sqLiteDatabase.rawQuery("SELECT * FROM collection WHERE dealid = '"+InvoiceId+"';",null);

        total = cForCollection.getCount();


        myList = new ArrayList<>();
        SingleRowForPaymentHistory singleRow;
        int x = 0;
        while(cForCollection.moveToNext()){
            singleRow = new SingleRowForPaymentHistory(x++,cForCollection.getString(4),cForCollection.getString(5));
            myList.add(singleRow);

        }

        myAdapter = new ListViewPaymentHistory(this,myList);

        customerList.setAdapter(myAdapter);
    }
}
