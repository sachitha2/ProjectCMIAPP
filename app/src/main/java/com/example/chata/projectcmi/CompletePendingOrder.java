package com.example.chata.projectcmi;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

public class CompletePendingOrder extends AppCompatActivity   {


    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    //For bluetooth
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // will show the statuses like bluetooth open, close or data sent
    TextView myLabel;

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
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    //For bluetooth
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////






    private RequestQueue requestQueueUpload;
    Button print;
    public  String mac;
    public  String val;
    public EditText editText;
    public EditText getCash;
    public String BILL = "";

    String cash;
    String json;
    String itemTotla;
    String invoiceN;
    String shopId;
    String ShopName;
    String DriverName;
    float preCredit;
    float totalWithCredit = 0;
    Date currentTime = Calendar.getInstance().getTime();
    private static final String TAG = "bluetooth1";

    //TextViews
    TextView txtInvoiceId;
    TextView itemTotal;
    TextView previousCredit;
    TextView totalCredit;
    //TextViews

    ///Font Helpers

    final String SPACES = "         ";//9
    final String uline = "________________________________________";
    final String dline = "----------------------------------------";
    //sachitha hirushan
    ///Font Helpers
    Button btnOn, btnOff;
    Bitmap bitmap;



    SQLiteDatabase sqLiteSelectShop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_pending_order);
        setTitle("Complete Pending Order");



        print = findViewById(R.id.btnPrint);
        mac = "02:2F:01:1E:CA:40";
        ///progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(CompletePendingOrder.this);
        progressDialog.setTitle(" Printing Bill");
        progressDialog.setMessage("");
        progressDialog.setCancelable(false);

        ///progress dialog





        sqLiteSelectShop = openOrCreateDatabase("cmi", CompletePendingOrder.MODE_PRIVATE,null);


        getCash = findViewById(R.id.cash);
        shopId = getIntent().getStringExtra("ShopId");
        ShopName = getIntent().getStringExtra("ShopName");
        invoiceN = getIntent().getStringExtra("invoiceNumber");
        json = getIntent().getStringExtra("json");
        itemTotla = getIntent().getStringExtra("itemTotla");



        txtInvoiceId = findViewById(R.id.txtInvoiceId);
        itemTotal = findViewById(R.id.itemTotal);
        txtInvoiceId.setText(invoiceN);




        itemTotal.setText(itemTotla+"");

        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



//
//                    //updating deal table
                      progressDialog.show();


//                    sqLiteSelectShop.execSQL("UPDATE deal SET credit = '"+tmpTotal+"',cash = "+cash+", s = 0 WHERE id = '"+invoiceN+"';");
//                    sqLiteSelectShop.execSQL("UPDATE invoice SET s = 1 WHERE dealId LIKE '"+invoiceN+"';");



//                    //Make bill
                    BILL =
                                    "-----------------------------------------------\n"+
                                    "                    CMI Pvt Ltd                \n"+
                                    "-----------------------------------------------\n"+
                                    "  Address                    \n"+
                                    "       5TH Mile Post Rajanganaya              \n\n" +
                                    "  Telephone:               \n" +
                                    "       0777 59 79 29 / 0711 59 79 29               \n"
                                    +"-----------------------------------------------\n"
                                    +"Invoice Number  : "+invoiceN+"\n"
                                    +"Agent           : "+DriverName+"\n"

                                    +"Customer Name   : "+ShopName+"\n"
                                    +"Customer Id     : "+shopId+"\n"
                                    +"Date : "+currentTime+"\n"
                                    +"-----------------------------------------------\n";


                    BILL = BILL + String.format("%1$-10s %2$10s %3$13s %4$10s", "Item", "Qty", "Rate", "Total");
                    BILL = BILL + "\n";
                    BILL = BILL
                            + "-----------------------------------------------\n";
                    String itemName,qty,rate ;
                    double total;
                    float sPrice ;
                    int nItems = 0;
                    float fullTotal = 0;
                    try {
                        JSONObject obj = new JSONObject(json);
                        nItems = obj.length();
                        for(int x = 0;x < obj.length();x++){

                            JSONArray tmpJson = obj.getJSONArray(""+x+"");
                            itemName = tmpJson.getString(0);
                            rate = tmpJson.getString(1);
                            qty = tmpJson.getString(2);
//                        total = ();
                            sPrice = Float.parseFloat(rate);
                            total = sPrice * Integer.valueOf(qty);
                            fullTotal += total;
                            BILL = BILL + "  "+itemName+" \n";
                            BILL = BILL + "\n " + String.format("%1$20s %2$11s %3$10s", qty, rate, total+"")+"\n";
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    BILL = BILL
                            + "\n-----------------------------------------------";
                    BILL = BILL + "\n\n";

                    BILL = BILL + "  Total Qty       :" + "     " + nItems + "\n";
                    BILL = BILL + "  Total Value     :" + "     " + fullTotal + "\n";
                    BILL = BILL + "  Your Profit     :" + "     " + fullTotal + "\n";

                    BILL = BILL
                            + "-----------------------------------------------\n"
                            + "     Solution by\n"
                            + "         www.infinisolutionslk.com\n" +
                            "           077-1466460/071-7191137\n"
                            + "-----------------------------------------------\n";
                    BILL = BILL + "\n\n ";
//
//
//                    //Make bill
//
//                    print bill here
                    try {

                        findBT();
                        openBT();


                        sendData(BILL,progressDialog);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                }
        });

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
    void findBT() {

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
                for (BluetoothDevice device : pairedDevices) {

                    // RPP300 is the name of the bluetooth printer device
                    // we got this name from the list of paired devices
                    if (device.getName().equals("MTP-3")) {
                        mmDevice = device;
                        break;
                    }
                }
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
                    CompletePendingOrder.this.finish();
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
}
