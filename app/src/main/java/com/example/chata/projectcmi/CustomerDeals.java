package com.example.chata.projectcmi;

        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.text.Editable;
        import android.text.TextWatcher;
        import android.util.Log;
        import android.widget.EditText;
        import android.widget.ListView;
        import android.widget.TextView;

        import java.util.ArrayList;

public class CustomerDeals extends AppCompatActivity   implements TextWatcher {
    private String customerId;
    private TextView nDeals;
    private String[] id, name,nic,area;
    private int[] rpayment,total;
    EditText searchCustomers;
    ListView customerList;

    ArrayList<SingleRowForDealsOfACustomer> myList;
    ListViewDealsOfaCustomer myAdapter;
    SQLiteDatabase sqLiteDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_deals);
        customerId= getIntent().getStringExtra("customerId");
        setTitle("Deals - "+customerId);

        nDeals = findViewById(R.id.nDeals);
        nDeals.setText("");

        sqLiteDatabase  = openOrCreateDatabase("cmi", Context.MODE_PRIVATE,null);

        Cursor cDeals =sqLiteDatabase.rawQuery("SELECT * FROM deals WHERE  cid = "+customerId+" ;",null);

        int nRow = cDeals.getCount();

        //new


        id = new String[nRow];
        name = new String[nRow];
        nic = new String[nRow];
        area = new String[nRow];

        rpayment = new int[nRow];
        total = new int[nRow];

        int i=0;
        while (cDeals.moveToNext()){

            id[i] = cDeals.getString(0);
            name[i] = cDeals.getString(1);
            nic[i] = cDeals.getString(2);
            rpayment[i] = cDeals.getInt(6);
            total[i] = cDeals.getInt(5);
            i++;
        }
        ///Read data End
        Log.d("Customers", "DATA: "+nRow);
        searchCustomers = findViewById(R.id.textSListDeals);

        customerList = findViewById(R.id.listDeals);



        searchCustomers.addTextChangedListener(this);


        myList = new ArrayList<>();
        SingleRowForDealsOfACustomer singleRow;

        for( i = 0; i < name.length;i++){
            singleRow = new SingleRowForDealsOfACustomer(id[i],total[i],true,total[i]-rpayment[i],rpayment[i]);

            myList.add(singleRow);
        }

        myAdapter = new ListViewDealsOfaCustomer(this,myList);

        customerList.setAdapter(myAdapter);

        //new
    }


    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        this.myAdapter.getFilter().filter(s);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
