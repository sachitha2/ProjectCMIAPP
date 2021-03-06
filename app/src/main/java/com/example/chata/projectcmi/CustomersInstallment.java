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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

public class CustomersInstallment extends AppCompatActivity  {

    private static final String TAG = "CustomersInstallment";
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
    private String BillNumber;
    long time;


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

                            //take last id of collection;
                            Cursor cForLastCollection = sqLiteDatabase.rawQuery("SELECT * FROM collection ORDER BY id DESC",null);
                            cForLastCollection.moveToNext();

                            Log.d(TAG,""+cForLastCollection.getString(0));


                            //TODO
                            //download status update
                            time = System.currentTimeMillis();
                            BillNumber = lookForUserId(CustomersInstallment.this)+"-"+time;
                            Log.d(TAG,BillNumber);


                            sqLiteDatabase.execSQL("INSERT INTO collection(id,userId,installmentId,dealid,payment,date,time,app) VALUES ('"+BillNumber+"',"+lookForUserId(CustomersInstallment.this)+",3,"+InvoiceId+","+inRemain+",CURRENT_DATE,CURRENT_TIME,1)");
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

                                    if(haveNet()){
                                        Toast.makeText(CustomersInstallment.this,"Bill not printed, Turn on BT",Toast.LENGTH_SHORT).show();
                                        CustomersInstallment.this.finish();
                                        Intent i = new Intent(CustomersInstallment.this,UploadData.class);
                                        startActivity(i);
                                    }else{
                                        Toast.makeText(CustomersInstallment.this,"No internet connection\n Data will be uploaded next time.",Toast.LENGTH_SHORT).show();
                                    }


                                }else{
                                    findBT(progressDialog);
                                    openBT();
                                    progressDialog.show();

//                                    Cursor getItemName = sqLiteDatabase.rawQuery("SELECT * FROM item WHERE dealid = "+InvoiceId+";",null);;

                                    BILL =
                                                    "-----------------------------------------------\n"+
                                                    "TransLanka Marketing                \n"+
                                                    "-----------------------------------------------\n"+
                                                    "  No 152, 2nd Floor Mailagas Junction A/pura                   \n"+
                                                    "  Telephone:               \n" +
                                                    "     071-6000061               \n"
                                                    +"-----------------------------------------------\n"
                                                    +"Deal Id : "+InvoiceId+" \n"
//                                                    +"Agent : \n"
                                                    +"Customer Name : "+cForCustomer.getString(1)+"\n"
                                                    +"Customer Id : "+deal.getString(9)+" \n"
                                                    +"Date : "+getDateString(currentTime)+" \n"
                                                    +"-----------------------------------------------\n"+
//                                                    +"Item Name:\n" +
                                                    "Item Price:"+deal.getString(5)+"\n" +
                                                    "Total Received Payment:"+(deal.getInt(5) - deal.getInt(6) + inRemain)+"\n" +

                                                    "-----------------------------------------------\n"+
                                                    "Today Payment :"+str+"\n"
                                                    +"-----------------------------------------------\n"
                                                        ;

                                                    BILL = BILL + String.format("%1$-15s %2$15s", "Payment", "Received Date");
                                                    BILL = BILL + "\n-----------------------------------------------\n";
                                                    Cursor cForInstallmentBill = sqLiteDatabase.rawQuery("SELECT * FROM collection WHERE dealid = "+InvoiceId+";",null);

                                                    int lenInstall = cForInstallmentBill.getCount();
                                                    cForInstallmentBill.moveToNext();

                                                    for(int x = 0;x < lenInstall;x++){
                                                        BILL = BILL + String.format("%1$-15s %2$15s", cForInstallmentBill.getString(4),  cForInstallmentBill.getString(5));BILL = BILL + "\n";
                                                        cForInstallmentBill.moveToNext();
                                                    }
                                                    BILL = BILL + "\n-----------------------------------------------\n";
                                                    BILL = BILL+ "\nBalance:"+(deal.getInt(6) - Float.parseFloat(str));
                                                    BILL = BILL + "\n-----------------------------------------------\n";


                                    Log.d(TAG,BILL);
                                    Thread.sleep(1000);
                                    if(BILL == null){
                                        Toast.makeText(CustomersInstallment.this,"NULL/ CLICK Again ",Toast.LENGTH_SHORT).show();

                                    }else{
                                        sendData(BILL,progressDialog);
                                    }

                                }

                            } catch (IOException ex) {
                                ex.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
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
            mmOutputStream.write(BILL.getBytes());
            //Added by chata
//            closeBT();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    // Actions to do after 3 seconds
                    o.hide();
                    //TODO next intent
                    if(!haveNet()){
                        Toast.makeText(CustomersInstallment.this,"No internet connection\n Data will be uploaded next time.",Toast.LENGTH_SHORT).show();
                    }else{
                        CustomersInstallment.this.finish();
                        Intent i = new Intent(CustomersInstallment.this,UploadData.class);
                        startActivity(i);

                    }

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
            o.hide();
            Toast.makeText(CustomersInstallment.this,"Got an error",Toast.LENGTH_SHORT).show();
        }
    }

//--------------------------------------------------------------------------------------------------
    //For bluetooth
//--------------------------------------------------------------------------------------------------


    public String lookForBTName(Context context){
        SharedPreferences sharedPreferences = getSharedPreferences("btInfo", context.MODE_PRIVATE);
        return sharedPreferences.getString("btName", "");
    }
    public String lookForUserId(Context context){
        SharedPreferences sharedPreferences = getSharedPreferences("loginInfo", context.MODE_PRIVATE);
        return sharedPreferences.getString("userId", "");
    }


//    public boolean lookForUpload(Context context){
//        SharedPreferences sharedPreferences = getSharedPreferences("upload", context.MODE_PRIVATE);
//        int loginStatus = sharedPreferences.getInt("status",0);
//        if(loginStatus == 1){
//            return true;
//        }else {
//            return false;
//        }
//    }
//
//
//    public void uploadPreference(Context context,int val){
//        if (lookForUpload(context)){
//
//            //update login status
//
//            SharedPreferences sharedPreferences = getSharedPreferences("upload", context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//
//            editor.putInt("status", val);
//            editor.apply();
//
//        }else{
//
//            //create cache
//
//            SharedPreferences sharedPreferences = getSharedPreferences("upload", context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//
//            editor.putInt("status", val);
//            editor.apply();
//
//        }
//    }


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


    public static String getDateString(Date date){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
    }


