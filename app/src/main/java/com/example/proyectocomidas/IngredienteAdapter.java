package com.example.proyectocomidas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class IngredienteAdapter extends ArrayAdapter {
    private Context context;
    private ArrayList<Ingrediente> datos;
    public IngredienteAdapter(Context context, ArrayList<Ingrediente> datos) {
        super(context, R.layout.ingrediente_layout, datos);
        this.context = context;
        this.datos = datos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View item = inflater.inflate(R.layout.ingrediente_layout, null);
        TextView nombre = item.findViewById(R.id.ingrediente);
        nombre.setText(datos.get(position).getNombre());
        return item;
    }
}
