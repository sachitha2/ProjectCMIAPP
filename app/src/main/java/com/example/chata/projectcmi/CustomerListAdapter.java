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

public class CustomerListAdapter extends BaseAdapter implements Filterable {


    Context c;
    ArrayList<SingleRowForCustomer> originalArray,tmpArray;
    ///filter
    CustomFilter cs;
    ///filter



    public  CustomerListAdapter(Context c, ArrayList<SingleRowForCustomer> originalArray){
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

        View row = inflater.inflate(R.layout.customer_list,null);

        TextView textView =(TextView)row.findViewById(R.id.txtId);
        TextView textAge = row.findViewById(R.id.txtCID);
        TextView textNic = row.findViewById(R.id.txtNic);
        TextView textArea = row.findViewById(R.id.txtArea);
        TextView txtAddress = row.findViewById(R.id.txtAddress);


        textView.setText(originalArray.get(position).getName().toUpperCase());
        textAge.setText(originalArray.get(position).getAge());

        textNic.setText(originalArray.get(position).getNic());
        textArea.setText(originalArray.get(position).getArea());
        txtAddress.setText(originalArray.get(position).getAddress());

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentCustomerProfile = new Intent(c, CustomerData.class);



                //TODO Config putExtra variables here
                intentCustomerProfile.putExtra("customerId", originalArray.get(position).getAge());
                c.startActivity(intentCustomerProfile);
                Toast.makeText(c, originalArray.get(position).getName() + " was clicked", Toast.LENGTH_SHORT).show();
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


                ArrayList<SingleRowForCustomer> filters = new ArrayList<>();

                for (int i = 0; i < tmpArray.size(); i++) {
                    if (tmpArray.get(i).getName().toUpperCase().contains(constraint)) {
                        SingleRowForCustomer singleRow = new SingleRowForCustomer(tmpArray.get(i).getName(),tmpArray.get(i).getAge(),tmpArray.get(i).getNic(),"","",tmpArray.get(i).getArea());



                        filters.add(singleRow);




                    }

                    if (tmpArray.get(i).getNic().toUpperCase().contains(constraint)) {
                        SingleRowForCustomer singleRow = new SingleRowForCustomer(tmpArray.get(i).getName(),tmpArray.get(i).getAge(),tmpArray.get(i).getNic(),"","",tmpArray.get(i).getArea());



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
            originalArray  = (ArrayList<SingleRowForCustomer>)results.values;
            notifyDataSetChanged();
        }
    }
    ///Codes for filter
}
