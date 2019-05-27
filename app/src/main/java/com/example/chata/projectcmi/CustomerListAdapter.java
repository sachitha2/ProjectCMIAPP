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
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View row = inflater.inflate(R.layout.customer_list,null);

        TextView textView =(TextView)row.findViewById(R.id.textView);
        TextView textAge = row.findViewById(R.id.txtAge);


        textView.setText(originalArray.get(position).getName());
        textAge.setText(originalArray.get(position).getAge());



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
                        SingleRowForCustomer singleRow = new SingleRowForCustomer(tmpArray.get(i).getName(),tmpArray.get(i).getAge());



                        filters.add(singleRow);




                    }

                    if (tmpArray.get(i).getAge().toUpperCase().contains(constraint)) {
                        SingleRowForCustomer singleRow = new SingleRowForCustomer(tmpArray.get(i).getName(),tmpArray.get(i).getAge());



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
