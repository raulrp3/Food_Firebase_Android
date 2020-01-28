package com.example.proyectocomidas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;



public class AlergenoAdapter extends ArrayAdapter {
    private Context context;
    private ArrayList<Alergeno> datos;
    public AlergenoAdapter(Context context, ArrayList<Alergeno> datos) {
        super(context, R.layout.alergeno_layout, datos);
        // Guardamos los parámetros en variables de clase.
        this.context = context;
        this.datos = datos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View item = inflater.inflate(R.layout.alergeno_layout, null);
        //ImageView imagen = (ImageView) item.findViewById(R.id.alergeno_icono);
        //imagen.setImageResource(datos.get(position).getDrawableImageID());

        TextView nombre = (TextView) item.findViewById(R.id.alergeno);
        nombre.setText(datos.get(position).getNombre());

        return item;
    }
}
