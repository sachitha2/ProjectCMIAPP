package com.example.chata.projectcmi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewPaymentHistory  extends BaseAdapter {

    Context c;
    ArrayList<SingleRowForPaymentHistory> originalArray,tmpArray;



    public  ListViewPaymentHistory(Context c, ArrayList<SingleRowForPaymentHistory> originalArray){
        this.c = c;
        this.originalArray = originalArray;
        this.tmpArray = originalArray;
    }


    @Override
    public Object getItem(int position) {
        return originalArray.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View row = inflater.inflate(R.layout.list_view_payment_history,null);

        TextView txtId =row.findViewById(R.id.txt_id);
        TextView txtColloction = row.findViewById(R.id.txt_collection);
        TextView txtDate = row.findViewById(R.id.txt_date);


        txtId.setText(originalArray.get(position).getId());
        txtColloction.setText(originalArray.get(position).getCollection()+"");
        txtDate.setText(originalArray.get(position).getDate()+"");

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent viewAInvoice = new Intent(c, CustomersInstallment.class);
////
//                viewAInvoice.putExtra("InvoiceId", originalArray.get(position).getDealId());
//                c.startActivity(viewAInvoice);
//                Toast.makeText(c, "Invoice " + " was clicked", Toast.LENGTH_SHORT).show();
            }
        });

        return row;
    }

    @Override
    public int getCount() {
        return originalArray.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
