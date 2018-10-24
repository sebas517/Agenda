package com.example.sebas.agenda;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Detalle extends AppCompatActivity {
    private Contacto c;
    private EditText etTelefono;
    private EditText etNombre;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);

        Bundle extras = getIntent().getExtras();
        c = extras.getParcelable("contacto");
        etNombre = findViewById(R.id.etNombre);
        etTelefono = findViewById(R.id.etTelefono);
        Button btEdit = findViewById(R.id.btEditar);
        Button btEliminar = findViewById(R.id.btEliminar);
        Button btCancel = findViewById(R.id.btCancelar);

        etNombre.setText(c.getNombre());
        etTelefono.setText(c.getTelefono());

        btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c.setNombre(etNombre.getText().toString());
                c.setTelefono(etTelefono.getText().toString());


                Intent i = getIntent();
                i.putExtra("nuevoContacto", c);
                setResult(RESULT_OK, i);
                finish();

            }
        });

        btEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = getIntent();
                i.putExtra("eliminar", c);
                setResult(2, i);
                finish();
            }
        });

    }
}
