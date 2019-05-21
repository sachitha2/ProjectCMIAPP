package com.example.chata.projectcmi;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class SelectBuletoothMAC extends AppCompatActivity {
    private ListView lstvw;
    private Object mac;
    private ArrayAdapter aAdapter;
    private BluetoothAdapter bAdapter = BluetoothAdapter.getDefaultAdapter();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_buletooth_mac);
        setTitle("Select Butooth Device");


        if(bAdapter==null){
            Toast.makeText(getApplicationContext(),"Bluetooth Not Supported",Toast.LENGTH_SHORT).show();
        }
        else{
            final Set<BluetoothDevice> pairedDevices = bAdapter.getBondedDevices();
            ArrayList list = new ArrayList();
            final ArrayList bt = new ArrayList();
            if(pairedDevices.size()>0){
                int i = 0;
                for(BluetoothDevice device: pairedDevices){
                    String devicename = device.getName();
                    String macAddress = device.getAddress();
                    list.add("Name: "+devicename);//+"MAC Address: "+macAddress
                    bt.add(i,macAddress);
                    i++;
                }
                lstvw = (ListView) findViewById(R.id.deviceList);
                aAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, list);
                lstvw.setAdapter(aAdapter);

                lstvw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if(0 == 0){
                            Toast.makeText(SelectBuletoothMAC.this, "clicked"+ bt.get(position), Toast.LENGTH_SHORT).show();
//                                    Intent myIntent = new Intent(view.getContext(),ConnectToPrinter.class);
////                                    startActivityForResult(myIntent,bt.get(position));
                            mac = bt.get(position);


                            ///Creating Sharedpreferense
                            SharedPreferences pref = getSharedPreferences("prefs",MODE_PRIVATE);
                            boolean firstStart = pref.getBoolean("firstStart",true);
                            String MACAddress = pref.getString("MAC",mac.toString());

                            if (firstStart){
                                Toast.makeText(SelectBuletoothMAC.this, "First", Toast.LENGTH_SHORT).show();
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putBoolean("firstStart",false);
                                editor.putString("MAC",mac.toString());
                                editor.apply();
                            }else{
                                Toast.makeText(SelectBuletoothMAC.this, "Not First", Toast.LENGTH_SHORT).show();
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putBoolean("firstStart",false);
                                editor.putString("MAC",mac.toString());
                                editor.apply();
                            }
                            ///Creating Sharedpreferense








                            Intent intent =new  Intent(SelectBuletoothMAC.this,ConnectToPrinter.class);
                            intent.putExtra("MAC", String.valueOf(mac) );
                            intent.putExtra("email","chatson@chatson.com");

                            startActivity(intent);
                        }
                    }
                });
            }
        }
    }
}
