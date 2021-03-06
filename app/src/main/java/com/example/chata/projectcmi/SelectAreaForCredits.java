package com.example.chata.projectcmi;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;

public class SelectAreaForCredits extends AppCompatActivity {
    public ListView areaList;
    SQLiteDatabase sqlite;//1

    public String[] name;
    public String[] id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_area_for_credits);
        sqlite = openOrCreateDatabase("cmi", Context.MODE_PRIVATE,null);//10


        areaList = findViewById(R.id.areaList);

        Cursor c =sqlite.rawQuery("SELECT * FROM area;",null);

        if(c.getCount() == 0 ){
            Toast.makeText(this, "No data in database", Toast.LENGTH_SHORT).show();
        }else{
            name = new String[c.getCount()];
            id = new String[c.getCount()];



            int nRow = c.getCount();
            Toast.makeText(this,Integer.toString(nRow), Toast.LENGTH_SHORT).show();

            StringBuffer buffer = new StringBuffer();
            int i = 0;
            while (c.moveToNext()){
                buffer.append("NAME "+c.getString(1));
                name[i] = c.getString(1);
                id[i] = c.getString(0);
                i++;
            }
            // then create the grid view
            ListViewAdapter areaListViewAdapter = new ListViewAdapter(this, name, id,"SelectArea");
            areaList.setAdapter(areaListViewAdapter);
        }

    }
}
