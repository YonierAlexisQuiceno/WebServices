package com.example.webservicesaplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {

    ListView listView;
    private static String[] titulos_menu = {"Listado de Apps", "categorias"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        listView = findViewById(R.id.lst_aplicacion);

        Menu_Adapter adapter = new Menu_Adapter(this, titulos_menu);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MenuActivity.this, MainActivity.class);
                if (position == 0) {
                    intent.putExtra("extra", "app");
                } else {
                    intent.putExtra("extra", "category");
                }
                startActivity(intent);
            }
        });
    }
}
