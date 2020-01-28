package com.example.proyectocomidas.models;

public class Comentario {


    private String comentario;
    private String usuario;



    public Comentario(String comentario,String usuario) {
        this.comentario = comentario;
        this.usuario = usuario;



    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "Comentario{" +
                "comentario='" + comentario + '\'' +
                ", usuario='" + usuario + '\'' +
                '}';
    }
}
