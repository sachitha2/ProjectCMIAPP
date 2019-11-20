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



public class PendingOrdersListAdapter extends BaseAdapter implements Filterable {

    Context c;
    ArrayList<SingleRowForPendingOrders> originalArray,tmpArray;
    ///filter
    CustomFilter cs;
    ///filter



    public  PendingOrdersListAdapter(Context c, ArrayList<SingleRowForPendingOrders> originalArray){
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

        View row = inflater.inflate(R.layout.pending_orders_list_layout,null);

        TextView textView =(TextView)row.findViewById(R.id.txtShopName);
        TextView textId = row.findViewById(R.id.txtInvoiceId);
        TextView shopId = row.findViewById(R.id.txtShopId);
        TextView total = row.findViewById(R.id.txtPayment);
        TextView qty = row.findViewById(R.id.txtQty);

        textView.setText(originalArray.get(position).getName());
        textId.setText(originalArray.get(position).getId());
        shopId.setText(originalArray.get(position).getShopId());
        total.setText(originalArray.get(position).getTotal());
        qty.setText(originalArray.get(position).getQty());

//        textAddress.setText(originalArray.get(position).getAddress());
//        textContact.setText("071-5591137");
//        textRoute.setText("Galgamuwa");



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
            cs = new CustomFilter();
        }

        return cs;
    }

    class CustomFilter extends  Filter{
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if(constraint != null && constraint.length() > 0) {
                constraint = constraint.toString().toUpperCase();


                ArrayList<SingleRowForPendingOrders> filters = new ArrayList<>();

                for (int i = 0; i < tmpArray.size(); i++) {
                    if (tmpArray.get(i).getName().toUpperCase().contains(constraint)) {
                        SingleRowForPendingOrders singleRow = new SingleRowForPendingOrders(tmpArray.get(i).getName(),tmpArray.get(i).getId(),tmpArray.get(i).getShopId(),tmpArray.get(i).getTotal(),tmpArray.get(i).getQty());



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
            originalArray  = (ArrayList<SingleRowForPendingOrders>)results.values;
            notifyDataSetChanged();
        }
    }
    ///Codes for filter
}

