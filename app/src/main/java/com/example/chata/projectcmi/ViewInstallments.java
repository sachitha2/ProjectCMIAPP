package com.example.chata.projectcmi;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

        Cursor cDeals =sqLiteDatabase.rawQuery("SELECT * FROM installment WHERE status = 0;",null);

        int nRow = cDeals.getCount();

        cDeals.moveToNext();
        for(int i = 0; i < nRow;i++){
            singleRow = new SingleRowForInstallments(i +1,"2019-10-20",(cDeals.getInt(3) - cDeals.getInt(8))+"","Sam wilson","250","Galgamuwa","983142044V");

            myList.add(singleRow);
            cDeals.moveToNext();
        }

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
}
