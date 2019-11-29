package com.example.chata.projectcmi;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UploadData extends AppCompatActivity {
    String URL = "http://192.168.43.44/shop/APP/";
    private RequestQueue requestQueueForStock;
    ProgressDialog progressDialog;
    SQLiteDatabase sqLiteDatabase;
    TextView txtUploaded;
    int step = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_data);


        sqLiteDatabase = openOrCreateDatabase("cmi", UploadData.MODE_PRIVATE,null);




        progressDialog = new ProgressDialog(UploadData.this);
        progressDialog.setTitle("Uploading Data....");
        progressDialog.setMessage("STEP "+step);
        progressDialog.setCancelable(false);



        requestQueueForStock = Volley.newRequestQueue(UploadData.this);
        try {
            //looking for status start
//            Cursor cForDeals =sqLiteDatabase.rawQuery("SELECT * FROM deal where s = 0  AND date =  date('now','localtime');",null);
//            int nRow = cForDeals.getCount();
//            Log.d("Data",nRow + "");



//            int CForNRNum = cForDeals.getCount();
//            txtUploaded.setText("To upload "+CForNRNum);

//            if(CForNRNum != 0){
                jsonParseStockAndPriceRangeTable(progressDialog);
                progressDialog.show();
//            }else{
//                Log.d("SQL","Nodata to upload");
//            }



            //looking for status End

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    public void jsonParseStockAndPriceRangeTable(final ProgressDialog progressDialog) throws JSONException {

        Cursor cForInstallment =sqLiteDatabase.rawQuery("SELECT * FROM installment WHERE app = 1;",null);

        int nRow = cForInstallment.getCount();

        progressDialog.setMessage("Uploading data");
        String data = "sachitha hirushannn";

        JSONObject json = new JSONObject();
        JSONArray installmentArray = new JSONArray();
        JSONArray dealsArray = new JSONArray();
        JSONArray collectionArray = new JSONArray();


        Log.d("DATA", "jsonParseStockAndPriceRangeTable: "+nRow);


        int i=0;
        while (cForInstallment.moveToNext()){
            JSONObject invoiceData = new JSONObject();
            invoiceData.put("iid",cForInstallment.getString(0));//invoice id
            invoiceData.put("rpayment",cForInstallment.getString(8));//Received Payment
            invoiceData.put("status",cForInstallment.getString(7));//Received Payment
            installmentArray.put(invoiceData);
//            if(i == 10){
//                break;
//            }
            i++;
        }
        //deals array making start
        Cursor cForDeals =sqLiteDatabase.rawQuery("SELECT * FROM deals WHERE app = 1;",null);

        int nRowDeals = cForInstallment.getCount();



        while(cForDeals.moveToNext()){
            JSONObject dealsData = new JSONObject();
            dealsData.put("rprice",cForDeals.getString(6));
            dealsData.put("id",cForDeals.getString(0));
            dealsData.put("s",cForDeals.getString(7));

            dealsArray.put(dealsData);
        }


        //deals array making end

        //collection array start
        Cursor cForCollection =sqLiteDatabase.rawQuery("SELECT * FROM collection WHERE app = 1;",null);

        int nRowCollecgion = cForInstallment.getCount();
        Log.d("JSON", "jsonParseStockAndPriceRangeTable: n row for deals"+nRowCollecgion);
        while(cForCollection.moveToNext()){
            JSONObject collectionsData = new JSONObject();

            collectionsData.put("id",cForCollection.getString(0));
            collectionsData.put("userId",cForCollection.getString(1));
            collectionsData.put("installmentId",cForCollection.getString(2));
            collectionsData.put("dealid",cForCollection.getString(3));
            collectionsData.put("payment",cForCollection.getString(4));

            //TODO Date configuration


            collectionArray.put(collectionsData);
        }

        //collection array end

        ///////////////////////invoice item part
        json.put("dealsData",dealsArray);
        json.put("iData",installmentArray);
        json.put("collection",collectionArray);

        Log.d("JSON","json"+json);
        String url = URL+"takeData.json.php?data="+json.toString();
        Log.d("JSON URL",url);
        JsonObjectRequest requestDownloadStock = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            //Read json and assign them to local variables

                            JSONArray invoices = response.getJSONArray("data");
                            JSONArray s = response.getJSONArray("s");
                            //TODO Here
                            //We have to config
                            if(s.get(0).toString() == "1"){
                                for(int i = 0; i < invoices.length(); i++){
                                    sqLiteDatabase.execSQL("UPDATE installment SET  app = 0 WHERE id = "+invoices.get(i).toString()+";");

                                }
                            }
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

        requestQueueForStock.add(requestDownloadStock);
        // recursie function
//            jsonParseStockAndPriceRangeTable(progressDialog);


    }


}
