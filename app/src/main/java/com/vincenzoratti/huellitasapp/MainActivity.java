package com.vincenzoratti.huellitasapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnAgregar = findViewById(R.id.btnAgregar);
        Button btnListar = findViewById(R.id.btnListar);

        btnAgregar.setOnClickListener(v ->
                startActivity(new Intent(this, AgregarActivity.class))
        );

        btnListar.setOnClickListener(v ->
                startActivity(new Intent(this, ListarActivity.class))
        );
    }
}