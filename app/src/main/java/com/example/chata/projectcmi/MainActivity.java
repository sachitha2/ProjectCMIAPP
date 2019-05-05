package com.example.chata.projectcmi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private RequestQueue loginQueue;
    private EditText edtUsername;
    private EditText edtPassword;
    private Button btnLogin;

    public static String loggedAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_main);

        if(lookForLoggedAccount(MainActivity.this)){//there is no logged account

            edtUsername = (EditText) findViewById(R.id.edtUsername);
            edtPassword = (EditText) findViewById(R.id.edtPassword);
            btnLogin = (Button) findViewById(R.id.btnLogin);

            loginQueue = Volley.newRequestQueue(MainActivity.this);

        }else {
            SharedPreferences sharedPreferences01 = getSharedPreferences("loginInfo", MainActivity.this.MODE_PRIVATE);
            loggedAccount = sharedPreferences01.getString("uName", "");

            Intent intentHome = new Intent();
            intentHome.setClass(MainActivity.this, Home.class);
            intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intentHome);
        }

    }

    public void onLoginClick(View view){

        if (!TextUtils.isEmpty(edtUsername.getText())){
            if (!TextUtils.isEmpty(edtPassword.getText())){

                jsonParse();

            }else {
                Toast.makeText(MainActivity.this,"Enter Password", Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(MainActivity.this,"Enter Username", Toast.LENGTH_LONG).show();
        }

    }

    private void jsonParse(){

        String url = "http://cms.infinisolutionslk.com/APP/login.json.php?uName="+ edtUsername.getText() + "&uPass="+ edtPassword.getText();

        JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("JSONPRASE","in response");
                        try{

                            Integer status = response.getInt("satus");
                            String message = response.getString("Messege");
                            //String logOutTime = response.getString("logOutTime");

                            if (status == 1){

                                //Check if this email is available in cache

                                if (lookForUsername(MainActivity.this).equals(edtUsername.getText())){

                                    //update login status

                                    SharedPreferences sharedPreferences = getSharedPreferences("loginInfo", MainActivity.this.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();

                                    editor.putInt("loginStatus", 1);
                                    editor.apply();

                                }else{

                                    //create cache

                                    SharedPreferences sharedPreferences = getSharedPreferences("loginInfo", MainActivity.this.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();

                                    editor.putString("uName", edtUsername.getText().toString());
                                    editor.putInt("loginStatus", 1);
                                    editor.apply();

                                }

                                SharedPreferences sharedPreferences02 = getSharedPreferences("loginInfo", MainActivity.this.MODE_PRIVATE);
                                loggedAccount = sharedPreferences02.getString("uName", "");

                                Intent intentHome = new Intent();
                                intentHome.setClass(MainActivity.this, Home.class);
                                intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                //make the progress bar invisible before go to the home screen
                                //progressBar.setVisibility(View.GONE);

                                startActivity(intentHome);

                            }else if (status == 0){

                                //make the progress bar invisible before show the error message
                                //progressBar.setVisibility(View.GONE);
                                Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();

                            }

                        }catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONPRASE ERROR","Catch");
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        loginQueue.add(loginRequest);

    }

    public String lookForUsername(Context context){
        SharedPreferences sharedPreferences = getSharedPreferences("loginInfo", context.MODE_PRIVATE);
        return sharedPreferences.getString("uName", "");
    }

    public boolean lookForLoggedAccount(Context context){
        SharedPreferences sharedPreferences = getSharedPreferences("loginInfo", context.MODE_PRIVATE);
        String Username = sharedPreferences.getString("uName","");
        int loginStatus = sharedPreferences.getInt("loginStatus",-1);
        if(loginStatus != 1){
            return true;
        }else {
            return false;
        }
    }

//    public void showOrHidePassword(){
//
//        if (btnShow.getText().toString() == "SHOW") {
//            edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
//            btnShow.setText("HIDE");
//
//        } else {
//
//            edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
//            btnShow.setText("SHOW");
//        }
//
//    }
//
//    public void onSignUpClick(View v){
//        Intent myIntent = new Intent(MainActivity.this, CreateAccount.class);
//        startActivity(myIntent);
//    }

    long back_pressed;

    @Override
    public void onBackPressed() {
        if (back_pressed + 1000 > System.currentTimeMillis()){
            super.onBackPressed();
            return;
        }
        else{
            Toast.makeText(getBaseContext(),
                    "Press again to exit!", Toast.LENGTH_SHORT)
                    .show();
        }
        back_pressed = System.currentTimeMillis();
    }

}
