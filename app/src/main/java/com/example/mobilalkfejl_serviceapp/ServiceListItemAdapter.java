package com.example.mobilalkfejl_serviceapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class ServiceListItemAdapter extends ArrayAdapter<ServiceItem> {

    private static final int SECRET_KEY = 45;

    public ServiceListItemAdapter(Context context, ArrayList<ServiceItem> serviceItems) {
        super(context, 0, serviceItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ServiceItem serviceItem = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.servicelist_item_layout, parent, false);
        }

        TextView serviceName = convertView.findViewById(R.id.service_name);
        TextView servicePrice = convertView.findViewById(R.id.service_price);
        Button bookButton = convertView.findViewById(R.id.book_button);

        serviceName.setText(serviceItem.getName());
        servicePrice.setText(String.valueOf(serviceItem.getPrice() + " Ft"));
        bookButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ServiceDetailActivity.class);
            intent.putExtra("SECRET_KEY", SECRET_KEY);
            intent.putExtra("SERVICE", serviceItem);
            getContext().startActivity(intent);

        });

        return convertView;
    }
}
