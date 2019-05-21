package com.example.chata.projectcmi;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DownloadData extends AppCompatActivity {
    SQLiteDatabase sqlite;
    private RequestQueue requestQueueForCreditList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_data);
        requestQueueForCreditList = Volley.newRequestQueue(DownloadData.this);
        createDatabases();
    }

    public int createDatabases(){
        sqlite = openOrCreateDatabase("cmi", Context.MODE_PRIVATE,null);//10


        //Drop Table if Exist
        sqlite.execSQL("DROP TABLE IF EXISTS area;");

        ////Creating Area Table
        sqlite.execSQL("CREATE TABLE IF  NOT EXISTS area (id int(11) NOT NULL,name varchar(200) NOT NULL);");




        ///Download Area Table



        ///Download Area Table




        sqlite.execSQL("INSERT INTO area (id, name) VALUES ('11', 'Galgamuwa');");
        sqlite.execSQL("INSERT INTO area (id, name) VALUES ('12', 'Rajanganaya');");
        //sqlite.execSQL("INSERT INTO test (ROLL_NO, NAME) VALUES ('1', '7');");
        jsonParseCreditList();

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


    private void jsonParseCreditList() {

        String url = "http://cms.infinisolutionslk.com/APP/area.json.php" ;

        JsonObjectRequest requestCreditList = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            //Read json and assign them to local variables
                            JSONArray area = response.getJSONArray("area");
                            JSONArray id = response.getJSONArray("id");



//
                            for (int i = 0; i < id.length(); i++){
                                sqlite.execSQL("INSERT INTO area (id, name) VALUES ('"+id.get(i).toString()+"', '"+area.get(i).toString()+"');");
                            }



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
        requestQueueForCreditList.add(requestCreditList);

    }
}
