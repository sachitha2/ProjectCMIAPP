package com.example.chata.projectcmi;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListViewDealsOfaCustomer extends BaseAdapter implements Filterable {
    Context c;
    ArrayList<SingleRowForDealsOfACustomer> originalArray,tmpArray;
    ///filter
    CustomFilter cs;
    ///filter



    public  ListViewDealsOfaCustomer(Context c, ArrayList<SingleRowForDealsOfACustomer> originalArray){
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

        View row = inflater.inflate(R.layout.list_deals_of_a_customer,null);

        TextView txtDealId =(TextView)row.findViewById(R.id.id);
        TextView txtTotal = row.findViewById(R.id.payment);
        TextView txtStatus = row.findViewById(R.id.txtStatus);
        TextView txtTotH = row.findViewById(R.id.txtTotH);
        TextView rpayment = row.findViewById(R.id.rpayment);
        TextView balance = row.findViewById(R.id.balance);


        txtTotH.setText("Total");


        rpayment.setText(""+originalArray.get(position).getRpayment());


        balance.setText(""+originalArray.get(position).getBalance());

        txtDealId.setText(originalArray.get(position).getDealId());
        txtTotal.setText(""+originalArray.get(position).getTotal()+"");

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent viewAInvoice = new Intent(c, CustomersInstallment.class);
//
                viewAInvoice.putExtra("InvoiceId", originalArray.get(position).getDealId());
                c.startActivity(viewAInvoice);
                Toast.makeText(c, "Invoice " + " was clicked", Toast.LENGTH_SHORT).show();
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
            cs = new ListViewDealsOfaCustomer.CustomFilter();
        }

        return cs;
    }

    class CustomFilter extends  Filter{
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if(constraint != null && constraint.length() > 0) {
                constraint = constraint.toString().toUpperCase();


                ArrayList<SingleRowForDealsOfACustomer> filters = new ArrayList<>();

                for (int i = 0; i < tmpArray.size(); i++) {
                    if (tmpArray.get(i).getDealId().toUpperCase().contains(constraint)) {
                        SingleRowForDealsOfACustomer singleRow = new SingleRowForDealsOfACustomer(tmpArray.get(i).getDealId(),tmpArray.get(i).getTotal(),tmpArray.get(i).isStatus(),tmpArray.get(i).getRpayment(),tmpArray.get(i).getBalance());



                        filters.add(singleRow);




                    }



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
            originalArray  = (ArrayList<SingleRowForDealsOfACustomer>)results.values;
            notifyDataSetChanged();
        }
    }
    ///Codes for filter
}
