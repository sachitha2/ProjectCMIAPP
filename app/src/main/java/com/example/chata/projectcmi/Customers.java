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

import java.util.ArrayList;

public class Customers extends AppCompatActivity implements TextWatcher {
    EditText searchCustomers;
    ListView customerList;

    SQLiteDatabase sqLiteDatabase;
    private String[] id, name,nic,area;


    ArrayList<SingleRowForCustomer> myList;
    CustomerListAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers);
        setTitle("Customers");
        ///Read data Start
        sqLiteDatabase = openOrCreateDatabase("cmi", Customers.MODE_PRIVATE,null);
        Cursor cForCustomers =sqLiteDatabase.rawQuery("SELECT * FROM customer ;",null);

        int nRow = cForCustomers.getCount();

        id = new String[nRow];
        name = new String[nRow];
        nic = new String[nRow];
        area = new String[nRow];

        int i=0;
        while (cForCustomers.moveToNext()){

            id[i] = cForCustomers.getString(0);
            name[i] = cForCustomers.getString(1);
            nic[i] = cForCustomers.getString(2);


            //get area name
            Cursor cForArea =sqLiteDatabase.rawQuery("SELECT name FROM area  WHERE id = '"+cForCustomers.getString(3)+"' ;",null);
            cForArea.moveToNext();
            area[i] =cForArea.getString(0);


            i++;
        }
        ///Read data End
        Log.d("Customers", "DATA: "+nRow);
        searchCustomers = findViewById(R.id.txtsearchCustomers);

        customerList = findViewById(R.id.listCustomers);



        searchCustomers.addTextChangedListener(this);


        myList = new ArrayList<>();
        SingleRowForCustomer singleRow;

        for( i = 0; i < name.length;i++){
            singleRow = new SingleRowForCustomer(name[i],id[i] ,nic[i] ,"","",area[i]);

            myList.add(singleRow);
        }

        myAdapter = new CustomerListAdapter(this,myList);

        customerList.setAdapter(myAdapter);






    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        this.myAdapter.getFilter().filter(s);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

}
