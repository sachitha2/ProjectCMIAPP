package com.example.chata.projectcmi;

import android.content.Intent;
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

        for(int i = 0; i < 10;i++){
            singleRow = new SingleRowForInstallments(10,"2019-10-20","1000","Sam wilson","250","Galgamuwa","983142044V");

            myList.add(singleRow);
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
