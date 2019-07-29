package com.example.chata.projectcmi;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
    String URL = "http://192.168.43.230/shop/APP/";
    SQLiteDatabase sqlite;
    private RequestQueue requestQueueForCreditList;
    Button bluetoothBtn,btnDownloadData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_data);
        requestQueueForCreditList = Volley.newRequestQueue(DownloadData.this);


        final ProgressDialog progressDialog = new ProgressDialog(DownloadData.this);






          btnDownloadData = findViewById(R.id.btnDownloadData);
          btnDownloadData.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  progressDialog.setTitle("Download");
                  progressDialog.setMessage("hello this is a progress dialog box");
                  progressDialog.setCancelable(false);
                  progressDialog.show();

                  progressDialog.setMessage("Hiiii");
                  createDatabases(progressDialog);
              }
          });
//        bluetoothBtn = findViewById(R.id.SelectBTBtn);
//
//        bluetoothBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intentSelectArea = new Intent(DownloadData.this, SelectBuletoothMAC.class);
//                startActivity(intentSelectArea);
//            }
//        });


    }

    public int createDatabases(ProgressDialog progressDialog){



        sqlite = openOrCreateDatabase("cmi", Context.MODE_PRIVATE,null);//10

        progressDialog.setMessage("Deleting Area Table");
        //Drop Table if Exist
        sqlite.execSQL("DROP TABLE IF EXISTS area;");

        progressDialog.setMessage("creating Area Table");
        ////Creating Area Table
        sqlite.execSQL("CREATE TABLE IF  NOT EXISTS area (id int(11) NOT NULL,name varchar(200) NOT NULL);");


        //Drop Customer Table if Exist
        sqlite.execSQL("DROP TABLE IF EXISTS customer;");

        //Create Customer Table
        sqlite.execSQL("CREATE TABLE IF  NOT EXISTS customer (" +
                "id int(11) NOT NULL" +
                ",name varchar(200) NOT NULL" +
                ",nic varchar(20) NOT NULL" +
                ",areaId varchar(3) NOT NULL);");

        //Drop pack Table if Exist
        sqlite.execSQL("DROP TABLE IF EXISTS pack;");
        //Create pack Table
        sqlite.execSQL("CREATE TABLE IF  NOT EXISTS pack (" +
                "id int(11) NOT NULL" +
                ",name varchar(100) NOT NULL);");

        //Drop packitems Table if Exist
        sqlite.execSQL("DROP TABLE IF EXISTS packitems;");

        //Create packitems Table
        sqlite.execSQL("CREATE TABLE IF  NOT EXISTS packitems (" +
                "id int(11) NOT NULL" +
                ",pid varchar(100) NOT NULL" +
                ",itemId varchar(11) NOT NULL" +
                ",amount float NOT NULL);");

        //Drop item Table if Exist
        sqlite.execSQL("DROP TABLE IF EXISTS item;");

        //Create item Table
        sqlite.execSQL("CREATE TABLE IF  NOT EXISTS item (" +
                "id int(11) NOT NULL" +
                ",name varchar(100) NOT NULL);");


        ///Download Area Table
        jsonParseAreaList(progressDialog);
        ///Download customers list
        jsonParseCustomerList(progressDialog);

        //Download pack Data
        jsonParsepackDataList(progressDialog);



        return 1;
    }


    private void jsonParseAreaList(final ProgressDialog progressDialog) {
        progressDialog.setMessage("Downloading Area Table");
        String url = URL+"area.json.php";

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
                            Log.d("DOWNLOAD", "DATA :AREA  AMOUNT "+id.length());
                            Log.d("DOWNLOAD", "DATA :AREA  DATA "+response);
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

    //Download customers Start
    private void jsonParseCustomerList(final ProgressDialog progressDialog) {
        progressDialog.setMessage("Downloading Customer Table");
        String url = URL+"customer.json.php";

        JsonObjectRequest requestCreditList = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            //Read json and assign them to local variables
                            JSONArray name = response.getJSONArray("name");
                            JSONArray id = response.getJSONArray("id");
                            JSONArray nic = response.getJSONArray("nic");
                            JSONArray areaId = response.getJSONArray("areaid");


//
                            for (int i = 0; i < id.length(); i++){
                                sqlite.execSQL("INSERT INTO customer (id, name,nic,areaId) VALUES ('"+id.get(i).toString()+"', '"+name.get(i).toString()+"', '"+nic.get(i).toString()+"','"+areaId.get(i).toString()+"');");
                            }
                            Log.d("DOWNLOAD", "DATA :Customer  AMOUNT "+id.length());
                            Log.d("DOWNLOAD", "DATA :Customer  DATA "+response);
                            progressDialog.setMessage("Customer Data Downloaded");
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
    //Download customers End

    //Download PackData Start
    private void jsonParsepackDataList(final ProgressDialog progressDialog) {
        progressDialog.setMessage("Downloading Customer Table");
        String url = URL+"pack.json.php";

        JsonObjectRequest requestCreditList = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            //Read json and assign them to local variables
                            JSONArray item = response.getJSONArray("item");
                            JSONArray id = response.getJSONArray("id");


//
                            for (int i = 0; i < id.length(); i++){
                                sqlite.execSQL("INSERT INTO pack (id, name) VALUES ('"+id.get(i).toString()+"', '"+item.get(i).toString()+"');");
                            }
                            Log.d("DOWNLOAD", "DATA :pack  AMOUNT "+id.length());
                            Log.d("DOWNLOAD", "DATA :pack  DATA "+response);
                            progressDialog.setMessage("pack Data Downloaded");
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
    //Download PackData End


    //Download Item data Start
    private void jsonParseItemData(final ProgressDialog progressDialog) {
        progressDialog.setMessage("Downloading Item Table");
        String url = URL+"item.json.php";

        JsonObjectRequest requestCreditList = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            //Read json and assign them to local variables
                            JSONArray item = response.getJSONArray("item");
                            JSONArray id = response.getJSONArray("id");


//
                            for (int i = 0; i < id.length(); i++){
                                sqlite.execSQL("INSERT INTO item (id, name) VALUES ('"+id.get(i).toString()+"', '"+item.get(i).toString()+"');");
                            }
                            Log.d("DOWNLOAD", "DATA :item  AMOUNT "+id.length());
                            Log.d("DOWNLOAD", "DATA :item  DATA "+response);
                            progressDialog.setMessage("item Data Downloaded");
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
    //Download item data End
}
