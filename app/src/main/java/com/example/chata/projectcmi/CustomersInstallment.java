package com.example.chata.projectcmi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class CustomersInstallment extends AppCompatActivity  implements TextWatcher {
    private String InvoiceId;


    ArrayList<SingleRowForInstallment> myList;
    ListViewInstallmentOfACustomer myAdapter;

    EditText searchCustomers;
    ListView customerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers_installment);

        InvoiceId= getIntent().getStringExtra("InvoiceId");

        setTitle("Deal Id "+InvoiceId);




        searchCustomers = findViewById(R.id.txtsearchInstallments);

        customerList = findViewById(R.id.listInstallments);



        searchCustomers.addTextChangedListener(this);


        myList = new ArrayList<>();
        SingleRowForInstallment singleRow;

//        for( i = 0; i < name.length;i++){
            singleRow = new SingleRowForInstallment(5+"");
            myList.add(singleRow);
            singleRow = new SingleRowForInstallment(5+"");
            myList.add(singleRow);
            singleRow = new SingleRowForInstallment(5+"");
            myList.add(singleRow);


//        }

        myAdapter = new ListViewInstallmentOfACustomer(this,myList);

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
