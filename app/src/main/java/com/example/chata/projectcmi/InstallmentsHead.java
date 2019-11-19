package com.example.chata.projectcmi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class InstallmentsHead extends AppCompatActivity {
    Button all,month,week,today,passed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_installments_head);

        all = findViewById(R.id.btnAll);

        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentInstall = new Intent(InstallmentsHead.this,ViewInstallments.class);
                startActivity(intentInstall);
            }
        });

        setTitle("Installments");


    }
}
