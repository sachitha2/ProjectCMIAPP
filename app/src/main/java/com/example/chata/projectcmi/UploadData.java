package com.example.chata.projectcmi;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UploadData extends AppCompatActivity {
    private static  final  String TAG = "UploadData";
//    String URL = ;
    private RequestQueue requestQueueForStock;
    ProgressDialog progressDialog;
    SQLiteDatabase sqLiteDatabase;
    TextView txtDeals,txtInstallments,txtCollection;
    int step = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_data);


        sqLiteDatabase = openOrCreateDatabase("cmi", UploadData.MODE_PRIVATE,null);

        //text views
        txtCollection = findViewById(R.id.txtCollection);
        txtDeals = findViewById(R.id.txtDeal);
        txtInstallments = findViewById(R.id.txtInstallments);


        updateText();


        progressDialog = new ProgressDialog(UploadData.this);
        progressDialog.setTitle("Uploading Data....");
        progressDialog.setMessage("STEP "+step);
        progressDialog.setCancelable(false);



        requestQueueForStock = Volley.newRequestQueue(UploadData.this);
//        try {
            //looking for status start
            Cursor cForDeals =sqLiteDatabase.rawQuery("SELECT * FROM deals where app = 1 ;",null);
            int nRow = cForDeals.getCount();
            Log.d("Data",nRow + "");



//            txtUploaded.setText("To upload "+CForNRNum);

            if(nRow != 0){

                //check net connection start
                if(haveNet()){
//                    jsonParseStockAndPriceRangeTable(progressDialog);
                    uploadData(progressDialog);
                    progressDialog.show();
                }else if(!haveNet()){
                    Toast.makeText(UploadData.this,"Network connection is not available",Toast.LENGTH_SHORT).show();
                }
                //check net connection end



            }else{
                Toast.makeText(UploadData.this, "No Data Found To Upload", Toast.LENGTH_LONG).show();
                Log.d("SQL","Nodata to upload");
            }



            //looking for status End

//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

    }
    void uploadData(final ProgressDialog progressDialog){
//        String  url = "http://192.168.1.100/shop/APP/TEST.php";
        String  url = Common.URL+"takeData.json.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        try {

                            JSONObject obj = new JSONObject(response);

                            Log.d("My App", obj.toString());
                            JSONArray invoices = obj.getJSONArray("data");
                            JSONArray deal = obj.getJSONArray("dealId");
                            JSONArray collection = obj.getJSONArray("collection");
                            JSONArray s = obj.getJSONArray("s");

                            JSONArray newCollectionId = obj.getJSONArray("collectionNewId");

                            Log.d("invoices",s.get(0).toString()+"");
                            if(s.get(0).toString().equals("1")){


                                Log.d("IFFF","IN IF");
                                for(int i = 0; i < invoices.length(); i++){
                                    sqLiteDatabase.execSQL("UPDATE installment SET  app = 0 WHERE id = "+invoices.get(i).toString()+";");

                                }
                                //update deal id start
                                for(int i = 0; i < deal.length(); i++){
                                    sqLiteDatabase.execSQL("UPDATE deals SET  app = 0 WHERE id = "+deal.get(i).toString()+";");

                                }
                                //update deal id end

                                //update collection start


                                Cursor cForCustomer;
                                Cursor cForCollection;
                                for(int i = 0; i < collection.length(); i++){

                                    Log.d(TAG,"Collection old >"+collection.get(i).toString()+" New id > "+newCollectionId.get(i).toString());

                                    sqLiteDatabase.execSQL("UPDATE collection SET  app = 0,id = "+newCollectionId.get(i).toString()+"  WHERE id = "+collection.get(i).toString()+";");

                                      //write a query to take data from database
                                    //take deal id from collection.get(i).toString;
                                    cForCollection = sqLiteDatabase.rawQuery("SELECT * FROM collection WHERE id = "+collection.get(i).toString()+";",null);
                                    cForCollection.moveToNext();
                                    Log.d(TAG,"Collection id deal id:"+cForCollection.getString(3));
                                    cForCustomer = sqLiteDatabase.rawQuery("SELECT * FROM customer,deals WHERE customer.id = deals.cid AND deals.id = "+cForCollection.getString(3)+";", null);

                                    cForCustomer.moveToNext();

                                    Log.d(TAG,"Customer data :"+cForCustomer.getString(4));


                                    sendSMS(progressDialog,cForCollection.getString(4),cForCustomer.getString(4));
                                }
                                //update collection end




                            }else {
                                Log.d("IFFF","IN ELSE");
                            }

                        } catch (Throwable tx) {
                            Log.e("My App", "Could not parse malformed JSON: \"" + response + "\"");
                        }
                        //TODO
                        updateText();

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error+"");
                        progressDialog.hide();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                try {
                    params.put("data",data());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return params;
            }
        };
        requestQueueForStock.add(postRequest);
    }

    void updateText(){
        //set text in textViews
        //take nRows;
        Cursor c =sqLiteDatabase.rawQuery("SELECT * FROM deals where app = 1 ;",null);
        int nRow = c.getCount();
        txtDeals.setText(""+nRow);

        c =sqLiteDatabase.rawQuery("SELECT * FROM installment  where app = 1 ;",null);
        nRow = c.getCount();

        txtInstallments.setText(""+nRow);


        c =sqLiteDatabase.rawQuery("SELECT * FROM collection  where app = 1 ;",null);
        nRow = c.getCount();

        txtCollection.setText(""+nRow);
    }






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


    //take data function start
    String data() throws JSONException {
        Cursor cForInstallment = sqLiteDatabase.rawQuery("SELECT * FROM installment WHERE app = 1;", null);

        int nRow = cForInstallment.getCount();

        progressDialog.setMessage("Uploading data");
        String data = "sachitha hirushannn";

        JSONObject json = new JSONObject();
        JSONArray installmentArray = new JSONArray();
        JSONArray dealsArray = new JSONArray();
        JSONArray collectionArray = new JSONArray();


        Log.d("DATA", "jsonParseStockAndPriceRangeTable: " + nRow);


        int i = 0;
        while (cForInstallment.moveToNext()) {
            JSONObject invoiceData = new JSONObject();
            invoiceData.put("iid", cForInstallment.getString(0));//invoice id
            invoiceData.put("rpayment", cForInstallment.getString(8));//Received Payment
            invoiceData.put("status", cForInstallment.getString(7));//Received Payment
            installmentArray.put(invoiceData);
//            if(i == 10){
//                break;
//            }
            i++;
        }
        //deals array making start
        Cursor cForDeals = sqLiteDatabase.rawQuery("SELECT * FROM deals WHERE app = 1;", null);

        int nRowDeals = cForInstallment.getCount();


        while (cForDeals.moveToNext()) {
            JSONObject dealsData = new JSONObject();
            dealsData.put("rprice", cForDeals.getString(6));
            dealsData.put("id", cForDeals.getString(0));
            dealsData.put("s", cForDeals.getString(7));

            dealsArray.put(dealsData);
        }


        //deals array making end

        //collection array start
        Cursor cForCollection = sqLiteDatabase.rawQuery("SELECT * FROM collection WHERE app = 1;", null);

        int nRowCollecgion = cForInstallment.getCount();
        Log.d("JSON", "jsonParseStockAndPriceRangeTable: n row for deals" + nRowCollecgion);
        while (cForCollection.moveToNext()) {
            JSONObject collectionsData = new JSONObject();

            collectionsData.put("id", cForCollection.getString(0));
            collectionsData.put("userId", cForCollection.getString(1));
            collectionsData.put("installmentId", cForCollection.getString(2));
            collectionsData.put("dealid", cForCollection.getString(3));
            collectionsData.put("payment", cForCollection.getString(4));

            //TODO Date configuration


            collectionArray.put(collectionsData);
        }

        //collection array end

        ///////////////////////invoice item part
        json.put("dealsData", dealsArray);
        json.put("iData", installmentArray);
        json.put("collection", collectionArray);

        Log.d("JSON", "json" + json);


        return json.toString();
    }



    //take data function end



    //SMS SENDING PART START
    void sendSMS(final ProgressDialog progressDialog,String cash,String tp){
//        String  url = "http://192.168.1.100/shop/APP/TEST.php";

        String text = "Received your payment of  \n" +
                "Rs. "+cash+".00 \n" +
                "Thank you very much. Dont pay any payment without a Receipt \n" +
                "Hot line 0716000061 \n" +
                "TransLanka";

        String  url = "http://app.newsletters.lk/smsAPI?sendsms&apikey=fWU8bCDzTimMSqIm2qZJBRWMVN0QGqDr&apitoken=icBN1567943789&type=sms&from=TransLanka&to=+94"+tp.substring(1,10)+"&text="+text+"&scheduledate=&route=0";
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("SMSResponse", response);
                        try {

                            JSONObject obj = new JSONObject(response);

                            Log.d("My App", obj.toString());


                        } catch (Throwable tx) {
                            Log.e("My App", "Could not parse malformed JSON: \"" + response + "\"");
                        }

                        progressDialog.hide();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error+"");
                        progressDialog.hide();
                    }
                }
        );
        requestQueueForStock.add(postRequest);
    }

    //SMS SENDING PART END
}
