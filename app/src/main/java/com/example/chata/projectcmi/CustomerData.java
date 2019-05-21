package com.example.chata.projectcmi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CustomerData extends AppCompatActivity {
    private String customerId, customerName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_data);

        customerId= getIntent().getStringExtra("customerId");
        customerName= getIntent().getStringExtra("customerName");

        setTitle(customerId+"-"+customerName);
    }
}
