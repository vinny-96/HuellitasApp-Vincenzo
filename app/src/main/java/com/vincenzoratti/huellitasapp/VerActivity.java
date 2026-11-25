package com.vincenzoratti.huellitasapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class VerActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "id_mascota";

    private String idMascota;
    private TextView txtDetalle;
    private Button btnEditar;
    private Button btnEliminar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver);

        idMascota = getIntent().getStringExtra(EXTRA_ID);

        if (idMascota == null || idMascota.trim().isEmpty()) {
            Toast.makeText(this, "Falta el ID de la mascota", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        txtDetalle = findViewById(R.id.txtDetalle);
        btnEditar = findViewById(R.id.btnEditar);
        btnEliminar = findViewById(R.id.btnEliminar);

        findViewById(R.id.btnVolver).setOnClickListener(v -> finish());

        btnEditar.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditarActivity.class);
            intent.putExtra(EXTRA_ID, idMascota);
            startActivity(intent);
        });

        btnEliminar.setOnClickListener(v -> confirmarEliminacion());
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (idMascota == null) {
            return;
        }

        btnEditar.setEnabled(false);
        btnEliminar.setEnabled(false);
        txtDetalle.setText("Cargando mascota...");

        FirebaseFirestore.getInstance()
                .collection("mascotas")
                .document(idMascota)
                .get()
                .addOnSuccessListener(documento -> {
                    if (!documento.exists()) {
                        txtDetalle.setText("Esta mascota ya no está registrada.");
                        return;
                    }

                    Mascota mascota = documento.toObject(Mascota.class);

                    if (mascota == null) {
                        txtDetalle.setText("No se pudieron leer los datos.");
                        return;
                    }

                    txtDetalle.setText(
                            "Nombre: " + mostrar(mascota.getNombre())
                                    + "\nEspecie: " + mostrar(mascota.getEspecie())
                                    + "\nEdad: " + (mascota.getEdad() == null
                                    ? "Sin dato" : mascota.getEdad() + " años")
                                    + "\nVacunado: " + (mascota.isVacunado()
                                    ? "Sí" : "No")
                                    + "\nEstado: " + mostrar(mascota.getEstado())
                                    + "\nDescripción: "
                                    + mostrar(mascota.getDescripcion())
                    );

                    btnEditar.setEnabled(true);
                    btnEliminar.setEnabled(true);
                })
                .addOnFailureListener(error ->
                        txtDetalle.setText(
                                "No se pudo cargar: " + error.getMessage()
                        )
                );
    }

    private String mostrar(String valor) {
        return valor == null || valor.trim().isEmpty()
                ? "Sin dato" : valor;
    }

    private void confirmarEliminacion() {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar mascota")
                .setMessage("¿Quieres eliminar este registro?")
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Eliminar", (dialogo, boton) ->
                        eliminarMascota()
                )
                .show();
    }

    private void eliminarMascota() {
        btnEditar.setEnabled(false);
        btnEliminar.setEnabled(false);

        FirebaseFirestore.getInstance()
                .collection("mascotas")
                .document(idMascota)
                .delete()
                .addOnSuccessListener(resultado -> {
                    Toast.makeText(
                            this, "Mascota eliminada", Toast.LENGTH_SHORT
                    ).show();
                    finish();
                })
                .addOnFailureListener(error -> {
                    btnEditar.setEnabled(true);
                    btnEliminar.setEnabled(true);
                    Toast.makeText(
                            this,
                            "No se pudo eliminar: " + error.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                });
    }
}