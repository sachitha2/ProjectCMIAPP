
package com.example.chata.projectcmi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class CreditsByAreaId extends AppCompatActivity {

    private ListView creditList;
    private RequestQueue requestQueueForCreditList;

    private String[] customer;
    private String[] nic;
    private String[] installment;
    private String[] total;

    private String areaId,areaName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits_by_area_id);

        areaId= getIntent().getStringExtra("areaId");
        areaName= getIntent().getStringExtra("areaName");

        setTitle("" + areaName);

        creditList = (ListView) findViewById(R.id.creditList);
        requestQueueForCreditList = Volley.newRequestQueue(CreditsByAreaId.this);
        jsonParseCreditList();

    }

    private void jsonParseCreditList() {

        String url = "http://cms.infinisolutionslk.com/APP/getCreditsByAreaId.json.php?areaId=" + areaId;

        JsonObjectRequest requestCreditList = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            //Read json and assign them to local variables
                            JSONArray Lcustomer = response.getJSONArray("customerName");
                            JSONArray Lnic = response.getJSONArray("nic");
                            JSONArray Linstallment = response.getJSONArray("installments");
                            JSONArray Ltotal = response.getJSONArray("total");

                            customer = new String[Lcustomer.length()];
                            nic = new String[Lnic.length()];
                            installment = new String[Linstallment.length()];
                            total = new String[Ltotal.length()];

                            for (int i = 0; i < Lcustomer.length(); i++){
                                customer[i] = Lcustomer.get(i).toString();
                                nic[i] = Lnic.get(i).toString();
                                installment[i] = Linstallment.get(i).toString();
                                total[i] = Ltotal.get(i).toString();
                            }

                            //then create the grid view
                            ListViewAdapter creditListViewAdapter = new ListViewAdapter(CreditsByAreaId.this, customer, installment, total, nic,"CreditsByAreaId");
                            creditList.setAdapter(creditListViewAdapter);

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
