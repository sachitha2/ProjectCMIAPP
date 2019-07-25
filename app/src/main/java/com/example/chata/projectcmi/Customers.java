package com.example.chata.projectcmi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class Customers extends AppCompatActivity implements TextWatcher {
    EditText searchCustomers;
    ListView customerList;

    String[] name = {"Sachitha Hirushan","Chavindu Nuwanpriya","Sandali Hirunika","Dumidu kasun","Dilshan Shankalpa","Sachin Tharaka","Sandun Malitha"};
    String[] age = {"10","20","30","40","10","10","10","20","30","40","10","10"};
    ArrayList<SingleRowForCustomer> myList;
    CustomerListAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers);
        setTitle("Customers");


        searchCustomers = findViewById(R.id.txtsearchCustomers);

        customerList = findViewById(R.id.listCustomers);



        searchCustomers.addTextChangedListener(this);


        myList = new ArrayList<>();
        SingleRowForCustomer singleRow;

        for(int i = 0; i < name.length;i++){
            singleRow = new SingleRowForCustomer(name[i],age[i]);

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
