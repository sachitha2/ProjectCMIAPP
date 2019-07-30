package com.example.chata.projectcmi;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

    private String[] Id,ShopId,ShopName,Address,Contact,Root,IdNo,Credit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_orders);


        sqLiteShops = openOrCreateDatabase("cmi", PendingOrders.MODE_PRIVATE,null);

        shopsList = (ListView) findViewById(R.id.dataList);
        search = (EditText) findViewById(R.id.search);
        total = findViewById(R.id.txtTotal);
        txtNumInvoice = findViewById(R.id.txtNumInvoice);

//        Cursor totalCash =sqLiteShops.rawQuery("SELECT SUM(Total) FROM deal WHERE S=0 OR S = 1 AND   date =  date('now','localtime');",null);
//        totalCash.moveToNext();totalCash.getInt(0)

        total.setText("Total : 1000");



//        Cursor c =sqLiteShops.rawQuery("SELECT * FROM deal WHERE   date =  date('now','localtime');",null);

        int nRow =1;// c.getCount();
        txtNumInvoice.setText("Number of invoices " + nRow);
        Id = new String[nRow];
        ShopId = new String[nRow];
        ShopName = new String[nRow];
        Address = new String[nRow];
        Contact = new String[nRow];
        Root = new String[nRow];
        IdNo = new String[nRow];
        Credit = new String[nRow];
        myList = new ArrayList<>();
        SingleRowForPendingOrders singleRow;
        //c.moveToNext()
        int i = 0;
        while (i < 1){

            Id[i] = "" + i;
            ShopId[i] ="1"; //c.getString(0);
            ShopName[i] = "sam";//c.getString(1);
            Address[i] = "no";//c.getString(2);

            //Looking for shop name
            Cursor shopNameFind =sqLiteShops.rawQuery("SELECT * FROM shop where id = "+c.getString(1)+";",null);

//            Cursor invoiceQty =sqLiteShops.rawQuery("SELECT * FROM invoice where dealId = '"+c.getString(0)+"';",null);
            int nRowInvoiceQty = 10;//invoiceQty.getCount();


//            shopNameFind.moveToNext();
            customerName = "Hello shop";//shopNameFind.getString(1);

            //Looking for shop name




            singleRow = new SingleRowForPendingOrders(customerName, ShopId[i], ShopName[i],Address[i],nRowInvoiceQty+"");

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
