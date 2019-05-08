package com.example.chata.projectcmi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.android.volley.RequestQueue;

public class SelectAreaForCredits extends AppCompatActivity {
    public ListView areaList;

    public String[] name;
    public String[] id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_area_for_credits);


        name = new String[5];
        id = new String[5];
        areaList = findViewById(R.id.areaList);
        for (int i = 0; i < 5; i++){
                                name[i] = "sachitha";
                                id[i] = "sam";
                            }

                           // then create the grid view
                            ListViewAdapter areaListViewAdapter = new ListViewAdapter(this, name, id,"SelectArea");
                            areaList.setAdapter(areaListViewAdapter);
    }
}
