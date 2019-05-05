package com.example.chata.projectcmi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SelectArea extends AppCompatActivity {

    private ListView areaList;
    private RequestQueue requestQueueForAreaList;

    private String[] name;
    private String[] id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_area);

        areaList = (ListView) findViewById(R.id.areaList);
        requestQueueForAreaList = Volley.newRequestQueue(SelectArea.this);
        jsonParseAreaList();

    }

    private void jsonParseAreaList() {

        String url = "http://cms.infinisolutionslk.com/APP/area.json.php";

        JsonObjectRequest requestAreaList = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            //Read json and assign them to local variables
                            JSONArray Lname = response.getJSONArray("area");
                            JSONArray Lid = response.getJSONArray("id");
                            name = new String[Lname.length()];
                            id = new String[Lname.length()];

                            for (int i = 0; i < Lname.length(); i++){
                                name[i] = Lname.get(i).toString();
                                id[i] = Lid.get(i).toString();
                            }

                            //then create the grid view
                            ListViewAdapter areaListViewAdapter = new ListViewAdapter(SelectArea.this, name, id,"SelectArea");
                            areaList.setAdapter(areaListViewAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueueForAreaList.add(requestAreaList);

    }

}
