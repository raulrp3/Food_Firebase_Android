package com.example.proyectocomidas;

public class Ingrediente{

    private String name;
    private String id;

    public Ingrediente(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getNombre() {
        return name;
    }

    public String getId(){ return id; }

    public void setNombre(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "Ingredient{" +
                "nombre='" + name + '\'' +
                '}';
    }
}
