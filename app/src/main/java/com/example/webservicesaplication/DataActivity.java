package com.example.webservicesaplication;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class DataActivity extends AppCompatActivity {

    private ListView list_view_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        list_view_data = findViewById(R.id.list_view_data);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String extra = extras.getString("extra");

            if ("app".equals(extra)) {
                AppAdapter appAdapter = new AppAdapter(this);
                list_view_data.setAdapter(appAdapter);
            } else {
                CategoryAdapter categoryAdapter = new CategoryAdapter(this);
                list_view_data.setAdapter(categoryAdapter);
            }
        }
    }
}
