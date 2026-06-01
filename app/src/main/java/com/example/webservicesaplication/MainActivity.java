package com.example.webservicesaplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ListView lst_aplicacion;
    private String[] titulos_menu = {"Listado de Apps", "categorias"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lst_aplicacion = findViewById(R.id.lst_aplicacion);

        Menu_Adapter adapter = new Menu_Adapter(this, titulos_menu);
        lst_aplicacion.setAdapter(adapter);

        lst_aplicacion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DataActivity.class);
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
