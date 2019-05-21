package com.example.chata.projectcmi;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class DownloadData extends AppCompatActivity {
    SQLiteDatabase sqlite;//1
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_data);
        createDatabases();
    }

    public int createDatabases(){
        sqlite = openOrCreateDatabase("cmi", Context.MODE_PRIVATE,null);//10

        ////Creating Area Table

        sqlite.execSQL("CREATE TABLE IF  NOT EXISTS area (id int(11) NOT NULL,name varchar(200) NOT NULL);");




        sqlite.execSQL("INSERT INTO area (id, name) VALUES ('11', 'Galgamuwa');");
        sqlite.execSQL("INSERT INTO area (id, name) VALUES ('12', 'Rajanganaya');");
        //sqlite.execSQL("INSERT INTO test (ROLL_NO, NAME) VALUES ('1', '7');");


        Cursor c =sqlite.rawQuery("SELECT * FROM area;",null);

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
