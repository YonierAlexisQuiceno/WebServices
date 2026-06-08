package com.example.webservicesaplication;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayAdapter arrayadapter;
    private ProgressBar progressBar;
    private TextView textError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.list_view_data);
        progressBar = findViewById(R.id.progressBar);
        textError = findViewById(R.id.textError);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String extra = extras.getString("extra");

            if ("app".equals(extra)) {
                setTitle("Listado de Apps");
                AppAdapter appAdapter = new AppAdapter(this, progressBar, textError);
                arrayadapter = appAdapter;
                listView.setAdapter(arrayadapter);
            } else {
                setTitle("Categorías");
                CategoryAdapter categoryAdapter = new CategoryAdapter(this, progressBar, textError);
                arrayadapter = categoryAdapter;
                listView.setAdapter(arrayadapter);
            }
        }
    }
}
