package com.vincenzoratti.huellitasapp;

import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class FormularioMascota {

    private final Activity activity;
    private final EditText nombre;
    private final EditText edad;
    private final EditText descripcion;
    private final CheckBox vacunado;
    private final Spinner especie;
    private final Spinner estado;

    private final String[] especies = {
            "Seleccionar especie", "Perro", "Gato", "Otro"
    };

    private final String[] estados = {
            "Seleccionar estado", "Disponible", "En proceso", "Adoptado"
    };

    public FormularioMascota(Activity activity) {
        this.activity = activity;

        nombre = activity.findViewById(R.id.etNombre);
        edad = activity.findViewById(R.id.etEdad);
        descripcion = activity.findViewById(R.id.etDescripcion);
        vacunado = activity.findViewById(R.id.cbVacunado);
        especie = activity.findViewById(R.id.spEspecie);
        estado = activity.findViewById(R.id.spEstado);

        configurarSpinner(especie, especies);
        configurarSpinner(estado, estados);
    }

    private void configurarSpinner(Spinner spinner, String[] opciones) {
        ArrayAdapter<String> adaptador = new ArrayAdapter<>(
                activity,
                android.R.layout.simple_spinner_item,
                opciones
        );
        adaptador.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );
        spinner.setAdapter(adaptador);
    }

    public void mostrar(Mascota mascota) {
        nombre.setText(mascota.getNombre());

        if (mascota.getEdad() != null) {
            edad.setText(String.valueOf(mascota.getEdad()));
        }

        descripcion.setText(
                mascota.getDescripcion() == null
                        ? ""
                        : mascota.getDescripcion()
        );

        vacunado.setChecked(mascota.isVacunado());
        seleccionar(especie, especies, mascota.getEspecie());
        seleccionar(estado, estados, mascota.getEstado());
    }

    private void seleccionar(
            Spinner spinner,
            String[] opciones,
            String valor
    ) {
        for (int i = 1; i < opciones.length; i++) {
            if (opciones[i].equals(valor)) {
                spinner.setSelection(i);
                return;
            }
        }
        spinner.setSelection(0);
    }

    // Devuelve null si falta corregir algún dato.
    public Map<String, Object> valoresValidos() {
        String nombreIngresado = nombre.getText().toString().trim();
        String edadIngresada = edad.getText().toString().trim();

        if (nombreIngresado.isEmpty()) {
            nombre.setError("Escribe el nombre");
            nombre.requestFocus();
            return null;
        }

        if (especie.getSelectedItemPosition() == 0) {
            Toast.makeText(
                    activity, "Selecciona la especie", Toast.LENGTH_SHORT
            ).show();
            return null;
        }

        int edadNumero;
        try {
            edadNumero = Integer.parseInt(edadIngresada);
        } catch (NumberFormatException error) {
            edad.setError("Escribe la edad en años");
            edad.requestFocus();
            return null;
        }

        if (edadNumero < 0 || edadNumero > 99) {
            edad.setError("Ingresa una edad entre 0 y 99 años");
            edad.requestFocus();
            return null;
        }

        if (estado.getSelectedItemPosition() == 0) {
            Toast.makeText(
                    activity, "Selecciona el estado", Toast.LENGTH_SHORT
            ).show();
            return null;
        }

        Map<String, Object> datos = new HashMap<>();
        datos.put("nombre", nombreIngresado);
        datos.put("especie", especie.getSelectedItem().toString());
        datos.put("edad", edadNumero);
        datos.put("vacunado", vacunado.isChecked());
        datos.put("estado", estado.getSelectedItem().toString());
        datos.put("descripcion", descripcion.getText().toString().trim());

        return datos;
    }
}