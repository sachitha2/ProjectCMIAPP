package com.example.chata.projectcmi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.widget.Toast;

public class Home extends AppCompatActivity {

    private DrawerLayout navigationDrawerHome;
    private ActionBarDrawerToggle toggleHome;

    public static SharedPreferences sharedPreferences03;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sharedPreferences03 = getSharedPreferences("loginInfo", Home.MODE_PRIVATE);

        navigationDrawerHome = (DrawerLayout) findViewById(R.id.activity_home);
        toggleHome = new ActionBarDrawerToggle(Home.this, navigationDrawerHome, R.string.open, R.string.close){

            @Override
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            /*@Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                super.onDrawerSlide(drawerView, 0); // this disables the arrow @ completed state
            }*/

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, 0); // this disables the animation
            }

        };


        navigationDrawerHome.addDrawerListener(toggleHome);
        toggleHome.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final NavigationView navigationViewHome = (NavigationView) findViewById(R.id.nav_home);
        View headerViewHome = navigationViewHome.getHeaderView(0);

        navigationViewHome.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        navigationDrawerHome.closeDrawers();

                        // unset item as selected to persist highlight
                        menuItem.setChecked(false);

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        onNavigationItemClick(Home.this, menuItem);

                        return true;
                    }
                });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggleHome.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void onNavigationItemClick(Context context, MenuItem menuItem){

        switch(menuItem.toString()) {
            case "Credits (Area)" :
                Intent intentSelectArea = new Intent(context, SelectAreaForCredits.class);
                context.startActivity(intentSelectArea);
                break;

            case "Customers" :
                Intent intentCustomers = new Intent(context, Customers.class);
                context.startActivity(intentCustomers);
                break;

            case "Mark Sheet" :
                Toast.makeText(context, menuItem.toString(), Toast.LENGTH_LONG).show();
                break;

            case "High Score" :
                Toast.makeText(context, menuItem.toString(), Toast.LENGTH_LONG).show();
                break;

            case "Settings" :
                Intent Settings = new Intent(context, DownloadData.class);
                context.startActivity(Settings);
                Toast.makeText(context, menuItem.toString(), Toast.LENGTH_LONG).show();
                break;

            case "Log Out" :

                SharedPreferences.Editor editor = sharedPreferences03.edit();

                editor.putInt("loginStatus", 0);
                editor.apply();

                Intent intentLogin = new Intent();
                intentLogin.setClass(context, MainActivity.class);
                intentLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intentLogin);
                break;

            case "Share" :
                //shareApplication();
//                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
//                intent.setData(Uri.parse("mailto:"));
//                intent.setType("text/plain");
//                intent.putExtra(Intent.EXTRA_SUBJECT, "VAST - eClz");
//                System.out.println(""+R.string.email_content);
//                intent.putExtra(Intent.EXTRA_TEXT, ""+context.getText(R.string.email_content)+context.getText(R.string.link));
//                Intent chooser = Intent.createChooser(intent, "Share VAST via");
//                context.startActivity(chooser);
                break;

            case "Help" :
//                Intent intentHelp = new Intent(context, Help.class);
//                context.startActivity(intentHelp);
                break;

            case "About Us" :
//                Intent intentAbout = new Intent(context, About.class);
//                context.startActivity(intentAbout);
                break;

            //default : // Optional
            // Statements
        }

    }

    long back_pressed;

    @Override
    public void onBackPressed() {
        if (back_pressed + 1000 > System.currentTimeMillis()){
            //super.onBackPressed();

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

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
