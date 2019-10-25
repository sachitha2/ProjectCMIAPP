package com.example.chata.projectcmi;

        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;

public class CustomerDeals extends AppCompatActivity {
    private String customerId;


    SQLiteDatabase sqLiteDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_deals);
        customerId= getIntent().getStringExtra("customerId");
        setTitle("Deals - "+customerId);


        sqLiteDatabase  = openOrCreateDatabase("cmi", Context.MODE_PRIVATE,null);

        Cursor cDeals =sqLiteDatabase.rawQuery("SELECT * FROM deals ;",null);

        int nRow = cDeals.getCount();


        Log.d("Deals",  "number of data " + nRow);
    }
}
