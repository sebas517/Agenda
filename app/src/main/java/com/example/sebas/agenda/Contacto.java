package com.example.sebas.agenda;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;

import java.util.List;

public class Contacto implements Parcelable {
    private String id;
    private String nombre;
    private String telefono;
    private int editado;
    //0 false 1 true
    @Override
    public String toString() {
        return "Contacto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", telefono='" + telefono + '\'' +
                ", editable= " + editado +
                '}';
    }

    public Contacto(String id, String nombre, String telefono, int editable) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.editado = editable;
    }

    public int getEditable() {
        return editado;
    }

    public void setEditable(int editable) {
        this.editado = editable;
    }

    protected Contacto(Parcel in) {
        id = in.readString();
        nombre = in.readString();
        telefono = in.readString();
        editado = in.readInt();
    }

    public static final Creator<Contacto> CREATOR = new Creator<Contacto>() {
        @Override
        public Contacto createFromParcel(Parcel in) {
            return new Contacto(in);
        }

        @Override
        public Contacto[] newArray(int size) {
            return new Contacto[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(nombre);
        parcel.writeString(telefono);
        parcel.writeInt(editado);
    }
}
