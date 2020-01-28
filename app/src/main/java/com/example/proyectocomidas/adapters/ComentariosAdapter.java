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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.proyectocomidas.ProductosActivity;
import com.example.proyectocomidas.R;
import com.example.proyectocomidas.models.Categoria;
import com.example.proyectocomidas.models.Comentario;

import java.util.List;

public class ComentariosAdapter extends RecyclerView.Adapter<ComentariosAdapter.ViewHolderCategoria>  {


    private Context context;
    private List<Comentario> comentario;

    public ComentariosAdapter(Context context, List<Comentario> comentario){
        this.context = context;
        this.comentario = comentario;
    }

    @NonNull
    @Override
    public ComentariosAdapter.ViewHolderCategoria onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_comentario, viewGroup, false);
        final ComentariosAdapter.ViewHolderCategoria vhp = new ComentariosAdapter.ViewHolderCategoria(view);

        return vhp;
    }

    @Override
    public void onBindViewHolder(@NonNull ComentariosAdapter.ViewHolderCategoria viewHolderCategoria, final int i) {

        viewHolderCategoria.tvComentario.setText(comentario.get(i).getComentario());
        viewHolderCategoria.tvUsuarioComentario.setText("Usuario: " + comentario.get(i).getUsuario());




    }

    @Override
    public int getItemCount() {
        return comentario.size();
    }

    public void setComentario(List<Comentario> comentario){
        this.comentario = comentario;
    }

    public class ViewHolderCategoria extends RecyclerView.ViewHolder{


        TextView tvComentario;

        TextView tvUsuarioComentario;


        ImageView ivImagen;

        LinearLayout filaComentario;


        public ViewHolderCategoria(View itemView){
            super(itemView);

            tvComentario = itemView.findViewById(R.id.tvComentario);
            tvUsuarioComentario = itemView.findViewById(R.id.tvUsuarioComentario);

            ivImagen = itemView.findViewById(R.id.ivImagenCategoria);
            filaComentario = itemView.findViewById(R.id.lyfilaComentarios);


        }
    }


}
