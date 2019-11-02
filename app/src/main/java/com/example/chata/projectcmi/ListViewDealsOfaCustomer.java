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

        TextView txtDealId =(TextView)row.findViewById(R.id.textView);
        TextView txtTotal = row.findViewById(R.id.txtTotal);
        TextView txtStatus = row.findViewById(R.id.txtStatus);


        txtDealId.setText(originalArray.get(position).getDealId());
        txtTotal.setText("Total price "+originalArray.get(position).getTotal()+"");

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent viewAInvoice = new Intent(c, ViewAInvoice.class);
//
//                viewAInvoice.putExtra("InvoiceId", originalArray.get(position).getId());
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
                        SingleRowForDealsOfACustomer singleRow = new SingleRowForDealsOfACustomer(tmpArray.get(i).getDealId(),tmpArray.get(i).getTotal(),tmpArray.get(i).isStatus());



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