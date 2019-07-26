package com.example.chata.projectcmi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Create extends AppCompatActivity {
    Button addACustomer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        setTitle("Create");

        addACustomer = findViewById(R.id.btnAddACustomer);
        addACustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAddACustomer = new Intent();
                intentAddACustomer.setClass(Create.this, AddACustomer.class);
                intentAddACustomer.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(intentAddACustomer);
            }
        });
    }
}
