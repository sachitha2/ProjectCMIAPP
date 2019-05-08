package com.example.chata.projectcmi;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class Test extends AppCompatActivity {
    SQLiteDatabase sqlite;
    private Button createDBBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }



    public int createDatabases(){
        sqlite = openOrCreateDatabase("cmi", Context.MODE_PRIVATE,null);

        ////Creating Area Database

        sqlite.execSQL("CREATE TABLE IF  NOT EXISTS area (id int(11) NOT NULL,name varchar(200) NOT NULL);");

        ///Creating Customer Database
        sqlite.execSQL("CREATE TABLE IF  NOT EXISTS customer (" +
                "id int(11) NOT NULL," +
                "  name varchar(200) NOT NULL," +
                "  address text NOT NULL," +
                "  tp varchar(10) NOT NULL," +
                "  regdate date NOT NULL," +
                "  areaid int(3) NOT NULL," +
                "  nic varchar(15) NOT NULL," +
                "  agentid int(3) NOT NULL," +
                "  status int(1) NOT NULL );");//COMMENT '1=active,0=not active'




        //sqlite.execSQL("INSERT INTO test (ROLL_NO, NAME) VALUES ('11', '77');");
        //sqlite.execSQL("INSERT INTO test (ROLL_NO, NAME) VALUES ('1', '7');");


        //Cursor c =sqlite.rawQuery("SELECT * FROM test;",null);

//        if(c.getCount() == 0 ){
//            Toast.makeText(this, "No data in database", Toast.LENGTH_SHORT).show();
//        }
//        int nRow = c.getCount();
//        Toast.makeText(this,Integer.toString(nRow), Toast.LENGTH_SHORT).show();
//
//        StringBuffer buffer = new StringBuffer();
//        while (c.moveToNext()){
//            buffer.append("NAME "+c.getString(1));
//        }
//        Toast.makeText(this,buffer.toString(), Toast.LENGTH_SHORT).show();

        return 1;
    }

}
