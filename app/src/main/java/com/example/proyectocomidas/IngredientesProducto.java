package com.example.proyectocomidas;

public class IngredientesProducto {

    String idIngrediente;
    String idProducto;

    public IngredientesProducto(String idIngrediente, String idProducto) {
        this.idIngrediente = idIngrediente;
        this.idProducto = idProducto;
    }

    public String getIdIngrediente() {
        return idIngrediente;
    }

    public void setIdIngrediente(String idIngrediente) {
        this.idIngrediente = idIngrediente;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }
}
