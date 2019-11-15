package com.example.chata.projectcmi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewInstallmentOfACustomer extends BaseAdapter  {
    Context c;
    ArrayList<SingleRowForInstallment> originalArray,tmpArray;




    public  ListViewInstallmentOfACustomer(Context c, ArrayList<SingleRowForInstallment> originalArray){
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

        View row = inflater.inflate(R.layout.list_view_installment_of_a_customer,null);

        TextView txtId =row.findViewById(R.id.id);
        TextView txtPayment = row.findViewById(R.id.payment);
        TextView txtRPayment = row.findViewById(R.id.rpayment);
        TextView txtDate = row.findViewById(R.id.dueDate);
        TextView txtRDate = row.findViewById(R.id.rDate);






        txtId.setText(originalArray.get(position).getId());
        txtPayment.setText(originalArray.get(position).getPayment()+"");
        txtRPayment.setText(originalArray.get(position).getRpayment()+"");
        txtDate.setText(originalArray.get(position).getDueDate()+"");
        txtRDate.setText(originalArray.get(position).getRdate()+"");

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
    ///Codes for filter




}
