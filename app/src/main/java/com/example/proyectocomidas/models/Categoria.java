package com.example.proyectocomidas.models;

public class Categoria {

    private String name;
    private String urlFoto;
    private String id;


    public Categoria(String name, String urlFoto, String id) {
        this.name = name;
        this.urlFoto = urlFoto;
        this.id = id;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    @Override
    public String toString() {
        return "Categoria{" +
                "name='" + name + '\'' +
                ", urlFoto='" + urlFoto + '\'' +
                '}';
    }
}
