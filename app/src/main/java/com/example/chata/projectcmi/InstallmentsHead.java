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


        setTitle("Installments");


        all = findViewById(R.id.btnAll);
        month = findViewById(R.id.btnMonth);
        week = findViewById(R.id.btnWeek);
        today = findViewById(R.id.btnToday);
        passed = findViewById(R.id.btnpassed);

        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentInstall = new Intent(InstallmentsHead.this,ViewInstallments.class);
                intentInstall.putExtra("type","ALL");
                startActivity(intentInstall);
            }
        });

        month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentInstall = new Intent(InstallmentsHead.this,ViewInstallments.class);
                intentInstall.putExtra("type","MONTH");
                startActivity(intentInstall);
            }
        });

        week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentInstall = new Intent(InstallmentsHead.this,ViewInstallments.class);
                intentInstall.putExtra("type","WEEK");
                startActivity(intentInstall);
            }
        });

        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentInstall = new Intent(InstallmentsHead.this,ViewInstallments.class);
                intentInstall.putExtra("type","TODAY");
                startActivity(intentInstall);
            }
        });


        passed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentInstall = new Intent(InstallmentsHead.this,ViewInstallments.class);
                intentInstall.putExtra("type","PASSED");
                startActivity(intentInstall);
            }
        });



    }
}
