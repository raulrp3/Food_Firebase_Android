package com.example.proyectocomidas.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.proyectocomidas.ProductosActivity;
import com.example.proyectocomidas.R;
import com.example.proyectocomidas.models.Categoria;

import java.util.List;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.ViewHolderCategoria> {

    private Context context;
    private List<Categoria> categorias;

    public CategoriaAdapter(Context context, List<Categoria> categorias){
        this.context = context;
        this.categorias = categorias;
    }

    @NonNull
    @Override
    public ViewHolderCategoria onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_categoria, viewGroup, false);
        final ViewHolderCategoria vhp = new ViewHolderCategoria(view);

        return vhp;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderCategoria viewHolderCategoria, final int i) {

        viewHolderCategoria.tvName.setText(categorias.get(i).getName());

        // Para enviar la imagen, con Glide podemos pasarle en el load la url donde esta la imagen y la convierte a una imagen que
        //mediante el into la ponemos en el ImagenView ivImagen  que esta en el layout item_categoria

        Glide.with(context.getApplicationContext())
                .load(categorias.get(i).getUrlFoto())
                .into(viewHolderCategoria.ivImagen);

        // Ahora le digo que cuando se pinche encima de una fila haga algo, provisionalmente un alert

            viewHolderCategoria.filaCategoria.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, ProductosActivity.class);
                    intent.putExtra("nombreCategoria", categorias.get(i).getName());
                    intent.putExtra("idCategoria", categorias.get(i).getId());
                    context.startActivity(intent);
                }
            });


    }

    @Override
    public int getItemCount() {
        return categorias.size();
    }

    public void setCategorias(List<Categoria> categorias){
        this.categorias = categorias;
    }

    public class ViewHolderCategoria extends RecyclerView.ViewHolder{


        TextView tvName;

        ImageView ivImagen;

        LinearLayout filaCategoria;


        public ViewHolderCategoria(View itemView){
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            ivImagen = itemView.findViewById(R.id.ivImagenCategoria);
            filaCategoria = itemView.findViewById(R.id.lyfilaCategorias);


        }
    }


}
