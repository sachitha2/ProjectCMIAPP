package com.example.chata.projectcmi;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.jar.Attributes;

public class ListViewAdapter extends BaseAdapter {

    Context context;

    String[] Area;
    String[] Id;
    String From;

    String[] Customer;
    String[] NIC;
    String[] Installment;
    String[] Total;

    public ListViewAdapter(Context context, String[] Area, String[] Id, String From){

        this.context = context;
        this.Area = Area;
        this.Id = Id;
        this.From = From;

    }

    public ListViewAdapter(Context context, String[] Customer, String[] Installment, String[] Total, String[] NIC, String From){

        this.context = context;
        this.Customer = Customer;
        this.NIC = NIC;
        this.Installment = Installment;
        this.Total = Total;
        this.From = From;

        this.Id = Customer;

    }

    @Override
    public int getCount() {
        return Id.length;
    }

    @Override
    public Object getItem(int position) {
        return Id[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

//        LayoutInflater myInflater = LayoutInflater.from(context);
//        View btnView = myInflater.inflate(R.layout.btn_layout,null);
//
//        TextView name = (TextView)btnView.findViewById(R.id.txtName);
//        TextView id = (TextView)btnView.findViewById(R.id.txtId);
//
//        name.setText(Area[position]);
//        id.setText(Id[position]);

        LayoutInflater myInflater = LayoutInflater.from(context);
        View btnView = myInflater.inflate(R.layout.btn_layout,null);

        switch (From){

            case "SelectArea":

                btnView = myInflater.inflate(R.layout.btn_layout,null);

                TextView name = (TextView)btnView.findViewById(R.id.txtName);
                TextView id = (TextView)btnView.findViewById(R.id.txtId);

                name.setText(Area[position]);
                id.setText(Id[position]);

                btnView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentCreditsByAreaId = new Intent(context, CreditsByAreaId.class);
                        intentCreditsByAreaId.putExtra("areaId", Id[position]);
                        intentCreditsByAreaId.putExtra("areaName", Area[position]);
                        context.startActivity(intentCreditsByAreaId);
                        //Toast.makeText(context, Area[position] + " was clicked", Toast.LENGTH_SHORT).show();
                    }
                });
                break;

            case "CreditsByAreaId":

                btnView = myInflater.inflate(R.layout.btn_credits_layout,null);

                TextView customer = (TextView)btnView.findViewById(R.id.txtCustomer);
                TextView installments = (TextView)btnView.findViewById(R.id.txtInstallments);
                TextView total = (TextView)btnView.findViewById(R.id.txtTotal);

                customer.setText(Customer[position]);
                installments.setText(Installment[position]);
                total.setText(Total[position]);

                btnView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intentCreditsOfCustomer = new Intent(context, CreditsOfCustomer.class);
                        intentCreditsOfCustomer.putExtra("customerNIC", NIC[position]);
                        intentCreditsOfCustomer.putExtra("customerName", Customer[position]);
                        context.startActivity(intentCreditsOfCustomer);
                        //Toast.makeText(context, Customer[position] + " was clicked", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
//
//            case "PracticeUnits":
//                btnView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        //Intent intentPractice = new Intent();
//                        ///intentPractice.setClass(context, Practice.class);
//                        //intentPractice.putExtra("unitId", Ide[position]);
//                        //intentPractice.putExtra("unitName", Namese[position]);
//
//                        //context.startActivity(intentPractice);
//
//                        //Toast.makeText(context, Id[position] + " was clicked", Toast.LENGTH_SHORT).show();
//                    }
//                });
//                break;

        }

        return btnView;

    }

}
