package com.vincenzoratti.huellitasapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class AgregarActivity extends AppCompatActivity {

    private FormularioMascota formulario;
    private Button btnGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        TextView titulo = findViewById(R.id.txtTitulo);
        titulo.setText("Registrar mascota");

        formulario = new FormularioMascota(this);
        btnGuardar = findViewById(R.id.btnGuardar);

        findViewById(R.id.btnCancelar).setOnClickListener(v -> finish());
        btnGuardar.setOnClickListener(v -> guardarMascota());
    }

    private void guardarMascota() {
        Map<String, Object> datos = formulario.valoresValidos();

        if (datos == null) {
            return;
        }

        btnGuardar.setEnabled(false);

        FirebaseFirestore.getInstance()
                .collection("mascotas")
                .add(datos)
                .addOnSuccessListener(documento -> {
                    Toast.makeText(
                            this,
                            "Mascota registrada",
                            Toast.LENGTH_SHORT
                    ).show();
                    finish();
                })
                .addOnFailureListener(error -> {
                    btnGuardar.setEnabled(true);
                    Toast.makeText(
                            this,
                            "No se pudo guardar: " + error.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                });
    }
}