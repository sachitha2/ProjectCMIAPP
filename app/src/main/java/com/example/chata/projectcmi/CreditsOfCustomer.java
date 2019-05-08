package com.example.chata.projectcmi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CreditsOfCustomer extends AppCompatActivity {

    private String customerId, customerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits_of_customer);

        customerId= getIntent().getStringExtra("customerId");
        customerName= getIntent().getStringExtra("customerName");

        setTitle("Credits of " + customerName);

    }
}
