package com.example.chata.projectcmi;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NewPendingOrder extends AppCompatActivity {
    Spinner sPinerItems,item;
    SQLiteDatabase sqLiteDatabase;
    private String[] id, name;
    private TextView txtInvoiceId;
    EditText edtQuantity,price;
    private String[] Id,ItemName;
    private LinearLayout itemList;
    JSONObject invoice;
    String qty;

    Button btnCompleteI;
    private TextView total;


    public  float fullTotal;
    int itemCount = 0;

    long time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pending_order);

        sPinerItems = findViewById(R.id.spinerItems);
        txtInvoiceId = (TextView) findViewById(R.id.txtInvoiceId);

        invoice = new JSONObject();
        total = findViewById(R.id.total);

        btnCompleteI = findViewById(R.id.btnCompleteI);

        itemList = (LinearLayout) findViewById(R.id.itemList);
        edtQuantity = (EditText) findViewById(R.id.edtQuantity);

        txtInvoiceId = (TextView) findViewById(R.id.txtInvoiceId);
        price = findViewById(R.id.edtPrices);

        item = (Spinner) findViewById(R.id.spinerItems);

        time = System.currentTimeMillis();

        txtInvoiceId.setText( time+"");

        sqLiteDatabase = openOrCreateDatabase("cmi", Customers.MODE_PRIVATE,null);

        Cursor cForPack =sqLiteDatabase.rawQuery("SELECT * FROM item ;",null);

        int nRow = cForPack.getCount();

        id = new String[nRow];
        name = new String[nRow];

        int i=0;
        while (cForPack.moveToNext()){

            id[i] = cForPack.getString(0);
            name[i] = cForPack.getString(1);
            i++;
        }
        ArrayAdapter<String> packAdaptor = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, name);
        sPinerItems.setAdapter(packAdaptor);



        Cursor cForItems =sqLiteDatabase.rawQuery("SELECT * FROM item ;",null);

        int nRowItem = cForItems.getCount();

        Id = new String[nRowItem];
        ItemName = new String[nRowItem];

        i = 0;
        while (cForItems.moveToNext()){

            Id[i] = cForItems.getString(0);
            ItemName[i] = cForItems.getString(1);

            i++;
        }


//
//        SharedPreferences sharedPreferencesSS = getSharedPreferences("loginInfo", NewPendingOrder.MODE_PRIVATE);
//        final int vehicleId = sharedPreferencesSS.getInt("uName", -1);
        txtInvoiceId.setText(  "-" + time);

        btnCompleteI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Goto Complete invoice activiy

                ///save data in table
                String itemId;
                String amount;

                for(int x = 0;x < invoice.length();x++){
                    try {
                        JSONArray tmpJson = invoice.getJSONArray(""+x+"");
//                        sqLiteDatabase.execSQL("INSERT INTO invoice (id, dealId, itemId, amount,sPrice,shopId,stockId,date,s) VALUES (100, '"+vehicleId+"-"+time+"',"+tmpJson.getString(3)+" , "+tmpJson.getString(2)+","+tmpJson.getString(1)+",2502,25, date('now','localtime'),0);");
                        Log.d("json arr", "onClick: "+tmpJson);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


//                sqLiteDatabase.execSQL("INSERT INTO deal (id, shopId, Total, credit, cash,s,date) VALUES ('"+vehicleId+"-"+time+"', "+ShopId+", "+fullTotal+",25,25,2, date('now','localtime'));");
                Log.d("Reading json object", "onClick: "+invoice);
                Log.d("Reading json object L", "onClick: length of json object"+invoice.length());
//                sqLiteSelectShop.execSQL("");


                ///save data in table

                Intent intent = new Intent(NewPendingOrder.this, CompletePendingOrder.class);
//                String message = mMessageEditText.getText().toString();

                intent.putExtra("ShopId", 10);
                intent.putExtra("ShopName", "sam");
                intent.putExtra("invoiceNumber", "-"+time);
                intent.putExtra("json",invoice.toString());
                intent.putExtra("itemTotla",fullTotal+"");
                startActivity(intent);
                NewPendingOrder.this.finish();
            }
        });
    }

    public void onAddClick(View view) {
        if(edtQuantity.getText().length() != 0){

            if (price.getText().toString() == null) {
                Log.d("Sachitha","Price is null");
            } else {


                JSONArray temp = new JSONArray();
                temp.put(item.getSelectedItem() + "");
                temp.put(price.getText() + "");
                temp.put(edtQuantity.getText() + "");
                temp.put(Id[item.getSelectedItemPosition()]);



                try {
                    invoice.put(itemCount + "", temp);//Id[item.getSelectedItemPosition()]
                    itemCount++;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("Sachitha", invoice.toString());


                //new code
                LinearLayout linearLayout = new LinearLayout(NewPendingOrder.this);
                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setPadding(0, 20, 0, 20);


                TextView textView1 = new TextView(this);
                textView1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.4f));
                textView1.setText(item.getSelectedItem() + "");

                TextView textView2 = new TextView(this);
                textView2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.2f));
                textView2.setText(price.getText() + "");

                TextView textView3 = new TextView(this);
                textView3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.2f));
                textView3.setText(edtQuantity.getText() + "");

                TextView textView4 = new TextView(this);
                textView4.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.2f));

                qty = edtQuantity.getText() + "";
                edtQuantity.setText("");

                float sPrice = Float.parseFloat(price.getText() + "");
                price.setText("");
                textView4.setText((Integer.valueOf(qty) * sPrice) + "");

                fullTotal += (Integer.valueOf(qty) * sPrice);

                total.setText("Total \n" + fullTotal + "");

                //new code

                linearLayout.addView(textView1);
                linearLayout.addView(textView2);
                linearLayout.addView(textView3);
                linearLayout.addView(textView4);


                itemList.addView(linearLayout);

            }
        }
    }

    @Override
    public void onBackPressed() {



        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(this);
        alertDialog2.setTitle("Delete Data");
        alertDialog2.setMessage("Are you sure you want to go back?.");
        // Add the buttons
        alertDialog2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                NewPendingOrder.super.onBackPressed();

            }
        });


        alertDialog2.show();

    }
}
