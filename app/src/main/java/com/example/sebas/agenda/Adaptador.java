package com.example.sebas.agenda;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class Adaptador extends RecyclerView.Adapter<Adaptador.ViewHolder>{

    public interface OnItemClickListener{
        public void onClick(ViewHolder holder, int id);
    }

    private OnItemClickListener oyente;
    List<Contacto> contactos;

    public Adaptador(OnItemClickListener oyente, List<Contacto> contactos) {
        this.oyente = oyente;
        this.contactos = contactos;
        //this.cursor = cursor;
    }

    @NonNull
    @Override
    public Adaptador.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Adaptador.ViewHolder viewHolder, int i) {
        Contacto c = contactos.get(i);

        viewHolder.tvNombre.setText(c.getNombre());
        viewHolder.tvTelefono.setText(c.getTelefono());
        viewHolder.tvId.setText(c.getId());
    }

    @Override
    public int getItemCount() {
        return contactos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvNombre;
        public TextView tvId;
        public TextView tvTelefono;

        public ViewHolder(View v) {
            super(v);
            tvNombre = v.findViewById(R.id.textView);
            tvTelefono = v.findViewById(R.id.textView2);
            tvId = v.findViewById(R.id.tvId);
            v.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            int id = getAdapterPosition();
            oyente.onClick(this, id);
        }
    }
}
