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
    String URL = "http://192.168.43.44/shop/APP/";
    SQLiteDatabase sqlite;
    private RequestQueue requestQueueForCreditList;
    Button bluetoothBtn,btnDownloadData,uploadData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_data);
        requestQueueForCreditList = Volley.newRequestQueue(DownloadData.this);


        final ProgressDialog progressDialog = new ProgressDialog(DownloadData.this);


        uploadData = findViewById(R.id.btnUpload);
        uploadData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DownloadData.this,UploadData.class);
                startActivity(i);
            }
        });



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

        //Creating pending order tables START
        ///Set ID as auto increment
        sqlite.execSQL("CREATE TABLE IF  NOT EXISTS orderdata (" +
                "id int(11) NOT NULL" +
                ",dealId varchar(100) NOT NULL" +
                ",cid varchar(15)" +
                ",total float(10)" +
                ",date datetime NOT NULL " +
                ",s int(1) NOT NULL);");

        //Creating pending order tables END

        //creating deals table START
        sqlite.execSQL("DROP TABLE IF EXISTS deals;");
        sqlite.execSQL("CREATE TABLE IF  NOT EXISTS deals (" +
                "id int(15) NOT NULL" +
                ",date DATE NOT NULL" +
                ",time TIME NOT NULL" +
                ",fdate DATE NOT NULL" +
                ",ftime TIME NOT NULL" +
                ",tprice FLOAT NOT NULL" +
                ",rprice FLOAT NOT NULL" +
                ",status INT(1) NOT NULL" +
                ",ni INT(1) NOT NULL" +
                ",cid INT(11) NOT NULL" +
                ",discount FLOAT NOT NULL" +
                ",agentId int(2) NOT NULL," +
                "app int(1) NOT NULL" +
                ",PRIMARY KEY (id)" +
                ");");
        sqlite.execSQL("DROP TABLE IF EXISTS installment;");
        sqlite.execSQL("CREATE TABLE installment (" +
                "id int(11) NOT NULL ," +
                "dealid int(15) NOT NULL," +
                "installmentid  int(11) NOT NULL," +
                "payment FLOAT NOT NULL," +
                "time TIME NOT NULL," +
                "date DATE NOT NULL," +
                "rdate DATE NOT NULL," +
                "status int(1) NOT NULL," +
                "rpayment FLOAT NOT NULL," +
                "cid int(11) NOT NULL," +
                "app int(1) NOT NULL);");


        sqlite.execSQL("DROP TABLE IF EXISTS collection;");

        sqlite.execSQL("CREATE TABLE collection (" +
                "id int(11) NOT NULL," +
                "userId int(11) NOT NULL," +
                "installmentId int(11) NOT NULL," +
                "dealid int(15) NOT NULL," +
                "payment FLOAT NOT NULL," +
                "date DATE NOT NULL," +
                "time TIME NOT NULL," +
                "app int(1) NOT NULL);");
//        sqlite.execSQL("INSERT INTO collection(id,userId,installmentId,dealid,payment,date,time) VALUES (1,2,3,4,5,'','')");
//
//
//        sqlite.execSQL("INSERT INTO deals (id, date,time,fdate,ftime,tprice,rprice,status,ni,cid,discount,agentId) VALUES (13,'2015-08-25','','2019-10-10','',3.6,2,1,5,2588,2.3,19);");
        //Creating deals table END




        ///Download Area Table
        jsonParseAreaList(progressDialog);
        ///Download customers list
        jsonParseCustomerList(progressDialog);

        //Download pack Data
        jsonParsepackDataList(progressDialog);

        //Download item table
        jsonParseItemData(progressDialog);


        // Download Deals Table
        jsonParseDealsList(progressDialog);

        //Download Installments data
        jsonParseInstallment(progressDialog);



        //Download Collection table
        jsonParseCollection(progressDialog);
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
    //  Collection table;
    private void jsonParseCollection(final ProgressDialog progressDialog) {
        progressDialog.setMessage("Downloading Collection Table");
        String url = URL+"collection.json.php";

        JsonObjectRequest requestCreditList = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            //Read json and assign them to local variables
                            JSONArray id = response.getJSONArray("id");
                            JSONArray userId = response.getJSONArray("userId");
                            JSONArray installmentId = response.getJSONArray("installmentId");
                            JSONArray dealid = response.getJSONArray("dealid");
                            JSONArray payment = response.getJSONArray("payment");
                            JSONArray date = response.getJSONArray("date");
                            JSONArray time = response.getJSONArray("time");
                            JSONArray dateTime = response.getJSONArray("dateTime");


//
                            for (int i = 0; i < id.length(); i++){
                                  sqlite.execSQL("INSERT INTO collection(id,userId,installmentId,dealid,payment,date,time,app) VALUES ("+id.get(i).toString()+","+userId.get(i).toString()+","+installmentId.get(i).toString()+","+dealid.get(i).toString()+","+payment.get(i).toString()+",'"+date.get(i).toString()+"','"+time.get(i).toString()+"',0)");
//                                sqlite.execSQL("INSERT INTO installment (id, dealid,installmentid,payment,time,date,rdate,status,rpayment,cid) VALUES ("+id.get(i).toString()+","+dealid.get(i).toString()+","+installmentId.get(i).toString()+","+payment.get(i).toString()+",'"+time.get(i).toString()+"','"+date.get(i).toString()+"','"+rDate.get(i).toString()+"',"+status.get(i).toString()+","+rPayment.get(i).toString()+","+cid.get(i).toString()+");");
                            }
                            progressDialog.setMessage("Installments data downloaded");
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



    //  Installment table;
    private void jsonParseInstallment(final ProgressDialog progressDialog) {
        progressDialog.setMessage("Downloading Installment Table");
        String url = URL+"installments.json.php";

        JsonObjectRequest requestCreditList = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            //Read json and assign them to local variables
                            JSONArray id = response.getJSONArray("id");
                            JSONArray dealid = response.getJSONArray("dealId");
                            JSONArray payment = response.getJSONArray("payment");
                            JSONArray rPayment = response.getJSONArray("rpayment");
                            JSONArray date = response.getJSONArray("date");
                            JSONArray rDate = response.getJSONArray("rdate");
                            JSONArray status = response.getJSONArray("status");
                            JSONArray cid = response.getJSONArray("cid");
                            JSONArray installmentId = response.getJSONArray("installmentid");
                            JSONArray time = response.getJSONArray("time");


//
                            for (int i = 0; i < id.length(); i++){
                                sqlite.execSQL("INSERT INTO installment (id, dealid,installmentid,payment,time,date,rdate,status,rpayment,cid,app) VALUES ("+id.get(i).toString()+","+dealid.get(i).toString()+","+installmentId.get(i).toString()+","+payment.get(i).toString()+",'"+time.get(i).toString()+"','"+date.get(i).toString()+"','"+rDate.get(i).toString()+"',"+status.get(i).toString()+","+rPayment.get(i).toString()+","+cid.get(i).toString()+",0);");
                            }
                            progressDialog.setMessage("Installments data downloaded");
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

    //download deals
    private void jsonParseDealsList(final ProgressDialog progressDialog) {
        progressDialog.setMessage("Downloading Deals Table");
        String url = URL+"deals.json.php";

        JsonObjectRequest requestCreditList = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            //Read json and assign them to local variables
                            JSONArray id = response.getJSONArray("id");
                            JSONArray total = response.getJSONArray("tprice");
                            JSONArray cid = response.getJSONArray("cid");
                            JSONArray date = response.getJSONArray("date");
                            JSONArray time = response.getJSONArray("time");
                            JSONArray fdate  = response.getJSONArray("fdate");
                            JSONArray ftime = response.getJSONArray("ftime");
                            JSONArray rprice = response.getJSONArray("rprice");
                            JSONArray status = response.getJSONArray("status");
                            JSONArray ni = response.getJSONArray("ni");
                            JSONArray discount = response.getJSONArray("discount");
                            JSONArray agentId = response.getJSONArray("agentId");
//
                            for (int i = 0; i < id.length(); i++){
                                sqlite.execSQL("INSERT INTO deals (id, date,time,fdate,ftime,tprice,rprice,status,ni,cid,discount,agentId,app) VALUES ('"+id.get(i).toString()+"','"+date.get(i).toString()+"','"+time.get(i).toString()+"','"+fdate.get(i).toString()+"','"+ftime.get(i).toString()+"',"+total.get(i).toString()+","+rprice.get(i).toString()+","+status.get(i).toString()+","+ni.get(i).toString()+","+cid.get(i).toString()+","+discount.get(i).toString()+","+agentId.get(i).toString()+",0);");
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
