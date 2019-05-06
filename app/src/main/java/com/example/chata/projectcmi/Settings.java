/**
 * By sachitha hirushan
 * */
package com.example.chata.projectcmi;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Settings extends AppCompatActivity {
    SQLiteDatabase sqlite;
    private Button createDBBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        createDBBtn = findViewById(R.id.createDBBtn);

        createDBBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("onClick","Button clicked");


                int x = createDatabases();

                Log.d("onClick ","Database fn " + Integer.toString(x));
            }
        });


    }

    public int createDatabases(){
        sqlite = openOrCreateDatabase("infini", Context.MODE_PRIVATE,null);


        sqlite.execSQL("CREATE TABLE IF  NOT EXISTS test (ROLL_NO int(3),NAME varchar(20));");

        sqlite.execSQL("INSERT INTO test (ROLL_NO, NAME) VALUES ('11', '77');");
        sqlite.execSQL("INSERT INTO test (ROLL_NO, NAME) VALUES ('1', '7');");


        Cursor c =sqlite.rawQuery("SELECT * FROM test;",null);

        if(c.getCount() == 0 ){
            Toast.makeText(this, "No data in database", Toast.LENGTH_SHORT).show();
        }
        int nRow = c.getCount();
        Toast.makeText(this,Integer.toString(nRow), Toast.LENGTH_SHORT).show();

        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            buffer.append("NAME "+c.getString(1));
        }
        Toast.makeText(this,buffer.toString(), Toast.LENGTH_SHORT).show();

        return 1;
    }

    }

