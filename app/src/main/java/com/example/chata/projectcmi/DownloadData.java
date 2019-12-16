package com.example.chata.projectcmi;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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

    //init textView start
    TextView txtDeal;
    TextView txtInstallment;
    TextView txtCustomer;
    TextView txtCollection;
    TextView txtItem;
    TextView txtArea;
    //init textView end

    SQLiteDatabase sqlite;
    private RequestQueue requestQueueForCreditList;
    Button bluetoothBtn,btnDownloadData,uploadData,btnBT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_data);
        requestQueueForCreditList = Volley.newRequestQueue(DownloadData.this);


        //init textView start
         txtDeal = findViewById(R.id.txtDeals);
         txtInstallment = findViewById(R.id.txtInstallments) ;
         txtCustomer  = findViewById(R.id.txtCustomers);
         txtCollection = findViewById(R.id.txtCollection);
         txtItem = findViewById(R.id.txtItem);
         txtArea = findViewById(R.id.txtArea);
        //init textView end


        txtDeal.setText("Deal");
        txtInstallment.setText("installment");
        txtCustomer.setText("customer");
        txtCollection.setText("collection");
        txtItem.setText("item");
        txtArea.setText("area");

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



                  //check net connection start
                  if(haveNet()){
                      progressDialog.setTitle("Download");
                      progressDialog.setMessage("hello this is a progress dialog box");
                      progressDialog.setCancelable(false);
                      progressDialog.show();

                      progressDialog.setMessage("Hiiii");
                      createDatabases(progressDialog);
                  }else if(!haveNet()){
                      Toast.makeText(DownloadData.this,"Network connection is not available",Toast.LENGTH_SHORT).show();
                  }
                  //check net connection end

              }
          });

          btnBT = findViewById(R.id.btnBlue);

          btnBT.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent intentBT = new Intent(DownloadData.this, BTConfig.class);
                startActivity(intentBT);
              }
          });


        bluetoothBtn = findViewById(R.id.SelectBTBtn);

        bluetoothBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSelectArea = new Intent(DownloadData.this, SelectBuletoothMAC.class);
                startActivity(intentSelectArea);
            }
        });


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
                ",areaId varchar(3) NOT NULL" +
                ",tp VARCHAR(10) NOT NULL" +
                ",address TEXT);");

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
                "id varchar(20) NOT NULL," +
                "userId int(11) NOT NULL," +
                "installmentId int(11) NOT NULL," +
                "dealid int(15) NOT NULL," +
                "payment FLOAT NOT NULL," +
                "date DATE NOT NULL," +
                "time TIME NOT NULL," +
                "app int(1) NOT NULL);");

        sqlite.execSQL("DROP TABLE IF EXISTS localData;");
        //Creating local database for holding bluetooth address
        sqlite.execSQL("CREATE TABLE localData(" +
                "btName varchar(50)," +
                "date int(1));");
//        sqlite.execSQL("INSERT INTO collection(id,userId,installmentId,dealid,payment,date,time) VALUES (1,2,3,4,5,'','')");
//
//
//        sqlite.execSQL("INSERT INTO deals (id, date,time,fdate,ftime,tprice,rprice,status,ni,cid,discount,agentId) VALUES (13,'2015-08-25','','2019-10-10','',3.6,2,1,5,2588,2.3,19);");
        //Creating deals table END




        ///Download Area Table
//        jsonParseAreaList(progressDialog);
        ///Download customers list
//        jsonParseCustomerList(progressDialog);

        //Download pack Data
//        jsonParsepackDataList(progressDialog);

        //Download item table
//        jsonParseItemData(progressDialog);


        // Download Deals Table
//        jsonParseDealsList(progressDialog);

        //Download Installments data
//        jsonParseInstallment(progressDialog);



        //Download Collection table
//        jsonParseCollection(progressDialog);


        //new download json function start
        jsonDownload(progressDialog);
        //new download json function end
        return 1;
    }
    //new download method start
    private void jsonDownload(final ProgressDialog progressDialog) {
        progressDialog.setMessage("Downloading data\nThis will take a while.");
        String url = Common.URL+"downloadData.php";

        JsonObjectRequest requestCreditList = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            //Read json and assign them to local variables

                                int i = 0;
                                JSONObject deal = response.getJSONObject("deal");
                                JSONObject installmet = response.getJSONObject("installmet");
                                JSONObject collection = response.getJSONObject("collection");
                                JSONObject item = response.getJSONObject("item");
                                JSONObject customer = response.getJSONObject("customer");
                                JSONObject area = response.getJSONObject("area");


                                Log.d("JSON","DATA"+deal);
                                Log.d("JSON","DATA"+installmet);
                                Log.d("JSON","DATA"+collection);
                                Log.d("JSON","DATA"+item);
                                Log.d("JSON","DATA"+customer);
                                Log.d("JSON","DATA"+area);


                                //Deals start
                                JSONArray dealId = deal.getJSONArray("id");
                                JSONArray Dtotal = deal.getJSONArray("tprice");
                                JSONArray Dcid = deal.getJSONArray("cid");
                                JSONArray Ddate = deal.getJSONArray("date");
                                JSONArray Dtime = deal.getJSONArray("time");
                                JSONArray Dfdate  = deal.getJSONArray("fdate");
                                JSONArray Dftime = deal.getJSONArray("ftime");
                                JSONArray Drprice = deal.getJSONArray("rprice");
                                JSONArray Dstatus = deal.getJSONArray("status");
                                JSONArray Dni = deal.getJSONArray("ni");
                                JSONArray Ddiscount = deal.getJSONArray("discount");
                                JSONArray DagentId = deal.getJSONArray("agentId");
                                for (i = 0; i < dealId.length(); i++){
                                    sqlite.execSQL("INSERT INTO deals (id, date,time,fdate,ftime,tprice,rprice,status,ni,cid,discount,agentId,app) VALUES ('"+dealId.get(i).toString()+"','"+Ddate.get(i).toString()+"','"+Dtime.get(i).toString()+"','"+Dfdate.get(i).toString()+"','"+Dftime.get(i).toString()+"',"+Dtotal.get(i).toString()+","+Drprice.get(i).toString()+","+Dstatus.get(i).toString()+","+Dni.get(i).toString()+","+Dcid.get(i).toString()+","+Ddiscount.get(i).toString()+","+DagentId.get(i).toString()+",0);");
                                }
                                //Deals end


                                //installment start
                                JSONArray IId = installmet.getJSONArray("id");
                                JSONArray Idealid = installmet.getJSONArray("dealid");
                                JSONArray Ipayment = installmet.getJSONArray("payment");
                                JSONArray IrPayment = installmet.getJSONArray("rpayment");
                                JSONArray Idate = installmet.getJSONArray("date");
                                JSONArray IrDate = installmet.getJSONArray("rdate");
                                JSONArray Istatus = installmet.getJSONArray("status");
                                JSONArray Icid = installmet.getJSONArray("cid");
                                JSONArray INumber = installmet.getJSONArray("installmentid");
                                JSONArray Itime = installmet.getJSONArray("time");
                                for (i = 0; i < IId.length(); i++){
                                    sqlite.execSQL("INSERT INTO installment (id, dealid,installmentid,payment,time,date,rdate,status,rpayment,cid,app) VALUES ("+IId.get(i).toString()+","+Idealid.get(i).toString()+","+INumber.get(i).toString()+","+Ipayment.get(i).toString()+",'"+Itime.get(i).toString()+"','"+Idate.get(i).toString()+"','"+IrDate.get(i).toString()+"',"+Istatus.get(i).toString()+","+IrPayment.get(i).toString()+","+Icid.get(i).toString()+",0);");
                                }
                                //installment end


                                //collection start
                                JSONArray collectionId = collection.getJSONArray("id");


                                JSONArray Cid = collection.getJSONArray("id");
                                JSONArray CuserId = collection.getJSONArray("userId");
                                JSONArray CinstallmentId = collection.getJSONArray("installmentId");
                                JSONArray Cdealid = collection.getJSONArray("dealid");
                                JSONArray Cpayment = collection.getJSONArray("payment");
                                JSONArray Cdate = collection.getJSONArray("date");
                                JSONArray Ctime = collection.getJSONArray("time");
                                JSONArray CdateTime = collection.getJSONArray("dateTime");



                                for (i = 0; i < Cid.length(); i++){
                                    sqlite.execSQL("INSERT INTO collection(id,userId,installmentId,dealid,payment,date,time,app) VALUES ("+Cid.get(i).toString()+","+CuserId.get(i).toString()+","+CinstallmentId.get(i).toString()+","+Cdealid.get(i).toString()+","+Cpayment.get(i).toString()+",'"+Cdate.get(i).toString()+"','"+Ctime.get(i).toString()+"',0)");
                                   }
                                //collection end


                                //item start
                                JSONArray itemId = item.getJSONArray("id");
                                JSONArray itemName = item.getJSONArray("item");

                                for (i = 0; i < itemId.length(); i++){
                                    sqlite.execSQL("INSERT INTO item (id, name) VALUES ('"+itemId.get(i).toString()+"', '"+itemName.get(i).toString()+"');");
                                }
                                //item end



                                //customer start
                                JSONArray customerId = customer.getJSONArray("id");
                                JSONArray customerName = customer.getJSONArray("name");
                                JSONArray customerNIC = customer.getJSONArray("nic");
                                JSONArray customerAreaId = customer.getJSONArray("areaid");
                                JSONArray customerTp = customer.getJSONArray("tp");
                                JSONArray customerAddress = customer.getJSONArray("address");

                                //sqlite.execSQL("INSERT INTO customer (id, name,nic,areaId) VALUES ('"+id.get(i).toString()+"', '"+name.get(i).toString()+"', '"+nic.get(i).toString()+"','"+areaId.get(i).toString()+"');");
                                for(i = 0;i<customerId.length();i++){
                                    sqlite.execSQL("INSERT INTO customer (id, name,nic,areaId,tp,address) VALUES ('"+customerId.get(i).toString()+"', '"+customerName.get(i).toString()+"', '"+customerNIC.get(i).toString()+"','"+customerAreaId.get(i).toString()+"','"+customerTp.get(i).toString()+"','"+customerAddress.get(i).toString()+"');");

                                }
                                //customer end


                                //area start
                                JSONArray areaId = area.getJSONArray("id");
                                JSONArray areaName = area.getJSONArray("area");

                                for(i = 0;i < areaId.length();i++){
                                    sqlite.execSQL("INSERT INTO area (id, name) VALUES ('"+areaId.get(i).toString()+"', '"+areaName.get(i).toString()+"');");
                                }
                                //area end
                                  txtDeal.setText("Deal "+dealId.length());
                                  txtInstallment.setText("installment "+IId.length());
                                  txtCustomer.setText("customer "+customerId.length());
                                txtCollection.setText("collection "+collectionId.length());
                                txtItem.setText("item "+itemId.length());
                                txtArea.setText("area "+areaId.length());
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
    //new download method end
    
    //check net connection function start
    private  boolean haveNet(){
        boolean haveWifi = false;
        boolean haveMobileData = false;
        ConnectivityManager connectivityManager =(ConnectivityManager) getSystemService( CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
        for(NetworkInfo info:networkInfos){
            if(info.getTypeName().equalsIgnoreCase("WIFI")){
                if(info.isConnected()){
                    haveWifi  = true;
                }

            }
            if(info.getTypeName().equalsIgnoreCase("MOBILE")){
                if(info.isConnected()){
                    haveMobileData = true;
                }

            }
        }
        return haveMobileData || haveWifi;
    }
    //check net connection function End;
}
