package com.example.chata.projectcmi;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListViewInstallmentOfACustomer extends BaseAdapter implements Filterable {
    Context c;
    ArrayList<SingleRowForInstallment> originalArray,tmpArray;
    ///filter
    ListViewInstallmentOfACustomer.CustomFilter cs;
    ///filter



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

        TextView txtDealId =row.findViewById(R.id.textView);




        TextView txtTotal = row.findViewById(R.id.txtTotal);


        txtDealId.setText(originalArray.get(position).getTotal());
        txtTotal.setText("Total price "+originalArray.get(position).getTotal()+"");

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
    @Override
    public Filter getFilter() {

        if(cs == null){
            cs = new ListViewInstallmentOfACustomer.CustomFilter();
        }

        return cs;
    }

    class CustomFilter extends  Filter{
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if(constraint != null && constraint.length() > 0) {
                constraint = constraint.toString().toUpperCase();


                ArrayList<SingleRowForInstallment> filters = new ArrayList<>();

                for (int i = 0; i < tmpArray.size(); i++) {
//                    if (tmpArray.get(i).getTotal().toUpperCase().contains(constraint)) {
//                        SingleRowForInstallment singleRow = new SingleRowForInstallment(tmpArray.get(i).getDealId(),tmpArray.get(i).getTotal(),tmpArray.get(i).isStatus());
//
//
//
//                        filters.add(singleRow);
//
//
//
//
//                    }



                }
                results.count = filters.size();
                results.values = filters;

            }else {
                results.count = tmpArray.size();
                results.values = tmpArray;

            }
            return results;
        }


        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            originalArray  = (ArrayList<SingleRowForInstallment>)results.values;
            notifyDataSetChanged();
        }
    }
    ///Codes for filter



}
