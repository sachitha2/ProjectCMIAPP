package com.example.chata.projectcmi;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class PendingOrders extends AppCompatActivity implements TextWatcher {
    private ListView shopsList;
    private TextView total;
    private TextView txtNumInvoice;
    EditText search;

    String customerName;

    //chatson
    ArrayList<SingleRowForPendingOrders> myList;
    PendingOrdersListAdapter shopsListAdapter;
    //chatson

    SQLiteDatabase sqLiteShops;

    private String[] Id,CustomerId,CustomerName,Address,Contact,Root,IdNo,Credit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_orders);


        sqLiteShops = openOrCreateDatabase("cmi", PendingOrders.MODE_PRIVATE,null);

        shopsList =  findViewById(R.id.dataList);
        search =  findViewById(R.id.search);
        total = findViewById(R.id.txtTotal);
        txtNumInvoice = findViewById(R.id.txtNumInvoice);

//        Cursor totalCash =sqLiteShops.rawQuery("SELECT SUM(Total) FROM deal WHERE S=0 OR S = 1 AND   date =  date('now','localtime');",null);
//        totalCash.moveToNext();totalCash.getInt(0)

        total.setText("Total : 1000");



        Cursor c =sqLiteShops.rawQuery("SELECT * FROM orderdata WHERE   date =  date('now','localtime');",null);
        Log.d("pending ","orderData : "+c.getCount());
        int nRow = c.getCount();
        txtNumInvoice.setText("Number of Pending Orders  " + nRow);
        Id = new String[nRow];
        CustomerId = new String[nRow];
        CustomerName = new String[nRow];
        Address = new String[nRow];
        Contact = new String[nRow];
        Root = new String[nRow];
        IdNo = new String[nRow];
        Credit = new String[nRow];
        myList = new ArrayList<>();
        SingleRowForPendingOrders singleRow;
        int i = 0;
        while (c.moveToNext()){

            Id[i] = "" + i;
            CustomerId[i] ="1"; //c.getString(0);
            CustomerName[i] = "";//c.getString(1);
            Address[i] = "no";//c.getString(2);

            //Looking for customer name
            Cursor cForCustomer =sqLiteShops.rawQuery("SELECT * FROM customer where id = "+c.getString(2)+";",null);

//            Cursor invoiceQty =sqLiteShops.rawQuery("SELECT * FROM invoice where dealId = '"+c.getString(0)+"';",null);
            int nRowInvoiceQty = 10;//invoiceQty.getCount();


//            shopNameFind.moveToNext();
//            customerName = cForCustomer.getString(1);

            //Looking for shop name




            singleRow = new SingleRowForPendingOrders(customerName, CustomerId[i], CustomerName[i],Address[i],nRowInvoiceQty+"");

            myList.add(singleRow);


            i++;

        }

        search.addTextChangedListener(this);


        shopsListAdapter = new PendingOrdersListAdapter(this,myList);

        shopsList.setAdapter(shopsListAdapter);

        sqLiteShops.close();

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        this.shopsListAdapter.getFilter().filter(s);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
