package com.example.webservicesaplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Menu_Adapter extends ArrayAdapter<String> {

    private String[] data;
    private Context context;

    public Menu_Adapter(@NonNull Context context, @NonNull String[] objects) {
        super(context, R.layout.adapter_menu, objects);
        this.context = context;
        this.data = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_menu, parent, false);
        }

        ImageView imagen_menu = convertView.findViewById(R.id.imagen_menu);
        TextView test_titulo_menu = convertView.findViewById(R.id.test_titulo_menu);

        test_titulo_menu.setText(data[position]);

        if (position == 0) {
            imagen_menu.setImageResource(R.drawable.phone);
        } else {
            imagen_menu.setImageResource(R.drawable.macbook);
        }

        return convertView;
    }
}
