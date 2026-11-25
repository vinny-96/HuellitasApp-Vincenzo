package com.vincenzoratti.huellitasapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.List;

public class ListarActivity extends AppCompatActivity {

    private MascotaAdapter adaptador;
    private TextView txtConteo;
    private TextView txtVacio;
    private ListenerRegistration escucha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar);

        txtConteo = findViewById(R.id.txtConteo);
        txtVacio = findViewById(R.id.txtVacio);

        RecyclerView lista = findViewById(R.id.listaMascotas);
        lista.setLayoutManager(new LinearLayoutManager(this));

        adaptador = new MascotaAdapter(mascota -> {
            Intent intent = new Intent(this, VerActivity.class);
            intent.putExtra(VerActivity.EXTRA_ID, mascota.getId());
            startActivity(intent);
        });

        lista.setAdapter(adaptador);

        findViewById(R.id.btnNueva).setOnClickListener(v ->
                startActivity(new Intent(this, AgregarActivity.class))
        );
    }

    @Override
    protected void onStart() {
        super.onStart();

        escucha = FirebaseFirestore.getInstance()
                .collection("mascotas")
                .addSnapshotListener((resultado, error) -> {
                    if (error != null) {
                        Toast.makeText(
                                this,
                                "Error al cargar mascotas: " + error.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                        return;
                    }

                    if (resultado == null) {
                        return;
                    }

                    List<Mascota> mascotas = new ArrayList<>();

                    for (DocumentSnapshot documento : resultado.getDocuments()) {
                        Mascota mascota = documento.toObject(Mascota.class);

                        if (mascota != null) {
                            mascota.setId(documento.getId());
                            mascotas.add(mascota);
                        }
                    }

                    adaptador.actualizar(mascotas);
                    txtConteo.setText(
                            "Mascotas registradas: " + mascotas.size()
                    );
                    txtVacio.setVisibility(
                            mascotas.isEmpty() ? View.VISIBLE : View.GONE
                    );
                });
    }

    @Override
    protected void onStop() {
        if (escucha != null) {
            escucha.remove();
            escucha = null;
        }

        super.onStop();
    }
}