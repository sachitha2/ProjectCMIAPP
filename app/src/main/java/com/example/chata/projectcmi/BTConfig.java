package com.example.chata.projectcmi;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class BTConfig extends AppCompatActivity {

    Button btnSetBT;
    EditText textBtName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btconfig);

       btnSetBT =  findViewById(R.id.btnSetBT);

       textBtName =  findViewById(R.id.editBTName);


       textBtName.setText(lookForBTName(BTConfig.this));

       //get shared preference here

        btnSetBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create cache

                SharedPreferences sharedPreferences = getSharedPreferences("btInfo", BTConfig.this.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("btName", textBtName.getText().toString());
//                editor.putInt("loginStatus", 1);
                editor.apply();
                Toast.makeText(BTConfig.this,"Done",Toast.LENGTH_SHORT).show();
                BTConfig.this.finish();
            }
        });
    }

    public String lookForBTName(Context context){
        SharedPreferences sharedPreferences = getSharedPreferences("btInfo", context.MODE_PRIVATE);
        return sharedPreferences.getString("btName", "");
    }
}
