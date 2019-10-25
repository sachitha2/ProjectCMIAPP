package com.example.chata.projectcmi;

        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;

public class CustomerDeals extends AppCompatActivity {
    private String customerId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_deals);
        customerId= getIntent().getStringExtra("customerId");
        setTitle("Deals - "+customerId);
    }
}
