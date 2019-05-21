package com.example.chata.projectcmi;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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
    Button bluetoothBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_data);
        requestQueueForCreditList = Volley.newRequestQueue(DownloadData.this);

        bluetoothBtn = findViewById(R.id.SelectBTBtn);

        bluetoothBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSelectArea = new Intent(DownloadData.this, SelectBuletoothMAC.class);
                startActivity(intentSelectArea);
            }
        });

        ProgressDialog progressDialog = new ProgressDialog(DownloadData.this);
        progressDialog.setTitle("Download");
        progressDialog.setMessage("hello this is a progress dialog box");
        progressDialog.setCancelable(true);
        progressDialog.show();

        progressDialog.setMessage("Hiiii");


        createDatabases(progressDialog);
    }

    public int createDatabases(ProgressDialog progressDialog){



        sqlite = openOrCreateDatabase("cmi", Context.MODE_PRIVATE,null);//10

        progressDialog.setMessage("Deleting Area Table");
        //Drop Table if Exist
        sqlite.execSQL("DROP TABLE IF EXISTS area;");

        progressDialog.setMessage("creating Area Table");
        ////Creating Area Table
        sqlite.execSQL("CREATE TABLE IF  NOT EXISTS area (id int(11) NOT NULL,name varchar(200) NOT NULL);");




        ///Download Area Table
        jsonParseCreditList(progressDialog);


        ///Download Area Table

        //sqlite.execSQL("INSERT INTO test (ROLL_NO, NAME) VALUES ('1', '7');");


//        Cursor c =sqlite.rawQuery("SELECT * FROM area;",null);
//
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


    private void jsonParseCreditList(final ProgressDialog progressDialog) {
        progressDialog.setMessage("Downloading Area Table");
        String url = "http://cms.infinisolutionslk.com/APP/area.json.php";

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

                            progressDialog.setMessage("Area Data Downloaded");
                            progressDialog.hide();
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
