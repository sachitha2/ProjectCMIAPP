package com.example.chata.projectcmi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CustomerData extends AppCompatActivity {
    private String customerId, customerName;
    private Button btnNewPendingOrder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_data);

        customerId= getIntent().getStringExtra("customerId");

        setTitle("Customer Profile - "+customerId);
//        customerName= getIntent().getStringExtra("customerName");
        btnNewPendingOrder = findViewById(R.id.btnNewPendingOrder);
        btnNewPendingOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentNewPending = new Intent(getBaseContext(),   NewPendingOrder.class);
                startActivity(intentNewPending);
            }
        });


    }
}
