package com.example.chata.projectcmi;

import android.content.Context;
import android.content.Intent;
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

public class ViewInstallments extends AppCompatActivity implements TextWatcher {
    String type;
    ListView list;
    EditText search;
    SQLiteDatabase sqLiteDatabase;


    ArrayList<SingleRowForInstallments> myList;
    ListViewForInstallments myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_installments);



        type= getIntent().getStringExtra("type");

        setTitle("INSTALLMENTS "+type);

        list= findViewById(R.id.list);
        search = findViewById(R.id.txtSearch);

        search.addTextChangedListener(this);


        myList = new ArrayList<>();
        SingleRowForInstallments singleRow;

        sqLiteDatabase  = openOrCreateDatabase("cmi", Context.MODE_PRIVATE,null);
        Cursor cDeals;
        if(type.equals("MONTH")){
             cDeals =sqLiteDatabase.rawQuery("SELECT * FROM installment WHERE  status = 0 AND strftime('%m', date) = strftime('%m', CURRENT_DATE) AND  strftime('%Y', date) = strftime('%Y', CURRENT_DATE);",null);

        }
        else if(type.equals("TODAY")){
            cDeals =sqLiteDatabase.rawQuery("SELECT * FROM installment WHERE  status = 0 AND date = CURRENT_DATE;",null);

        } else if(type.equals("PASSED")){
            cDeals =sqLiteDatabase.rawQuery("SELECT * FROM installment WHERE  status = 0 AND date < CURRENT_DATE;",null);

        }
        else{
             cDeals =sqLiteDatabase.rawQuery("SELECT * FROM installment WHERE  status = 0;",null);

        }


        int nRow = cDeals.getCount();

        cDeals.moveToNext();
        for(int i = 0; i < nRow;i++){

            Cursor customer = sqLiteDatabase.rawQuery("SELECT * FROM customer WHERE id = "+cDeals.getString(9)+";",null);
            customer.moveToNext();

//            Log.d("AREA TABLE",area(1));
            singleRow = new SingleRowForInstallments(i +1,cDeals.getString(5),(cDeals.getInt(3) - cDeals.getInt(8))+"",customer.getString(1),cDeals.getString(9),area(customer.getInt(3)),customer.getString(2),"-",cDeals.getString(1));

            myList.add(singleRow);
            cDeals.moveToNext();
            customer.close();
        }

        cDeals.close();
        myAdapter = new ListViewForInstallments(this,myList);

        list.setAdapter(myAdapter);



    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        this.myAdapter.getFilter().filter(s);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

String area(int areaId){
    if(type.equals("ALL")){
        return "-";
    }else {


        Cursor customer = sqLiteDatabase.rawQuery("SELECT * FROM area WHERE id = " + areaId + ";", null);
        customer.moveToNext();
        if (customer.getCount() != 0) {
            return customer.getCount() + customer.getString(1);
        } else {
            return "-";
        }
    }
}



}
