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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.util.BitSet;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

public class PaymentHistory extends AppCompatActivity {


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

    Date currentTime = Calendar.getInstance().getTime();
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    //For bluetooth
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////


    Button btnAddPayment;

    SQLiteDatabase sqLiteDatabase;
    int total;
    ListViewPaymentHistory myAdapter;
    ArrayList<SingleRowForPaymentHistory> myList;
    ListView customerList;
    String  BILL;
    TextView txtTotal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_history);
        final String InvoiceId = getIntent().getStringExtra("InvoiceId");

        sqLiteDatabase = openOrCreateDatabase("cmi", Customers.MODE_PRIVATE,null);


        setTitle("Payment History - "+InvoiceId);

        txtTotal = findViewById(R.id.titleData);

        final Cursor deal = sqLiteDatabase.rawQuery("SELECT * FROM deals WHERE id = "+InvoiceId+";",null);
        deal.moveToNext();


        txtTotal.setText(" "+(deal.getFloat(5)-deal.getFloat(6)));


        customerList = findViewById(R.id.listInstallments);

        Cursor cForCollection =sqLiteDatabase.rawQuery("SELECT * FROM collection WHERE dealid = '"+InvoiceId+"';",null);

        total = cForCollection.getCount();


        myList = new ArrayList<>();
        SingleRowForPaymentHistory singleRow;
        int x = 1;
        while(cForCollection.moveToNext()){
            singleRow = new SingleRowForPaymentHistory(x++,cForCollection.getString(4),cForCollection.getString(5));
            myList.add(singleRow);

        }

        myAdapter = new ListViewPaymentHistory(this,myList);

        customerList.setAdapter(myAdapter);


        btnAddPayment = findViewById(R.id.btnAddPayment);


        final ProgressDialog progressDialog = new ProgressDialog(PaymentHistory.this);
        progressDialog.setTitle(" Printing Bill");
        progressDialog.setMessage("");
        progressDialog.setCancelable(false);



        btnAddPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    //look for BT on
                    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

                    if(mBluetoothAdapter == null) {
//                              myLabel.setText("No bluetooth adapter available");
                    }

                    if(!mBluetoothAdapter.isEnabled()) {
                        //no BT available
                        Toast.makeText(PaymentHistory.this,"Bill not printed, Turn on BT",Toast.LENGTH_SHORT).show();
                        PaymentHistory.this.finish();
                        Intent i = new Intent(PaymentHistory.this,UploadData.class);
                        startActivity(i);
                    }else{
                        findBT();
                        openBT();
                        progressDialog.show();
                        BILL =
                                        "-----------------------------------------------\n"+
                                        "             Saveero Lanka Furnitures                  \n"+
                                        "-----------------------------------------------\n"+
                                        "  Address                    \n"+
                                        "    5 Kanuwa,Rajanganaya                    \n" +
                                        "  Telephone:               \n" +
                                        "     077-1632769               \n"
                                        +"-----------------------------------------------\n"
                                        +"Deal Id : "+InvoiceId+" \n"
                                        +"Agent : \n"
//                                        +"Customer Name : "+cForCustomer.getString(1)+"\n"
                                        +"Customer Id : "+deal.getString(9)+" \n"
                                        +"Date : "+currentTime+" \n"
                                        +"-----------------------------------------------\n"
                                        +"Item Name:\n" +
                                        "Item Price:"+deal.getString(5)+"\n" +
                                        "Total Received Payment:"+(deal.getInt(5) - deal.getInt(6))+"\n" +
                                        "Balance:"+(deal.getInt(6))+"\n" +
                                        "-----------------------------------------------\n"+
                                        "Today Payment :0\n"
                                        +"-----------------------------------------------\n"
                        ;
                        sendData(BILL,progressDialog);
                    }
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
                    if (device.getName().equals(lookForBTName(PaymentHistory.this))) {
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
                    PaymentHistory.this.finish();
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
