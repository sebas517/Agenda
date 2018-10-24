package com.example.sebas.agenda;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class Agenda extends AppCompatActivity {
    private Adaptador adaptador;
    private static final int REQUEST_CODE = 1;
    private List<Contacto> agenda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);

        Intent accion = getIntent();
        String memoria = accion.getStringExtra("memoria");
        String rest = accion.getStringExtra("reestablecer");

        if (rest != null){
            crearArchivo(memoria);
        }

        if (!existeArchivo(this)){
            crearArchivo(memoria);
        }
        agenda = leerArchivo();

        Adaptador.OnItemClickListener oyente = new Adaptador.OnItemClickListener() {
            @Override
            public void onClick(Adaptador.ViewHolder holder, int id) {
                for (Contacto c : agenda ) {
                    if (c.getId() == holder.tvId.getText().toString()){
                        Intent i = new Intent(Agenda.this, Detalle.class);
                        i.putExtra("contacto", c);
                        startActivityForResult(i, REQUEST_CODE);
                    }
                }
            }
        };

        RecyclerView rv = findViewById(R.id.rvContactos);
        adaptador = new Adaptador(oyente, agenda);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adaptador);

    }



    private boolean existeArchivo(Context context){
        boolean existe = false;
        FileInputStream fos;
        try {
            fos = openFileInput("contacto.txt");
            existe = true;
        } catch (FileNotFoundException e) {
        }
        return existe;
    }


    private OutputStreamWriter crearArchivoMemoriaInterna(){
        OutputStreamWriter escritor=null;
        try {
            escritor = new OutputStreamWriter(openFileOutput("contacto.txt", this.MODE_PRIVATE));
        }
        catch (Exception ex) {
            Log.v("Error", "Error al crear");
        }
        return escritor;
    }

    public void crearArchivo(String memoria){
        //Creamos el archivo segun nos indiquen
        OutputStreamWriter escritor = null;
        if (memoria.equals("interna")){
            escritor = crearArchivoMemoriaInterna();
        }

        //Cogemos los datos de contactos
        String[] projeccion = new String[] { ContactsContract.Data._ID, ContactsContract.Data.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.TYPE };
        String selectionClause = ContactsContract.Data.MIMETYPE + "='" +
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "' AND "
                + ContactsContract.CommonDataKinds.Phone.NUMBER + " IS NOT NULL";
        String sortOrder = ContactsContract.Data.DISPLAY_NAME + " ASC";

        Cursor c = getContentResolver().query(
                ContactsContract.Data.CONTENT_URI,
                projeccion,
                selectionClause,
                null,
                sortOrder);
        //Creamos la cadena que compondrá nuestro arhcivo
        String cadena ="";
        while(c.moveToNext()){
            cadena += c.getString(0) + ";" + c.getString(1) + ";" + c.getString(2)+";\n";
        }
        c.close();
        //Escribimos el archivo
        try {
            escritor.write(cadena);
            escritor.close();
        } catch (IOException e) {
            Log.v("", "Error al escribir");
        }
    }

    public List<Contacto> leerArchivo(){
        List<Contacto> contactos = new ArrayList<>();
        InputStreamReader flujo=null;
        BufferedReader lector=null;

        try {
            flujo= new InputStreamReader(openFileInput("contacto.txt"));
            lector= new BufferedReader(flujo);
            String texto = lector.readLine();
            while(texto!=null) {
                String[] contacto = texto.split(";");
                Contacto c = new Contacto(contacto[0], contacto[1], contacto[2]);
                contactos.add(c);
                texto=lector.readLine();
            }
        } catch (Exception ex) {
            Log.e("ivan", "Error al leer fichero desde memoria interna");
        }
        return contactos;
    }

    private void guardarCambios(List<Contacto> contactos){
        OutputStreamWriter escritor = crearArchivoMemoriaInterna();
        String cadena = "";
        for (Contacto c: contactos) {
            cadena += c.getId() + ";" + c.getNombre() + ";" + c.getTelefono()+";\n";
        }
        try {
            escritor.write(cadena);
            escritor.close();
        } catch (IOException e) {
            Log.v("", "Error al escribir");
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case REQUEST_CODE:
                 if (resultCode == RESULT_OK) {
                    Contacto contacto = data.getParcelableExtra("nuevoContacto");
                    for (Contacto c: agenda) {
                        if (contacto.getId().equals(c.getId())){
                            agenda.set(agenda.indexOf(c), contacto);
                            guardarCambios(agenda);
                            adaptador.notifyDataSetChanged();
                        }
                    }
                }else if(resultCode == 2){
                     List<Contacto> nuevaAgenda = agenda;
                     Contacto contacto = data.getParcelableExtra("eliminar");
                     for (Contacto c: nuevaAgenda) {
                         if (contacto.getId().equals(c.getId())){
                             System.out.println(" y borramos a....... " + nuevaAgenda.get(nuevaAgenda.indexOf(c)));
                             int posicion = nuevaAgenda.indexOf(c);
                             nuevaAgenda.remove(posicion);
                             break;
                         }
                     }
                     guardarCambios(nuevaAgenda);
                     adaptador.notifyDataSetChanged();
                 }
                break;
        }
    }



}
