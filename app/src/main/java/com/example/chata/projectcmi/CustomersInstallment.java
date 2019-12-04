package com.example.chata.projectcmi;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

public class CustomersInstallment extends AppCompatActivity  {


    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    //For bluetooth
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // will show the statuses like bluetooth open, close or data sent
    TextView myLabel;
    Date currentTime = Calendar.getInstance().getTime();
    // will enable user to enter any text to be printed
    EditText myTextbox;

    // android built in classes for bluetooth operations
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;

    // needed for communication to bluetooth device / network
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;

    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;
    String  BILL;
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    //For bluetooth
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////



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
        balance = findViewById(R.id.txtAreaHead);

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
                            sqLiteDatabase.execSQL("INSERT INTO collection(id,userId,installmentId,dealid,payment,date,time,app) VALUES (1,2,3,"+InvoiceId+","+inRemain+",CURRENT_DATE,CURRENT_TIME,1)");
                            Cursor cursorInstall  = sqLiteDatabase.rawQuery("SELECT * FROM installment WHERE dealid = "+InvoiceId+" AND status = 0;",null);
                            int numRows = cursorInstall.getCount();
                            int count = 1;

                            float tmpAmount = 0;


                            while(cursorInstall.moveToNext()){



                                tmpAmount = (cursorInstall.getFloat(3) - cursorInstall.getFloat(8));

                                if(tmpAmount == inRemain){
                                    Log.d("DATA INSTALLMENTS","Remain payment "+count+" "+tmpAmount);
                                    sqLiteDatabase.execSQL("UPDATE deals SET rprice = rprice - "+tmpAmount+" ,app = 1 WHERE id = "+InvoiceId+";");
                                    sqLiteDatabase.execSQL("UPDATE installment SET rpayment = rpayment + "+tmpAmount+" , rdate = CURRENT_DATE , app = 1 WHERE id = "+cursorInstall.getString(0)+";");
                                    updateList(InvoiceId);
                                    updateHead(InvoiceId);
                                    break;
                                }else if(tmpAmount < inRemain){
                                    Log.d("DATA INSTALLMENTS","Remain payment "+count+" "+tmpAmount);
                                    sqLiteDatabase.execSQL("UPDATE installment SET rpayment = rpayment + "+tmpAmount+"  , rdate = CURRENT_DATE  , app = 1 WHERE id = "+cursorInstall.getString(0)+";");
                                    sqLiteDatabase.execSQL("UPDATE deals SET rprice = rprice - "+tmpAmount+" ,app = 1 WHERE id = "+InvoiceId+";");
                                    inRemain -= tmpAmount;
                                }else{
                                    sqLiteDatabase.execSQL("UPDATE installment SET rpayment = rpayment + "+inRemain+"  , rdate = CURRENT_DATE  , app = 1 WHERE id = "+cursorInstall.getString(0)+";");
                                    Log.d("DATA INSTALLMENTS","Remain payment "+count+" "+inRemain);
                                    sqLiteDatabase.execSQL("UPDATE deals SET rprice = rprice - "+inRemain+" ,app = 1 WHERE id = "+InvoiceId+";");
                                    updateList(InvoiceId);
                                    updateHead(InvoiceId);
                                    break;

                                }


                                count++;
                            }
                            Cursor cForCustomer = sqLiteDatabase.rawQuery("SELECT * FROM customer WHERE id = "+deal.getString(9)+";",null);
                            cForCustomer.moveToNext();
                            editPayment.setText("");
                            //Bill print part start
                            final ProgressDialog progressDialog = new ProgressDialog(CustomersInstallment.this);
                            progressDialog.setTitle(" Printing Bill");
                            progressDialog.setMessage("");
                            progressDialog.setCancelable(false);
                            updateStatus(InvoiceId);
                            //bill print here
                            try {
                                //look for BT on
                                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

                                if(mBluetoothAdapter == null) {
//                              myLabel.setText("No bluetooth adapter available");
                                }

                                if(!mBluetoothAdapter.isEnabled()) {
                                    //no BT available
                                    Toast.makeText(CustomersInstallment.this,"Bill not printed, Turn on BT",Toast.LENGTH_SHORT).show();
                                    CustomersInstallment.this.finish();
                                    Intent i = new Intent(CustomersInstallment.this,UploadData.class);
                                    startActivity(i);
                                }else{
                                    findBT(progressDialog);
                                    openBT();
                                    progressDialog.show();
                                    BILL =
                                                    "-----------------------------------------------\n"+
                                                    "                  TRANS LANKA                  \n"+
                                                    "-----------------------------------------------\n"+
                                                    "  Address                    \n"+
                                                    "     Mailagashandiya                \n" +
                                                    "     Anuradhapura                   \n" +
                                                    "  Telephone:               \n" +
                                                    "     071-6000061               \n"
                                                    +"-----------------------------------------------\n"
                                                    +"Deal Id : "+InvoiceId+" \n"
                                                    +"Agent : \n"
                                                    +"Customer Name : "+cForCustomer.getString(1)+"\n"
                                                    +"Customer Id : "+deal.getString(9)+" \n"
                                                    +"Date : "+currentTime+" \n"
                                                    +"-----------------------------------------------\n"
                                                    +"Item Name:\n" +
                                                    "Item Price:"+deal.getString(5)+"\n" +
                                                    "Total Received Payment:"+(deal.getInt(5) - deal.getInt(6) + inRemain)+"\n" +
                                                    "Balance:"+(deal.getInt(6) - inRemain)+"\n" +
                                                    "-----------------------------------------------\n"+
                                                    "Today Payment :"+inRemain+"\n"
                                                    +"-----------------------------------------------\n"
                                                        ;
                                    sendData(BILL,progressDialog);
                                }

                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                            //Bill print part exit

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


    void updateStatus(String dealId) {
        Cursor cForCustomers = sqLiteDatabase.rawQuery("SELECT * FROM installment WHERE dealid = " + dealId + " AND status = 0;", null);
        while (cForCustomers.moveToNext()) {
            Log.d("IID","Amount"+(cForCustomers.getInt(3)-cForCustomers.getInt(8)));

            if((cForCustomers.getInt(3)-cForCustomers.getInt(8)) == 0){
                sqLiteDatabase.execSQL("UPDATE installment SET status = 1 WHERE id = "+cForCustomers.getString(0)+";");
            }
        }


        Cursor cFordeals = sqLiteDatabase.rawQuery("SELECT * FROM deals WHERE id = "+dealId+";",null);

        cFordeals.moveToNext();

        if(cFordeals.getInt(6) <= 0){
            sqLiteDatabase.execSQL("UPDATE deals SET status = 1 WHERE id = "+cFordeals.getString(0)+";");
            Log.d("CustomerI", "updateStatus: Status changed to zero ");
        }
    }

    //--------------------------------------------------------------------------------------------------
    //For Bluetooth
//--------------------------------------------------------------------------------------------------
// close the connection to bluetooth printer.
    void closeBT() throws IOException {
        try {
            stopWorker = true;
            mmOutputStream.close();
            mmInputStream.close();
            mmSocket.close();
//        myLabel.setText("Bluetooth Closed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void findBT(ProgressDialog progressDialog) {

        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if(mBluetoothAdapter == null) {
//                myLabel.setText("No bluetooth adapter available");
            }

            if(!mBluetoothAdapter.isEnabled()) {
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, 0);
            }

            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

            if(pairedDevices.size() > 0) {
                boolean printer = false;
                for (BluetoothDevice device : pairedDevices) {

                    // RPP300 is the name of the bluetooth printer device
                    // we got this name from the list of paired devices
                    if (device.getName().equals(lookForBTName(CustomersInstallment.this))) {
                        mmDevice = device;
                        printer = true;
                        break;
                    }



                }

                if(printer == false){
                    Toast.makeText(CustomersInstallment.this,"Can not connect to the printer.",Toast.LENGTH_SHORT).show();
                    CustomersInstallment.this.finish();
                }


            }else{
                Toast.makeText(CustomersInstallment.this,"No paired devices found",Toast.LENGTH_SHORT).show();
                CustomersInstallment.this.finish();
            }

//            myLabel.setText("Bluetooth device found.");

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    // tries to open a connection to the bluetooth printer device
    void openBT() throws IOException {
        try {

            // Standard SerialPortService ID
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();

            beginListenForData();

//            myLabel.setText("Bluetooth Opened");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*
     * after opening a connection to bluetooth printer device,
     * we have to listen and check if a data were sent to be printed.
     */
    void beginListenForData() {
        try {
            final Handler handler = new Handler();

            // this is the ASCII code for a newline character
            final byte delimiter = 10;

            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];

            workerThread = new Thread(new Runnable() {
                public void run() {

                    while (!Thread.currentThread().isInterrupted() && !stopWorker) {

                        try {

                            int bytesAvailable = mmInputStream.available();

                            if (bytesAvailable > 0) {

                                byte[] packetBytes = new byte[bytesAvailable];
                                mmInputStream.read(packetBytes);

                                for (int i = 0; i < bytesAvailable; i++) {

                                    byte b = packetBytes[i];
                                    if (b == delimiter) {

                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer, 0,
                                                encodedBytes, 0,
                                                encodedBytes.length
                                        );

                                        // specify US-ASCII encoding
                                        final String data = new String(encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;

                                        // tell the user data were sent to bluetooth printer device
                                        handler.post(new Runnable() {
                                            public void run() {
//                                                myLabel.setText(data);
                                            }
                                        });

                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }

                        } catch (IOException ex) {
                            stopWorker = true;
                        }

                    }
                }
            });

            workerThread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // this will send text data to be printed by the bluetooth printer
    void sendData(String BILL, final ProgressDialog o) throws IOException {
        try {

            // the text typed by the user
            String msg ;
            msg = BILL;
            mmOutputStream.write(msg.getBytes());
            //Added by chata
//            closeBT();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    // Actions to do after 3 seconds
                    o.hide();
                    //TODO next intent

                    CustomersInstallment.this.finish();
                    Intent i = new Intent(CustomersInstallment.this,UploadData.class);
                    startActivity(i);
                    try {
                        closeBT();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }, 3000);

            // tell the user data were sent

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//--------------------------------------------------------------------------------------------------
    //For bluetooth
//--------------------------------------------------------------------------------------------------


    public String lookForBTName(Context context){
        SharedPreferences sharedPreferences = getSharedPreferences("btInfo", context.MODE_PRIVATE);
        return sharedPreferences.getString("btName", "");
    }

    }
