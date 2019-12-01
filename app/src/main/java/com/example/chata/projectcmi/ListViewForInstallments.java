package com.example.chata.projectcmi;

import android.app.Activity;
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

public class ListViewForInstallments extends BaseAdapter implements Filterable {
    Context c;
    ArrayList<SingleRowForInstallments> originalArray,tmpArray;
    ///filter
    ListViewForInstallments.CustomFilter cs;
    ///filter



    public  ListViewForInstallments(Context c, ArrayList<SingleRowForInstallments> originalArray){
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

        View row = inflater.inflate(R.layout.list_view_installment,null);

        TextView txtDealId =(TextView)row.findViewById(R.id.txtId);
        TextView txtTotal = row.findViewById(R.id.txtPayment);
        TextView txtStatus = row.findViewById(R.id.txtDueDate);
        TextView txtTotH = row.findViewById(R.id.txtPaymentHead);
        TextView rpayment = row.findViewById(R.id.txtCID);


        txtTotH.setText("Total");


//        rpayment.setText(""+originalArray.get(position).getAmount());



        txtDealId.setText("ID:"+originalArray.get(position).getId());
        txtTotal.setText(""+originalArray.get(position).getAmount()+"");

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent viewAInvoice = new Intent(c, CustomersInstallment.class);

                viewAInvoice.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
                viewAInvoice.putExtra("InvoiceId", originalArray.get(position).getId());
                c.startActivity(viewAInvoice);
                Toast.makeText(c, "Invoice " + " was clicked", Toast.LENGTH_SHORT).show();

                ((Activity)c).finish();

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
            cs = new ListViewForInstallments.CustomFilter();
        }

        return cs;
    }

    class CustomFilter extends  Filter{
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if(constraint != null && constraint.length() > 0) {
                constraint = constraint.toString().toUpperCase();


                ArrayList<SingleRowForInstallments> filters = new ArrayList<>();

                for (int i = 0; i < tmpArray.size(); i++) {
                    if (tmpArray.get(i).getcName().toUpperCase().contains(constraint)) {
                        SingleRowForInstallments singleRow = new SingleRowForInstallments(10,"2019-10-20","1000","Sam wilson","250","Galgamuwa","983142044V");



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
            originalArray  = (ArrayList<SingleRowForInstallments>)results.values;
            notifyDataSetChanged();
        }
    }
    ///Codes for filter
}
