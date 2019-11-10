package com.example.chata.projectcmi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CustomersInstallment extends AppCompatActivity {
    private String InvoiceId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers_installment);

        InvoiceId= getIntent().getStringExtra("InvoiceId");

        setTitle("Deal Id "+InvoiceId);


    }
}
