package com.example.chata.projectcmi;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomersInstallment extends AppCompatActivity  {
    private String InvoiceId;


    ArrayList<SingleRowForInstallment> myList;
    ListViewInstallmentOfACustomer myAdapter;

    EditText searchCustomers,editPayment;
    ListView customerList;
    TextView tot,receivePayment,balance;
    SQLiteDatabase sqLiteDatabase;
    Context context;
    Button btnPayment,btnPayHistory;
    AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers_installment);

        InvoiceId= getIntent().getStringExtra("InvoiceId");

        sqLiteDatabase = openOrCreateDatabase("cmi", Customers.MODE_PRIVATE,null);


        tot = findViewById(R.id.total);
        receivePayment = findViewById(R.id.rPrice);
        balance = findViewById(R.id.balance);

        editPayment = (EditText) findViewById(R.id.editPayment);
        btnPayment = findViewById(R.id.btnAddPayment);
        btnPayHistory = findViewById(R.id.btnPayHistory);


        btnPayHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    Intent intentPayH = new Intent(CustomersInstallment.this, PaymentHistory.class);
                    intentPayH.putExtra("InvoiceId",InvoiceId);
                    startActivity(intentPayH);
            }
        });


        builder = new AlertDialog.Builder(this);

        final AlertDialog alert = builder.create();

        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Button Click","Sachitha clicked a button");

                //take remain payment
                Cursor deal = sqLiteDatabase.rawQuery("SELECT * FROM deals WHERE id = "+InvoiceId+";",null);
                deal.moveToNext();

                float remain = deal.getFloat(6);
                Log.d("Remain","Remained "+remain+editPayment.getText());
                String str=editPayment.getText().toString();

                if(!str.isEmpty()){
                    //get status = 0; installments


                    float inRemain = Float.parseFloat(str);

                        if(inRemain <=  remain){
                            //add that payment to collection table
                            sqLiteDatabase.execSQL("INSERT INTO collection(id,userId,installmentId,dealid,payment,date,time) VALUES (1,2,3,"+InvoiceId+","+inRemain+",CURRENT_DATE,'')");
                            Cursor cursorInstall  = sqLiteDatabase.rawQuery("SELECT * FROM installment WHERE dealid = "+InvoiceId+" AND status = 0;",null);
                            int numRows = cursorInstall.getCount();
                            int count = 1;

                            float tmpAmount = 0;


                            while(cursorInstall.moveToNext()){



                                tmpAmount = (cursorInstall.getFloat(3) - cursorInstall.getFloat(8));

                                if(tmpAmount == inRemain){
                                    Log.d("DATA INSTALLMENTS","Remain payment "+count+" "+tmpAmount);
                                    sqLiteDatabase.execSQL("UPDATE deals SET rprice = rprice - "+tmpAmount+" WHERE id = "+InvoiceId+";");
                                    sqLiteDatabase.execSQL("UPDATE installment SET rpayment = rpayment + "+tmpAmount+" WHERE id = "+cursorInstall.getString(0)+";");
                                    updateList(InvoiceId);
                                    updateHead(InvoiceId);
                                    break;
                                }else if(tmpAmount < inRemain){
                                    Log.d("DATA INSTALLMENTS","Remain payment "+count+" "+tmpAmount);
                                    sqLiteDatabase.execSQL("UPDATE installment SET rpayment = rpayment + "+tmpAmount+" WHERE id = "+cursorInstall.getString(0)+";");
                                    sqLiteDatabase.execSQL("UPDATE deals SET rprice = rprice - "+tmpAmount+" WHERE id = "+InvoiceId+";");
                                    inRemain -= tmpAmount;
                                }else{
                                    sqLiteDatabase.execSQL("UPDATE installment SET rpayment = rpayment + "+inRemain+" WHERE id = "+cursorInstall.getString(0)+";");
                                    Log.d("DATA INSTALLMENTS","Remain payment "+count+" "+inRemain);
                                    sqLiteDatabase.execSQL("UPDATE deals SET rprice = rprice - "+inRemain+" WHERE id = "+InvoiceId+";");
                                    updateList(InvoiceId);
                                    updateHead(InvoiceId);
                                    break;

                                }


                                count++;
                            }

                            editPayment.setText("");

                    }else{
                        alert.setTitle("Enter Amount less than "+remain);
                        alert.show();
                    }
                }

            }
        });



        setTitle("Deal Id "+InvoiceId);

        //update Installment List

        updateList(InvoiceId);
        updateHead(InvoiceId);
    }




    void updateList(String InvoiceId){
        Cursor cForCustomers =sqLiteDatabase.rawQuery("SELECT * FROM installment WHERE dealid = "+InvoiceId+";",null);

        int nRow = cForCustomers.getCount();


        customerList = findViewById(R.id.listInstallments);





        myList = new ArrayList<>();
        SingleRowForInstallment singleRow;

        for(int i = 1; i <= nRow;i++){
            cForCustomers.moveToNext();
            singleRow = new SingleRowForInstallment(cForCustomers.getString(3),cForCustomers.getString(8),cForCustomers.getString(6),cForCustomers.getString(5),i+"");
            myList.add(singleRow);
        }

        myAdapter = new ListViewInstallmentOfACustomer(this,myList);

        customerList.setAdapter(myAdapter);
    }


    void updateHead(String InvoiceId){
        Cursor deal = sqLiteDatabase.rawQuery("SELECT * FROM deals WHERE id = "+InvoiceId+";",null);
        deal.moveToNext();


        tot.setText("Total-"+deal.getString(5));
        receivePayment.setText("Received Payment-"+(deal.getFloat(5)-deal.getFloat(6)));
        balance.setText("Balance-"+deal.getString(6));
    }
}
