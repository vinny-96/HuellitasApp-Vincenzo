package com.vincenzoratti.huellitasapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class EditarActivity extends AppCompatActivity {

    private String idMascota;
    private FormularioMascota formulario;
    private Button btnGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        idMascota = getIntent().getStringExtra(VerActivity.EXTRA_ID);

        if (idMascota == null || idMascota.trim().isEmpty()) {
            Toast.makeText(this, "Falta el ID de la mascota", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        ((TextView) findViewById(R.id.txtTitulo)).setText("Editar mascota");

        formulario = new FormularioMascota(this);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnGuardar.setText("Actualizar mascota");
        btnGuardar.setEnabled(false);

        findViewById(R.id.btnCancelar).setOnClickListener(v -> finish());
        btnGuardar.setOnClickListener(v -> actualizarMascota());

        cargarMascota();
    }

    private void cargarMascota() {
        FirebaseFirestore.getInstance()
                .collection("mascotas")
                .document(idMascota)
                .get()
                .addOnSuccessListener(documento -> {
                    if (!documento.exists()) {
                        Toast.makeText(this, "Esta mascota ya no existe",
                                Toast.LENGTH_LONG).show();
                        finish();
                        return;
                    }

                    Mascota mascota = documento.toObject(Mascota.class);

                    if (mascota == null) {
                        Toast.makeText(this, "No se pudieron leer los datos",
                                Toast.LENGTH_LONG).show();
                        finish();
                        return;
                    }

                    formulario.mostrar(mascota);
                    btnGuardar.setEnabled(true);
                })
                .addOnFailureListener(error -> {
                    Toast.makeText(this,
                            "No se pudo cargar: " + error.getMessage(),
                            Toast.LENGTH_LONG).show();
                    finish();
                });
    }

    private void actualizarMascota() {
        Map<String, Object> datos = formulario.valoresValidos();

        if (datos == null) {
            return;
        }

        btnGuardar.setEnabled(false);

        FirebaseFirestore.getInstance()
                .collection("mascotas")
                .document(idMascota)
                .update(datos)
                .addOnSuccessListener(resultado -> {
                    Toast.makeText(this, "Mascota actualizada",
                            Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(this, ListarActivity.class);
                    intent.addFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    | Intent.FLAG_ACTIVITY_SINGLE_TOP
                    );
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(error -> {
                    btnGuardar.setEnabled(true);
                    Toast.makeText(this,
                            "No se pudo actualizar: " + error.getMessage(),
                            Toast.LENGTH_LONG).show();
                });
    }
}